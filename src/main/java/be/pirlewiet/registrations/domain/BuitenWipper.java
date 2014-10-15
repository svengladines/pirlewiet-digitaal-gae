package be.pirlewiet.registrations.domain;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;
import javax.mail.internet.MimeMessage;

import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;

import be.pirlewiet.registrations.model.CodeRequest;
import be.pirlewiet.registrations.model.Organisatie;
import be.pirlewiet.registrations.model.Persoon;
import be.pirlewiet.registrations.repositories.OrganisatieRepository;
import be.pirlewiet.registrations.web.util.DataGuard;
import freemarker.template.Configuration;
import freemarker.template.Template;

public class BuitenWipper extends Persoon {
	
	@Resource
	protected OrganisatieRepository organisatieRepository;
	
	@Resource
	protected BuitenWipper buitenWipper;
	
	@Resource
	protected DataGuard dataGuard;
	
	@Resource
	JavaMailSender javaMailSender;
	
	@Resource
	PostBode postBode;
	
	protected String fromAddress = "noreply@pirlewiet.be";
	
    public BuitenWipper guard() {
    	this.dataGuard.guard();
    	return this;
    }
	
	public Organisatie whoHasCode( String code ) {
		return this.organisatieRepository.findOneByCode( code.replaceAll("\"", "") );
	}
	
	public Organisatie whoHasID( Long id ) {
		return this.organisatieRepository.findOneById( id );
	}
	
	public void processCodeRequest( CodeRequest codeRequest ) {
		
		String email
			= codeRequest.getEmail();
		
		Organisatie organisatie
			= this.organisatieRepository.findOneByEmail( email );
		
		if ( organisatie == null ) {
			codeRequest.setStatus( CodeRequest.Status.REJECTED );
			return;
		}
		
		String code = "xyz123"; // TODO
		
		MimeMessage message
			= formatCodeRequestMessages( organisatie , email, code );
		
		if ( message != null ) {
			
			postBode.deliver( message );
			logger.info( "email sent" );
			
		}
		
	}
	
	protected MimeMessage formatCodeRequestMessages( Organisatie organisatie, String email, String code ) {
		
		MimeMessage message
			= null;
		
		Configuration cfg 
			= new Configuration();
	
		try {
			
			InputStream tis
				= this.getClass().getResourceAsStream( "/templates/code-request.tmpl" );
			
			Template template 
				= new Template("code", new InputStreamReader( tis ), cfg );
			
			Map<String, Object> model = new HashMap<String, Object>();
					
			model.put( "organisatie", organisatie );
			model.put( "code", code );
			
			StringWriter bodyWriter 
				= new StringWriter();
			
			template.process( model , bodyWriter );
			
			bodyWriter.flush();
				
			message = this.javaMailSender.createMimeMessage();
			MimeMessageHelper helper = new MimeMessageHelper(message);
				
			helper.setFrom( this.fromAddress );
			helper.setTo( email );
			helper.setSubject( "Aanvraag code" );
				
			String text
				= bodyWriter.toString();
				
			logger.info( "email text is [{}]", text );
				
			helper.setText(text, true);
				
		}
		catch( Exception e ) {
			logger.warn( "could not write e-mail", e );
			throw new RuntimeException( e );
		}
		
		return message;
    	
    }

}

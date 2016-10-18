package be.pirlewiet.digitaal.domain.people;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;
import javax.mail.internet.MimeMessage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.transaction.annotation.Transactional;

import be.pirlewiet.digitaal.application.config.PirlewietApplicationConfig;
import be.pirlewiet.digitaal.domain.exception.ErrorCodes;
import be.pirlewiet.digitaal.domain.exception.PirlewietException;
import be.pirlewiet.digitaal.model.CodeRequest;
import be.pirlewiet.digitaal.model.Organisation;
import be.pirlewiet.digitaal.repositories.OrganisationRepository;
import be.pirlewiet.digitaal.web.util.DataGuard;
import be.pirlewiet.digitaal.web.util.PirlewietUtil;

import com.google.appengine.api.datastore.KeyFactory;

import freemarker.template.Configuration;
import freemarker.template.Template;

public class DoorMan {
	
	protected final Logger logger
		= LoggerFactory.getLogger( this.getClass() );
	
	@Resource
	protected OrganisationRepository organisationRepository;
	
	@Resource
	protected DataGuard dataGuard;
	
	@Resource
	JavaMailSender javaMailSender;
	
	@Resource
	MailMan postBode;
	
	@Resource
	CodeMan codeMan;
	
	@Resource
	Organisation pDiddy;
	
    public DoorMan guard() {
    	this.dataGuard.guard();
    	return this;
    }
	
	public Organisation whoHasCode( String code ) {
		
		code = code.replaceAll("\"", "").toLowerCase();
		
		if ( PirlewietUtil.PDIDDY_CODE.equals( code ) ) {
			return pDiddy;
		}
		
		return this.organisationRepository.findOneByCode( code );
	}
	
	@Transactional(readOnly=true)
	public Organisation actor( String uuid ) {
		
		if ( PirlewietUtil.PDIDDY_ID.equals( uuid ) ) {
			return pDiddy;
		}
		
		Organisation organisatie
			= this.organisationRepository.findByUuid( uuid );
		
		if ( organisatie != null ) {
			return organisatie;
		}
		else {
			throw new PirlewietException( ErrorCodes.PWT_UNKNOWN_ACTOR );
		}
		
	}
	
	@Transactional(readOnly=true)
	public Organisation whoHasID( String uuid ) {
		
		if ( PirlewietUtil.PDIDDY_ID.equals( uuid ) ) {
			return pDiddy;
		}
		
		Organisation organisatie
			= this.organisationRepository.findByUuid( uuid );
		if ( organisatie != null ) {
			return organisatie;
		}
		else {
			logger.info( "no organisation with id [{}]", uuid );
		}
		
		return null;
		
	}
	
	@Transactional(readOnly=false)
	public void processCodeRequest( CodeRequest codeRequest ) {
		
		this.processCodeRequest( codeRequest, false );
		
	}
	
	@Transactional(readOnly=false)
	public void processCodeRequest( CodeRequest codeRequest, boolean sendEmail ) {
		
		String email
			= codeRequest.getEmail();
		
		Organisation organisatie
			= this.organisationRepository.findOneByEmail( email );
		
		if ( organisatie == null ) {
			logger.warn( "no organisation with email [{}]", email );
			codeRequest.setStatus( CodeRequest.Status.REJECTED );
			return;
		}
		
		String code
			= null;
		
		if ( organisatie.getCode() != null ) {
			code = organisatie.getCode();
			logger.info( "code exists, code is [{}]", code );
			if ( ( organisatie.getUuid() == null ) || ( organisatie.getUuid().isEmpty() ) ) {
				organisatie.setUuid( KeyFactory.keyToString( organisatie.getKey() ) );
				organisatie = this.organisationRepository.saveAndFlush( organisatie );
			}
		}
		
		while ( code == null ) {
			code = this.codeMan.generateCode();
			if ( this.organisationRepository.findOneByCode( code ) != null ) {
				// code already taken
				code = null;
				continue;
			}
			logger.info( "generated code for [{}] : [{}]", organisatie.getName(), code );
			organisatie.setCode( code );
			organisatie = this.organisationRepository.saveAndFlush( organisatie );
			logger.info( "saved code [{}] for [{}]", code, organisatie.getName() );
		}
		
		
		codeRequest.setCode( code );
		codeRequest.setStatus( CodeRequest.Status.OK );
		
		if ( sendEmail ) {
		
			MimeMessage message
				= formatCodeRequestMessages( organisatie , email, code );
			
			if ( message != null ) {
				
				postBode.deliver( message );
				logger.info( "code request email sent to [{}]", email );
				
			}
			
		}
		
	}
	
	@Transactional(readOnly=false)
	public String uniqueCode( ) {
		
		String code
			= null;
		
		while ( code == null ) {
			code = this.codeMan.generateCode();
			if ( this.organisationRepository.findOneByCode( code ) != null ) {
				// code already taken
				code = null;
				continue;
			}
			logger.info( "generated unique code [{}]", code );
		}
		
		return code;
		
	}
	
	protected MimeMessage formatCodeRequestMessages( Organisation organisatie, String email, String code ) {
		
		MimeMessage message
			= null;
		
		Configuration cfg 
			= new Configuration();
	
		try {
			
			InputStream tis
				= this.getClass().getResourceAsStream( "/templates/to-organisation/code-request.tmpl" );
			
			Template template 
				= new Template("code", new InputStreamReader( tis ), cfg );
			
			Map<String, Object> model = new HashMap<String, Object>();
					
			model.put( "organisatie", organisatie );
			model.put( "code", code.toUpperCase() );
			
			StringWriter bodyWriter 
				= new StringWriter();
			
			template.process( model , bodyWriter );
			
			bodyWriter.flush();
				
			message = this.javaMailSender.createMimeMessage();
			MimeMessageHelper helper = new MimeMessageHelper(message);
				
			helper.setFrom( PirlewietApplicationConfig.EMAIL_ADDRESS );
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

package be.pirlewiet.registrations.domain;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringWriter;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;
import javax.mail.internet.MimeMessage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.transaction.annotation.Transactional;

import be.pirlewiet.registrations.application.config.PirlewietApplicationConfig;
import be.pirlewiet.registrations.model.InschrijvingX;
import be.pirlewiet.registrations.model.Organisatie;
import be.pirlewiet.registrations.model.Status;
import be.pirlewiet.registrations.model.Tags;
import be.pirlewiet.registrations.model.Vakantie;
import be.pirlewiet.registrations.model.Vraag;
import be.pirlewiet.registrations.repositories.InschrijvingXRepository;
import be.pirlewiet.registrations.repositories.VakantieRepository;
import be.pirlewiet.registrations.web.util.DataGuard;
import freemarker.template.Configuration;
import freemarker.template.Template;

public class Intaker {

	protected final Logger logger
		= LoggerFactory.getLogger( this.getClass() );
	
	@Resource
	PostBode postBode;
	
	@Resource
	VakantieRepository vakantieRepository;
	
	@Resource
	protected SecretariaatsMedewerker secretariaatsMedewerker;
	
	@Resource
	protected InschrijvingXRepository inschrijvingXRepository;
	
	@Resource
	JavaMailSender javaMailSender;
	
	@Resource
	DataGuard dataGuard;
	
	public Intaker guard() {
	    	this.dataGuard.guard();
	    	return this;
	}
	
	@Transactional(readOnly=false)
	public void update( InschrijvingX inschrijving, Status status ) {
	
		// if status = SUBMITTED
		// then
		// 	send e-mail to secretariaat
		
		InschrijvingX loaded
			= this.secretariaatsMedewerker.findInschrijving( inschrijving.getUuid() );
		
		if ( Status.Value.SUBMITTED.equals( status.getValue() ) ) {
			
			if ( ! isComplete( loaded ) ) {
				return;
			}
			
			Organisatie organisation
				= inschrijving.getOrganisatie();
		
			MimeMessage message
				= formatIntakeMessage( loaded, organisation );

			if ( message != null ) {
				
				postBode.deliver( message );
				logger.info( "email sent" );
				
			}
			
		}
		
		loaded.getStatus().setValue( status.getValue() );
		loaded.setInschrijvingsdatum( new Date() );
		this.inschrijvingXRepository.saveAndFlush( loaded );

		if ( Boolean.TRUE.equals( status.getEmailMe() ) ) {
			
			MimeMessage message
				= formatUpdateMessage( loaded );

			if ( message != null ) {
				
				postBode.deliver( message );
				logger.info( "email sent" );
				
			}
			
		}
			
	}
	
	protected MimeMessage formatIntakeMessage( InschrijvingX inschrijving, Organisatie organisation ) {
		
		MimeMessage message
			= null;
		
		Configuration cfg 
			= new Configuration();
	
		try {
			
			InputStream tis
				= this.getClass().getResourceAsStream( "/templates/intake.tmpl" );
			
			Template template 
				= new Template("code", new InputStreamReader( tis ), cfg );
			
			Map<String, Object> model = new HashMap<String, Object>();
					
			model.put( "organisatie", organisation );
			model.put( "inschrijving", inschrijving );
			model.put( "id", inschrijving.getUuid() );
			// TODO use vakantiedetails for more efficiency ?
			model.put("vakanties", vakanties( inschrijving ) );
			
			StringWriter bodyWriter 
				= new StringWriter();
			
			template.process( model , bodyWriter );
			
			bodyWriter.flush();
				
			message = this.javaMailSender.createMimeMessage();
			MimeMessageHelper helper = new MimeMessageHelper(message,"utf-8");
				
			helper.setFrom( PirlewietApplicationConfig.EMAIL_ADDRESS );
			helper.setTo( "info@pirlewiet.be" );
			helper.setReplyTo( inschrijving.getContactGegevens().getEmail() );
			helper.setSubject( "Nieuwe inschrijving" );
				
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
	
	protected MimeMessage formatUpdateMessage( InschrijvingX inschrijving ) {
		
		MimeMessage message
			= null;
		
		Configuration cfg 
			= new Configuration();
	
		try {
			
			InputStream tis
				= this.getClass().getResourceAsStream( "/templates/update.tmpl" );
			
			Template template 
				= new Template("code", new InputStreamReader( tis ), cfg );
			
			Map<String, Object> model = new HashMap<String, Object>();
					
			model.put( "organisatie", inschrijving.getOrganisatie() );
			model.put( "inschrijving", inschrijving );
			// TODO use vakantiedetails for more efficiency ?
			model.put("vakanties", vakanties( inschrijving ) );
			
			StringWriter bodyWriter 
				= new StringWriter();
			
			template.process( model , bodyWriter );
			
			bodyWriter.flush();
				
			message = this.javaMailSender.createMimeMessage();
			MimeMessageHelper helper = new MimeMessageHelper(message);
				
			helper.setFrom( PirlewietApplicationConfig.EMAIL_ADDRESS );
			helper.setTo( "info@pirlewiet.be" );
			helper.setReplyTo( inschrijving.getContactGegevens().getEmail() );
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
	
	protected String vakanties( InschrijvingX inschrijving ) {
		
		StringBuilder b
			= new StringBuilder();
		
		for (  Vakantie v : inschrijving.getVakanties() ) {
			
			if ( v != null ) {
				b.append( b.length() == 0 ? v.getNaam() : new StringBuilder(", ").append( v.getNaam() ));
			}
			
		}
		
		return b.toString();
		
	} 
	
	protected boolean isComplete( InschrijvingX inschrijving ) {
		
		boolean complete
			= true;
		
		for ( Vraag vraag : inschrijving.getVragen() ) {
			
			if ( ! Vraag.Type.Label.equals( vraag.getType() ) ) {
				if ( ! Tags.TAG_MEDIC.equals( vraag.getTag() ) ) { 
					if ( ( vraag.getAntwoord() == null ) || ( vraag.getAntwoord().isEmpty() ) ) {
						logger.info( "question [{}] was not answered", vraag.getVraag() );
						complete = false;
						break;
					}
				}
			}
			
		}
		
		return complete;
	}

}
 
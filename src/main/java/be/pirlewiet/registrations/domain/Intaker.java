package be.pirlewiet.registrations.domain;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringWriter;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.mail.internet.MimeMessage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.transaction.annotation.Transactional;

import be.pirlewiet.registrations.application.config.PirlewietApplicationConfig;
import be.pirlewiet.registrations.model.InschrijvingX;
import be.pirlewiet.registrations.model.Organisatie;
import be.pirlewiet.registrations.model.Status;
import be.pirlewiet.registrations.model.Status.Value;
import be.pirlewiet.registrations.model.Tags;
import be.pirlewiet.registrations.model.Vakantie;
import be.pirlewiet.registrations.model.Vraag;
import be.pirlewiet.registrations.repositories.InschrijvingXRepository;
import be.pirlewiet.registrations.repositories.VakantieRepository;
import be.pirlewiet.registrations.web.util.DataGuard;
import freemarker.template.Configuration;
import freemarker.template.Template;

/*
 * Receives applications, checks them and passes them on to the secretaries, notifying them and the applicant via e-mail.
 * 
 */
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
	protected HeadQuarters headQuarters;
	
	@Resource
	JavaMailSender javaMailSender;
	
	@Resource
	DataGuard dataGuard;
	
	@Resource
	Mapper mapper;
	
	public Intaker guard() {
	    	this.dataGuard.guard();
	    	return this;
	}
	
	@Transactional(readOnly=false)
	public void update( InschrijvingX inschrijving, Status status ) {
	
		// if new then send intake email to pirlewiet, otherwise send update email
		
		InschrijvingX loaded
			= this.secretariaatsMedewerker.findInschrijving( inschrijving.getUuid() );
		
		if ( Status.Value.AUTO.equals( status.getValue() ) && ( Status.Value.DRAFT.equals( loaded.getStatus().getValue() ) ) ) {
			
			if ( ! isComplete( loaded ) ) {
				return;
			}
			
			Organisatie organisation
				= inschrijving.getOrganisatie();
			
			loaded.setInschrijvingsdatum( new Date() );
			loaded.getStatus().setValue( Value.SUBMITTED );
			if ( isEmpty( status.getComment() ) ) {
				loaded.getStatus().setComment( "Geen commentaar gegeven" );
			}
			else {
				loaded.getStatus().setComment( status.getComment() );
			}
			
			this.inschrijvingXRepository.saveAndFlush( loaded );
		
			MimeMessage message
				= formatIntakeMessage( loaded, organisation );

			if ( message != null ) {
				
				postBode.deliver( message );
				
			}
			
		}
		else {
			
			loaded.getStatus().setComment( status.getComment() );

			if ( Boolean.TRUE.equals( status.getEmailMe() ) ) {
				
				MimeMessage message
					= formatUpdateMessage( loaded );
	
				if ( message != null ) {
					
					postBode.deliver( message );
					
				}
				
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
				
			message = this.javaMailSender.createMimeMessage( );
			// SGL| GAE does not support multipart_mode_mixed_related (default, when flag true is set)
			MimeMessageHelper helper = new MimeMessageHelper(message, MimeMessageHelper.MULTIPART_MODE_MIXED, "utf-8");
				
			helper.setFrom( PirlewietApplicationConfig.EMAIL_ADDRESS );
			helper.setTo( headQuarters.getEmail() );
			helper.setReplyTo( inschrijving.getContactGegevens().getEmail() );
			helper.setSubject( "Nieuwe inschrijving" );
				
			String text
				= bodyWriter.toString();
				
			logger.info( "email text is [{}]", text );
				
			helper.setText(text, true);
			
			List<String[]> strings
				= this.mapper.asStrings( Arrays.asList( inschrijving ), null );
			
			if ( strings != null ) {
				
				byte[] bytes
					= this.mapper.asBytes( strings );
				
				ByteArrayResource bis
					= new ByteArrayResource( bytes );
				
				helper.addAttachment( 
						new StringBuilder( inschrijving.getUuid() ).append( ".xlsx" ).toString(),
						bis,
						"application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
			}
				
		}
		catch( Exception e ) {
			logger.warn( "could not create e-mail", e );
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
			helper.setTo( headQuarters.getEmail() );
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
	
	 protected boolean isEmpty( String x ) {
	    	
		 return ( x == null ) || ( x.trim().isEmpty() );
	    	
	 }

}
 
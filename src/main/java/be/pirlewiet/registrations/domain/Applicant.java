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
import be.pirlewiet.registrations.repositories.EnrollmentRepository;
import be.pirlewiet.registrations.repositories.VakantieRepository;
import be.pirlewiet.registrations.web.util.DataGuard;
import freemarker.template.Configuration;
import freemarker.template.Template;

/*
 * Receives applications, checks them and passes them on to the secretaries, notifying them and the applicant via e-mail.
 * 
 */
public class Applicant {

	protected final Logger logger
		= LoggerFactory.getLogger( this.getClass() );
	
	@Resource
	PostBode postBode;
	
	@Resource
	VakantieRepository vakantieRepository;
	
	@Resource
	protected SecretariaatsMedewerker secretariaatsMedewerker;
	
	@Resource
	protected EnrollmentRepository inschrijvingXRepository;
	
	@Resource
	protected HeadQuarters headQuarters;
	
	@Resource
	JavaMailSender javaMailSender;
	
	@Resource
	DataGuard dataGuard;
	
	@Resource
	Mapper mapper;
	
	public Applicant guard() {
	    	this.dataGuard.guard();
	    	return this;
	}
	
	@Transactional(readOnly=false)
	public void updateStatus( InschrijvingX inschrijving, Status status ) {
	
		// if new then send intake email to pirlewiet, otherwise send update email
		
		InschrijvingX loaded
			= this.secretariaatsMedewerker.findInschrijving( inschrijving.getUuid() );
		
		if ( Status.Value.AUTO.equals( status.getValue() ) ) {
			
			if ( Status.Value.DRAFT.equals( loaded.getStatus().getValue() ) ) {
				
				// should check completeness ? TODO
			
				submit( loaded, status );
			}
			
		}
		else {
			
			loaded.getStatus().setComment( status.getComment() );
			loaded.getStatus().setValue( status.getValue() );
			
			this.inschrijvingXRepository.saveAndFlush( loaded );
			
			logger.info( "updated enrollment [{}] to status [{}]", loaded.getUuid(), loaded.getStatus().getValue() );
			
			List<InschrijvingX> related
				= this.inschrijvingXRepository.findByReference( loaded.getUuid() );
			
			for ( InschrijvingX r : related ) {
				
				r.getStatus().setValue( status.getValue() );
				this.inschrijvingXRepository.saveAndFlush( r );
				
				logger.info( "updated related enrollment [{}] to status [{}]", r.getUuid(), r.getStatus().getValue() );
				
			}

			if ( Boolean.TRUE.equals( status.getEmailMe() ) ) {
				
				/*
				MimeMessage message
					= formatUpdateMessage( loaded, related );
	
				if ( message != null ) {
					
					postBode.deliver( message );
					
				}
				*/
				
			}
		}
			
	}
	
	protected InschrijvingX submit( InschrijvingX enrollment, Status status ) {
		
		Date submitted
			= new Date();
		
		enrollment.setInschrijvingsdatum( submitted );
		
		enrollment.getStatus().setValue( Value.SUBMITTED );
		
		if ( isEmpty( status.getComment() ) ) {
			enrollment.getStatus().setComment( "Geen commentaar gegeven" );
		}
		else {
			enrollment.getStatus().setComment( status.getComment() );
		}
		
		this.inschrijvingXRepository.saveAndFlush( enrollment );
		
		if ( enrollment.getReference() != null ) {
			// only intake for 'head' enrollments
			return null;
		}
		
		// also update related enrollments
		List<InschrijvingX> related
			= this.inschrijvingXRepository.findByReference( enrollment.getUuid() );
		
		for ( InschrijvingX r : related ) {
			
			r.getStatus().setValue( Value.SUBMITTED );
			r.setInschrijvingsdatum( submitted );
			this.inschrijvingXRepository.saveAndFlush( r );
			
		}
		
		// send e-mails
		Organisatie organisation
			= enrollment.getOrganisatie();
		
		this.sendSubmittedEmailToPirlewiet( enrollment, related, organisation );
		this.sendSubmittedEmailToOrganisation( enrollment, related, organisation);
		
		return enrollment;
			
	}
	
	 protected boolean sendSubmittedEmailToOrganisation( InschrijvingX enrollment, List<InschrijvingX> related, Organisatie organisation ) {
	    	
			MimeMessage message
				= formatIntakeMessageToOrganisation( enrollment, related, organisation );

			if ( message != null ) {
				
				return postBode.deliver( message );
				
			}
			
		return false;
		
	}
	 
	 protected boolean sendSubmittedEmailToPirlewiet( InschrijvingX enrollment, List<InschrijvingX> related, Organisatie organisation ) {
	    	
		MimeMessage message
			= formatIntakeMessageToPirlewiet( enrollment, related, organisation );
		
		if ( message != null ) {
			
			return postBode.deliver( message );
			
		}
			
		return false;
		
	}
	 
	protected MimeMessage formatIntakeMessageToOrganisation( InschrijvingX enrollment, List<InschrijvingX> related, Organisatie organisation ) {
			
		String templateString
			= "/templates/to-organisation/enrollment-takenin.tmpl";
		
		String to
			= enrollment.getContactGegevens().getEmail();
		
		MimeMessage message
			= null;
			
		Configuration cfg 
			= new Configuration();
		
		try {
			
			InputStream tis
				= this.getClass().getResourceAsStream( templateString );
			
			Template template 
				= new Template("code", new InputStreamReader( tis ), cfg );
			
			Map<String, Object> model = new HashMap<String, Object>();
					
			model.put( "organisation", organisation );
			model.put( "enrollment", enrollment );
			model.put( "id", enrollment.getUuid() );
			model.put( "related", related );
			// TODO use vakantiedetails for more efficiency ?
			model.put("vakanties", vakanties( enrollment ) );
			
			StringWriter bodyWriter 
				= new StringWriter();
			
			template.process( model , bodyWriter );
			
			bodyWriter.flush();
				
			message = this.javaMailSender.createMimeMessage( );
			// SGL| GAE does not support multipart_mode_mixed_related (default, when flag true is set)
			MimeMessageHelper helper = new MimeMessageHelper(message, MimeMessageHelper.MULTIPART_MODE_NO, "utf-8");
				
			helper.setFrom( PirlewietApplicationConfig.EMAIL_ADDRESS );
			helper.setTo( to );
			helper.setBcc( "sven.gladines@gmail.com" );
			helper.setReplyTo( enrollment.getContactGegevens().getEmail() );
			helper.setSubject( new StringBuilder( "Inschrijving ontvangen: " ).append( enrollment.getDeelnemers().get(0).getVoorNaam() ).append( " " ).append( enrollment.getDeelnemers().get( 0 ).getFamilieNaam() ).toString() );
				
			String text
				= bodyWriter.toString();
				
			logger.info( "email text is [{}]", text );
				
			helper.setText(text, true);
			
		}
		catch( Exception e ) {
			logger.warn( "could not create e-mail", e );
			throw new RuntimeException( e );
		}
		
		return message;
    	
    }
	 
	
	protected MimeMessage formatIntakeMessageToPirlewiet( InschrijvingX application, List<InschrijvingX> related, Organisatie organisation ) {
		
		String templateString
			 = "/templates/to-pirlewiet/enrollment-takenin.tmpl";
		
		String to
			= this.headQuarters.getEmail();
		
		MimeMessage message
			= null;
		
		Configuration cfg 
			= new Configuration();
	
		try {
			
			InputStream tis
				= this.getClass().getResourceAsStream( templateString );
			
			Template template 
				= new Template("code", new InputStreamReader( tis ), cfg );
			
			Map<String, Object> model = new HashMap<String, Object>();
					
			model.put( "organisation", organisation );
			model.put( "enrollment", application );
			model.put( "id", application.getUuid() );
			model.put( "related", related );
			// TODO use vakantiedetails for more efficiency ?
			model.put("vakanties", vakanties( application ) );
			
			StringWriter bodyWriter 
				= new StringWriter();
			
			template.process( model , bodyWriter );
			
			bodyWriter.flush();
				
			message = this.javaMailSender.createMimeMessage( );
			// SGL| GAE does not support multipart_mode_mixed_related (default, when flag true is set)
			MimeMessageHelper helper = new MimeMessageHelper(message, MimeMessageHelper.MULTIPART_MODE_MIXED, "utf-8");
				
			helper.setFrom( PirlewietApplicationConfig.EMAIL_ADDRESS );
			helper.setTo( to );
			helper.setBcc( "sven.gladines@gmail.com" );
			helper.setReplyTo( application.getContactGegevens().getEmail() );
			helper.setSubject( "Nieuwe inschrijving" );
				
			String text
				= bodyWriter.toString();
				
			logger.info( "email text is [{}]", text );
				
			helper.setText(text, true);
			
			if ( related != null ) {
				logger.info( "[{}]; [{}] related enrollments", application.getUuid(), related.size() );
			}
			else {
				logger.info( "[{}]; no related enrollments", application.getUuid() );
			}
			
			List<String[]> rows
				= this.mapper.asStrings( application, related, Status.Value.SUBMITTED );
			
			if ( rows != null ) {
				
				byte[] bytes
					= this.mapper.asBytes( rows );
				
				ByteArrayResource bis
					= new ByteArrayResource( bytes );
				
				helper.addAttachment( 
						new StringBuilder( application.getUuid() ).append( ".xlsx" ).toString(),
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
	
	 protected boolean isEmpty( String x ) {
	    	
		 return ( x == null ) || ( x.trim().isEmpty() );
	    	
	 }

}
 
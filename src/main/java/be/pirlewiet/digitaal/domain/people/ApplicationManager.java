package be.pirlewiet.digitaal.domain.people;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringWriter;
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

import be.pirlewiet.digitaal.application.config.PirlewietApplicationConfig;
import be.pirlewiet.digitaal.domain.HeadQuarters;
import be.pirlewiet.digitaal.domain.Mapper;
import be.pirlewiet.digitaal.model.Enrollment;
import be.pirlewiet.digitaal.model.EnrollmentStatus;
import be.pirlewiet.digitaal.model.EnrollmentStatus.Value;
import be.pirlewiet.digitaal.model.Holiday;
import be.pirlewiet.digitaal.model.Organisation;
import be.pirlewiet.digitaal.repositories.EnrollmentRepository;
import be.pirlewiet.digitaal.repositories.HolidayRepository;
import be.pirlewiet.digitaal.web.util.DataGuard;
import freemarker.template.Configuration;
import freemarker.template.Template;

/*
 * Receives applications, checks them and passes them on to the secretaries, notifying them and the applicant via e-mail.
 * 
 */
public class ApplicationManager {

	protected final Logger logger
		= LoggerFactory.getLogger( this.getClass() );
	
	@Resource
	MailMan postBode;
	
	@Resource
	HolidayRepository holidayRepository;
	
	@Resource
	protected Secretary secretary;
	
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
	
	public ApplicationManager guard() {
	    	this.dataGuard.guard();
	    	return this;
	}
	
	@Transactional(readOnly=false)
	public void updateStatus( Enrollment inschrijving, EnrollmentStatus status ) {
	
		// if new then send intake email to pirlewiet, otherwise send update email
		
		Enrollment loaded
			= this.secretary.findInschrijving( inschrijving.getUuid() );
		
		if ( EnrollmentStatus.Value.AUTO.equals( status.getValue() ) ) {
			
			if ( EnrollmentStatus.Value.DRAFT.equals( loaded.getStatus().getValue() ) ) {
				
				// should check completeness ? TODO
			
				submit( loaded, status );
			}
			
		}
		else {
			
			loaded.getStatus().setComment( status.getComment() );
			loaded.getStatus().setValue( status.getValue() );
			
			this.inschrijvingXRepository.saveAndFlush( loaded );
			
			logger.info( "updated enrollment [{}] to status [{}]", loaded.getUuid(), loaded.getStatus().getValue() );
			
			List<Enrollment> related
				= this.inschrijvingXRepository.findByReference( loaded.getUuid() );
			
			for ( Enrollment r : related ) {
				
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
	
	protected Enrollment submit( Enrollment enrollment, EnrollmentStatus status ) {
		
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
		List<Enrollment> related
			= this.inschrijvingXRepository.findByReference( enrollment.getUuid() );
		
		for ( Enrollment r : related ) {
			
			r.getStatus().setValue( Value.SUBMITTED );
			r.setInschrijvingsdatum( submitted );
			this.inschrijvingXRepository.saveAndFlush( r );
			
		}
		
		// send e-mails
		Organisation organisation
			= enrollment.getOrganisatie();
		
		this.sendSubmittedEmailToPirlewiet( enrollment, related, organisation );
		this.sendSubmittedEmailToOrganisation( enrollment, related, organisation);
		
		return enrollment;
			
	}
	
	 protected boolean sendSubmittedEmailToOrganisation( Enrollment enrollment, List<Enrollment> related, Organisation organisation ) {
	    	
			MimeMessage message
				= formatIntakeMessageToOrganisation( enrollment, related, organisation );

			if ( message != null ) {
				
				return postBode.deliver( message );
				
			}
			
		return false;
		
	}
	 
	 protected boolean sendSubmittedEmailToPirlewiet( Enrollment enrollment, List<Enrollment> related, Organisation organisation ) {
	    	
		MimeMessage message
			= formatIntakeMessageToPirlewiet( enrollment, related, organisation );
		
		if ( message != null ) {
			
			return postBode.deliver( message );
			
		}
			
		return false;
		
	}
	 
	protected MimeMessage formatIntakeMessageToOrganisation( Enrollment enrollment, List<Enrollment> related, Organisation organisation ) {
			
		String templateString
			= "/templates/to-organisation/enrollment-submitted.tmpl";
		
		String to
			= enrollment.getContactGegevens().getEmail();
		
		MimeMessage message
			= null;
			
		Configuration cfg 
			= new Configuration();
		
		try {
			
			cfg.setDefaultEncoding( "utf-8");
			
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
	 
	
	protected MimeMessage formatIntakeMessageToPirlewiet( Enrollment application, List<Enrollment> related, Organisation organisation ) {
		
		String templateString
			 = "/templates/to-pirlewiet/enrollment-submitted.tmpl";
		
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
				= this.mapper.asStrings( application, related, EnrollmentStatus.Value.SUBMITTED );
			
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
	
	protected String vakanties( Enrollment inschrijving ) {
		
		StringBuilder b
			= new StringBuilder();
		
		for (  Holiday v : inschrijving.getVakanties() ) {
			
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
 
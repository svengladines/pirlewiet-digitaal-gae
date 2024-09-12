package be.pirlewiet.digitaal.domain.people;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringWriter;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import be.pirlewiet.digitaal.repository.ApplicationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import jakarta.mail.internet.MimeMessage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.MessageSource;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.LocaleResolver;

import be.pirlewiet.digitaal.application.config.PirlewietApplicationConfig;
import be.pirlewiet.digitaal.domain.HeadQuarters;
import be.pirlewiet.digitaal.domain.q.QIDs;
import be.pirlewiet.digitaal.domain.q.QuestionSheet;
import be.pirlewiet.digitaal.model.Application;
import be.pirlewiet.digitaal.model.Enrollment;
import be.pirlewiet.digitaal.model.EnrollmentStatus;
import be.pirlewiet.digitaal.model.Holiday;
import be.pirlewiet.digitaal.model.HolidayType;
import be.pirlewiet.digitaal.model.Person;
import be.pirlewiet.digitaal.model.QuestionAndAnswer;
import be.pirlewiet.digitaal.model.Tags;
import be.pirlewiet.digitaal.repository.EnrollmentRepository;
import freemarker.template.Configuration;
import freemarker.template.Template;

@Component
public class EnrollmentManager {

	protected final Logger logger
		= LoggerFactory.getLogger( this.getClass() );
	
	@Autowired
	protected EnrollmentRepository enrollmentRepository;
	
	@Autowired
	protected QuestionAndAnswerManager questionAndAnswerManager;
	
	@Autowired
	protected HolidayManager holidayManager;
	
	@Autowired
	protected ApplicationRepository applicationRepository;
	
	@Autowired
	protected OrganisationManager organisationManager;
	
	@Autowired
	protected PersonManager personManager;
	
	@Autowired
	MailMan mailMan;
	
	@Autowired
	HeadQuarters headQuarters;
	
	@Autowired
	MessageSource messageSource;
	
	@Autowired
	LocaleResolver localeResolver;
	
	public EnrollmentManager( ) {
	}
	
	public List<Enrollment> findByApplicationUuid( String applicationUuid ) {
		
		List<Enrollment> enrollments
			= this.enrollmentRepository.findByApplicationUuid( applicationUuid );
		
		// TODO, sort ...
		
		return enrollments;
		
	}
	
	public Enrollment findOneByUuid( String uuid ) {
		return this.enrollmentRepository.findByUuid( uuid );
	}
	
	public List<Enrollment> findAll( ) {
		return this.enrollmentRepository.findAll();
	}
	
	public List<Enrollment> findMostRecent( ) {
		return Arrays.asList( );// this.enrollmentRepository.findFirst100ByOrderByCreatedDateDesc();
	}
	
	public Enrollment create( Enrollment toCreate ) {
		
		toCreate.setUuid( UUID.randomUUID().toString() );
		
		Enrollment created
			= this.enrollmentRepository.saveAndFlush( toCreate );
		
		List<QuestionAndAnswer> participant
			= QuestionSheet.template().getQuestions( ).get( Tags.TAG_PARTICIPANT );
	
		for ( QuestionAndAnswer qna : participant ) {
			qna.setUuid( UUID.randomUUID().toString() );
			qna.setEntityUuid( created.getUuid() );
			this.questionAndAnswerManager.create( qna );
		}
		
		Application application
			= this.applicationRepository.findByUuid(created.getApplicationUuid());
		
		Set<Holiday> holidays
			= this.holidayManager.holidaysFromUUidString( application.getHolidayUuids() );
		
		// for VOV, add questions about partner (2018)
		if ( this.holidayManager.hasType( holidays, HolidayType.Vov ) ) {
			logger.info( "VOV; add questions...");
			QuestionAndAnswer qna = QuestionSheet.template().getQuestion( QIDs.QID_ADULTERY_WITH );
			qna.setUuid( UUID.randomUUID().toString() );
			qna.setTag( Tags.TAG_PARTICIPANT );
			qna.setEntityUuid( created.getUuid() );
			this.questionAndAnswerManager.create( qna );
			QuestionAndAnswer qna2 = QuestionSheet.template().getQuestion( QIDs.QID_ADULTERY_WITH_WHO );
			qna2.setUuid( UUID.randomUUID().toString() );
			qna2.setTag( Tags.TAG_PARTICIPANT );
			qna2.setEntityUuid( created.getUuid() );
			this.questionAndAnswerManager.create( qna2 );
		}
		
		return created;
		
	}


	public Enrollment update( Enrollment toUpdate, Enrollment update ) {
		
		// TODO
		toUpdate.setHolidayName( update.getHolidayName() );
		toUpdate.setHolidayUuid( update.getHolidayUuid() );
		if ( update.getStatus() != null ) {
			toUpdate.setStatus( update.getStatus() );
		}
		
		Enrollment updated
			= this.enrollmentRepository.saveAndFlush( toUpdate );
		
		return updated;
		
	}
	
	
	public Enrollment updateHolidays( String uuid, List<Holiday> holidays ) {
		
		logger.info("application.updateHolidays");
		
		Enrollment enrollment
			= this.findOneByUuid( uuid );
		
		if ( enrollment != null ) {
			
			StringBuilder uuids
				= new StringBuilder();
			
			StringBuilder names
				= new StringBuilder();
			
			for ( int i=0; i < holidays.size(); i++ ) {
				
				Holiday holiday = holidays.get( i );
				
				uuids.append( holiday.getUuid() );
				
				Holiday one
					= this.holidayManager.findOneByUuid( holiday.getUuid() );
				
				logger.info("found holiday [{}], adding...", one.getName() );
				
				names.append( one.getName() );
				
				if ( i < holidays.size() -1 ) {
					uuids.append(",");
					names.append(",");
				}
				
			}
			
			enrollment.setHolidayUuid( uuids.toString() );
			enrollment.setHolidayName( names.toString() );
			
			enrollment = this.enrollmentRepository.saveAndFlush( enrollment );
			
		}
		
		return enrollment;
	}
	
	public Enrollment updateHolidays( String uuid, String holidayUuids, boolean resolveNames ) {
		
		logger.info("enrollment.updateHolidays");
		
		Enrollment enrollment
			= this.findOneByUuid( uuid );
		
		if ( enrollment != null ) {
			
			enrollment.setHolidayUuid( holidayUuids );
			
			if ( resolveNames ) {
				
				Set<Holiday> holidays 
					= this.holidayManager.holidaysFromUUidString( holidayUuids );
				
				StringBuilder b
					= new StringBuilder();
				
				Iterator<Holiday> it
					= holidays.iterator();
				
				while ( it.hasNext() ) {
					Holiday holiday 
						= it.next();
					b.append( holiday.getName() );
					if ( it.hasNext() ) {
						b.append( "," );
					}
				}
				
				enrollment.setHolidayName( b.toString() );
			}
			
			enrollment = this.enrollmentRepository.saveAndFlush( enrollment );
			
		}
		
		return enrollment;
	}
	
	public void delete( Enrollment enrollment ) {
		
		this.enrollmentRepository.delete( enrollment );
		
	}
	
	public Enrollment updateStatus( String enrollmentUUid, EnrollmentStatus newStatus, boolean sendUpdate ) {
		
		Enrollment toUpdate
			= this.findOneByUuid( enrollmentUUid );
		
		EnrollmentStatus.Value oldStatus
			= toUpdate.getStatus().getValue();
		
		toUpdate.setStatus( newStatus );
		
		Enrollment updated
			= this.enrollmentRepository.saveAndFlush( toUpdate );
		
		Application application
			= this.applicationRepository.findByUuid(updated.getApplicationUuid());
		
		Person contact
			= this.personManager.findOneByUuid( application.getContactPersonUuid() );
		
		Set<Holiday> holidays
			= this.holidayManager.holidaysFromUUidString( updated.getHolidayUuid() );
		
		Person participant
			= this.personManager.findOneByUuid( updated.getParticipantUuid() );
		
		if ( sendUpdate ) {
			this.sendStatusUpdateToOrganisation( updated, participant, holidays, oldStatus, contact );
		}
		
		return updated;
		
	}
	
	public Enrollment template() {
		
		Enrollment enrollment 
			= new Enrollment();
		
		return enrollment;
		
	}

	public Enrollment updateQList( String uuid, List<QuestionAndAnswer> qList ) {
		
		logger.info("enrollment.updateQList");
		
		Enrollment enrollment
			= this.findOneByUuid( uuid );
		
		if ( enrollment != null ) {
			
			for ( QuestionAndAnswer q : qList ) {
				
				QuestionAndAnswer one
					= this.questionAndAnswerManager.findOneByUuid( q.getUuid() );
				
				if ( one != null ) {
					this.questionAndAnswerManager.update( one,q );
				}
				
			}
						
		}
		
		return enrollment;
	}
	
	 protected boolean sendStatusUpdateToOrganisation( Enrollment enrollment,  Person participant, Set<Holiday> holidays, EnrollmentStatus.Value oldStatus, Person contact ) {
	    	
			MimeMessage message
				= this.formatUpdateMessageToOrganisation( enrollment, participant, holidays, oldStatus, contact );

			if ( message != null ) {
				
				return mailMan.deliver( message );
				
			}
			
			return false;
		
	 }
	 
	 protected MimeMessage formatUpdateMessageToOrganisation( Enrollment enrollment, Person participant, Set<Holiday> holidays, EnrollmentStatus.Value oldStatus, Person recipient ) {
			
			MimeMessage message
				= null;
			
			Configuration cfg 
				= new Configuration();
		
			try {
				
				EnrollmentStatus newStatus
					= enrollment.getStatus();
				
				InputStream tis
					= this.getClass().getResourceAsStream( "/templates/to-organisation/enrollment-status-update.tmpl" );
				
				Template template 
					= new Template("code", new InputStreamReader( tis ), cfg );
				
				Map<String, Object> model = new HashMap<String, Object>();
				
				String oldStatusMessage
					= this.messageSource.getMessage( String.format( "enrollment.status.%s",  oldStatus) , new Object[] {}, localeResolver.resolveLocale( null ) );
				
				String newStatusMessage
					= this.messageSource.getMessage( String.format( "enrollment.status.%s",  newStatus.getValue() ) , new Object[] {}, localeResolver.resolveLocale( null ) );
				
				String newStatusDescription
					= this.messageSource.getMessage( String.format( "enrollment.status.%s.description",  newStatus.getValue() ) , new Object[] {}, localeResolver.resolveLocale( null ) );
						
				model.put( "enrollment", enrollment );
				model.put( "participant", participant );
				model.put( "holiday", enrollment.getHolidayName() );
				model.put( "oldStatusMessage", oldStatusMessage );
				model.put( "newStatusMessage", newStatusMessage );
				model.put( "newStatusDescription", newStatusDescription );
				model.put( "newStatusComment", newStatus.getComment() );
				model.put( "uuid", enrollment.getUuid() );
				
				StringWriter bodyWriter 
					= new StringWriter();
				
				template.process( model , bodyWriter );
				
				bodyWriter.flush();
					
				message = this.mailMan.message();
				// SGL| GAE does not support multipart_mode_mixed_related (default, when flag true is set)
				MimeMessageHelper helper = new MimeMessageHelper(message, MimeMessageHelper.MULTIPART_MODE_MIXED, "utf-8");
			
				helper.setTo( recipient.getEmail() );
				helper.setFrom( PirlewietApplicationConfig.EMAIL_ADDRESS );
				helper.setReplyTo( headQuarters.getEmail() );
				helper.setSubject( "Uw inschrijving bij Pirlewiet werd aangepast" );
					
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
 
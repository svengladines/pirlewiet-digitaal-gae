package be.pirlewiet.digitaal.domain.people;

import static be.occam.utils.javax.Utils.isEmpty;
import static be.occam.utils.javax.Utils.list;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringWriter;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import jakarta.mail.internet.MimeMessage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.mail.javamail.MimeMessageHelper;

import be.pirlewiet.digitaal.application.config.PirlewietApplicationConfig;
import be.pirlewiet.digitaal.domain.HeadQuarters;
import be.pirlewiet.digitaal.domain.q.QIDs;
import be.pirlewiet.digitaal.domain.q.QuestionSheet;
import be.pirlewiet.digitaal.model.Application;
import be.pirlewiet.digitaal.model.ApplicationStatus;
import be.pirlewiet.digitaal.model.Enrollment;
import be.pirlewiet.digitaal.model.EnrollmentStatus;
import be.pirlewiet.digitaal.model.Holiday;
import be.pirlewiet.digitaal.model.HolidayType;
import be.pirlewiet.digitaal.model.Organisation;
import be.pirlewiet.digitaal.model.Person;
import be.pirlewiet.digitaal.model.QuestionAndAnswer;
import be.pirlewiet.digitaal.model.Tags;
import be.pirlewiet.digitaal.repository.ApplicationRepository;
import freemarker.template.Configuration;
import freemarker.template.Template;
import org.springframework.stereotype.Component;

/*
 * Receives applications, checks them and passes them on to the secretaries, notifying them and the applicant via e-mail.
 * 
 */
@Component
public class ApplicationManager {

	protected final Logger logger
		= LoggerFactory.getLogger( this.getClass() );
	
	@Autowired
	MailMan mailMan;
	
	@Autowired
	HeadQuarters headQuarters;
	
	@Autowired
	OrganisationManager organisationManager;

	protected final Comparator<Application> mostRecentlySubmitted
		= new Comparator<Application>() {
		
		@Override
		public int compare(Application o1, Application o2) {
			
			Date d1 = o1.getSubmitted();
			Date d2 = o2.getSubmitted();
			
			if ( d1 == null ) {
				return 1;
			}
			else if ( d2 == null ) {
				return -1;
			}
			else {
				return  ( 0- d1.compareTo( d2 ) );
			}
			
		}
		
	};
	
	protected final Comparator<Application> mostRecentlyCreated
		= new Comparator<Application>() {
	
	@Override
	public int compare(Application o1, Application o2) {
		
		Date d1 = o1.getCreated();
		Date d2 = o2.getCreated();
		
		if ( d1 == null ) {
			return 1;
		}
		else if ( d2 == null ) {
			return -1;
		}
		else {
			return  ( 0- d1.compareTo( d2 ) );
		}
		
	}
	
};
	
	protected final int currentYear;
	
	@Autowired
	protected ApplicationRepository applicationRepository;
	
	@Autowired
	protected HolidayManager holidayManager;
	
	@Autowired
	protected PersonManager personManager;
	
	@Autowired
	protected QuestionAndAnswerManager questionAndAnswerManager;
	
	@Autowired
	protected Secretary secretary;
	
	@Autowired
	protected EnrollmentManager enrollmentManager;
	
	public ApplicationManager( int currentYear ) {
		this.currentYear = currentYear;
	}
	
	public List<Application> findByOrganisation( Organisation actor ) {
		
		logger.info( "find applications for organisation [{}] and year 2024", actor.getUuid() );
		List<Application> byOrganisationAndYear
			= this.applicationRepository.findByOrganisationUuidAndYear( actor.getUuid(), 2024 );//.findByYear(2019);//.findAll();//.findByOrganisationUuidAndYear( actor.getUuid(), this.currentYear );
		
		Collections.sort( byOrganisationAndYear, this.mostRecentlyCreated );
		
		return byOrganisationAndYear;
		
	}
	
	public List<Application> findAll( ) {
		
		List<Application> all
			= this.applicationRepository.findAll();
	
		return all;
		
	}
	
	public List<Application> findActiveByYear( ) {
		
		List<Application> byYear
			= this.applicationRepository.findByYear( this.currentYear );//.findByYear(2019);//.findAll();//.findByOrganisationUuidAndYear( actor.getUuid(), this.currentYear );
		
		List<Application> filtered
			= list();
		
		for ( Application application : byYear ) {
			
			if ( ( ! ApplicationStatus.Value.DRAFT.equals( application.getStatus().getValue() ) ) && ( ! ApplicationStatus.Value.CANCELLED.equals( application.getStatus().getValue() ) ) ) {
				filtered.add( application );
			}
		
			
		}
		
		Collections.sort( filtered, this.mostRecentlySubmitted );
		
		return filtered;
		
	}
	
	public Application findOne( String uuid ) {
		
		Application one
			= this.applicationRepository.findByUuid( uuid );
		
		return one;
		
	}
	
	   public Application create( Application application, Organisation actor ) {
	    	
	    	application.setYear( this.currentYear );
	    	application.setStatus( new ApplicationStatus( ApplicationStatus.Value.DRAFT ) );
	    	application.setHolidayUuids( "" );
	    	application.setHolidayNames( "" );
	    	application.setOrganisationUuid( actor.getUuid() );
	    	application.setUuid( UUID.randomUUID().toString() );
	    	
	    	Application created 
	    		= this.applicationRepository.saveAndFlush( application );
	    	
	    	logger.info( "created application with uuid [{}]", new Object[] { created.getUuid() } );
	    	
	    	List<QuestionAndAnswer> appQList
				= QuestionSheet.template().getQuestions( ).get( Tags.TAG_APPLICATION );
	    	
			for ( QuestionAndAnswer qna : appQList ) {
				qna.setEntityUuid( created.getUuid() );
				this.questionAndAnswerManager.create( qna );
			}
	    	
	    	return created;
	    	
	    }
	
	public Application updateHolidays( String uuid, List<Holiday> holidays ) {
		
		logger.info("application.updateHolidays");
		
		Application application
			= this.findOne( uuid );
		
		if ( application != null ) {
			
			StringBuilder uuids
				= new StringBuilder();
			
			StringBuilder names
				= new StringBuilder();
			
			List<Holiday> loaded = list();
			
			for ( int i=0; i < holidays.size(); i++ ) {
				
				Holiday holiday = holidays.get( i );
				
				uuids.append( holiday.getUuid() );
				
				Holiday one
					= this.holidayManager.findOneByUuid( holiday.getUuid() );
				
				logger.info("found holiday [{}], adding...", one.getName() );
				
				loaded.add( one );
				
				names.append( one.getName() );
				
				if ( i < holidays.size() -1 ) {
					uuids.append(",");
					names.append(",");
				}
				
			}
			
			application.setHolidayUuids( uuids.toString() );
			application.setHolidayNames( names.toString() );
			
			application = this.applicationRepository.saveAndFlush( application );
			
			// for CAVASOL, add question(s) about mobility (family...car)
			if ( this.holidayManager.hasType( loaded, HolidayType.CavaSol ) ) {
				logger.info( "CAVASOL; add questions...");
				QuestionAndAnswer familycar
					= QuestionSheet.template().getQuestion(QIDs.QID_FAMILY_CAR);
				familycar.setEntityUuid( application.getUuid() );
				logger.info( "CAVASOL; add question about family car for application [{}]", application.getUuid() );
				this.questionAndAnswerManager.create( familycar );
			}
			
		}
		
		return application;
	}
	
	public Application updateQList( String uuid, List<QuestionAndAnswer> qList ) {
		
		logger.info("application.updateQList");
		
		Application application
			= this.findOne( uuid );
		
		if ( application != null ) {
			
			for ( QuestionAndAnswer q : qList ) {
				
				QuestionAndAnswer one
					= this.questionAndAnswerManager.findOneByUuid( q.getUuid() );
				
				if ( one != null ) {
					this.questionAndAnswerManager.update( one,q );
				}
				
			}
						
		}
		
		return application;
	}
	
	public Application updateContact( String uuid, Person contactPerson ) {
		
		logger.info("application.updateContact");
		
		Application application
			= this.findOne( uuid );
		
		if ( application != null ) {
			
			StringBuilder uuids
				= new StringBuilder();
			
			StringBuilder names
				= new StringBuilder();
			
			Person p
				= isEmpty(contactPerson.getUuid() ) ? this.personManager.template() : this.personManager.findOneByUuid( contactPerson.getUuid() ) ;
				
			if ( ! isEmpty(contactPerson.getUuid() ) && ( p != null ) ) {
				logger.info("found existing person [{} {}], update...", p.getGivenName(), p.getFamilyName() );	
			}
			else if ( p == null ) {
				logger.info("existing person with uuid [{}] not found ...",  contactPerson.getUuid() );
			}
			else {
				logger.info("no existing person [{} {}], create..." );
			}
			
			p = this.personManager.update( p, contactPerson );
			
			application.setContactPersonUuid( p.getUuid() );
			application.setContactPersonName( String.format( "%s %s", p.getGivenName(), p.getFamilyName() ) );
			
			application = this.applicationRepository.saveAndFlush( application );
			
		}
		
		return application;
	}
	
	public Application updateStatus( String uuid, ApplicationStatus applicationStatus ) {
		
		logger.info("application.updateStatus");
		
		Application application
			= this.findOne( uuid );
		
		if ( application != null ) {
			
			boolean save
				= false;
			
			if ( ApplicationStatus.Value.AUTO.equals( applicationStatus.getValue() ) ) {
				
				if ( ApplicationStatus.Value.DRAFT.equals( application.getStatus().getValue() ) ) {
					logger.info( "intake");
					// TODO, load and manager will load again. performance optimization possible
					List<Enrollment> enrollments
						= this.enrollmentManager.findByApplicationUuid( application.getUuid() );
					for ( Enrollment enrollment : enrollments ) {
						if ( ( isEmpty( enrollment.getHolidayUuid() ) || ( ! enrollment.getHolidayUuid().equals( application.getHolidayUuids() )) ) || ( isEmpty( enrollment.getHolidayName() ) ) ) {
							this.enrollmentManager.updateHolidays( enrollment.getUuid(), application.getHolidayUuids(), true );
						}
						this.enrollmentManager.updateStatus( enrollment.getUuid(), new EnrollmentStatus( EnrollmentStatus.Value.TRANSIT ), false );
					}
					applicationStatus.setValue( ApplicationStatus.Value.SUBMITTED );
					application.setSubmitted( new Date() );
					save = true;
					
					Organisation organisation
						= this.organisationManager.findOneByUuid( application.getOrganisationUuid() );
					
					Person contact
						= this.personManager.findOneByUuid( application.getContactPersonUuid() );
					
					Set<Holiday> holidays
						= this.holidayManager.holidaysFromUUidString( application.getHolidayUuids() );
					
					List<Person> participants
						= list();
					
					for ( Enrollment enrollment : enrollments ) {
						
						Person participant
							= this.personManager.findOneByUuid( enrollment.getParticipantUuid() );
						
						participants.add( participant );
						
					}
					
					this.sendIntakeMessageToOrganisation(application, participants, holidays, contact);
					// TODO, email to pwt
					this.sendIntakeMessageToPirlewiet(application, participants, holidays, contact, organisation);
					logger.info( "taken in");
					
				}
				
			}
			else if ( ApplicationStatus.Value.CANCELLED.equals( applicationStatus.getValue() ) ) {
				
				if ( ApplicationStatus.Value.DRAFT.equals( application.getStatus().getValue() ) ) {
					logger.info( "delete draft");
					// TODO delete all enrollments
					this.applicationRepository.delete( application );	
				}
				else if ( ApplicationStatus.Value.SUBMITTED.equals( application.getStatus().getValue() ) ) {
					logger.info( "cancel submitted");
					applicationStatus.setValue( ApplicationStatus.Value.CANCELLED );
					save = true;	
				}
				
			}
			
			if ( save ) {
				application.setStatus( applicationStatus );
				application = this.applicationRepository.saveAndFlush( application );
			}
			
		}
		
		return application;
	}
	
	protected boolean sendIntakeMessageToOrganisation( Application application,  List<Person> participants, Set<Holiday> holidays, Person contact ) {
	    	
			MimeMessage message
				= this.formatIntakeMessageOrganisation( application, participants, holidays, contact );

			if ( message != null ) {
				
				return mailMan.deliver( message );
				
			}
			
			return false;
		
	 }
	 
	 protected MimeMessage formatIntakeMessageOrganisation( Application application, List<Person> participants, Set<Holiday> holidays, Person recipient ) {
			
			MimeMessage message
				= null;
			
			Configuration cfg 
				= new Configuration();
		
			try {
				
				InputStream tis
					= this.getClass().getResourceAsStream( "/templates/to-organisation/intake.tmpl" );
				
				Template template 
					= new Template("intake", new InputStreamReader( tis ), cfg );
				
				Map<String, Object> model = new HashMap<String, Object>();
				
				model.put( "application", application );
				model.put( "participants", participants );
				model.put( "holidays", holidays );
				model.put( "uuid", application.getUuid() );
				
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
	 
	 protected boolean sendIntakeMessageToPirlewiet( Application application,  List<Person> participants, Set<Holiday> holidays, Person contact, Organisation organisation ) {
	    	
			MimeMessage message
				= this.formatIntakeMessagePirlewiet( application, participants, holidays, contact, organisation );

			if ( message != null ) {
				
				return mailMan.deliver( message );
				
			}
			
			return false;
		
	 }
	 
	 protected MimeMessage formatIntakeMessagePirlewiet( Application application, List<Person> participants, Set<Holiday> holidays, Person contact, Organisation organisation ) {
			
			MimeMessage message
				= null;
			
			Configuration cfg 
				= new Configuration();
		
			try {
				
				InputStream tis
					= this.getClass().getResourceAsStream( "/templates/to-pirlewiet/intake.tmpl" );
				
				Template template 
					= new Template("intake", new InputStreamReader( tis ), cfg );
				
				Map<String, Object> model = new HashMap<String, Object>();
				
				model.put( "application", application );
				model.put( "participants", participants );
				model.put( "holidays", holidays );
				model.put( "uuid", application.getUuid() );
				model.put( "organisation", organisation );
				model.put( "contact", contact );
				
				StringWriter bodyWriter 
					= new StringWriter();
				
				template.process( model , bodyWriter );
				
				bodyWriter.flush();
					
				message = this.mailMan.message();
				// SGL| GAE does not support multipart_mode_mixed_related (default, when flag true is set)
				MimeMessageHelper helper = new MimeMessageHelper(message, MimeMessageHelper.MULTIPART_MODE_MIXED, "utf-8");
			
				helper.setTo( this.headQuarters.getEmail() );
				helper.setFrom( PirlewietApplicationConfig.EMAIL_ADDRESS );
				helper.setReplyTo( headQuarters.getEmail() );
				helper.setSubject( "Nieuw dossier ingediend" );
					
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
 
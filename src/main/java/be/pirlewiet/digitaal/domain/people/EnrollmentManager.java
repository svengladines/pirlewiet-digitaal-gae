package be.pirlewiet.digitaal.domain.people;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringWriter;
import java.util.*;

import be.occam.utils.timing.Timing;
import be.pirlewiet.digitaal.infrastructure.salesforce.Contact;
import be.pirlewiet.digitaal.infrastructure.salesforce.SalesForceMapper;
import be.pirlewiet.digitaal.infrastructure.salesforce.SalesforceClient;
import be.pirlewiet.digitaal.model.*;
import be.pirlewiet.digitaal.repository.ApplicationRepository;
import org.springframework.beans.factory.annotation.Autowired;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.LocaleResolver;

import be.pirlewiet.digitaal.application.config.PirlewietApplicationConfig;
import be.pirlewiet.digitaal.domain.HeadQuarters;
import be.pirlewiet.digitaal.domain.q.QIDs;
import be.pirlewiet.digitaal.domain.q.QuestionSheet;
import be.pirlewiet.digitaal.repository.EnrollmentRepository;

import static be.pirlewiet.digitaal.infrastructure.salesforce.SalesForceMapper.map;
import static java.util.Optional.ofNullable;

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
	protected AddressManager addressManager;

	@Autowired
	SpokesPerson spokesPerson;
	
	@Autowired
	MailMan mailMan;
	
	@Autowired
	HeadQuarters headQuarters;
	
	@Autowired
	MessageSource messageSource;
	
	@Autowired
	LocaleResolver localeResolver;

	@Value("${salesforce.enabled}")
	protected boolean salesforceEnabled;

	@Autowired(required = false)
	SalesforceClient salesforceClient;
	
	public EnrollmentManager( ) {
	}
	
	public List<Enrollment> findByApplicationUuid( String applicationUuid ) {
		
		List<Enrollment> enrollments = this.enrollmentRepository.findByApplicationUuid( applicationUuid );
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
		
		Enrollment created = this.enrollmentRepository.saveAndFlush( toCreate );
		
		List<QuestionAndAnswer> participant
			= QuestionSheet.template().getQuestions( ).get( Tags.TAG_PARTICIPANT );
	
		for ( QuestionAndAnswer qna : participant ) {
			qna.setUuid( UUID.randomUUID().toString() );
			qna.setEntityUuid( created.getUuid() );
			this.questionAndAnswerManager.create( qna );
		}
		
		Application application = this.applicationRepository.findByUuid(created.getApplicationUuid());
		Set<Holiday> holidays = this.holidayManager.holidaysFromUUidString( application.getHolidayUuids() );
		
		// for VOV, add questions about partner (2018)
		if ( this.holidayManager.hasType( holidays, HolidayType.Vov ) ) {
			logger.debug( "VOV; add questions...");
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

		// for TIKA, add questions about partner (2018)
		if ( this.holidayManager.hasType( holidays, HolidayType.Tika ) ) {
			logger.debug( "TIKA; add questions...");
			// Add questions manually  for now ... RFC...
			QuestionAndAnswer qna = QuestionSheet.template().getQuestion( QIDs.QID_TIKA_CYCLING );
			qna.setUuid( UUID.randomUUID().toString() );
			qna.setTag( Tags.TAG_PARTICIPANT );
			qna.setEntityUuid( created.getUuid() );
			this.questionAndAnswerManager.create( qna );
		}

		logger.info("[{}]; enrollment created with uuid [{}] and participant [{}]", application.getUuid(), created.getUuid(), created.getParticipantName());
		
		return created;
		
	}


	public Enrollment update( Enrollment toUpdate, Enrollment update ) {
		
		// TODO
		toUpdate.setHolidayName( update.getHolidayName() );
		toUpdate.setHolidayUuid( update.getHolidayUuid() );
		if ( update.getStatus() != null ) {
			toUpdate.setStatus( update.getStatus() );
		}
		Enrollment updated = this.enrollmentRepository.saveAndFlush( toUpdate );
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
		
		logger.debug("enrollment.updateHolidays");
		
		Enrollment enrollment = this.findOneByUuid( uuid );
		
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
	
	public Enrollment updateStatus( String enrollmentUUid, EnrollmentStatus newStatus, Application application, boolean sendUpdate ) {

		logger.info("Enrollment [{}]; update status to [{}]", enrollmentUUid, newStatus.getValue());
		
		Enrollment toUpdate = this.findOneByUuid( enrollmentUUid );
		EnrollmentStatus oldStatus = toUpdate.getStatus();
		toUpdate.setStatus( newStatus );
		Enrollment updated = this.enrollmentRepository.saveAndFlush( toUpdate );
		if (application == null) {
			application = this.applicationRepository.findByUuid(updated.getApplicationUuid());
		}
		Person contact = this.personManager.findOneByUuid( application.getContactPersonUuid() );
		Set<Holiday> holidays = this.holidayManager.holidaysFromUUidString( updated.getHolidayUuid() );
		Person participant = this.personManager.findOneByUuid( updated.getParticipantUuid() );

		if ( sendUpdate ) {
			this.sendStatusUpdateToOrganisation( updated, participant, holidays, oldStatus, contact );
		}

		this.touch(updated,application);
		
		return updated;
		
	}
	
	public Enrollment template() {
		
		Enrollment enrollment = new Enrollment();
		return enrollment;
		
	}

	public Enrollment updateQList( String uuid, List<QuestionAndAnswer> qList ) {
		
		logger.debug("enrollment.updateQList");
		
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

	public Enrollment touch( Enrollment enrollment, Application application ) {

		if (salesforceEnabled){
			Person participant = this.personManager.findOneByUuid(enrollment.getParticipantUuid());
			logger.info("enrollment [{}] - ({}) ; touch", enrollment.getUuid(), participant.getName());
			Person applicant = this.personManager.findOneByUuid(application.getContactPersonUuid());
			Address participantAddress = this.addressManager.findOneByUuid(enrollment.getAddressUuid());
			Organisation organisation = this.organisationManager.findOneByUuid(application.getOrganisationUuid());
			Holiday holiday = this.holidayManager.findOneByUuid(enrollment.getHolidayUuid());
			List<QuestionAndAnswer> qnaList = this.questionAndAnswerManager.findByEntity(application.getUuid());
			qnaList.addAll(this.questionAndAnswerManager.findByEntity(enrollment.getUuid()));
			if (participant.getExternalId() == null){
				// create it
				logger.info("participant [{}]; is no SF contact yet, create", participant.getName());
				this.salesforceClient.createContact(toContact(participant, participantAddress)).ifPresent(contact -> {
					participant.setExternalId(contact.id());
					logger.info("person [{}]; saved as new SF contact with id {}", participant.getName(), contact.id());
					this.personManager.update(participant);
				});
			}
			String salesForceId = participant.getExternalId();
			logger.info("participant [{}], is SF contact [{}]; update...", participant.getName(), participant.getExternalId());
			Map<String, String> map = new HashMap<>();
			map.putAll(SalesForceMapper.mapApplication(application, applicant));
			map.putAll(SalesForceMapper.mapEnrollment(enrollment, holiday));
			map.putAll(SalesForceMapper.mapOrganisation(organisation));
			map.putAll(map(qnaList));
			SalesForceMapper.removeNulls(map);
			logger.info("participant [{}], update contact [{}] with data [{}]", participant.getName(), salesForceId, map);
			this.salesforceClient.updateContact(salesForceId, map).ifPresent(contact -> {
				logger.info("person [{}]; saved SF contact with id {}", participant.getName(), salesForceId);
			});
		}

		return enrollment;
	}

	 protected boolean sendStatusUpdateToOrganisation( Enrollment enrollment,  Person participant, Set<Holiday> holidays, EnrollmentStatus oldStatus, Person applicant ) {

		String message = this.spokesPerson.formatEnrollmentStatusUpdateMessageOrganisation(enrollment,participant,holidays, oldStatus, applicant);
		 if ( message != null ) {
			 mailMan.deliver(applicant.getEmail(),"Uw inschrijving voor %s werd aangepast".formatted(participant.getName()), message);
			 logger.info( "Enrollment [{}]; status update email sent to [{}]", enrollment.getUuid(), applicant.getEmail() );
			 return true;
		 }

		 return false;
	 }
	 
	protected String formatUpdateMessageToOrganisation( Enrollment enrollment, Person participant, Set<Holiday> holidays, EnrollmentStatus.Value oldStatus, Person recipient ) {
		return "TODO";
	}

	protected static Contact toContact(Person person, Address address ) {
		return new Contact()
				// TODO: allow other types
				.type("Deelnemer")
				.firstName(person.getGivenName())
				.lastName(person.getFamilyName())
				.phone(person.getPhone())
				.birthDate(Timing.date(person.getBirthDay(),"yyyy-MM-dd"))
				.city(address.getCity())
				.street("%s %s".formatted(address.getStreet(),address.getNumber()))
				.postalCode(address.getZipCode())
				.email(person.getEmail())
				.gender(switch (person.getGender()) {
					case Gender.M -> "Man";
					case Gender.F -> "Vrouw";
					default -> "X";
				});

	}

}
 
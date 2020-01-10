package be.pirlewiet.digitaal.jtests;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Transactional;

import be.pirlewiet.digitaal.domain.q.QIDs;
import be.pirlewiet.digitaal.model.Address;
import be.pirlewiet.digitaal.model.Application;
import be.pirlewiet.digitaal.model.ApplicationStatus;
import be.pirlewiet.digitaal.model.Enrollment;
import be.pirlewiet.digitaal.model.EnrollmentStatus;
import be.pirlewiet.digitaal.model.Gender;
import be.pirlewiet.digitaal.model.Holiday;
import be.pirlewiet.digitaal.model.HolidayType;
import be.pirlewiet.digitaal.model.Organisation;
import be.pirlewiet.digitaal.model.Period;
import be.pirlewiet.digitaal.model.Person;
import be.pirlewiet.digitaal.model.QuestionAndAnswer;
import be.pirlewiet.digitaal.model.QuestionType;
import be.pirlewiet.digitaal.model.Tags;
import be.pirlewiet.digitaal.repositories.AddressRepository;
import be.pirlewiet.digitaal.repositories.ApplicationRepository;
import be.pirlewiet.digitaal.repositories.EnrollmentRepository;
import be.pirlewiet.digitaal.repositories.HolidayRepository;
import be.pirlewiet.digitaal.repositories.OrganisationRepository;
import be.pirlewiet.digitaal.repositories.PersonRepository;
import be.pirlewiet.digitaal.repositories.QuestionAndAnswerRepository;
import be.pirlewiet.digitaal.web.util.PirlewietUtil;

import com.google.appengine.api.datastore.KeyFactory;

public class DevData {
	
	protected final Logger logger
		= LoggerFactory.getLogger( this.getClass() );

	@Resource
	OrganisationRepository organsiationRepository;
	
	@Resource
	ApplicationRepository applicationRepository;
	
	@Resource
	AddressRepository addressRepository;
	
	@Resource
	HolidayRepository holidayRepository;
	
	@Resource
	EnrollmentRepository enrollmentRepository;
	
	@Resource
	PersonRepository personRepository;
	
	@Resource
	QuestionAndAnswerRepositoryObjectify questionAndAnswerRepository;
	
	@PostConstruct
	@Transactional(readOnly=false)
	public void injectData() {
		
		Person lisa
			= new Person();
		
		// sarah.setUuid(  Ids.SARAH );
		lisa.setGivenName( "Lisa" );
		lisa.setFamilyName( "Simpson" );
		lisa.setEmail("lisa.simpson@springfield.net");
		lisa.setGender( Gender.F );
		lisa.setBirthDay( new Date() );
		lisa.setPhone( "014656896" );
		
		
		lisa = this.personRepository.saveAndFlush( lisa );
		lisa.setUuid( KeyFactory.keyToString( lisa.getKey() ) );
		lisa = this.personRepository.saveAndFlush( lisa );
		
		Address lisaAddress
			= new Address();
		
		lisaAddress.setCity( "Springfield" );
		lisaAddress.setStreet( "Evergreen Terrace" );
		lisaAddress.setNumber("x");
		lisaAddress.setZipCode( "Z-001" );
		
		lisaAddress = this.addressRepository.saveAndFlush( lisaAddress );
		lisaAddress.setUuid( KeyFactory.keyToString( lisaAddress.getKey() ) );
		lisaAddress = this.addressRepository.saveAndFlush( lisaAddress );		
		
		Organisation vzwSvekke
			= new Organisation();
		
		{
			vzwSvekke.setName("VZW Svekke");
			vzwSvekke.setCode( "svk013" );
			vzwSvekke.setEmail( "sven.gladines@gmail.com" );
			vzwSvekke.setPhone( "016123456" );
			vzwSvekke.setCity( "Neervelp" );
		
			vzwSvekke = this.organsiationRepository.saveAndFlush( vzwSvekke );
			vzwSvekke.setUuid( KeyFactory.keyToString( vzwSvekke.getKey() ) );
			
			Address vzwSvekkeAddress = new Address();
			vzwSvekkeAddress.setZipCode( "3370" );
			vzwSvekkeAddress.setCity("Neervelp");
			vzwSvekkeAddress.setStreet( "Vertrijksestraat" );
			vzwSvekkeAddress.setNumber( "33" );
			vzwSvekkeAddress = this.addressRepository.saveAndFlush( vzwSvekkeAddress );
			vzwSvekkeAddress.setUuid( KeyFactory.keyToString( vzwSvekke.getKey() ) );
			vzwSvekkeAddress = this.addressRepository.saveAndFlush( vzwSvekkeAddress );
			
			vzwSvekke.setAddressUuid( vzwSvekkeAddress.getUuid() );
			
			vzwSvekke = this.organsiationRepository.saveAndFlush( vzwSvekke );
			
			logger.info( "VZW Svekke has UUID [{}]", vzwSvekke.getUuid() );
			
		}
		

		Organisation vzwPirlewiet
			= new Organisation();
		
		{
			vzwPirlewiet.setName("VZW Pirlewiet");
			vzwPirlewiet.setCode( "dig151" );
			vzwPirlewiet.setEmail( "info@pirlewiet.be" );
			vzwPirlewiet.setPhone( "016123456" );
			vzwPirlewiet.setCity( "Gent" );
		
			vzwPirlewiet = this.organsiationRepository.saveAndFlush( vzwPirlewiet );
			vzwPirlewiet.setUuid( KeyFactory.keyToString( vzwPirlewiet.getKey() ) );
			
			Address address = new Address();
			address.setZipCode( "3370" );
			address.setCity("Neervelp");
			address.setStreet( "Vertrijksestraat" );
			address.setNumber( "33" );
			address = this.addressRepository.saveAndFlush( address );
			address.setUuid( KeyFactory.keyToString( address.getKey() ) );
			address = this.addressRepository.saveAndFlush( address );
			
			vzwPirlewiet.setAddressUuid( address.getUuid() );
			
			vzwPirlewiet = this.organsiationRepository.saveAndFlush( vzwPirlewiet );
			
			logger.info( "VZW Pirlewiet has UUID [{}]", vzwPirlewiet.getUuid() );
			
		}
		
		Holiday weekendAtBernies
			= new Holiday();
		
		GregorianCalendar bernieStart
			= new GregorianCalendar();
		bernieStart.set( Calendar.YEAR, 2018 );
		bernieStart.set( Calendar.MONTH, 7 );
		bernieStart.set( Calendar.DAY_OF_MONTH, 1 );
		
		GregorianCalendar bernieEnd
			= new GregorianCalendar();
		bernieStart.set( Calendar.YEAR, 2018 );
		bernieStart.set( Calendar.MONTH, 7 );
		bernieStart.set( Calendar.DAY_OF_MONTH, 15 );
		
		weekendAtBernies.setName( "Weekend at Bernie's");
		weekendAtBernies.setPeriod( Period.Spring );
		weekendAtBernies.setType( HolidayType.Vov );
		weekendAtBernies.setStart( bernieStart.getTime() );
		weekendAtBernies.setEnd( bernieEnd.getTime() );
		weekendAtBernies.setDeadLine( new Date() );
		
		weekendAtBernies = holidayRepository.saveAndFlush( weekendAtBernies );
		
		weekendAtBernies.setUuid( KeyFactory.keyToString( weekendAtBernies.getKey() ) );
		weekendAtBernies = holidayRepository.saveAndFlush( weekendAtBernies );
		
		Holiday weekendAtVernies
			= new Holiday();
		
		GregorianCalendar vernieStart
			= new GregorianCalendar();
		vernieStart.set( Calendar.YEAR, 2018 );
		vernieStart.set( Calendar.MONTH, 7 );
		vernieStart.set( Calendar.DAY_OF_MONTH, 1 );
		
		GregorianCalendar vernieEnd
			= new GregorianCalendar();
		vernieStart.set( Calendar.YEAR, 2018 );
		vernieStart.set( Calendar.MONTH, 7 );
		vernieStart.set( Calendar.DAY_OF_MONTH, 15 );
		
		weekendAtVernies.setName( "Weekend at Vernie's");
		weekendAtVernies.setPeriod( Period.Spring );
		weekendAtVernies.setType( HolidayType.Vov );
		weekendAtVernies.setStart( vernieStart.getTime() );
		weekendAtVernies.setEnd( vernieEnd.getTime() );
		weekendAtVernies.setDeadLine( new Date() );
		
		weekendAtVernies = holidayRepository.saveAndFlush( weekendAtVernies );
		
		weekendAtVernies.setUuid( KeyFactory.keyToString( weekendAtVernies.getKey() ) );
		weekendAtVernies = holidayRepository.saveAndFlush( weekendAtVernies );
		
		Person bernie
			= new Person();
		
		bernie.setGivenName( "Bernie" );
		bernie.setFamilyName( "Whatchamacallit" );
		bernie.setEmail( "bernie@bermuda.com" );
		
		bernie = personRepository.saveAndFlush( bernie );
		bernie.setUuid( KeyFactory.keyToString( bernie.getKey() ) );
		bernie = this.personRepository.saveAndFlush( bernie );
		
		Application applicationOne
			= new Application();
	
		applicationOne.setStatus( new ApplicationStatus( ApplicationStatus.Value.DRAFT ) );
		// applicationOne.setStatus( new ApplicationStatus( ApplicationStatus.Value.SUBMITTED ) );
		applicationOne.setOrganisationUuid( vzwSvekke.getUuid() );
		applicationOne.setYear( 2018 );
		applicationOne.setReference( "APP123" );
		applicationOne.setContactPersonName( "Svekke" );
		applicationOne.setHolidayUuids( weekendAtBernies.getUuid() );
		applicationOne.setHolidayNames( weekendAtBernies.getName() );
		applicationOne.setContactPersonName( String.format( "%s %s", bernie.getGivenName(), bernie.getFamilyName() ) );
		applicationOne.setContactPersonUuid( bernie.getUuid() );
		
		applicationOne = this.applicationRepository.saveAndFlush( applicationOne );
		applicationOne.setUuid( KeyFactory.keyToString( applicationOne.getKey() ) );
		applicationOne = this.applicationRepository.saveAndFlush( applicationOne );
		
		logger.info( "Application One has UUID [{}]", applicationOne.getUuid() );
		logger.info( "Application One has Organisation [{}]", applicationOne.getOrganisationUuid() );
		logger.info( "Application One has Year [{}]", applicationOne.getYear() );
		
		QuestionAndAnswer qnaOne
			= new QuestionAndAnswer( 1, QuestionType.Text, Tags.TAG_APPLICATION, QIDs.QID_SHARED_BILL, "Wie betaalt de factuur ?" );
		qnaOne.setEntityUuid( applicationOne.getUuid() );
	
		qnaOne = this.questionAndAnswerRepository.saveAndFlush( qnaOne );
		qnaOne.setUuid( KeyFactory.keyToString( qnaOne.getKey() ) );
		qnaOne = this.questionAndAnswerRepository.saveAndFlush( qnaOne ); 
		
		Enrollment lisaAtBernies
			= new Enrollment();
		
		lisaAtBernies.setHolidayUuid( applicationOne.getHolidayUuids() );
		lisaAtBernies.setStatus( new EnrollmentStatus( EnrollmentStatus.Value.TRANSIT ) );
		lisaAtBernies.setParticipantName( "Lisa" );
		lisaAtBernies.setApplicationUuid( applicationOne.getUuid() );
		lisaAtBernies.setParticipantUuid( lisa.getUuid() );
		lisaAtBernies.setAddressUuid( lisaAddress.getUuid() );
		
		lisaAtBernies = enrollmentRepository.saveAndFlush( lisaAtBernies );
		lisaAtBernies.setUuid( KeyFactory.keyToString( lisaAtBernies.getKey() ) );
		lisaAtBernies = this.enrollmentRepository.saveAndFlush( lisaAtBernies );
		
		QuestionAndAnswer medicalOne
			= new QuestionAndAnswer( 2, QuestionType.Text, Tags.TAG_MEDIC, QIDs.QID_MEDIC_MEDIC_TEL, "Telefoon huisdokter ?" );
		medicalOne.setEntityUuid( lisaAtBernies.getUuid() );

		medicalOne = this.questionAndAnswerRepository.saveAndFlush( medicalOne );
		medicalOne.setUuid( KeyFactory.keyToString( medicalOne.getKey() ) );
		medicalOne = this.questionAndAnswerRepository.saveAndFlush( medicalOne );
		
		QuestionAndAnswer historyOne
			= new QuestionAndAnswer( 3, QuestionType.Text, Tags.TAG_HISTORY, QIDs.QID_HISTORY, "Ging deze deelnemer al mee ?" );
		
		historyOne.setEntityUuid( lisaAtBernies.getUuid() );

		historyOne = this.questionAndAnswerRepository.saveAndFlush( historyOne );
		historyOne.setUuid( KeyFactory.keyToString( historyOne.getKey() ) );
		historyOne = this.questionAndAnswerRepository.saveAndFlush( historyOne );
	
	}
	
}

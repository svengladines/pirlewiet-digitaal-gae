package be.pirlewiet.digitaal.web.util;

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

public class ProductionData {
	
	protected final Logger logger
		= LoggerFactory.getLogger( this.getClass() );

	@Resource
	OrganisationRepository organsiationRepository;
	
	@Resource
	AddressRepository addressRepository;
	
	@Resource
	HolidayRepository holidayRepository;
		
	@PostConstruct
	@Transactional(readOnly=false)
	public void injectData() {
		
		logger.info( "xxx, productiondata" );
		
		Organisation vzwPirlewiet
			= this.organsiationRepository.findOneByEmail( "info@pirlewiet.be" );
		
		if ( vzwPirlewiet == null ) {
			
			logger.info( "xxx, productiondata, building pirlewiet" );

			vzwPirlewiet = new Organisation();
		
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
		else {
			logger.info( "xxx, productiondata, pirlewiet existed" );
		}
		
	}
	
}

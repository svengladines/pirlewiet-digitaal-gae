package be.pirlewiet.digitaal.domain.scenario;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Transactional;

import be.pirlewiet.digitaal.domain.people.ScenarioRunner;
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
import be.pirlewiet.digitaal.web.util.DataGuard;
import be.pirlewiet.digitaal.web.util.PirlewietUtil;

import com.google.appengine.api.datastore.KeyFactory;

public class InjectProductionDataScenario extends Scenario {
	
	protected final Logger logger
		= LoggerFactory.getLogger( this.getClass() );

	@Resource
	OrganisationRepository organsiationRepository;
	
	@Resource
	AddressRepository addressRepository;
	
	@Resource
	HolidayRepository holidayRepository;
	
	@Resource
	DataGuard dataGuard;
	
	public InjectProductionDataScenario guard() {
    	this.dataGuard.guard();
    	return this;
    }
	
	@Transactional(readOnly=false)
	@Override
	public void execute( String... parameters ) {
		
		logger.info( "verify/inject productiondata" );
		
		Organisation vzwPirlewiet
			= this.organsiationRepository.findOneByEmail( "info@pirlewiet.be" );
		
		if ( vzwPirlewiet == null ) {
			
			logger.info( "productiondata, building pirlewiet" );

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
			logger.info( "productiondata, pirlewiet existed, do not inject" );
		}
		
		/**
		 * PaasKIKA 1	3/4 - 8/4	--- 4/4 - 8/4
			PaasKIKA 2	9/4 - 15/4	--- 10/4 - 14/4
			PaasGEZINS	10/4 - 15/4	--- 11/4 - 15/4
			VOV 1	15/5 - 19/5	--- 15/5 - 19/5

		
		// PaasKIKA 1	8/4 - 13/4
		{
			String name = "PaasKIKA 1";
			Holiday holiday
				= this.holidayRepository.findOneByName( name );
			
			if ( holiday == null ) {
				
				holiday = new Holiday();
		
				GregorianCalendar start
					= new GregorianCalendar();
				
				start.set( Calendar.YEAR, 2019 );
				start.set( Calendar.MONTH, 3 );
				start.set( Calendar.DAY_OF_MONTH, 8 );
				
				GregorianCalendar end
					= new GregorianCalendar();
				
				end.set( Calendar.YEAR, 2019 );
				end.set( Calendar.MONTH, 3 );
				end.set( Calendar.DAY_OF_MONTH, 13 );
				
				holiday.setName( name );
				holiday.setPeriod( Period.Spring );
				holiday.setType( HolidayType.Kika );
				holiday.setStart( start.getTime() );
				holiday.setEnd( end.getTime() );
				holiday.setDeadLine( new Date() );
				
				holiday = holidayRepository.saveAndFlush( holiday );
				holiday.setUuid( KeyFactory.keyToString( holiday.getKey() ) );
				holiday = holidayRepository.saveAndFlush( holiday );
				
				logger.info( "holiday [{}] created, got uuid [{}]", holiday.getName(), holiday.getUuid() );
			
			}
			
		}
		
		// PaasKIKA 2 --- 15/4 - 19/4
		{
			String name = "PaasKIKA 2";
			Holiday holiday
				= this.holidayRepository.findOneByName( name );
			
			if ( holiday == null ) {
				
				holiday = new Holiday();
		
				GregorianCalendar start
					= new GregorianCalendar();
				
				start.set( Calendar.YEAR, 2019 );
				start.set( Calendar.MONTH, 3 );
				start.set( Calendar.DAY_OF_MONTH, 15 );
				
				GregorianCalendar end
					= new GregorianCalendar();
				
				end.set( Calendar.YEAR, 2019 );
				end.set( Calendar.MONTH, 3 );
				end.set( Calendar.DAY_OF_MONTH, 19 );
				
				holiday.setName( name );
				holiday.setPeriod( Period.Spring );
				holiday.setType( HolidayType.Kika );
				holiday.setStart( start.getTime() );
				holiday.setEnd( end.getTime() );
				holiday.setDeadLine( new Date() );
				
				holiday = holidayRepository.saveAndFlush( holiday );
				holiday.setUuid( KeyFactory.keyToString( holiday.getKey() ) );
				holiday = holidayRepository.saveAndFlush( holiday );
				
				logger.info( "holiday [{}] created, got uuid [{}]", holiday.getName(), holiday.getUuid() );
			
			}
			
		}
		
		// PaasGezins --- 16/4 - 20/4
		{
			String name = "PaasGEZINS";
			Holiday holiday
				= this.holidayRepository.findOneByName( name );
			
			if ( holiday == null ) {
				
				holiday = new Holiday();
		
				GregorianCalendar start
					= new GregorianCalendar();
				
				start.set( Calendar.YEAR, 2019 );
				start.set( Calendar.MONTH, 3 );
				start.set( Calendar.DAY_OF_MONTH, 16 );
				
				GregorianCalendar end
					= new GregorianCalendar();
				
				end.set( Calendar.YEAR, 2019 );
				end.set( Calendar.MONTH, 3 );
				end.set( Calendar.DAY_OF_MONTH, 20 );
				
				holiday.setName( name );
				holiday.setPeriod( Period.Spring );
				holiday.setType( HolidayType.Gezin );
				holiday.setStart( start.getTime() );
				holiday.setEnd( end.getTime() );
				holiday.setDeadLine( new Date() );
				
				holiday = holidayRepository.saveAndFlush( holiday );
				holiday.setUuid( KeyFactory.keyToString( holiday.getKey() ) );
				holiday = holidayRepository.saveAndFlush( holiday );
				
				logger.info( "holiday [{}] created, got uuid [{}]", holiday.getName(), holiday.getUuid() );
			
			}
			
		}
		
		// VOV 1 --- 3/6 - 7/6
		{
			String name = "VOV 1";
			Holiday holiday
				= this.holidayRepository.findOneByName( name );
			
			if ( holiday == null ) {
				
				holiday = new Holiday();
		
				GregorianCalendar start
					= new GregorianCalendar();
				
				start.set( Calendar.YEAR, 2019 );
				start.set( Calendar.MONTH, 5 );
				start.set( Calendar.DAY_OF_MONTH, 3 );
				
				GregorianCalendar end
					= new GregorianCalendar();
				
				end.set( Calendar.YEAR, 2019 );
				end.set( Calendar.MONTH, 5 );
				end.set( Calendar.DAY_OF_MONTH, 7 );
				
				holiday.setName( name );
				holiday.setPeriod( Period.Spring );
				holiday.setType( HolidayType.Vov );
				holiday.setStart( start.getTime() );
				holiday.setEnd( end.getTime() );
				holiday.setDeadLine( new Date() );
				
				holiday = holidayRepository.saveAndFlush( holiday );
				holiday.setUuid( KeyFactory.keyToString( holiday.getKey() ) );
				holiday = holidayRepository.saveAndFlush( holiday );
				
				logger.info( "holiday [{}] created, got uuid [{}]", holiday.getName(), holiday.getUuid() );
			
			}
			
		}
		**/
		
		/**
		 * Summer 2019 **/
		
		// KIKA 1	10/7 > 17/7
		{
			String name = "KIKA 1";
			Holiday holiday
				= this.holidayRepository.findOneByName( name );
			
			if ( holiday == null ) {
				
				holiday = new Holiday();
		
				GregorianCalendar start
					= new GregorianCalendar();
				
				start.set( Calendar.YEAR, 2019 );
				start.set( Calendar.MONTH, 6 );
				start.set( Calendar.DAY_OF_MONTH, 10 );
				
				GregorianCalendar end
					= new GregorianCalendar();
				
				end.set( Calendar.YEAR, 2019 );
				end.set( Calendar.MONTH, 6 );
				end.set( Calendar.DAY_OF_MONTH, 17 );
				
				holiday.setName( name );
				holiday.setPeriod( Period.Summer );
				holiday.setType( HolidayType.Kika );
				holiday.setStart( start.getTime() );
				holiday.setEnd( end.getTime() );
				holiday.setDeadLine( new Date() );
				
				holiday = holidayRepository.saveAndFlush( holiday );
				holiday.setUuid( KeyFactory.keyToString( holiday.getKey() ) );
				holiday = holidayRepository.saveAndFlush( holiday );
				
				logger.info( "holiday [{}] created, got uuid [{}]", holiday.getName(), holiday.getUuid() );
			
			}
			else {
				logger.info( "holiday [{}] already existed, did not inject", holiday.getName() );
			}
			
		}
		
		// KIKA 2	19/7 > 26/7
		{
			String name = "KIKA 2";
			Holiday holiday
				= this.holidayRepository.findOneByName( name );
			
			if ( holiday == null ) {
				
				holiday = new Holiday();
		
				GregorianCalendar start
					= new GregorianCalendar();
				
				start.set( Calendar.YEAR, 2019 );
				start.set( Calendar.MONTH, 6 );
				start.set( Calendar.DAY_OF_MONTH, 19 );
				
				GregorianCalendar end
					= new GregorianCalendar();
				
				end.set( Calendar.YEAR, 2019 );
				end.set( Calendar.MONTH, 6 );
				end.set( Calendar.DAY_OF_MONTH, 26 );
				
				holiday.setName( name );
				holiday.setPeriod( Period.Summer );
				holiday.setType( HolidayType.Kika );
				holiday.setStart( start.getTime() );
				holiday.setEnd( end.getTime() );
				holiday.setDeadLine( new Date() );
				
				holiday = holidayRepository.saveAndFlush( holiday );
				holiday.setUuid( KeyFactory.keyToString( holiday.getKey() ) );
				holiday = holidayRepository.saveAndFlush( holiday );
				
				logger.info( "holiday [{}] created, got uuid [{}]", holiday.getName(), holiday.getUuid() );
			
			}
		}
		
			// TIKA	21/7 > 27/7
			{
				String name = "TIKA";
				Holiday holiday
					= this.holidayRepository.findOneByName( name );
				
				if ( holiday == null ) {
					
					holiday = new Holiday();
			
					GregorianCalendar start
						= new GregorianCalendar();
					
					start.set( Calendar.YEAR, 2019 );
					start.set( Calendar.MONTH, 6 );
					start.set( Calendar.DAY_OF_MONTH, 21 );
					
					GregorianCalendar end
						= new GregorianCalendar();
					
					end.set( Calendar.YEAR, 2019 );
					end.set( Calendar.MONTH, 6 );
					end.set( Calendar.DAY_OF_MONTH, 27 );
					
					holiday.setName( name );
					holiday.setPeriod( Period.Summer );
					holiday.setType( HolidayType.Tika );
					holiday.setStart( start.getTime() );
					holiday.setEnd( end.getTime() );
					holiday.setDeadLine( new Date() );
					
					holiday = holidayRepository.saveAndFlush( holiday );
					holiday.setUuid( KeyFactory.keyToString( holiday.getKey() ) );
					holiday = holidayRepository.saveAndFlush( holiday );
					
					logger.info( "holiday [{}] created, got uuid [{}]", holiday.getName(), holiday.getUuid() );
				
				}
			}
			
			// Gezins 1	2/8 > 9/8
			{
				String name = "Gezins 1";
				Holiday holiday
					= this.holidayRepository.findOneByName( name );
				
				if ( holiday == null ) {
					
					holiday = new Holiday();
			
					GregorianCalendar start
						= new GregorianCalendar();
					
					start.set( Calendar.YEAR, 2019 );
					start.set( Calendar.MONTH, 7 );
					start.set( Calendar.DAY_OF_MONTH, 2 );
					
					GregorianCalendar end
						= new GregorianCalendar();
					
					end.set( Calendar.YEAR, 2019 );
					end.set( Calendar.MONTH, 7 );
					end.set( Calendar.DAY_OF_MONTH, 9 );
					
					holiday.setName( name );
					holiday.setPeriod( Period.Summer );
					holiday.setType( HolidayType.Gezin );
					holiday.setStart( start.getTime() );
					holiday.setEnd( end.getTime() );
					holiday.setDeadLine( new Date() );
					
					holiday = holidayRepository.saveAndFlush( holiday );
					holiday.setUuid( KeyFactory.keyToString( holiday.getKey() ) );
					holiday = holidayRepository.saveAndFlush( holiday );
					
					logger.info( "holiday [{}] created, got uuid [{}]", holiday.getName(), holiday.getUuid() );
				
				}
			}
			
			// Gezins 2	10/08 > 17/8
			{
				String name = "Gezins 2";
				Holiday holiday
					= this.holidayRepository.findOneByName( name );
				
				if ( holiday == null ) {
					
					holiday = new Holiday();
			
					GregorianCalendar start
						= new GregorianCalendar();
					
					start.set( Calendar.YEAR, 2019 );
					start.set( Calendar.MONTH, 7 );
					start.set( Calendar.DAY_OF_MONTH, 10 );
					
					GregorianCalendar end
						= new GregorianCalendar();
					
					end.set( Calendar.YEAR, 2019 );
					end.set( Calendar.MONTH, 7 );
					end.set( Calendar.DAY_OF_MONTH, 17 );
					
					holiday.setName( name );
					holiday.setPeriod( Period.Summer );
					holiday.setType( HolidayType.Gezin );
					holiday.setStart( start.getTime() );
					holiday.setEnd( end.getTime() );
					holiday.setDeadLine( new Date() );
					
					holiday = holidayRepository.saveAndFlush( holiday );
					holiday.setUuid( KeyFactory.keyToString( holiday.getKey() ) );
					holiday = holidayRepository.saveAndFlush( holiday );
					
					logger.info( "holiday [{}] created, got uuid [{}]", holiday.getName(), holiday.getUuid() );
				
				}
			}
			
			// VOV 2 6/7 > 12/7
			{
				String name = "VOV 2";
				Holiday holiday
					= this.holidayRepository.findOneByName( name );
				
				if ( holiday == null ) {
					
					holiday = new Holiday();
			
					GregorianCalendar start
						= new GregorianCalendar();
					
					start.set( Calendar.YEAR, 2019 );
					start.set( Calendar.MONTH, 6 );
					start.set( Calendar.DAY_OF_MONTH, 7 );
					
					GregorianCalendar end
						= new GregorianCalendar();
					
					end.set( Calendar.YEAR, 2019 );
					end.set( Calendar.MONTH, 6 );
					end.set( Calendar.DAY_OF_MONTH, 12 );
					
					holiday.setName( name );
					holiday.setPeriod( Period.Summer );
					holiday.setType( HolidayType.Vov );
					holiday.setStart( start.getTime() );
					holiday.setEnd( end.getTime() );
					holiday.setDeadLine( new Date() );
					
					holiday = holidayRepository.saveAndFlush( holiday );
					holiday.setUuid( KeyFactory.keyToString( holiday.getKey() ) );
					holiday = holidayRepository.saveAndFlush( holiday );
					
					logger.info( "holiday [{}] created, got uuid [{}]", holiday.getName(), holiday.getUuid() );
				
				}
			}
			
			// APi TODO
			{
				String name = "APi";
				Holiday holiday
					= this.holidayRepository.findOneByName( name );
				
				if ( holiday == null ) {
					
					holiday = new Holiday();
			
					GregorianCalendar start
						= new GregorianCalendar();
					
					start.set( Calendar.YEAR, 2019 );
					start.set( Calendar.MONTH, 7 );
					start.set( Calendar.DAY_OF_MONTH, 19 );
					
					GregorianCalendar end
						= new GregorianCalendar();
					
					end.set( Calendar.YEAR, 2019 );
					end.set( Calendar.MONTH, 7 );
					end.set( Calendar.DAY_OF_MONTH, 23 );
					
					holiday.setName( name );
					holiday.setPeriod( Period.Summer );
					holiday.setType( HolidayType.Api );
					holiday.setStart( start.getTime() );
					holiday.setEnd( end.getTime() );
					holiday.setDeadLine( new Date() );
					
					holiday = holidayRepository.saveAndFlush( holiday );
					holiday.setUuid( KeyFactory.keyToString( holiday.getKey() ) );
					holiday = holidayRepository.saveAndFlush( holiday );
					
					logger.info( "holiday [{}] created, got uuid [{}]", holiday.getName(), holiday.getUuid() );
				
				}
			}
			
			/* Driedaagse niet via PD
			{
				String name = "Driedaagse";
				Holiday holiday
					= this.holidayRepository.findOneByName( name );
				
				if ( holiday == null ) {
					
					holiday = new Holiday();
			
					GregorianCalendar start
						= new GregorianCalendar();
					
					start.set( Calendar.YEAR, 2019 );
					start.set( Calendar.MONTH, 6 );
					start.set( Calendar.DAY_OF_MONTH, 7 );
					
					GregorianCalendar end
						= new GregorianCalendar();
					
					end.set( Calendar.YEAR, 2019 );
					end.set( Calendar.MONTH, 6 );
					end.set( Calendar.DAY_OF_MONTH, 9 );
					
					holiday.setName( name );
					holiday.setPeriod( Period.Summer );
					holiday.setType( HolidayType.DrieDaagse );
					holiday.setStart( start.getTime() );
					holiday.setEnd( end.getTime() );
					holiday.setDeadLine( new Date() );
					
					holiday = holidayRepository.saveAndFlush( holiday );
					holiday.setUuid( KeyFactory.keyToString( holiday.getKey() ) );
					holiday = holidayRepository.saveAndFlush( holiday );
					
					logger.info( "holiday [{}] created, got uuid [{}]", holiday.getName(), holiday.getUuid() );
				
				}
			}
			*/
			
			/* Cava niet via PD
			{
				String name = "CAVA";
				Holiday holiday
					= this.holidayRepository.findOneByName( name );
				
				if ( holiday == null ) {
					
					holiday = new Holiday();
			
					GregorianCalendar start
						= new GregorianCalendar();
					
					start.set( Calendar.YEAR, 2019 );
					start.set( Calendar.MONTH, 6 );
					start.set( Calendar.DAY_OF_MONTH, 9 );
					
					GregorianCalendar end
						= new GregorianCalendar();
					
					end.set( Calendar.YEAR, 2019 );
					end.set( Calendar.MONTH, 6 );
					end.set( Calendar.DAY_OF_MONTH, 13 );
					
					holiday.setName( name );
					holiday.setPeriod( Period.Summer );
					holiday.setType( HolidayType.Cava );
					holiday.setStart( start.getTime() );
					holiday.setEnd( end.getTime() );
					holiday.setDeadLine( new Date() );
					
					holiday = holidayRepository.saveAndFlush( holiday );
					holiday.setUuid( KeyFactory.keyToString( holiday.getKey() ) );
					holiday = holidayRepository.saveAndFlush( holiday );
					
					logger.info( "holiday [{}] created, got uuid [{}]", holiday.getName(), holiday.getUuid() );
				
				}
			}
			*/
		
	}
	
}

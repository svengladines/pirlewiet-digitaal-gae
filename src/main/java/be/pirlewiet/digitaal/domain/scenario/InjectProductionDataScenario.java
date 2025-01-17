package be.pirlewiet.digitaal.domain.scenario;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Transactional;

import be.occam.utils.timing.Timing;
import be.pirlewiet.digitaal.model.Address;
import be.pirlewiet.digitaal.model.Holiday;
import be.pirlewiet.digitaal.model.HolidayType;
import be.pirlewiet.digitaal.model.Organisation;
import be.pirlewiet.digitaal.model.Period;
import be.pirlewiet.digitaal.repository.AddressRepository;
import be.pirlewiet.digitaal.repository.HolidayRepository;
import be.pirlewiet.digitaal.repository.OrganisationRepository;
import be.pirlewiet.digitaal.web.util.DataGuard;

public class InjectProductionDataScenario extends Scenario {
	
	protected final Logger logger
		= LoggerFactory.getLogger( this.getClass() );

	@Autowired
	OrganisationRepository organisationRepository;
	
	@Autowired
	AddressRepository addressRepository;
	
	@Autowired
	HolidayRepository holidayRepository;
	
	@Autowired
	DataGuard dataGuard;
	
	public InjectProductionDataScenario guard() {
    	this.dataGuard.guard();
    	return this;
    }
	
	//@Transactional(readOnly=false)
	@Override
	public void execute( String... parameters ) {
		
		logger.info( "inject productiondata" );
		
		Organisation vzwPirlewiet
			= this.organisationRepository.findOneByEmail( "info@pirlewiet.be" );
		
		if ( vzwPirlewiet == null ) {
			
			logger.info( "productiondata, building pirlewiet" );

			vzwPirlewiet = new Organisation();
		
			vzwPirlewiet.setName("VZW Pirlewiet");
			vzwPirlewiet.setCode( "dig151" );
			vzwPirlewiet.setEmail( "info@pirlewiet.be" );
			vzwPirlewiet.setPhone( "016123456" );
			vzwPirlewiet.setUuid( "pwt" );
		
			
			Address address = new Address();
			address.setZipCode( "3370" );
			address.setCity("Brecht");
			address.setStreet( "Vertrijksestraat" );
			address.setNumber( "33" );
			address.setUuid( "pwt-address" );
			address = this.addressRepository.saveAndFlush( address );
			
			logger.info( "Pirlewiet address has UUID [{}]", address.getUuid() );
			
			vzwPirlewiet.setAddressUuid( address.getUuid() );
			
			vzwPirlewiet = this.organisationRepository.saveAndFlush( vzwPirlewiet );
			
			logger.info( "VZW Pirlewiet has been saved" );
			
			Organisation organisation
				= this.organisationRepository.findByUuid( "pwt" );
			
			if ( organisation != null ) {
				logger.info( "VZW Pirlewiet has been loaded, and his uuid [{}]" );
			}
			else {
				logger.info( "VZW Pirlewiet could not be loaded" );
			}
			
		}
		else {
			logger.info( "productiondata, pirlewiet existed, rename id field" );
			this.organisationRepository.saveAndFlush( vzwPirlewiet );
		}

		// Voorjaar 2025
		{
			String name = "PaasKIKA";
			Holiday holiday = this.holidayRepository.findOneByName( name );
			
			if ( holiday == null ) {
				holiday = new Holiday();
				GregorianCalendar start = new GregorianCalendar();
				start.setTime( Timing.moment( "07/04/2025 08:00" ) );
				GregorianCalendar end = new GregorianCalendar();
				end.setTime( Timing.moment( "12/04/2025 23:00" ) );

				holiday.setName( name );
				holiday.setPeriod( Period.Spring );
				holiday.setType( HolidayType.Kika );
				holiday.setStart( start.getTime() );
				holiday.setEnd( end.getTime() );
				holiday.setDeadLine( new Date() );
				holiday.setUuid( UUID.randomUUID().toString() );				
				holiday = holidayRepository.saveAndFlush( holiday );
				logger.info( "holiday [{}] created, got uuid [{}]", holiday.getName(), holiday.getUuid() );
			}
			
		}
		{
			String name = "PaasGEZINS";
			Holiday holiday
				= this.holidayRepository.findOneByName( name );
			
			if ( holiday == null ) {
				
				holiday = new Holiday();
				GregorianCalendar start = new GregorianCalendar();
				start.setTime( Timing.moment( "15/04/2025 08:00" ) );
				
				GregorianCalendar end = new GregorianCalendar();
				end.setTime( Timing.moment( "19/04/2025 23:00" ) );
				holiday.setName( name );

				holiday.setPeriod( Period.Spring );
				holiday.setType( HolidayType.Gezin );
				holiday.setStart( start.getTime() );
				holiday.setEnd( end.getTime() );
				holiday.setDeadLine( new Date() );
				holiday.setUuid( UUID.randomUUID().toString() );
				holiday = holidayRepository.saveAndFlush( holiday );
				
				logger.info( "holiday [{}] created, got uuid [{}]", holiday.getName(), holiday.getUuid() );
			
			}
			
		}
		
		{
			String name = "VOV 1";
			Holiday holiday = this.holidayRepository.findOneByName( name );
			
			if ( holiday == null ) {
				holiday = new Holiday();
				GregorianCalendar start = new GregorianCalendar();
				start.setTime( Timing.moment( "19/05/2025 08:00" ) );
				GregorianCalendar end = new GregorianCalendar();
				end.setTime( Timing.moment( "23/05/2025 23:00" ) );
				holiday.setName( name );
				holiday.setPeriod( Period.Spring );
				holiday.setType( HolidayType.Vov );
				holiday.setStart( start.getTime() );
				holiday.setEnd( end.getTime() );
				holiday.setDeadLine( new Date() );
				holiday.setUuid( UUID.randomUUID().toString() );
				holiday = holidayRepository.saveAndFlush( holiday );
				logger.info( "holiday [{}] created, got uuid [{}]", holiday.getName(), holiday.getUuid() );
			}
		}

		 /*

		// Zomer 2025
				// VOV 2
				{
					String name = "VOV 2";
					Holiday holiday
						= this.holidayRepository.findOneByName( name );
					
					if ( holiday == null ) {
						
						holiday = new Holiday();
				
						GregorianCalendar start
							= new GregorianCalendar();
						
						start.setTime( Timing.moment( "8/07/2025 08:00" ) );
						
						GregorianCalendar end
							= new GregorianCalendar();
						
						end.setTime( Timing.moment( "12/07/2025 23:00" ) );
						
						holiday.setName( name );
						holiday.setPeriod( Period.Summer );
						holiday.setType( HolidayType.Vov );
						holiday.setStart( start.getTime() );
						holiday.setEnd( end.getTime() );
						holiday.setDeadLine( new Date() );
						holiday.setUuid( UUID.randomUUID().toString() );
						holiday = holidayRepository.saveAndFlush( holiday );
						
						logger.info( "holiday [{}] created, got uuid [{}]", holiday.getName(), holiday.getUuid() );
					
					}
					
				}
				
				// KIKA
				{
					String name = "Kika";
					Holiday holiday
						= this.holidayRepository.findOneByName( name );
					
					if ( holiday == null ) {
						
						holiday = new Holiday();
				
						GregorianCalendar start
							= new GregorianCalendar();
						
						start.setTime( Timing.moment( "17/07/2025 08:00" ) );
						
						GregorianCalendar end
							= new GregorianCalendar();
						
						end.setTime( Timing.moment( "24/07/2025 23:00" ) );
						
						holiday.setName( name );
						holiday.setPeriod( Period.Summer );
						holiday.setType( HolidayType.Kika );
						holiday.setStart( start.getTime() );
						holiday.setEnd( end.getTime() );
						holiday.setDeadLine( new Date() );
						holiday.setUuid( UUID.randomUUID().toString() );
						holiday = holidayRepository.saveAndFlush( holiday );
						
						logger.info( "holiday [{}] created, got uuid [{}]", holiday.getName(), holiday.getUuid() );
					
					}
					
				}
				
				// Tika
				{
					String name = "Tika";
					Holiday holiday
						= this.holidayRepository.findOneByName( name );
					
					if ( holiday == null ) {
						
						holiday = new Holiday();
				
						GregorianCalendar start
							= new GregorianCalendar();
						
						start.setTime( Timing.moment( "11/08/2025 08:00" ) );
						
						GregorianCalendar end
							= new GregorianCalendar();
						
						end.setTime( Timing.moment( "17/08/2025 23:00" ) );
						
						holiday.setName( name );
						holiday.setPeriod( Period.Summer );
						holiday.setType( HolidayType.Tika );
						holiday.setStart( start.getTime() );
						holiday.setEnd( end.getTime() );
						holiday.setDeadLine( new Date() );
						holiday.setUuid( UUID.randomUUID().toString() );
						holiday = holidayRepository.saveAndFlush( holiday );
						
						logger.info( "holiday [{}] created, got uuid [{}]", holiday.getName(), holiday.getUuid() );
					
					}
					
				}
				
				// Gezins 1
				{
					String name = "Gezins 1";
					Holiday holiday
						= this.holidayRepository.findOneByName( name );
					
					if ( holiday == null ) {
						
						holiday = new Holiday();
				
						GregorianCalendar start
							= new GregorianCalendar();
						
						start.setTime( Timing.moment( "02/08/2025 08:00" ) );
						
						GregorianCalendar end
							= new GregorianCalendar();
						
						end.setTime( Timing.moment( "08/08/2025 23:00" ) );
						
						holiday.setName( name );
						holiday.setPeriod( Period.Summer );
						holiday.setType( HolidayType.Gezin );
						holiday.setStart( start.getTime() );
						holiday.setEnd( end.getTime() );
						holiday.setDeadLine( new Date() );
						holiday.setUuid( UUID.randomUUID().toString() );
						holiday = holidayRepository.saveAndFlush( holiday );
						
						logger.info( "holiday [{}] created, got uuid [{}]", holiday.getName(), holiday.getUuid() );
					
					}
					
				}
				
				// Gezins 2
				{
					String name = "Gezins 2";
					Holiday holiday
						= this.holidayRepository.findOneByName( name );
					
					if ( holiday == null ) {
						
						holiday = new Holiday();
				
						GregorianCalendar start
							= new GregorianCalendar();
						
						start.setTime( Timing.moment( "09/08/2025 08:00" ) );
						
						GregorianCalendar end
							= new GregorianCalendar();
						
						end.setTime( Timing.moment( "15/08/2025 23:00" ) );
						
						holiday.setName( name );
						holiday.setPeriod( Period.Summer );
						holiday.setType( HolidayType.Gezin );
						holiday.setStart( start.getTime() );
						holiday.setEnd( end.getTime() );
						holiday.setDeadLine( new Date() );
						holiday.setUuid( UUID.randomUUID().toString() );
						holiday = holidayRepository.saveAndFlush( holiday );
						
						logger.info( "holiday [{}] created, got uuid [{}]", holiday.getName(), holiday.getUuid() );
					
					}
				}
					
				{
					String name = "CAVASOL";
					Holiday holiday= this.holidayRepository.findOneByName( name );
					if ( holiday == null ) {
						holiday = new Holiday();
						GregorianCalendar start= new GregorianCalendar();
						start.setTime( Timing.moment( "08/07/2025 08:00" ) );
						GregorianCalendar end= new GregorianCalendar();
						
						end.setTime( Timing.moment( "12/07/2025 23:00" ) );
						
						holiday.setName( name );
						holiday.setPeriod( Period.Summer );
						holiday.setType( HolidayType.CavaSol );
						holiday.setStart( start.getTime() );
						holiday.setEnd( end.getTime() );
						holiday.setDeadLine( new Date() );
						holiday.setUuid( UUID.randomUUID().toString() );
						holiday = holidayRepository.saveAndFlush( holiday );
						logger.info( "holiday [{}] created, got uuid [{}]", holiday.getName(), holiday.getUuid() );
					
					}
				}
		  */
	}
}

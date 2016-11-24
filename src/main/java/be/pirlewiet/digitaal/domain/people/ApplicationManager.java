package be.pirlewiet.digitaal.domain.people;

import java.util.List;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.mail.javamail.JavaMailSender;

import be.pirlewiet.digitaal.domain.HeadQuarters;
import be.pirlewiet.digitaal.domain.Mapper;
import be.pirlewiet.digitaal.model.Application;
import be.pirlewiet.digitaal.model.Holiday;
import be.pirlewiet.digitaal.model.Organisation;
import be.pirlewiet.digitaal.repositories.ApplicationRepository;
import be.pirlewiet.digitaal.web.util.DataGuard;

/*
 * Receives applications, checks them and passes them on to the secretaries, notifying them and the applicant via e-mail.
 * 
 */
public class ApplicationManager {

	protected final Logger logger
		= LoggerFactory.getLogger( this.getClass() );
	
	protected final int currentYear;
	
	@Resource
	protected ApplicationRepository applicationRepository;
	
	@Resource
	protected HolidayManager holidayManager;
	
	public ApplicationManager( int currentYear ) {
		this.currentYear = currentYear;
	}
	
	public List<Application> findByOrganisation( Organisation actor ) {
		
		List<Application> byOrganisationAndYear
			= this.applicationRepository.findByOrganisationUuidAndYear( actor.getUuid(), 2017 );//.findByYear(2017);//.findAll();//.findByOrganisationUuidAndYear( actor.getUuid(), this.currentYear );
		
		// TODO, sort ...
		
		return byOrganisationAndYear;
		
	}
	
	public Application findOne( String uuid ) {
		
		Application one
			= this.applicationRepository.findByUuid( uuid );
		
		return one;
		
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
			
			application.setHolidayUuids( uuids.toString() );
			application.setHolidayNames( names.toString() );
			
			application = this.applicationRepository.saveAndFlush( application );
			
		}
		
		return application;
	}
	

}
 
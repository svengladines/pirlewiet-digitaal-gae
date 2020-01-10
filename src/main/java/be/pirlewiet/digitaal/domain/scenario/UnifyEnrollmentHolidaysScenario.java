package be.pirlewiet.digitaal.domain.scenario;

import static be.occam.utils.javax.Utils.set;

import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.StringTokenizer;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import be.occam.utils.spring.web.Result;
import be.pirlewiet.digitaal.domain.people.DoorMan;
import be.pirlewiet.digitaal.domain.service.ApplicationService;
import be.pirlewiet.digitaal.domain.service.EnrollmentService;
import be.pirlewiet.digitaal.domain.service.HolidayService;
import be.pirlewiet.digitaal.domain.service.OrganisationService;
import be.pirlewiet.digitaal.model.Organisation;
import be.pirlewiet.digitaal.web.dto.ApplicationDTO;
import be.pirlewiet.digitaal.web.dto.EnrollmentDTO;
import be.pirlewiet.digitaal.web.util.DataGuard;

public class UnifyEnrollmentHolidaysScenario extends Scenario {
	
	protected final Logger logger 
		= LoggerFactory.getLogger( this.getClass() );
	
	@Resource
	OrganisationService organisationService;
	
	@Resource
	EnrollmentService enrollmentService;
	
	@Resource
	HolidayService holidayService;
	
	@Resource
	ApplicationService applicationService;
	
	@Resource
	DoorMan doorMan;
	
	@Resource
	DataGuard dataGuard;
	
	public UnifyEnrollmentHolidaysScenario guard() {
    	this.dataGuard.guard();
    	return this;
    }

	@Override
	public void execute( String... parameters ) {
		
		logger.info( "execute scenario [{}] ... ", this.getClass().getName() );
		
		Organisation pirlewiet
			= this.doorMan.whoHasCode( "dig151" );
		
			Result<List<Result<ApplicationDTO>>> applicationsResult
				= this.applicationService.query( pirlewiet );
			
			
			List<Result<ApplicationDTO>> applicationResults
				= applicationsResult.getObject();
			
			for ( Result<ApplicationDTO> applicationResult : applicationResults ) {
				
				ApplicationDTO application
					= applicationResult.getObject();
				
				Result<List<Result<EnrollmentDTO>>> enrollmentsResult
					= this.enrollmentService.guard().query( application.getUuid(), pirlewiet );
		
				List<Result<EnrollmentDTO>> enrollmentResults
					= enrollmentsResult.getObject();
					
		
				for ( Result<EnrollmentDTO> enrollmentResult : enrollmentResults ) {
			
					EnrollmentDTO enrollment
						= enrollmentResult.getObject();
					
					String holidayUuid
						= enrollment.getHolidayUuid();
			
					if ( ( holidayUuid != null ) && ( holidayUuid.indexOf( "," ) != -1 ) ) {
						
						Set<String> uniqueHolidays
							= set();
						
						StringTokenizer tok	
							= new StringTokenizer( holidayUuid, "," );
						
						while ( tok.hasMoreTokens() ) {
							
							String u
								= tok.nextToken().trim();
							
							uniqueHolidays.add( u );
							
						}
						
						StringBuilder b
							= new StringBuilder();
						
						Iterator<String> i
							= uniqueHolidays.iterator();
						
						while ( i.hasNext() ) {
							
							b.append( i.next() );
							if ( i.hasNext() ) {
								b.append( "," );
							}
							
						}
						
						String unifiedUuid
							= b.toString();
						
						if ( ! unifiedUuid.equals( holidayUuid ) ) {
							
							logger.info( "[{}] about to unify enrollment holidays from [{}]", enrollment.getUuid(), holidayUuid );
							
							this.enrollmentService.updateHolidays( enrollment.getUuid(), b.toString(), pirlewiet );
							
							logger.info( "[{}] unified enrollment holidays to [{}]", enrollment.getUuid(), b.toString() );
							
						}
						
						// TODO, don't break;
						// break;
					}
				}
		}
			
		logger.info( "executed scenario [{}] ... ", this.getClass().getName() );
		
	}
	
	

}

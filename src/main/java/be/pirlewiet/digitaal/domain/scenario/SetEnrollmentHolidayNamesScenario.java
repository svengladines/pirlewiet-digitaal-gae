package be.pirlewiet.digitaal.domain.scenario;

import static be.occam.utils.javax.Utils.isEmpty;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

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
import be.pirlewiet.digitaal.web.dto.HolidayDTO;
import be.pirlewiet.digitaal.web.util.DataGuard;

/**
 * Resolve LIMBO state where enrolmment holiday names are not set. Cause uncertain ? Was the application holiday changed after adding participants ?
 * @author psychox
 *
 */
public class SetEnrollmentHolidayNamesScenario extends Scenario {
	
	protected final Logger logger 
		= LoggerFactory.getLogger( this.getClass() );
	
	@Autowired
	OrganisationService organisationService;
	
	@Autowired
	EnrollmentService enrollmentService;
	
	@Autowired
	HolidayService holidayService;
	
	@Autowired
	ApplicationService applicationService;
	
	@Autowired
	DoorMan doorMan;
	
	@Autowired
	DataGuard dataGuard;
	
	public SetEnrollmentHolidayNamesScenario guard() {
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
				
				logger.info( "load enrollments for application with uuid [{}]", application.getUuid() );
				
				Result<List<Result<EnrollmentDTO>>> enrollmentsResult
					= this.enrollmentService.guard().query( application.getUuid(), pirlewiet );
		
				List<Result<EnrollmentDTO>> enrollmentResults
					= enrollmentsResult.getObject();
					
		
				for ( Result<EnrollmentDTO> enrollmentResult : enrollmentResults ) {
					
					EnrollmentDTO enrollment
						= enrollmentResult.getObject();
					
					logger.info( "check enrollment with uuid [{}] for missing holiday name(s)", enrollment.getUuid() );
			
					if ( isEmpty( enrollment.getHolidayName() ) ) {
						
						logger.info( "enrollment with uuid [{}] has missing holiday name(s)", enrollment.getUuid() );
						
						Result<List<HolidayDTO>> holidaysResult
							= this.holidayService.resolve( enrollment.getHolidayUuid(), application.getHolidayUuids(), false, false, false, pirlewiet );
						
						List<HolidayDTO> holidays
							= holidaysResult.getObject();
						
						this.enrollmentService.updateHolidays( enrollment.getUuid(), holidays, pirlewiet );
					}
				}
		}
			
		logger.info( "executed scenario [{}] ... ", this.getClass().getName() );
		
	}
	
	

}

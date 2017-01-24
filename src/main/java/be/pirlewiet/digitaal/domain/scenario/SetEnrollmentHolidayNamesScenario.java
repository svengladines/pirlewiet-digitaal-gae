package be.pirlewiet.digitaal.domain.scenario;

import static be.occam.utils.javax.Utils.isEmpty;

import java.util.List;

import javax.annotation.Resource;

import be.occam.utils.spring.web.Result;
import be.pirlewiet.digitaal.domain.people.DoorMan;
import be.pirlewiet.digitaal.domain.service.ApplicationService;
import be.pirlewiet.digitaal.domain.service.EnrollmentService;
import be.pirlewiet.digitaal.domain.service.HolidayService;
import be.pirlewiet.digitaal.domain.service.OrganisationService;
import be.pirlewiet.digitaal.dto.ApplicationDTO;
import be.pirlewiet.digitaal.dto.EnrollmentDTO;
import be.pirlewiet.digitaal.dto.HolidayDTO;
import be.pirlewiet.digitaal.model.Application;
import be.pirlewiet.digitaal.model.Enrollment;
import be.pirlewiet.digitaal.model.Organisation;
import be.pirlewiet.digitaal.web.util.DataGuard;
import be.pirlewiet.digitaal.web.util.PirlewietUtil;

public class SetEnrollmentHolidayNamesScenario extends Scenario {
	
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
	
	public SetEnrollmentHolidayNamesScenario guard() {
    	this.dataGuard.guard();
    	return this;
    }

	@Override
	public void execute( String... parameters ) {
		
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
			
					if ( isEmpty( enrollment.getHolidayName() ) ) {
						
						Result<List<HolidayDTO>> holidaysResult
							= this.holidayService.resolve( enrollment.getHolidayUuid(), application.getHolidayUuids(), false, false, false, pirlewiet );
						
						List<HolidayDTO> holidays
							= holidaysResult.getObject();
						
						
						this.enrollmentService.updateHolidays( enrollment.getUuid(), holidays, pirlewiet );
					}
				}
		}
		
		
		
	}
	
	

}

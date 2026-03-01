package be.pirlewiet.digitaal.web.controller.api;

import be.pirlewiet.digitaal.domain.scenario.*;
import be.pirlewiet.digitaal.web.util.ExcelImporter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping(value="/api/scenarios/{id}")
public class ScenarioController {
	
	private final Logger logger 
		= LoggerFactory.getLogger( ScenarioController.class );
	
	/*
	@Autowired
	ReadyToRockScenario readyToRockScenario;
	
	@Autowired
	ReadyToRockOneScenario readyToRockOneScenario;
	*/

	@Autowired
	TouchMissingSalesforceContactsScenario touchMissingSalesforceContactsScenario;
	
	@Autowired
	InjectProductionDataScenario injectProductionDataScenario;
	
	@Autowired
	DeleteOldEntitiesScenario deleteOldEntitiesScenario;
	
	@Autowired
	SetEnrollmentHolidayNamesScenario setEnrollmentHolidayNamesScenario;
	
	@Autowired
	UnifyEnrollmentHolidaysScenario unifyEnrollmentHolidaysScenario;

	@Autowired
	SetMissingApplicantsToReferencedScenario setMissingApplicantsToReferencedScenario;
	
	@Autowired
	ObjectifyScenario objectifyScenario;
	
	protected final ExcelImporter excelImporter
		= new ExcelImporter();
	
	@RequestMapping( method = { RequestMethod.GET } )
	@ResponseBody
	public ResponseEntity<Boolean> get( @PathVariable("id") String id, @RequestParam(required=false) String q ) {
		
		try {
			
			if ( "injectProductionDataScenario".equals( id ) ) {
				this.injectProductionDataScenario.guard().execute();
				return new ResponseEntity<>( Boolean.TRUE, HttpStatus.OK );
			}
			else if ( "deleteOldEntitiesScenario".equals( id ) ) {
				this.deleteOldEntitiesScenario.guard().execute();
				return new ResponseEntity<>( Boolean.TRUE, HttpStatus.OK );
			}
			else if ( "setEnrollmentHolidayNamesScenario".equals( id ) ) {
				this.setEnrollmentHolidayNamesScenario.guard().execute();
				return new ResponseEntity<>( Boolean.TRUE, HttpStatus.OK );
			}
			else if ( "unifyEnrollmentHolidaysScenario".equals( id ) ) {
				this.unifyEnrollmentHolidaysScenario.guard().execute();
				return new ResponseEntity<>( Boolean.TRUE, HttpStatus.OK );
			}
			else if ( "objectifyScenario".equals( id ) ) {
				this.objectifyScenario.guard().execute();
				return new ResponseEntity<>( Boolean.TRUE, HttpStatus.OK );
			}
			else if ( "setMissingApplicantsToReferencedScenario".equals( id ) ) {
				this.setMissingApplicantsToReferencedScenario.guard().execute();
				return new ResponseEntity<>( Boolean.TRUE, HttpStatus.OK );
			}

			else if ( "touchMissingSalesforceContactsScenario".equals( id ) ) {
				this.touchMissingSalesforceContactsScenario.guard().execute();
				return new ResponseEntity<>( Boolean.TRUE, HttpStatus.OK );
			}
			return new ResponseEntity<>( Boolean.FALSE, HttpStatus.NOT_FOUND );
			
		}
		catch( Exception e ) {
			logger.warn( "scenario execution failed", e );
			return new ResponseEntity<>( Boolean.FALSE, HttpStatus.INTERNAL_SERVER_ERROR );
		}
			
	}
		
}

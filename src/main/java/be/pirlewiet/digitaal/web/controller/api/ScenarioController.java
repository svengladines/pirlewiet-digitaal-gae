package be.pirlewiet.digitaal.web.controller.api;

import static be.occam.utils.spring.web.Controller.response;

import org.springframework.beans.factory.annotation.Autowired;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import be.pirlewiet.digitaal.domain.scenario.DeleteOldEntitiesScenario;
import be.pirlewiet.digitaal.domain.scenario.InjectProductionDataScenario;
import be.pirlewiet.digitaal.domain.scenario.ObjectifyScenario;
import be.pirlewiet.digitaal.domain.scenario.SetEnrollmentHolidayNamesScenario;
import be.pirlewiet.digitaal.domain.scenario.UnifyEnrollmentHolidaysScenario;
import be.pirlewiet.digitaal.web.util.ExcelImporter;

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
	InjectProductionDataScenario injectProductionDataScenario;
	
	@Autowired
	DeleteOldEntitiesScenario deleteOldEntitiesScenario;
	
	@Autowired
	SetEnrollmentHolidayNamesScenario setEnrollmentHolidayNamesScenario;
	
	@Autowired
	UnifyEnrollmentHolidaysScenario unifyEnrollmentHolidaysScenario;
	
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
				return response( Boolean.TRUE, HttpStatus.OK );
			}
			else if ( "deleteOldEntitiesScenario".equals( id ) ) {
				this.deleteOldEntitiesScenario.guard().execute();
				return response( Boolean.TRUE, HttpStatus.OK );
			}
			else if ( "setEnrollmentHolidayNamesScenario".equals( id ) ) {
				this.setEnrollmentHolidayNamesScenario.guard().execute();
				return response( Boolean.TRUE, HttpStatus.OK );
			}
			else if ( "unifyEnrollmentHolidaysScenario".equals( id ) ) {
				this.unifyEnrollmentHolidaysScenario.guard().execute();
				return response( Boolean.TRUE, HttpStatus.OK );
			}
			else if ( "objectifyScenario".equals( id ) ) {
				this.objectifyScenario.guard().execute();
				return response( Boolean.TRUE, HttpStatus.OK );
			}
		
			/*
			if ( "uuid".equals( id ) ) {
				this.setOrganisationsUuidScenario.guard().execute();
				return response( Boolean.TRUE, HttpStatus.OK );
			}
			else if ( "ready".equals( id ) ) {
				// this.readyToRockScenario.guard().execute();
				return response( Boolean.TRUE, HttpStatus.OK );
			}
			else if ( "rockme".equals( id ) ) {
				this.readyToRockOneScenario.guard().execute( q );
				return response( Boolean.TRUE, HttpStatus.OK );
			}
			else if ( "".equals( id ) ) {
				this.readyToRockOneScenario.guard().execute( q );
				return response( Boolean.TRUE, HttpStatus.OK );
			}
			else {
				return response( Boolean.FALSE, HttpStatus.NOT_FOUND );
			}
			if ( "qid".equals( id ) ) {
				this.setQuestionsQIDScenario.guard().execute();
				return response( Boolean.TRUE, HttpStatus.OK );
			}
			*/
			
			return response( Boolean.FALSE, HttpStatus.NOT_FOUND );
			
		}
		catch( Exception e ) {
			logger.warn( "scenario execution failed", e );
			return response( Boolean.FALSE, HttpStatus.INTERNAL_SERVER_ERROR );
		}
			
	}
		
}

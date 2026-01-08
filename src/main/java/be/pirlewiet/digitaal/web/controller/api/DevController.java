package be.pirlewiet.digitaal.web.controller.api;

import be.pirlewiet.digitaal.domain.scenario.*;
import be.pirlewiet.digitaal.web.util.ExcelImporter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Profile("dev")
@Controller
@RequestMapping(value="/dev")
public class DevController {
	
	private final Logger logger 
		= LoggerFactory.getLogger( DevController.class );
	
	@Autowired
	InjectProductionDataScenario injectProductionDataScenario;
	
	@RequestMapping( method = { RequestMethod.GET } )
	@ResponseBody
	public ResponseEntity<Boolean> get() {
		
		try {
			this.injectProductionDataScenario.guard().execute();
			return new ResponseEntity<>( Boolean.TRUE, HttpStatus.OK );
		}
		catch( Exception e ) {
			logger.warn( "scenario execution failed", e );
			return new ResponseEntity<>( Boolean.FALSE, HttpStatus.INTERNAL_SERVER_ERROR );
		}
			
	}
		
}

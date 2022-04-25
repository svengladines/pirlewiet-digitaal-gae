package be.pirlewiet.digitaal.web.controller.api;

import static be.occam.utils.spring.web.Controller.response;

import java.util.List;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import be.occam.utils.spring.web.Result;
import be.pirlewiet.digitaal.domain.people.DoorMan;
import be.pirlewiet.digitaal.domain.service.ApplicationService;
import be.pirlewiet.digitaal.model.Organisation;
import be.pirlewiet.digitaal.web.dto.ApplicationDTO;

@Controller
@RequestMapping( {"/applications"} )
public class ApplicationsController {
	
	protected Logger logger 
		= LoggerFactory.getLogger( this.getClass() );
	
	@Resource
	DoorMan doorMan;
	
	@Resource
	ApplicationService applicationService;
	
	@RequestMapping( method = { RequestMethod.POST }, consumes = { MediaType.APPLICATION_JSON_VALUE }, produces = { MediaType.APPLICATION_JSON_VALUE } )
	@ResponseBody
	public ResponseEntity<Result<ApplicationDTO>> post( 
			@RequestBody ApplicationDTO application, 
			@CookieValue(required=true, value="pwtid") String pwtid  ) {
		
		Organisation actor
			= this.doorMan.guard().whoHasID(  pwtid  );
		
		Result<ApplicationDTO> createdResult
			= this.applicationService.guard().create( application, actor );
		
		return response( createdResult, HttpStatus.OK );
		
	}
	
	@RequestMapping( method = { RequestMethod.GET }, produces = { MediaType.APPLICATION_JSON_VALUE } )
	@ResponseBody
	public ResponseEntity<List<ApplicationDTO>> get( @CookieValue(required=true, value="pwtid") String pwtid  ) {
		
		List<ApplicationDTO> applications
			= this.applicationService.guard().findAll();
		
		return response( applications, HttpStatus.OK );
		
	}
	
}

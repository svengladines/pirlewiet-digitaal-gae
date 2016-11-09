package be.pirlewiet.digitaal.web.controller;

import static be.occam.utils.spring.web.Controller.response;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import be.pirlewiet.digitaal.domain.people.DoorMan;
import be.pirlewiet.digitaal.domain.people.Secretary;
import be.pirlewiet.digitaal.model.CodeRequest;

@Controller
@RequestMapping(value="/coderequests")
public class CodeRequestsController {
	
	private final static Logger logger 
		= LoggerFactory.getLogger( CodeRequestsController.class );
	
	@Resource
	protected DoorMan buitenWipper;
	
	@Resource
	protected Secretary secretariaatsMedewerker;
	
	@RequestMapping(method=RequestMethod.POST)
	@ResponseBody
	public ResponseEntity<CodeRequest> post( @RequestBody CodeRequest codeRequest, HttpServletResponse response )  {
		
		// Map<String, String> headers
		// = new HashMap<String,String>();
		
		logger.info("code request for email [{}]", codeRequest.getEmail() );
		this.buitenWipper.guard().processCodeRequest( codeRequest, true );
		
		if ( CodeRequest.Status.REJECTED.equals( codeRequest.getStatus() ) ) {
			logger.warn( "email [{}] not found in db", codeRequest.getEmail() );
			return response( HttpStatus.UNPROCESSABLE_ENTITY );
		}
		
		return response( codeRequest, HttpStatus.OK );
		
	}
	
}

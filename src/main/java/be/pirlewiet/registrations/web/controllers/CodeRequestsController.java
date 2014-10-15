package be.pirlewiet.registrations.web.controllers;

import static be.occam.utils.spring.web.Controller.response;

import javax.annotation.Resource;
import javax.servlet.http.Cookie;
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

import be.pirlewiet.registrations.domain.BuitenWipper;
import be.pirlewiet.registrations.domain.SecretariaatsMedewerker;
import be.pirlewiet.registrations.model.CodeRequest;
import be.pirlewiet.registrations.model.Organisatie;

@Controller
@RequestMapping(value="/coderequests")
public class CodeRequestsController {
	
	private final static Logger logger 
		= LoggerFactory.getLogger( CodeRequestsController.class );
	
	@Resource
	protected BuitenWipper buitenWipper;
	
	@Resource
	protected SecretariaatsMedewerker secretariaatsMedewerker;
	
	@RequestMapping(method=RequestMethod.POST)
	@ResponseBody
	public ResponseEntity<CodeRequest> post( @RequestBody CodeRequest codeRequest, HttpServletResponse response )  {
		
		// Map<String, String> headers
		// = new HashMap<String,String>();
		
		this.buitenWipper.guard().processCodeRequest( codeRequest );
		
		if ( CodeRequest.Status.REJECTED.equals( codeRequest.getStatus() ) ) {
			logger.warn( "email [{}] not found in db", codeRequest.getEmail() );
			return response( HttpStatus.UNPROCESSABLE_ENTITY );
		}
		
		return response( codeRequest, HttpStatus.OK );
		
	}
	
}

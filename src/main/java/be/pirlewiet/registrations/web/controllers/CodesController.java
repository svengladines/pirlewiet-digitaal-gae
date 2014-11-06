package be.pirlewiet.registrations.web.controllers;

import static be.occam.utils.spring.web.Controller.response;

import java.util.HashMap;
import java.util.Map;

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
import be.pirlewiet.registrations.model.Organisatie;

@Controller
@RequestMapping(value="/codes")
public class CodesController {
	
	private final static Logger logger 
		= LoggerFactory.getLogger( CodesController.class );
	
	@Resource
	protected BuitenWipper buitenWipper;
	
	@RequestMapping(method=RequestMethod.POST)
	@ResponseBody
	public ResponseEntity<String> post( @RequestBody String code, HttpServletResponse response )  {
		
		// Map<String, String> headers
		// = new HashMap<String,String>();
		
		Organisatie organisatie
			= this.buitenWipper.guard().whoHasCode( code );
		
		if ( organisatie == null ) {
			logger.warn( "invalid code [{}] presented!", code );
			return response( "Code niet correct. Probeer opnieuw of vraag de code opnieuw aan", HttpStatus.UNPROCESSABLE_ENTITY );
		}
		
		Cookie cookie
			= new Cookie( "pwtid", "" + organisatie.getId() );
		
		cookie.setMaxAge( 3600 * 24 * 30 * 12 );
		
		cookie.setPath( "/" );
		
		response.addCookie( cookie );
		
		return response( HttpStatus.OK );
		
	}
	
}

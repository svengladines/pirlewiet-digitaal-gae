package be.pirlewiet.digitaal.web.controller;

import static be.occam.utils.spring.web.Controller.response;

import javax.annotation.Resource;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import be.pirlewiet.digitaal.domain.people.DoorMan;
import be.pirlewiet.digitaal.model.Organisation;

@Controller
@RequestMapping(value="/codes")
public class CodesController {
	
	private final static Logger logger 
		= LoggerFactory.getLogger( CodesController.class );
	
	@Resource
	protected DoorMan buitenWipper;
	
	@RequestMapping(method=RequestMethod.POST)
	@ResponseBody
	public ResponseEntity<String> post( @RequestBody String code, HttpServletResponse response )  {
		
		// Map<String, String> headers
		// = new HashMap<String,String>();
		
		if ( code != null ) {
			code = code.replaceAll(" ", "");
		}
		
		logger.info( "code [{}] presented", code );
		
		Organisation organisatie
			= this.buitenWipper.guard().whoHasCode( code );
		
		if ( organisatie == null ) {
			logger.warn( "unknown code [{}] presented!", code );
			return response( "De opgegeven code werd niet herkend. Probeer opnieuw of vraag je code opnieuw aan.", HttpStatus.UNPROCESSABLE_ENTITY );
		}
		
		Cookie cookie
			= new Cookie( "pwtid", "" + organisatie.getUuid() );
		
		cookie.setMaxAge( 3600 * 24 * 30 * 12 );
		
		cookie.setPath( "/" );
		
		response.addCookie( cookie );
		
		return response( HttpStatus.OK );
		
	}
	
	// TODO, move to cookiescontroller or such
	@RequestMapping(method=RequestMethod.DELETE)
	@ResponseBody
	public ResponseEntity<String> delete( @CookieValue(required=false, value="pwtid") String pwtid, HttpServletResponse response )  {
		
		if ( pwtid != null ) {
		
			Organisation organisatie
				= this.buitenWipper.guard().whoHasID( pwtid );
		
			if ( organisatie != null ) {
				
				Cookie cookie
					= new Cookie( "pwtid", "" + organisatie.getUuid() );
			
				// discard by setting zero age 
				cookie.setMaxAge( 0 );
				cookie.setPath( "/" );
				response.addCookie( cookie );				
				
			}
		}
		
		return response( HttpStatus.OK );
		
	}
	
}

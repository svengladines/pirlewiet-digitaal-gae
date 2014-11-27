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
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import be.pirlewiet.registrations.domain.OrganisationManager;
import be.pirlewiet.registrations.domain.SecretariaatsMedewerker;
import be.pirlewiet.registrations.model.CodeRequest;
import be.pirlewiet.registrations.model.Organisatie;

@Controller
@RequestMapping(value="/organisations")
public class OrganisatiesController {
	
	private final static Logger logger 
		= LoggerFactory.getLogger( OrganisatiesController.class );
	
	@Resource
	protected SecretariaatsMedewerker secretariaatsMedewerker;
	
	@Resource
	protected OrganisationManager organisationManager;
	
	@RequestMapping( method = { RequestMethod.POST} )
	@ResponseBody
	public ResponseEntity<Organisatie> post( @RequestBody Organisatie organisation,  HttpServletResponse response ) {
		
		if ( this.organisationManager.guard().isInComplete( organisation ) ) {
			
			return response( HttpStatus.UNPROCESSABLE_ENTITY );
			
		}
		else { 
		
			Organisatie created
				= this.secretariaatsMedewerker.guard().addOrganisatie( organisation );
			
			Cookie cookie
				= new Cookie( "pwtid", "" + created.getId() );
		
			cookie.setMaxAge( 3600 * 24 * 30 * 12 );
			
			cookie.setPath( "/" );
			
			response.addCookie( cookie );
			
			return response( created, HttpStatus.OK );
			
		}
			
	}
	
	@ExceptionHandler(Exception.class)
	@ResponseBody
	public ResponseEntity<String> handleError( Exception e ){
		
		logger.warn( "failure while handling request", e );
		return response( e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR );
		
	}
	
}

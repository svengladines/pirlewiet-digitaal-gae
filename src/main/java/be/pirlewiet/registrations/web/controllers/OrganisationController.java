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
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import be.pirlewiet.registrations.domain.BuitenWipper;
import be.pirlewiet.registrations.domain.OrganisationManager;
import be.pirlewiet.registrations.domain.PirlewietException;
import be.pirlewiet.registrations.domain.SecretariaatsMedewerker;
import be.pirlewiet.registrations.model.Adres;
import be.pirlewiet.registrations.model.CodeRequest;
import be.pirlewiet.registrations.model.Organisatie;

@Controller
@RequestMapping( {"/organisation"} )
public class OrganisationController {
	
	protected Logger logger 
		= LoggerFactory.getLogger( this.getClass() );
	
	@Resource
	SecretariaatsMedewerker secretariaatsMedewerker;
	
	@Resource
	OrganisationManager organisationManager;
	
	@Resource
	BuitenWipper buitenWipper;
	
	public ResponseEntity<Organisatie> retrieve( String id ) {
		
		Organisatie organisatie
			= this.buitenWipper.guard().whoHasID(  id  );
		
		if ( organisatie == null ) {
			return response( HttpStatus.NOT_FOUND );
		}

		return response( organisatie, HttpStatus.OK );
		
	}
	
	@RequestMapping( method = { RequestMethod.PUT } )
	@ResponseBody
	public ResponseEntity<Organisatie> update( @RequestBody Organisatie organisatie, @CookieValue(required=true, value="pwtid") String pwtid ) {
		
			Organisatie loaded
				= this.buitenWipper.guard().whoHasID(  pwtid  );
		
			if ( loaded == null ) {
				return response( HttpStatus.NOT_FOUND );
			}
		
			return response( this.organisationManager.guard().update(  pwtid , organisatie ), HttpStatus.OK );
			
	}
	
	@RequestMapping( value="/adres", method = { RequestMethod.PUT } )
	@ResponseBody
	public ResponseEntity<Adres> adressUpdate(
				@RequestBody Adres adres,
				@CookieValue(required=true, value="pwtid") String pwtid,
				HttpServletResponse response ) {
		
		Organisatie loaded
			= this.buitenWipper.guard().whoHasID(  pwtid  );
	
		if ( loaded == null ) {
				return response( HttpStatus.NOT_FOUND );
		}
		
		if ( ( loaded.getCode() == null ) || loaded.getCode().isEmpty() ) {
		
			CodeRequest codeRequest
				= new CodeRequest();
			codeRequest.setEmail( loaded.getEmail() );
			this.buitenWipper.guard().processCodeRequest( codeRequest, false );
			
			Cookie cookie
				= new Cookie( "pwtid", pwtid );

			// discard cookie by setting zero age, they must login first 
			cookie.setMaxAge( 0 );
			cookie.setPath( "/" );
			response.addCookie( cookie );
			
		}
		
		this.organisationManager.guard().updateAdres( loaded.getUuid(), adres );
		
		return response( adres, HttpStatus.OK );
		
	}
	
	@ExceptionHandler( PirlewietException.class)
	@ResponseBody
	public ResponseEntity<String> handleFailure( PirlewietException e ){
		
		logger.warn( "failure while handling request", e );
		return response( e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR );
		
	}
	
	@ExceptionHandler(Exception.class)
	@ResponseBody
	public ResponseEntity<String> handleError( Exception e ){
		
		logger.warn( "failure while handling request", e );
		return response( e.getMessage(), HttpStatus.BAD_REQUEST );
		
	}
	
	@RequestMapping( method = { RequestMethod.GET }, produces={ MediaType.TEXT_HTML_VALUE } )
	public ModelAndView view( @CookieValue(required=false, value="pwtid") String pwtid ) {
		
		Organisatie organisatie
			= null;
		
		if ( pwtid != null ) {
			
			ResponseEntity<Organisatie> entity
				= this.retrieve( pwtid );
			organisatie = entity.getBody();
			
		}
		
		if ( organisatie == null ) {
			organisatie = new Organisatie();
		}
		
		Map<String,Object> model
			= new HashMap<String,Object>();
		
		model.put( "organisation", organisatie );
		model.put( "incomplete", this.organisationManager.isInComplete( organisatie, true ) );
		
		String view 
			= be.pirlewiet.registrations.web.util.PirlewietUtil.isPirlewiet( organisatie) ? "pirlewiet" : "organisation";
		
		return new ModelAndView( view, model );	
		
	}
	
}

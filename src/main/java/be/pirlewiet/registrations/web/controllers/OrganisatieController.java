package be.pirlewiet.registrations.web.controllers;

import static be.occam.utils.spring.web.Controller.response;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.Cookie;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import be.pirlewiet.registrations.domain.BuitenWipper;
import be.pirlewiet.registrations.domain.OrganisationManager;
import be.pirlewiet.registrations.domain.SecretariaatsMedewerker;
import be.pirlewiet.registrations.model.Adres;
import be.pirlewiet.registrations.model.ContactGegevens;
import be.pirlewiet.registrations.model.Deelnemer;
import be.pirlewiet.registrations.model.InschrijvingX;
import be.pirlewiet.registrations.model.Organisatie;
import be.pirlewiet.registrations.model.Status;
import be.pirlewiet.registrations.model.Vakantie;
import be.pirlewiet.registrations.model.Vraag;
import be.pirlewiet.registrations.utils.PirlewietUtil;

@Controller
@RequestMapping( {"/organisation"} )
public class OrganisatieController {
	
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
			= this.buitenWipper.guard().whoHasID( Long.valueOf( id ) );
		
		if ( organisatie == null ) {
			return response( HttpStatus.NOT_FOUND );
		}

		return response( organisatie, HttpStatus.OK );
		
	}
	
	@RequestMapping( method = { RequestMethod.PUT } )
	@ResponseBody
	public ResponseEntity<Organisatie> update( @RequestBody Organisatie organisatie, @CookieValue(required=true, value="pwtid") String pwtid ) {
		
			Organisatie loaded
				= this.buitenWipper.guard().whoHasID( Long.valueOf( pwtid ) );
		
			if ( loaded == null ) {
				return response( HttpStatus.NOT_FOUND );
			}
		
			return response( this.organisationManager.guard().update( Long.valueOf( pwtid ), organisatie ), HttpStatus.OK );
			
	}
	
	@RequestMapping( value="/adres", method = { RequestMethod.PUT } )
	@ResponseBody
	public ResponseEntity<Adres> adressUpdate(
				@RequestBody Adres adres,
				@CookieValue(required=true, value="pwtid") String pwtid) {
		
		Organisatie loaded
			= this.buitenWipper.guard().whoHasID( Long.valueOf( pwtid ) );

		if ( loaded == null ) {
			return response( HttpStatus.NOT_FOUND );
		}
		
		this.secretariaatsMedewerker.guard().updateOrganisatieAdres( Long.valueOf( loaded.getId() ), adres );
		
		return response( adres, HttpStatus.OK );
		
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
		model.put( "incomplete", this.organisationManager.isInComplete( organisatie ) );
		
		String view 
			= PirlewietUtil.isPirlewiet( organisatie) ? "pirlewiet" : "organisation";
		
		return new ModelAndView( view, model );	
		
	}
	
}

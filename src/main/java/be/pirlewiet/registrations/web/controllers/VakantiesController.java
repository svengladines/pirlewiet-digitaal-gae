package be.pirlewiet.registrations.web.controllers;

import static be.occam.utils.spring.web.Controller.response;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

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
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.ModelAndView;

import be.pirlewiet.registrations.domain.BuitenWipper;
import be.pirlewiet.registrations.domain.SecretariaatsMedewerker;
import be.pirlewiet.registrations.model.Organisatie;
import be.pirlewiet.registrations.model.Vakantie;
import be.pirlewiet.registrations.web.util.PirlewietUtil;

@Controller
@RequestMapping( {"/vakanties"} )
public class VakantiesController {
	
	protected Logger logger 
		= LoggerFactory.getLogger( this.getClass() );
	
	@Resource
	SecretariaatsMedewerker secretariaatsMedewerker;
	
	@Resource
	BuitenWipper buitenWipper;
	
	@RequestMapping( method = { RequestMethod.GET }, produces={"application/json","text/xml"} )
	@ResponseBody
	public ResponseEntity<List<Vakantie>> get( WebRequest request, @CookieValue(required=true, value="pwtid") String pwtid ) {
		
		List<Vakantie> vakanties
			= new ArrayList<Vakantie>( );
		
		vakanties.addAll( this.secretariaatsMedewerker.actueleVakanties() );
		
		return response( vakanties, HttpStatus.OK );
		
	}
	
	@RequestMapping( method = { RequestMethod.POST } )
	@ResponseBody
	public ResponseEntity<Vakantie> post(
				@RequestBody Vakantie vakantie, WebRequest request, @CookieValue(required=true, value="pwtid") String pwtid ) {
		
		Organisatie organisatie
			= this.organisatie( request, pwtid );
		
		if ( ! PirlewietUtil.isPirlewiet( organisatie ) ) {
			throw new RuntimeException("only pirlewiet can add vakanties");
		}
		
		Vakantie aangemaakt
			= this.secretariaatsMedewerker.maakVakantie( vakantie );
		
		if ( aangemaakt == null ) {
			throw new RuntimeException("create failed");
		}
		
		return response( aangemaakt, HttpStatus.CREATED );
			
	}
	
	@RequestMapping( method = { RequestMethod.GET }, produces={ MediaType.TEXT_HTML_VALUE } )
	public ModelAndView view( @CookieValue(required=true, value="pwtid") String pwtid ) {
		

		Organisatie organisatie
			= this.buitenWipper.whoHasID(  pwtid  );
		
		// TODO: check organisatie != null
		
		List<Vakantie> inschrijvingen 
			= this.secretariaatsMedewerker.actueleVakanties();
		
		Map<String,Object> model
			= new HashMap<String,Object>();
		
		model.put( "organisatie", organisatie );
		model.put( "pwt", PirlewietUtil.isPirlewiet( organisatie ) );
		model.put( "inschrijvingen", inschrijvingen );
		
		return new ModelAndView( "inschrijvingen", model );
		
	}
	
	@ExceptionHandler(Exception.class)
	public ResponseEntity<String> handleError( Exception e ){
		
		logger.warn( "failure while handling request", e );
		return response( e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR );
		
	}
	
	protected Organisatie organisatie( WebRequest request, String pwtid ) {
		
		Organisatie organisatie
			= new Organisatie();
		organisatie.setUuid( pwtid );
		
		return organisatie;
		
	}
	
}

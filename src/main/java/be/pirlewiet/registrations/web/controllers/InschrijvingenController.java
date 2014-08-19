package be.pirlewiet.registrations.web.controllers;

import static be.occam.utils.spring.web.Controller.response;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

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
import org.springframework.web.context.request.WebRequest;

import be.pirlewiet.registrations.model.InschrijvingX;
import be.pirlewiet.registrations.model.Organisatie;
import be.pirlewiet.registrations.model.SecretariaatsMedewerker;

@Controller
@RequestMapping( {"/inschrijvingen"} )
public class InschrijvingenController {
	
	protected Logger logger 
		= LoggerFactory.getLogger( this.getClass() );
	
	@Resource
	SecretariaatsMedewerker secretariaatsMedewerker;
	
	@RequestMapping( method = { RequestMethod.GET }, produces={"application/json","text/xml"} )
	@ResponseBody
	public ResponseEntity<List<InschrijvingX>> get( WebRequest request ) {
		
		List<InschrijvingX> inschrijvingen
			= new ArrayList<InschrijvingX>( );
		
		Organisatie organisatie
			= this.organisatie( request );
		
		inschrijvingen.addAll( this.secretariaatsMedewerker.actueleInschrijvingen( organisatie ) );
		
		return response( inschrijvingen, HttpStatus.OK );
		
	}
	
	@RequestMapping( method = { RequestMethod.POST } )
	@ResponseBody
	public ResponseEntity<InschrijvingX> post(
				@RequestBody InschrijvingX inschrijving, WebRequest request ) {
		
		Organisatie organisatie
			= this.organisatie( request );
		
		inschrijving.setOrganisatie( organisatie );
		
		InschrijvingX aangemaakt
			= this.secretariaatsMedewerker.ontvangInschrijving( inschrijving );
		
		if ( aangemaakt == null ) {
			throw new RuntimeException("create failed");
		}
		
		return response( aangemaakt, HttpStatus.CREATED );
			
	}
	
	@ExceptionHandler(Exception.class)
	public ResponseEntity<String> handleError( Exception e ){
		
		logger.warn( "failure while handling request", e );
		return response( e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR );
		
	}
	
	protected Organisatie organisatie( WebRequest request ) {
		
		Organisatie organisatie 
			= new Organisatie();
		
		organisatie.setId( 2L );
		
		return organisatie;
		
	}
	
}

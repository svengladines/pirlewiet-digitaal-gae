package be.pirlewiet.registrations.web.controllers;

import static be.occam.utils.spring.web.Controller.response;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import be.pirlewiet.registrations.domain.PirlewietException;
import be.pirlewiet.registrations.domain.SecretariaatsMedewerker;
import be.pirlewiet.registrations.model.Deelnemer;

@Controller
@RequestMapping( {"/inschrijvingen/{inschrijving}/deelnemers/{id}"} )
public class ParticipantController {
	
	protected Logger logger 
		= LoggerFactory.getLogger( this.getClass() );
	
	@Resource
	protected SecretariaatsMedewerker secretariaatsMedewerker;
	
	@RequestMapping( method = { RequestMethod.GET }, produces={"application/json","text/xml"} )
	@ResponseBody
	public ResponseEntity<Deelnemer> retrieve( @PathVariable String id ) {
		
		Deelnemer deelnemer
			= this.secretariaatsMedewerker.deelnemer( id );
		
		return response( deelnemer, HttpStatus.OK );
		
	}
	
	@ExceptionHandler( HttpMessageNotReadableException.class)
	@ResponseBody
	public ResponseEntity<String> handleFailure( HttpMessageNotReadableException e ){
		
		logger.warn( "failure while parsing request", e );
		
		String message
			= e.getMessage();
		
		if ( message.contains("Date") ) {
			message = "De geboortedatum is niet in het gewenste formaat. Een goed voorbeeld is: 28/08/1977";
		}
		else {
			 message = "Er zit een fout in de gegevens van de deelnemer. Kijk AUB deze gegevens na.";
		}
		
		return response( message, HttpStatus.BAD_REQUEST );
		
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
		return response( "De gegevens van de deelnemer konden niet worden opgeslagen. Probeer AUB opnieuw.", HttpStatus.INTERNAL_SERVER_ERROR );
		
	}
	
}

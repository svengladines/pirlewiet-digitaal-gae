package be.pirlewiet.registrations.web.controllers;

import static be.occam.utils.spring.web.Controller.response;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import be.pirlewiet.registrations.domain.SecretariaatsMedewerker;
import be.pirlewiet.registrations.model.Deelnemer;

@Controller
@RequestMapping( {"/inschrijvingen/{inschrijving}/deelnemers/{id}"} )
public class DeelnemerController {
	
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
	
	@RequestMapping( method = { RequestMethod.PUT } )
	@ResponseBody
	public ResponseEntity<Deelnemer> update(
			 	@PathVariable String inschrijving,
				@RequestBody Deelnemer deelnemer ) {
		
		// don't use id, GAE keys clash (probably only unique within context, not global)
		// Deelnemer updated
		this.secretariaatsMedewerker.guard().updateDeelnemer( inschrijving, deelnemer );
		
		return response( deelnemer, HttpStatus.OK );
		
	}
	
	@ExceptionHandler(Exception.class)
	@ResponseBody
	public ResponseEntity<String> handleError( Exception e ){
		
		logger.warn( "failure while handling request", e );
		return response( "De gegevens van de deelnemer konden niet worden opgeslagen. Probeer AUB opnieuw.", HttpStatus.INTERNAL_SERVER_ERROR );
		
	}
	
}

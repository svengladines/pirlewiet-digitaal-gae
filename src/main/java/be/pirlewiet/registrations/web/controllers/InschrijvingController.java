package be.pirlewiet.registrations.web.controllers;

import static be.occam.utils.spring.web.Controller.response;

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
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import be.pirlewiet.registrations.domain.BuitenWipper;
import be.pirlewiet.registrations.domain.Intaker;
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
@RequestMapping( {"/inschrijvingen/{id}"} )
public class InschrijvingController {
	
	protected Logger logger 
		= LoggerFactory.getLogger( this.getClass() );
	
	@Resource
	SecretariaatsMedewerker secretariaatsMedewerker;
	
	@Resource
	BuitenWipper buitenWipper;
	
	@Resource
	Intaker intaker;
	
	@RequestMapping( method = { RequestMethod.GET }, produces={"application/json","text/xml"} )
	@ResponseBody
	public ResponseEntity<InschrijvingX> retrieve( @PathVariable String id ) {
		
		InschrijvingX inschrijving
			= this.secretariaatsMedewerker.guard().findInschrijving( Long.valueOf( id ) );
		
		if ( inschrijving == null ) {
			return response( HttpStatus.NOT_FOUND );
		}
		
		logger.info( "number of vakanties: [{}]", inschrijving.getVakanties().size() );

		return response( inschrijving, HttpStatus.OK );
		
	}
	
	@RequestMapping( method = { RequestMethod.PUT } )
	@ResponseBody
	public ResponseEntity<InschrijvingX> update(
				@RequestBody InschrijvingX inschrijving ) {
		
		return response( inschrijving, HttpStatus.OK );
		
	}
	
	@RequestMapping( value="/contact", method = { RequestMethod.PUT } )
	@ResponseBody
	public ResponseEntity<ContactGegevens> contactUpdate(
				@PathVariable String id,
				@RequestBody ContactGegevens contactGegevens ) {
		
		this.retrieve( id );
		
		this.secretariaatsMedewerker.guard().updateContact( Long.valueOf( id ), contactGegevens);
		
		return response( contactGegevens, HttpStatus.OK );
		
	}
	
	@RequestMapping( value="/vakanties", method = { RequestMethod.PUT } )
	@ResponseBody
	public ResponseEntity<String> updateVakanties(
				@PathVariable String id,
				@RequestBody String vakanties ) {
		
		InschrijvingX x 
			= this.secretariaatsMedewerker.guard().updateVakanties( Long.valueOf( id ), vakanties );
		
		return response( vakanties, HttpStatus.OK );
		
	}
	
	@RequestMapping( value="/deelnemers", method = { RequestMethod.GET }, produces={"application/json","text/xml"} )
	@ResponseBody
	public ResponseEntity<List<Deelnemer>> deelnemersRetrieve(
				@PathVariable String id  ) {
		
		ResponseEntity<InschrijvingX> retrieve
			= this.retrieve( id );
		
		InschrijvingX inschrijving
			= retrieve.getBody();
		
		return response( inschrijving.getDeelnemers(), HttpStatus.OK );
		
	}
	
	@RequestMapping( value="/deelnemers", method = { RequestMethod.POST } )
	@ResponseBody
	public ResponseEntity<Deelnemer> deelnemersAdd(
				@PathVariable String id,
				@RequestBody Deelnemer deelnemer ) {
		
		ResponseEntity<InschrijvingX> retrieve
			= this.retrieve( id );
		
		InschrijvingX inschrijving
			= retrieve.getBody();
		
		// temporarily not available (currently only one deelnemer per inschrijving)
		// this.secretariaatsMedewerker.addDeelnemer( inschrijving.getId(), deelnemer );
		
		return response( deelnemer, HttpStatus.OK );
		
	}
	
	@RequestMapping( value="/adres", method = { RequestMethod.PUT } )
	@ResponseBody
	public ResponseEntity<Adres> adressUpdate(
				@PathVariable String id,
				@RequestBody Adres adres ) {
		
		this.retrieve( id );
		
		this.secretariaatsMedewerker.guard().updateInschrijvingsAdres( Long.valueOf( id ), adres );
		
		return response( adres, HttpStatus.OK );
		
	}
	
	@RequestMapping( value="/vragen", method = { RequestMethod.PUT } )
	@ResponseBody
	public ResponseEntity<List<Vraag>> vragenUpdate(
				@PathVariable String id,
				@RequestBody List<Vraag> vragen ) {
		
		ResponseEntity<InschrijvingX> retrieve
			= this.retrieve( id );
		
		InschrijvingX inschrijving
			= retrieve.getBody();
		
		this.secretariaatsMedewerker.guard().updateVragenLijst( inschrijving.getId(), vragen );
		
		return response( vragen, HttpStatus.OK );
		
	}
	
	@RequestMapping( value="/opmerking", method = { RequestMethod.PUT } )
	@ResponseBody
	public ResponseEntity<String> opmerkingUpdate(
				@PathVariable String id,
				@RequestBody String opmerking ) {
		
		ResponseEntity<InschrijvingX> retrieve
			= this.retrieve( id );
		
		InschrijvingX inschrijving
			= retrieve.getBody();
		inschrijving.setOpmerking( opmerking );
		// this.secretariaatsMedewerker.guard().pasAan( inschrijving );
		
		return response( opmerking, HttpStatus.OK );
		
	}
	
	@RequestMapping( value="/status", method = { RequestMethod.PUT } )
	@ResponseBody
	public ResponseEntity<Status> statusUpdate(
				@PathVariable String id,
				@RequestBody Status status,
				@CookieValue(required=true, value="pwtid") String pwtid ) {
		
		Organisatie organisatie
			= this.buitenWipper.guard().whoHasID( Long.valueOf( pwtid ) );
		
		ResponseEntity<InschrijvingX> retrieve
			= this.retrieve( id );
		
		InschrijvingX inschrijving
			= retrieve.getBody();
		
		if ( PirlewietUtil.isPirlewiet( organisatie ) ) { 
		
			this.secretariaatsMedewerker.updateStatus( Long.valueOf( id ), status );
			
		}
		else {
			
			this.intaker.guard().update( inschrijving, status );
			
		}
		
		return response( status, HttpStatus.OK );
		
	}
	
	@ExceptionHandler(Exception.class)
	@ResponseBody
	public ResponseEntity<String> handleError( Exception e ){
		
		logger.warn( "failure while handling request", e );
		return response( e.getMessage(), HttpStatus.BAD_REQUEST );
		
	}
	
	@RequestMapping( method = { RequestMethod.GET }, produces={ MediaType.TEXT_HTML_VALUE } )
	public ModelAndView view( @PathVariable String id, @CookieValue(required=true, value="pwtid") String pwtid ) {
		
		Organisatie organisatie
			= this.buitenWipper.guard().whoHasID( Long.valueOf( pwtid ) );
		
		ResponseEntity<InschrijvingX> entity
			= this.retrieve( id );
		
		Map<String,Object> model
			= new HashMap<String,Object>();
		
		InschrijvingX inschrijving
			= entity.getBody();
		
		model.put( "inschrijving", inschrijving );
		
		List<Vakantie> vakanties
			= this.secretariaatsMedewerker.guard().actueleVakanties( );
		
		model.put( "vakanties", vakanties );
		
		String view
			= PirlewietUtil.isPirlewiet( organisatie ) ? "inschrijving_pirlewiet" : "inschrijving";

		return new ModelAndView( view, model );
		
	}
	
}

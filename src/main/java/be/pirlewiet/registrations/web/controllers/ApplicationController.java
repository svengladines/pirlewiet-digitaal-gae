package be.pirlewiet.registrations.web.controllers;

import static be.occam.utils.spring.web.Controller.response;

import java.util.Comparator;
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
import be.pirlewiet.registrations.domain.Applicant;
import be.pirlewiet.registrations.domain.SecretariaatsMedewerker;
import be.pirlewiet.registrations.domain.exception.PirlewietException;
import be.pirlewiet.registrations.model.Adres;
import be.pirlewiet.registrations.model.ContactGegevens;
import be.pirlewiet.registrations.model.Deelnemer;
import be.pirlewiet.registrations.model.InschrijvingX;
import be.pirlewiet.registrations.model.Organisatie;
import be.pirlewiet.registrations.model.Status;
import be.pirlewiet.registrations.model.Tags;
import be.pirlewiet.registrations.model.Vakantie;
import be.pirlewiet.registrations.model.Vraag;
import be.pirlewiet.registrations.web.util.PirlewietUtil;

@Controller
@RequestMapping( {"/inschrijvingen/{uuid}"} )
public class ApplicationController {
	
	protected Logger logger 
		= LoggerFactory.getLogger( this.getClass() );
	
	protected final Comparator<InschrijvingX> firstName
			= new Comparator<InschrijvingX>() {

			@Override
			public int compare(InschrijvingX o1, InschrijvingX o2) {
				return o1.getDeelnemers().get(0).getVoorNaam().compareTo(  o1.getDeelnemers().get(0).getVoorNaam() );
			}
			
			
		
		};
	
	@Resource
	SecretariaatsMedewerker secretariaatsMedewerker;
	
	@Resource
	BuitenWipper buitenWipper;
	
	@Resource
	Applicant intaker;
	
	@RequestMapping( method = { RequestMethod.GET }, produces={"application/json","text/xml"} )
	@ResponseBody
	public ResponseEntity<InschrijvingX> retrieve( @PathVariable String uuid ) {
		
		InschrijvingX inschrijving
			= this.secretariaatsMedewerker.guard().findInschrijving( uuid );
		
		if ( inschrijving == null ) {
			return response( HttpStatus.NOT_FOUND );
		}
		
		logger.debug( "[{}]; retrieved by secretary", inschrijving.getUuid() );
		//logger.debug( "[{}]; contact is [{}]", inschrijving.getUuid(), inschrijving.getContactGegevens().getName() );

		return response( inschrijving, HttpStatus.OK );
		
	}
	
	@RequestMapping( method = { RequestMethod.PUT } )
	@ResponseBody
	public ResponseEntity<InschrijvingX> update(
				@RequestBody InschrijvingX inschrijving ) {
		
		return response( inschrijving, HttpStatus.OK );
		
	}
	
	@RequestMapping( method = { RequestMethod.DELETE } )
	public ResponseEntity<InschrijvingX> delete( @PathVariable String uuid ) {
		
		this.secretariaatsMedewerker.guard().deleteEnrollment( uuid );
		
		return response( HttpStatus.OK );
		
	}
	
	@RequestMapping( value="/vakanties", method = { RequestMethod.PUT } )
	@ResponseBody
	public ResponseEntity<String> updateVakanties(
				@PathVariable String uuid,
				@RequestBody String vakanties ) {
		
		InschrijvingX x 
			= this.secretariaatsMedewerker.guard().updateVakanties( uuid, vakanties );
		
		return response( vakanties, HttpStatus.OK );
		
	}
	
	@RequestMapping( value="/contact", method = { RequestMethod.PUT } )
	@ResponseBody
	public ResponseEntity<ContactGegevens> contactUpdate(
				@PathVariable String uuid,
				@RequestBody ContactGegevens contactGegevens ) {
		
		this.secretariaatsMedewerker.guard().updateContact( uuid, contactGegevens);
		
		return response( contactGegevens, HttpStatus.OK );
		
	}
	
	@RequestMapping( value="/deelnemers", method = { RequestMethod.GET }, produces={"application/json","text/xml"} )
	@ResponseBody
	public ResponseEntity<List<Deelnemer>> deelnemersRetrieve(
				@PathVariable String uuid  ) {
		
		ResponseEntity<InschrijvingX> retrieve
			= this.retrieve( uuid );
		
		InschrijvingX inschrijving
			= retrieve.getBody();
		
		return response( inschrijving.getDeelnemers(), HttpStatus.OK );
		
	}
	
	@RequestMapping( value="/adres", method = { RequestMethod.PUT } )
	@ResponseBody
	public ResponseEntity<Adres> adressUpdate(
				@PathVariable String uuid,
				@RequestBody Adres adres ) {
		
		this.retrieve( uuid );
		
		this.secretariaatsMedewerker.guard().updateInschrijvingsAdres( uuid, adres );
		
		return response( adres, HttpStatus.OK );
		
	}
	
	@RequestMapping( value="/participant", method = { RequestMethod.PUT } )
	@ResponseBody
	public ResponseEntity<Deelnemer> participantUpdate(
			@PathVariable String uuid,
			@RequestBody Deelnemer participant ) {
		
		this.retrieve( uuid );
		
		this.secretariaatsMedewerker.guard().updateDeelnemer( uuid, participant );
		
		return response( participant, HttpStatus.OK );
		
	}
	
	@RequestMapping( value="/qlist", method = { RequestMethod.PUT } )
	@ResponseBody
	public ResponseEntity<List<Vraag>> questionsUpdate(
				@PathVariable String uuid,
				@RequestBody List<Vraag> vragen ) {
		
		ResponseEntity<InschrijvingX> retrieve
			= this.retrieve( uuid );
		
		InschrijvingX inschrijving
			= retrieve.getBody();
		
		this.secretariaatsMedewerker.guard().updateVragenLijst( inschrijving.getUuid(), vragen );
		
		return response( vragen, HttpStatus.OK );
		
	}
	
	@RequestMapping( value="/status", method = { RequestMethod.PUT } )
	@ResponseBody
	public ResponseEntity<Status> updateStatus(
				@PathVariable String uuid,
				@RequestBody Status status,
				@CookieValue(required=true, value="pwtid") String pwtid ) {
		
		Organisatie organisatie
			= this.buitenWipper.guard().whoHasID(  pwtid  );
		
		ResponseEntity<InschrijvingX> retrieve
			= this.retrieve( uuid );
		
		InschrijvingX inschrijving
			= retrieve.getBody();
		
		if ( PirlewietUtil.isPirlewiet( organisatie ) ) { 
		
			this.secretariaatsMedewerker.updateStatus( uuid, status );
			
		}
		else {
			
			this.intaker.guard().updateStatus( inschrijving, status );
			
		}
		
		return response( status, HttpStatus.OK );
		
	}
	
	@ExceptionHandler(Exception.class)
	@ResponseBody
	public ResponseEntity<String> handleError( Exception e ){
		
		logger.warn( "failure while handling request", e );
		return response( e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR );
		
	}
	
	@ExceptionHandler( PirlewietException.class)
	@ResponseBody
	public ResponseEntity<String> handleFailure( PirlewietException e ){
		
		logger.warn( "failure while handling request", e );
		return response( e.getMessage(), HttpStatus.BAD_REQUEST );
		
	}
	
	@RequestMapping( method = { RequestMethod.GET }, produces={ MediaType.TEXT_HTML_VALUE } )
	public ModelAndView view( @PathVariable String uuid, @CookieValue(required=true, value="pwtid") String pwtid ) {
		
		Organisatie organisatie
			= this.buitenWipper.guard().whoHasID(  pwtid  );
		
		ResponseEntity<InschrijvingX> entity
			= this.retrieve( uuid );
		
		Map<String,Object> model
			= new HashMap<String,Object>();
		
		InschrijvingX application
			= entity.getBody();
		
		model.put( "application", application );
		
		List<Vakantie> vakanties
			= this.secretariaatsMedewerker.guard().actueleVakanties( );
		
		model.put( "vakanties", vakanties );
		model.put( "applicationHolidaysResult", this.secretariaatsMedewerker.guard().checkApplicationHolidaysStatus( application ) );
		model.put( "applicationContactResult", this.secretariaatsMedewerker.guard().checkApplicationContactStatus( application ) );
		model.put( "applicationQuestionListResult", this.secretariaatsMedewerker.guard().checkApplicationQuestionList( application ) );
		model.put( "enrollmentsStatus", this.secretariaatsMedewerker.guard().checkEnrollmentsStatus( application ) );
		model.put( "related", this.secretariaatsMedewerker.guard().findRelated( application, true) );
		
		Status applicationStatus
			= this.secretariaatsMedewerker.guard().whatIsTheApplicationStatus( application );
		
		model.put("applicationStatus", applicationStatus );
		
		String view
			= PirlewietUtil.isPirlewiet( organisatie ) ? "inschrijving_pirlewiet" : "inschrijving";

		return new ModelAndView( view, model );
		
	}
	
}

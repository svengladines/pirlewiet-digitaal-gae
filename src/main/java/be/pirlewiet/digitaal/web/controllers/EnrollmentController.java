package be.pirlewiet.digitaal.web.controllers;

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

import be.pirlewiet.digitaal.domain.exception.PirlewietException;
import be.pirlewiet.digitaal.domain.people.ApplicationManager;
import be.pirlewiet.digitaal.domain.people.DoorMan;
import be.pirlewiet.digitaal.domain.people.Secretary;
import be.pirlewiet.digitaal.model.Address;
import be.pirlewiet.digitaal.model.PersonInfo;
import be.pirlewiet.digitaal.model.Participant;
import be.pirlewiet.digitaal.model.Enrollment;
import be.pirlewiet.digitaal.model.Organisation;
import be.pirlewiet.digitaal.model.EnrollmentStatus;
import be.pirlewiet.digitaal.model.Tags;
import be.pirlewiet.digitaal.model.Holiday;
import be.pirlewiet.digitaal.model.QnA;
import be.pirlewiet.digitaal.web.util.PirlewietUtil;

@Controller
@RequestMapping( {"/enrollments/{uuid}"} )
public class EnrollmentController {
	
	protected Logger logger 
		= LoggerFactory.getLogger( this.getClass() );
	
	protected final Comparator<Enrollment> firstName
			= new Comparator<Enrollment>() {

			@Override
			public int compare(Enrollment o1, Enrollment o2) {
				return o1.getDeelnemers().get(0).getVoorNaam().compareTo(  o1.getDeelnemers().get(0).getVoorNaam() );
			}
			
			
		
		};
	
	@Resource
	Secretary secretariaatsMedewerker;
	
	@Resource
	DoorMan buitenWipper;
	
	@Resource
	ApplicationManager intaker;
	
	@RequestMapping( method = { RequestMethod.GET }, produces={"application/json","text/xml"} )
	@ResponseBody
	public ResponseEntity<Enrollment> retrieve( @PathVariable String uuid ) {
		
		Enrollment inschrijving
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
	public ResponseEntity<Enrollment> update(
				@RequestBody Enrollment inschrijving ) {
		
		return response( inschrijving, HttpStatus.OK );
		
	}
	
	@RequestMapping( method = { RequestMethod.DELETE } )
	public ResponseEntity<Enrollment> delete( @PathVariable String uuid ) {
		
		this.secretariaatsMedewerker.guard().deleteEnrollment( uuid );
		
		return response( HttpStatus.OK );
		
	}
	
	@RequestMapping( value="/vakanties", method = { RequestMethod.PUT } )
	@ResponseBody
	public ResponseEntity<String> updateVakanties(
				@PathVariable String uuid,
				@RequestBody String vakanties ) {
		
		Enrollment x 
			= this.secretariaatsMedewerker.guard().updateVakanties( uuid, vakanties );
		
		return response( vakanties, HttpStatus.OK );
		
	}
	
	@RequestMapping( value="/contact", method = { RequestMethod.PUT } )
	@ResponseBody
	public ResponseEntity<PersonInfo> contactUpdate(
				@PathVariable String uuid,
				@RequestBody PersonInfo contactGegevens ) {
		
		this.secretariaatsMedewerker.guard().updateContact( uuid, contactGegevens);
		
		return response( contactGegevens, HttpStatus.OK );
		
	}
	
	@RequestMapping( value="/deelnemers", method = { RequestMethod.GET }, produces={"application/json","text/xml"} )
	@ResponseBody
	public ResponseEntity<List<Participant>> deelnemersRetrieve(
				@PathVariable String uuid  ) {
		
		ResponseEntity<Enrollment> retrieve
			= this.retrieve( uuid );
		
		Enrollment inschrijving
			= retrieve.getBody();
		
		return response( inschrijving.getDeelnemers(), HttpStatus.OK );
		
	}
	
	@RequestMapping( value="/adres", method = { RequestMethod.PUT } )
	@ResponseBody
	public ResponseEntity<Address> adressUpdate(
				@PathVariable String uuid,
				@RequestBody Address adres ) {
		
		this.retrieve( uuid );
		
		this.secretariaatsMedewerker.guard().updateInschrijvingsAdres( uuid, adres );
		
		return response( adres, HttpStatus.OK );
		
	}
	
	@RequestMapping( value="/participant", method = { RequestMethod.PUT } )
	@ResponseBody
	public ResponseEntity<Participant> participantUpdate(
			@PathVariable String uuid,
			@RequestBody Participant participant ) {
		
		this.retrieve( uuid );
		
		this.secretariaatsMedewerker.guard().updateDeelnemer( uuid, participant );
		
		return response( participant, HttpStatus.OK );
		
	}
	
	@RequestMapping( value="/qlist", method = { RequestMethod.PUT } )
	@ResponseBody
	public ResponseEntity<List<QnA>> questionsUpdate(
				@PathVariable String uuid,
				@RequestBody List<QnA> vragen ) {
		
		ResponseEntity<Enrollment> retrieve
			= this.retrieve( uuid );
		
		Enrollment inschrijving
			= retrieve.getBody();
		
		this.secretariaatsMedewerker.guard().updateVragenLijst( inschrijving.getUuid(), vragen );
		
		return response( vragen, HttpStatus.OK );
		
	}
	
	@RequestMapping( value="/status", method = { RequestMethod.PUT } )
	@ResponseBody
	public ResponseEntity<EnrollmentStatus> updateStatus(
				@PathVariable String uuid,
				@RequestBody EnrollmentStatus status,
				@CookieValue(required=true, value="pwtid") String pwtid ) {
		
		Organisation organisatie
			= this.buitenWipper.guard().whoHasID(  pwtid  );
		
		ResponseEntity<Enrollment> retrieve
			= this.retrieve( uuid );
		
		Enrollment inschrijving
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
		
		Organisation organisatie
			= this.buitenWipper.guard().whoHasID(  pwtid  );
		
		ResponseEntity<Enrollment> entity
			= this.retrieve( uuid );
		
		Map<String,Object> model
			= new HashMap<String,Object>();
		
		Enrollment enrollment
			= entity.getBody();
		
		model.put( "enrollment", enrollment );
		
		String view
			= PirlewietUtil.isPirlewiet( organisatie ) ? "enrollment_pirlewiet" : "error";

		return new ModelAndView( view, model );
		
	}
	
}

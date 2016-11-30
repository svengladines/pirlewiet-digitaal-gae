package be.pirlewiet.digitaal.web.controller.api;

import static be.occam.utils.spring.web.Controller.response;

import java.util.List;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import be.occam.utils.spring.web.Result;
import be.pirlewiet.digitaal.domain.people.DoorMan;
import be.pirlewiet.digitaal.domain.service.EnrollmentService;
import be.pirlewiet.digitaal.dto.ApplicationDTO;
import be.pirlewiet.digitaal.dto.EnrollmentDTO;
import be.pirlewiet.digitaal.dto.QuestionAndAnswerDTO;
import be.pirlewiet.digitaal.model.Organisation;

@Controller
@RequestMapping( {"/applications/{applicationUuid}/enrollments/{uuid}"} )
public class EnrollmentController {
	
	protected Logger logger 
		= LoggerFactory.getLogger( this.getClass() );
	
	@Resource
	EnrollmentService enrollmentService;
	
	@Resource
	DoorMan doorMan;
	
	@RequestMapping( value="/qlist", method = { RequestMethod.PUT } )
	@ResponseBody
	public ResponseEntity<Result<EnrollmentDTO>> updateQList(
				@PathVariable String uuid,
				@RequestBody List<QuestionAndAnswerDTO> qList ,
				@CookieValue(required=true, value="pwtid") String pwtid ) {
		
		logger.info("enrollment.updateQList");
		
		Organisation actor
			= this.doorMan.guard().whoHasID(  pwtid  );
		
		Result<EnrollmentDTO> x 
			= this.enrollmentService.guard().updateQList ( uuid, qList, actor );
		
		return response( x, HttpStatus.OK );
		
	}
	
	/*
	@RequestMapping( value="/status", method = { RequestMethod.PUT } )
	@ResponseBody
	public ResponseEntity<Result<ApplicationDTO>> updateStatus(
				@PathVariable String uuid,
				@RequestBody ApplicationStatus applicationStatus,
				@CookieValue(required=true, value="pwtid") String pwtid ) {
		
		logger.info("application.updateStatus");
		
		Organisation actor
			= this.doorMan.guard().whoHasID(  pwtid  );
		
		Result<ApplicationDTO> x 
			= this.applicationService.guard().updateStatus( uuid, applicationStatus, actor );
		
		return response( x, HttpStatus.OK );
		
	}
	
	/*
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
		
		// this.secretariaatsMedewerker.guard().deleteEnrollment( uuid );
		
		return response( HttpStatus.OK );
		
	}
	
	@RequestMapping( value="/contact", method = { RequestMethod.PUT } )
	@ResponseBody
	public ResponseEntity<PersonInfo> contactUpdate(
				@PathVariable String uuid,
				@RequestBody PersonInfo contactGegevens ) {
		
		// this.secretariaatsMedewerker.guard().updateContact( uuid, contactGegevens);
		
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
	public ResponseEntity<List<QuestionAndAnswer>> questionsUpdate(
				@PathVariable String uuid,
				@RequestBody List<QuestionAndAnswer> vragen ) {
		
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
		
		logger.info( "[{}]; updateStatus via PUT for [{}]", pwtid, uuid );
		
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
		
		Enrollment application
			= entity.getBody();
		
		model.put( "application", application );
		
		List<Holiday> vakanties
			= this.secretariaatsMedewerker.guard().actueleVakanties( );
		
		model.put( "vakanties", vakanties );
		model.put( "applicationHolidaysResult", this.secretariaatsMedewerker.guard().checkApplicationHolidaysStatus( application ) );
		model.put( "applicationContactResult", this.secretariaatsMedewerker.guard().checkApplicationContactStatus( application ) );
		model.put( "applicationQuestionListResult", this.secretariaatsMedewerker.guard().checkApplicationQuestionList( application ) );
		model.put( "enrollmentsStatus", this.secretariaatsMedewerker.guard().checkEnrollmentsStatus( application ) );
		model.put( "related", this.secretariaatsMedewerker.guard().findRelated( application, true) );
		
		EnrollmentStatus applicationStatus
			= this.secretariaatsMedewerker.guard().whatIsTheApplicationStatus( application );
		
		model.put("applicationStatus", applicationStatus );
		
		// pirlewiet no longer has separate 'inschrijving' view
		String view
			= PirlewietUtil.isPirlewiet( organisatie ) ? "inschrijving" : "inschrijving";

		return new ModelAndView( view, model );
		
	}
	*/
	
}

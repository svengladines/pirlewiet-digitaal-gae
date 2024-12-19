package be.pirlewiet.digitaal.web.controller.api;

import static be.occam.utils.spring.web.Controller.response;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

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
import be.pirlewiet.digitaal.domain.people.ApplicationManager;
import be.pirlewiet.digitaal.domain.people.DoorMan;
import be.pirlewiet.digitaal.domain.service.ApplicationService;
import be.pirlewiet.digitaal.model.ApplicationStatus;
import be.pirlewiet.digitaal.model.Organisation;
import be.pirlewiet.digitaal.web.dto.ApplicationDTO;
import be.pirlewiet.digitaal.web.dto.HolidayDTO;
import be.pirlewiet.digitaal.web.dto.PersonDTO;
import be.pirlewiet.digitaal.web.dto.QuestionAndAnswerDTO;

@Controller
@RequestMapping( {"/api/applications/{uuid}"} )
public class ApplicationController {
	
	protected Logger logger 
		= LoggerFactory.getLogger( this.getClass() );
	
	@Autowired
	ApplicationService applicationService;
	
	@Autowired
	DoorMan doorMan;
	
	@Autowired
	ApplicationManager intaker;
	
	@RequestMapping( value="/holidays", method = { RequestMethod.PUT } )
	@ResponseBody
	public ResponseEntity<Result<ApplicationDTO>> updateHolidays(
				@PathVariable String uuid,
				@RequestBody List<HolidayDTO> holidays,
				@CookieValue(required=true, value="pwtid") String pwtid ) {
		
		logger.debug("application.updateHolidays");
		Organisation actor = this.doorMan.guard().whoHasID(  pwtid  );
		Result<ApplicationDTO> x = this.applicationService.guard().updateHolidays( uuid, holidays, actor );
		return response( x, HttpStatus.OK );
		
	}
	
	@RequestMapping( value="/contact", method = { RequestMethod.PUT } )
	@ResponseBody
	public ResponseEntity<Result<ApplicationDTO>> updateContact(
				@PathVariable String uuid,
				@RequestBody PersonDTO contact,
				@CookieValue(required=true, value="pwtid") String pwtid ) {
		
		logger.debug("application.updateContact");
		Organisation actor = this.doorMan.guard().whoHasID(  pwtid  );
		Result<ApplicationDTO> x = this.applicationService.guard().updateContact( uuid, contact, actor );
		return response( x, HttpStatus.OK );
	}
	
	@RequestMapping( value="/qlist", method = { RequestMethod.PUT } )
	@ResponseBody
	public ResponseEntity<Result<ApplicationDTO>> updateQList(
				@PathVariable String uuid,
				@RequestBody List<QuestionAndAnswerDTO> qList ,
				@CookieValue(required=true, value="pwtid") String pwtid ) {
		
		logger.debug("application.updateQList");
		Organisation actor = this.doorMan.guard().whoHasID(  pwtid  );
		Result<ApplicationDTO> x = this.applicationService.guard().updateQList ( uuid, qList, actor );
		return response( x, HttpStatus.OK );
	}
	
	@RequestMapping( value="/status", method = { RequestMethod.PUT } )
	@ResponseBody
	public ResponseEntity<Result<ApplicationDTO>> updateStatus(
				@PathVariable String uuid,
				@RequestBody ApplicationStatus applicationStatus,
				@CookieValue(required=true, value="pwtid") String pwtid ) {
		logger.debug("application.updateStatus");
		Organisation actor = this.doorMan.guard().whoHasID(  pwtid  );
		Result<ApplicationDTO> x = this.applicationService.guard().updateStatus( uuid, applicationStatus, actor );
		return response( x, HttpStatus.OK );
	}
	
}

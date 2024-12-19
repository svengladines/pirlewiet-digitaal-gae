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
import be.pirlewiet.digitaal.domain.people.DoorMan;
import be.pirlewiet.digitaal.domain.service.EnrollmentService;
import be.pirlewiet.digitaal.model.EnrollmentStatus;
import be.pirlewiet.digitaal.model.Organisation;
import be.pirlewiet.digitaal.web.dto.EnrollmentDTO;
import be.pirlewiet.digitaal.web.dto.HolidayDTO;
import be.pirlewiet.digitaal.web.dto.QuestionAndAnswerDTO;

@Controller
@RequestMapping( {"api/applications/{applicationUuid}/enrollments/{uuid}"} )
public class EnrollmentController {
	
	protected Logger logger 
		= LoggerFactory.getLogger( this.getClass() );
	
	@Autowired
	EnrollmentService enrollmentService;
	
	@Autowired
	DoorMan doorMan;
	

	@RequestMapping( method = { RequestMethod.PUT } )
	@ResponseBody
	public ResponseEntity<Result<EnrollmentDTO>> update(
				@PathVariable String uuid,
				@RequestBody EnrollmentDTO enrollment,
				@CookieValue(required=true, value="pwtid") String pwtid ) {
		
		logger.info("application.updateStatus");
		
		Organisation actor
			= this.doorMan.guard().whoHasID(  pwtid  );
		
		// TODO, think about this ... securitywize ... iz ok for now
		enrollment.setUuid( uuid );
		
		Result<EnrollmentDTO> x 
			= this.enrollmentService.guard().update( enrollment, actor );
		
		return response( x, HttpStatus.OK );
		
	}
	
	@RequestMapping( value="/status", method = { RequestMethod.PUT } )
	@ResponseBody
	public ResponseEntity<Result<EnrollmentDTO>> updateStatus(
				@PathVariable String uuid,
				@RequestBody EnrollmentStatus enrollmentStatus,
				@CookieValue(required=true, value="pwtid") String pwtid ) {
		
		logger.debug("application.updateStatus");
		
		Organisation actor
			= this.doorMan.guard().whoHasID(  pwtid  );
		
		Result<EnrollmentDTO> x 
			= this.enrollmentService.guard().updateStatus( uuid, enrollmentStatus, actor );
		
		return response( x, HttpStatus.OK );
		
	}
	
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

	@RequestMapping( value="/holidays", method = { RequestMethod.PUT } )
	@ResponseBody
	public ResponseEntity<Result<EnrollmentDTO>> updateHolidays(
				@PathVariable String uuid,
				@RequestBody List<HolidayDTO> holidays,
				@CookieValue(required=true, value="pwtid") String pwtid ) {
		
		logger.info("enrolment.updateHolidays");
		
		Organisation actor
			= this.doorMan.guard().whoHasID(  pwtid  );
		
		Result<EnrollmentDTO> x 
			= this.enrollmentService.guard().updateHolidays( uuid, holidays, actor );
		
		return response( x, HttpStatus.OK );
		
	}
	
	@RequestMapping( method = { RequestMethod.DELETE } )
	@ResponseBody
	public ResponseEntity<Result<EnrollmentDTO>> delete(
				@PathVariable String uuid,
				@CookieValue(required=true, value="pwtid") String pwtid ) {
		
		logger.info("application.updateStatus");
		
		Organisation actor
			= this.doorMan.guard().whoHasID(  pwtid  );

		Result<EnrollmentDTO> x 
			= this.enrollmentService.guard().delete( uuid, actor );
		
		return response( x, HttpStatus.OK );
		
	}
	
		
}

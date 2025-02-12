package be.pirlewiet.digitaal.web.controller.api;

import static be.occam.utils.spring.web.Controller.response;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import be.pirlewiet.digitaal.model.OrganisationType;
import org.springframework.beans.factory.annotation.Autowired;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.WebRequest;

import be.occam.utils.spring.web.Result;
import be.occam.utils.timing.Timing;
import be.pirlewiet.digitaal.domain.Mapper;
import be.pirlewiet.digitaal.domain.people.DoorMan;
import be.pirlewiet.digitaal.domain.service.EnrollmentService;
import be.pirlewiet.digitaal.model.EnrollmentStatus;
import be.pirlewiet.digitaal.model.Organisation;
import be.pirlewiet.digitaal.web.dto.EnrollmentDTO;
import org.thymeleaf.util.Validate;

@Controller
@RequestMapping( {"/api/applications/{applicationUuid}/enrollments"} )
public class EnrollmentsController {
	
	protected Logger logger = LoggerFactory.getLogger( this.getClass() );
	
	@Autowired
	EnrollmentService enrollmentService;
	
	@Autowired
	DoorMan doorMan;
	
	//@Autowired
	Mapper mapper;
	
	@RequestMapping( method = { RequestMethod.POST }, consumes = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public ResponseEntity<Result<EnrollmentDTO>> postJSON(
				@PathVariable String applicationUuid,
				@RequestBody EnrollmentDTO enrollment,
				WebRequest request,
				@CookieValue(required=true, value="pwtid") String pwtid ) {
		
		Result<EnrollmentDTO> result = new Result<EnrollmentDTO>();
		Organisation actor = this.doorMan.guard().whoHasID( pwtid );
		enrollment.setApplicationUuid( applicationUuid );
		result = this.enrollmentService.create( enrollment, actor );
		
		return new ResponseEntity( result, HttpStatus.CREATED );
			
	}

	@RequestMapping( method = { RequestMethod.POST }, consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
	public String postForm(
			@PathVariable String applicationUuid,
			@ModelAttribute EnrollmentDTO enrollment,
			@CookieValue(required=true, value="pwtid") String pwtid ) {

		Result<EnrollmentDTO> result = new Result<EnrollmentDTO>();

		Organisation actor = this.doorMan.guard().whoHasID( pwtid );
		enrollment.setApplicationUuid( applicationUuid );
		if (enrollment.getUuid()!=null) {
			result = this.enrollmentService.update(enrollment,actor);
		}
		else {
			result = this.enrollmentService.create(enrollment, actor);
		}

		if (Result.Value.OK.equals(result.getValue())) {
			return "redirect:/%s/application-%s.html".formatted(OrganisationType.INDIVIDUAL.equals(actor.getType()) ? "referenced" : "organisation", applicationUuid);
		}
		return "error";

	}
	
	@RequestMapping( value="/download", method = { RequestMethod.GET }, produces={ "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet" } )
	public ResponseEntity<byte[]> download( @CookieValue(required=true, value="pwtid") String pwtid, @RequestParam(required=false) EnrollmentStatus.Value status ) {
		

		Organisation actor = this.doorMan.guard().whoHasID( pwtid  );
		byte[] result = this.enrollmentService.download( actor );
		String disp = new StringBuilder("attachment; filename=_").append( "pirlewiet-digitaal_" ).append( Timing.date(new Date(), Timing.dateFormat ) ).append( ".xlsx" ).toString();
		HttpHeaders headers = new HttpHeaders() {};
		headers.add( "Content-Disposition", disp.toString() );
	
		return new ResponseEntity<>( result, headers, HttpStatus.OK );
		
	}
	
	@RequestMapping( value="/download-100", method = { RequestMethod.GET }, produces={ "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet" } )
	public ResponseEntity<byte[]> downloadOneUndred( @CookieValue(required=true, value="pwtid") String pwtid, @RequestParam(required=false) EnrollmentStatus.Value status ) {
		

		Organisation actor
			= this.doorMan.guard().whoHasID( pwtid  );
		

		byte[] result 
			= this.enrollmentService.downloadOneHundred( actor );
	
		String disp = new StringBuilder("attachment; filename=_").append( "pirlewiet-digitaal" ).append( Timing.date(new Date(), Timing.dateFormat ) ).append( ".xlsx" ).toString();
		HttpHeaders headers = new HttpHeaders() {};
		headers.add( "Content-Disposition", disp.toString() );
		return new ResponseEntity<>(result, headers, HttpStatus.OK);
		
	}
	
}

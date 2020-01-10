package be.pirlewiet.digitaal.web.controller.api;

import static be.occam.utils.spring.web.Controller.response;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.request.WebRequest;

import be.occam.utils.spring.web.Result;
import be.occam.utils.timing.Timing;
import be.pirlewiet.digitaal.domain.Mapper;
import be.pirlewiet.digitaal.domain.people.DoorMan;
import be.pirlewiet.digitaal.domain.service.EnrollmentService;
import be.pirlewiet.digitaal.model.EnrollmentStatus;
import be.pirlewiet.digitaal.model.Organisation;
import be.pirlewiet.digitaal.web.dto.EnrollmentDTO;

@Controller
@RequestMapping( {"/applications/{applicationUuid}/enrollments"} )
public class EnrollmentsController {
	
	protected Logger logger 
		= LoggerFactory.getLogger( this.getClass() );
	
	@Resource
	EnrollmentService enrollmentService;
	
	@Resource
	DoorMan doorMan;
	
	//@Resource
	Mapper mapper;
	
	@RequestMapping( method = { RequestMethod.POST } )
	@ResponseBody
	public ResponseEntity<Result<EnrollmentDTO>> post(
				@PathVariable String applicationUuid,
				@RequestBody EnrollmentDTO enrollment, WebRequest request, @CookieValue(required=true, value="pwtid") String pwtid ) {
		
		Result<EnrollmentDTO> result
			= new Result<EnrollmentDTO>();
		
		Organisation actor
			= this.doorMan.guard().whoHasID( pwtid );
		
		enrollment.setApplicationUuid( applicationUuid );
		
		result = this.enrollmentService.create( enrollment, actor );
		
		return response( result, HttpStatus.CREATED );
			
	}
	
	/*
	
	@RequestMapping( method = { RequestMethod.GET }, produces={"application/json","text/xml"} )
	@ResponseBody
	public ResponseEntity<List<Enrollment>> query( WebRequest request, @CookieValue(required=true, value="pwtid") String pwtid ) {
		
		List<Enrollment> inschrijvingen
			= new ArrayList<Enrollment>( );
		
		Organisation organisatie
			= this.organisatie( request, pwtid );
		
		inschrijvingen.addAll( this.secretariaatsMedewerker.guard().actueleInschrijvingen( organisatie ) );
		
		return response( inschrijvingen, HttpStatus.OK );
		
	}
	
	*/
	
	@RequestMapping( value="/download", method = { RequestMethod.GET }, produces={ "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet" } )
	public ResponseEntity<byte[]> download( @CookieValue(required=true, value="pwtid") String pwtid, @RequestParam(required=false) EnrollmentStatus.Value status ) {
		

		Organisation actor
			= this.doorMan.guard().whoHasID( pwtid  );
		

		byte[] result 
			= this.enrollmentService.download( actor );
	
		String disp
			= new StringBuilder("attachment; filename=_").append( "pirlewiet-digitaal" ).append( Timing.date(new Date(), Timing.dateFormat ) ).append( ".xlsx" ).toString();
	
		Map<String,String> headers
			= new HashMap<String,String>();
		
		headers.put( "Content-Disposition", disp.toString() );
	
		return response( result, HttpStatus.OK, headers );
		
	}
	
	@RequestMapping( value="/download-100", method = { RequestMethod.GET }, produces={ "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet" } )
	public ResponseEntity<byte[]> downloadOneUndred( @CookieValue(required=true, value="pwtid") String pwtid, @RequestParam(required=false) EnrollmentStatus.Value status ) {
		

		Organisation actor
			= this.doorMan.guard().whoHasID( pwtid  );
		

		byte[] result 
			= this.enrollmentService.downloadOneHundred( actor );
	
		String disp
			= new StringBuilder("attachment; filename=_").append( "pirlewiet-digitaal" ).append( Timing.date(new Date(), Timing.dateFormat ) ).append( ".xlsx" ).toString();
	
		Map<String,String> headers
			= new HashMap<String,String>();
		
		headers.put( "Content-Disposition", disp.toString() );
	
		return response( result, HttpStatus.OK, headers );
		
	}
	
}

package be.pirlewiet.digitaal.web.controller;

import static be.occam.utils.spring.web.Controller.response;

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
import org.springframework.web.context.request.WebRequest;

import be.occam.utils.spring.web.Result;
import be.pirlewiet.digitaal.domain.Mapper;
import be.pirlewiet.digitaal.domain.people.DoorMan;
import be.pirlewiet.digitaal.domain.service.EnrollmentService;
import be.pirlewiet.digitaal.dto.EnrollmentDTO;
import be.pirlewiet.digitaal.model.Organisation;

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
	
	/*
	
	@RequestMapping( value="/download", method = { RequestMethod.GET }, produces={ "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet" } )
	public ResponseEntity<byte[]> download( @CookieValue(required=true, value="pwtid") String pwtid, @RequestParam(required=false) EnrollmentStatus.Value status ) {
		

		Organisation organisatie
			= this.buitenWipper.guard().whoHasID( pwtid  );
		
		// TODO: check organisatie != null
		List<Enrollment> applications
			= null;
		
		if ( EnrollmentStatus.Value.DRAFT.equals( status ) && ( PirlewietUtil.isPirlewiet( organisatie ) ) ) {
			applications = this.secretariaatsMedewerker.guard().drafts();
		}
		else {
			applications = this.secretariaatsMedewerker.guard().actueleInschrijvingen( organisatie );
		}
		
		logger.info( "download; number of applications is [{}]", applications.size() );
		
		List<String[]> rows
			= new LinkedList<String[]>();

		for ( Enrollment application : applications ) {
			
			List<Enrollment> related
				= this.secretariaatsMedewerker.guard().findRelated( application, false );
			
			if ( related != null ) {
				logger.info( "[{}]; found [{}] related enrollments", application.getUuid(), related.size() );
			}
			else {
				logger.info( "[{}]; found no related enrollments" );
			}
			
			List<String[]> mapped
				= this.mapper.asStrings( application, related, status );
			
			if ( mapped != null ) {
				
				rows.addAll( mapped );
				
			}
		
		}
		
		

		byte[] result 
			= this.mapper.asBytes( rows );
	
		String disp
			= new StringBuilder("attachment; filename=_").append( "pirlewiet-digitaal" ).append( Timing.date(new Date(), Timing.dateFormat ) ).append( ".xlsx" ).toString();
	
		Map<String,String> headers
			= new HashMap<String,String>();
		
		headers.put( "Content-Disposition", disp.toString() );
	
		return response( result, HttpStatus.OK, headers );
		
	}
	
	@RequestMapping( method = { RequestMethod.GET }, produces={ MediaType.TEXT_HTML_VALUE } )
	public ModelAndView view( @CookieValue(required=true, value="pwtid") String pwtid ) {
		
		Organisation organisatie
			= this.buitenWipper.guard().whoHasID( pwtid  );

		Map<String,Object> model
			= new HashMap<String,Object>();
	
		model.put( "organisation", organisatie );
	
		if ( PirlewietUtil.isPirlewiet( organisatie ) ) {
			
			List<Enrollment> inschrijvingen 
			= this.secretariaatsMedewerker.guard().actueleInschrijvingen( organisatie );
		
			List<Enrollment> enrollments
				= new LinkedList<Enrollment>();
		
			for ( Enrollment enrollment : inschrijvingen ) {
				
				List<Enrollment> r
					= new ArrayList<Enrollment>();
				
				r.add( enrollment );
				
				List<Enrollment> related
					= this.secretariaatsMedewerker.guard().findRelated( enrollment, false );
				
				if ( related != null ) {
					r.addAll( related );
				}
				
				enrollments.addAll( r );
				
			}
		
		model.put( "enrollments", enrollments );
		
		}
		else {
			List<Enrollment> inschrijvingen 
				= this.secretariaatsMedewerker.guard().actueleInschrijvingen( organisatie );
			
			List<Enrollment> enrollments
				= new LinkedList<Enrollment>();
			
			for ( Enrollment enrollment : inschrijvingen ) {
				
				enrollments.add( enrollment );
				
				List<Enrollment> related
					= this.secretariaatsMedewerker.guard().findRelated( enrollment, false );
				
				if ( related != null ) {
					enrollments.addAll( related );
				}
				
			}
			
			model.put( "enrollments", enrollments );	
		}
	
		String view
			= PirlewietUtil.isPirlewiet( organisatie ) ? "inschrijvingen_pirlewiet" : "inschrijvingen";
		
		return new ModelAndView( view, model );
		
	}
	
	
	
	@ExceptionHandler(Exception.class)
	public ResponseEntity<String> handleError( Exception e ){
		
		logger.warn( "failure while handling request", e );
		return response( e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR );
		
	}
	
	*/
	
	protected Organisation organisatie( WebRequest request, String pwtid ) {
		
		Organisation organisatie
			= new Organisation();
		organisatie.setUuid( pwtid );
		
		return organisatie;
		
	}
	
	

}

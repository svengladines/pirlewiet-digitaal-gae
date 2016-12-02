package be.pirlewiet.digitaal.web.controller.page;

import static be.occam.utils.spring.web.Controller.response;

import java.util.HashMap;
import java.util.LinkedList;
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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.ModelAndView;

import be.occam.utils.spring.web.Result;
import be.pirlewiet.digitaal.domain.people.DoorMan;
import be.pirlewiet.digitaal.domain.service.ApplicationService;
import be.pirlewiet.digitaal.domain.service.EnrollmentService;
import be.pirlewiet.digitaal.dto.ApplicationDTO;
import be.pirlewiet.digitaal.dto.EnrollmentDTO;
import be.pirlewiet.digitaal.model.Enrollment;
import be.pirlewiet.digitaal.model.Organisation;

@Controller
@RequestMapping( {"/applications.html"} )
public class ApplicationsPageController {
	
	protected Logger logger 
		= LoggerFactory.getLogger( this.getClass() );
	
	@Resource
	DoorMan doorMan;
	
	@Resource
	ApplicationService applicationService;
	
	@Resource
	EnrollmentService enrollmentService;
	
	/*
	
	@RequestMapping( method = { RequestMethod.POST } )
	@ResponseBody
	public ResponseEntity<Result<ApplicationDTO>> post( @RequestBody ApplicationDTO application, WebRequest request, @CookieValue(required=true, value="pwtid") String pwtid ) {
		
		Organisation actor
			= this.doorMan.guard().whoHasID(  pwtid  );
		
		application.setOrganisationUuid( actor.getUuid() );
		
		logger.info( "[{}]; request to create application", actor.getName() );
		
		Result<ApplicationDTO> created
			= this.applicationService.guard().create( application , actor);
		
		if ( created == null ) {
			throw new RuntimeException("create failed");
		}
		
		logger.info( "created enrollment with id [{}]", created.getObject().getUuid() );
		
		return response( created, HttpStatus.CREATED );
			
	}
	
	@RequestMapping( value="/download", method = { RequestMethod.GET }, produces={ "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet" } )
	public ResponseEntity<byte[]> download( @CookieValue(required=true, value="pwtid") String pwtid, @RequestParam(required=false) ApplicationStatus.Value status ) {
		

		Organisation actor
			= this.doorMan.guard().whoHasID( pwtid  );
		
		// TODO: check organisation != null
		List<Enrollment> applications
			= null;
		
		if ( ApplicationStatus.Value.DRAFT.equals( status ) && ( PirlewietUtil.isPirlewiet( organisation ) ) ) {
			applications = this.secretariaatsMedewerker.guard().drafts();
		}
		else {
			applications = this.secretariaatsMedewerker.guard().applicationsOfOrganisation( organisation );
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
	
	*/
	
	@RequestMapping( method = { RequestMethod.GET }, produces={ MediaType.TEXT_HTML_VALUE } )
	public ModelAndView view( @CookieValue(required=true, value="pwtid") String pwtid ) {
		
		Organisation actor
			= this.doorMan.guard().whoHasID( pwtid  );

		Map<String,Object> model
			= new HashMap<String,Object>();
	
		model.put( "organisation", actor );
	
		Result<List<Result<ApplicationDTO>>> applicationsResult 
				= this.applicationService.guard().query( actor );
			
		for ( Result<ApplicationDTO> applicationResult : applicationsResult.getObject() ) {
			
			ApplicationDTO application
				= applicationResult.getObject();
			
			Result<List<Result<EnrollmentDTO>>> enrollments
				= this.enrollmentService.guard().query( application.getUuid(), actor );
			
			if ( Result.Value.OK.equals( enrollments.getValue() ) ) {
				for ( Result<EnrollmentDTO> enrollmentResult : enrollments.getObject() ) {
					application.getEnrollments().add( enrollmentResult.getObject() );
				}
			}
			
		}
		
		model.put( "applicationsResult", applicationsResult );
	
		String view
			= "applications";
		
		return new ModelAndView( view, model );
		
	}
	
	
	
	@ExceptionHandler(Exception.class)
	public ResponseEntity<String> handleError( Exception e ){
		
		logger.warn( "failure while handling request", e );
		return response( e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR );
		
	}
	
	protected Organisation organisation( WebRequest request, String pwtid ) {
		
		Organisation organisation
			= new Organisation();
		organisation.setUuid( pwtid );
		
		return organisation;
		
	}
	
	

}

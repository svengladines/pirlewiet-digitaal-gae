package be.pirlewiet.digitaal.web.controller.page;

import static be.occam.utils.spring.web.Controller.response;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;

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
import be.pirlewiet.digitaal.model.Organisation;
import be.pirlewiet.digitaal.web.dto.ApplicationDTO;
import be.pirlewiet.digitaal.web.dto.EnrollmentDTO;

@Controller
@RequestMapping( {"/applications.html"} )
public class ApplicationsPageController {
	
	protected Logger logger 
		= LoggerFactory.getLogger( this.getClass() );
	
	@Autowired
	DoorMan doorMan;
	
	@Autowired
	ApplicationService applicationService;
	
	@Autowired
	EnrollmentService enrollmentService;
	
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
	
		String view = "applications";
		
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

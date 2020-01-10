package be.pirlewiet.digitaal.web.controller.api;

import static be.occam.utils.spring.web.Controller.response;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import be.occam.utils.spring.web.Result;
import be.pirlewiet.digitaal.domain.people.DoorMan;
import be.pirlewiet.digitaal.domain.service.ApplicationService;
import be.pirlewiet.digitaal.model.Organisation;
import be.pirlewiet.digitaal.web.dto.ApplicationDTO;

@Controller
@RequestMapping( {"/applications"} )
public class ApplicationsController {
	
	protected Logger logger 
		= LoggerFactory.getLogger( this.getClass() );
	
	@Resource
	DoorMan doorMan;
	
	@Resource
	ApplicationService applicationService;
	
	@RequestMapping( method = { RequestMethod.POST }, consumes = { MediaType.APPLICATION_JSON_VALUE }, produces = { MediaType.APPLICATION_JSON_VALUE } )
	@ResponseBody
	public ResponseEntity<Result<ApplicationDTO>> post( 
			@RequestBody ApplicationDTO application, 
			@CookieValue(required=true, value="pwtid") String pwtid  ) {
		
		Organisation actor
			= this.doorMan.guard().whoHasID(  pwtid  );
		
		Result<ApplicationDTO> createdResult
			= this.applicationService.guard().create( application, actor );
		
		return response( createdResult, HttpStatus.OK );
		
	}
	
	/*
	
	@RequestMapping( method = { RequestMethod.GET }, produces={"application/json"} )
	@ResponseBody
	public ResponseEntity<List<Application>> query( WebRequest request, @CookieValue(required=true, value="pwtid") String pwtid ) {
		
		List<Application> applications
			= new ArrayList<Application>( );
		
		Organisation organisation
			= this.organisation( request, pwtid );
		
		applications.addAll( this.secretary.guard().applicationsOfOrganisation( organisation ) );
		
		return response( applications, HttpStatus.OK );
		
	}
	
	@RequestMapping( method = { RequestMethod.POST } )
	@ResponseBody
	public ResponseEntity<Enrollment> post( @RequestBody Application application, WebRequest request, @CookieValue(required=true, value="pwtid") String pwtid ) {
		
		Organisation organisation
			= this.organisation( request, pwtid );
		
		inschrijving.setorganisation( organisation );
		
		logger.info( "[{}]; request to create application, provided reference [{}]", organisation.getNaam(), inschrijving.getReference() );
		
		Enrollment aangemaakt
			= this.secretariaatsMedewerker.guard().createEnrollment( inschrijving );
		
		if ( aangemaakt == null ) {
			throw new RuntimeException("create failed");
		}
		
		logger.info( "created enrollment with id [{}], reference [{}]", aangemaakt.getUuid(), aangemaakt.getReference() );
		
		return response( aangemaakt, HttpStatus.CREATED );
			
	}
	
	@RequestMapping( value="/download", method = { RequestMethod.GET }, produces={ "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet" } )
	public ResponseEntity<byte[]> download( @CookieValue(required=true, value="pwtid") String pwtid, @RequestParam(required=false) EnrollmentStatus.Value status ) {
		

		Organisation organisation
			= this.buitenWipper.guard().whoHasID( pwtid  );
		
		// TODO: check organisation != null
		List<Enrollment> applications
			= null;
		
		if ( EnrollmentStatus.Value.DRAFT.equals( status ) && ( PirlewietUtil.isPirlewiet( organisation ) ) ) {
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
	
	@RequestMapping( method = { RequestMethod.GET }, produces={ MediaType.TEXT_HTML_VALUE } )
	public ModelAndView view( @CookieValue(required=true, value="pwtid") String pwtid ) {
		
		Organisation organisation
			= this.buitenWipper.guard().whoHasID( pwtid  );

		Map<String,Object> model
			= new HashMap<String,Object>();
	
		model.put( "organisation", organisation );
	
		if ( PirlewietUtil.isPirlewiet( organisation ) ) {
			
			List<Enrollment> inschrijvingen 
			= this.secretariaatsMedewerker.guard().applicationsOfOrganisation( organisation );
		
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
				= this.secretariaatsMedewerker.guard().applicationsOfOrganisation( organisation );
			
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
			= PirlewietUtil.isPirlewiet( organisation ) ? "inschrijvingen_pirlewiet" : "inschrijvingen";
		
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
	
	*/
	
	

}

package be.pirlewiet.digitaal.web.controller;

import static be.occam.utils.spring.web.Controller.response;

import java.util.ArrayList;
import java.util.Date;
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
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.ModelAndView;

import be.occam.utils.timing.Timing;
import be.pirlewiet.digitaal.domain.Mapper;
import be.pirlewiet.digitaal.domain.people.DoorMan;
import be.pirlewiet.digitaal.domain.people.Secretary;
import be.pirlewiet.digitaal.model.Enrollment;
import be.pirlewiet.digitaal.model.Organisation;
import be.pirlewiet.digitaal.model.EnrollmentStatus;
import be.pirlewiet.digitaal.web.util.PirlewietUtil;

@Controller
@RequestMapping( {"/inschrijvingen"} )
public class EnrollmentsController {
	
	protected Logger logger 
		= LoggerFactory.getLogger( this.getClass() );
	
	@Resource
	Secretary secretariaatsMedewerker;
	
	@Resource
	DoorMan buitenWipper;
	
	@Resource
	Mapper mapper;
	
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
	
	@RequestMapping( method = { RequestMethod.POST } )
	@ResponseBody
	public ResponseEntity<Enrollment> post(
				@RequestBody Enrollment inschrijving, WebRequest request, @CookieValue(required=true, value="pwtid") String pwtid ) {
		
		Organisation organisatie
			= this.organisatie( request, pwtid );
		
		inschrijving.setOrganisatie( organisatie );
		
		logger.info( "[{}]; request to create enrollment, provided reference [{}]", organisatie.getCode(), inschrijving.getReference() );
		
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
	
	protected Organisation organisatie( WebRequest request, String pwtid ) {
		
		Organisation organisatie
			= new Organisation();
		organisatie.setUuid( pwtid );
		
		return organisatie;
		
	}
	
	

}

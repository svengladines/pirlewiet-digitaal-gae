package be.pirlewiet.registrations.web.controllers;

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
import be.pirlewiet.registrations.domain.BuitenWipper;
import be.pirlewiet.registrations.domain.Mapper;
import be.pirlewiet.registrations.domain.SecretariaatsMedewerker;
import be.pirlewiet.registrations.model.InschrijvingX;
import be.pirlewiet.registrations.model.Organisatie;
import be.pirlewiet.registrations.model.Status;
import be.pirlewiet.registrations.web.util.PirlewietUtil;

@Controller
@RequestMapping( {"/inschrijvingen"} )
public class EnrollmentsController {
	
	protected Logger logger 
		= LoggerFactory.getLogger( this.getClass() );
	
	@Resource
	SecretariaatsMedewerker secretariaatsMedewerker;
	
	@Resource
	BuitenWipper buitenWipper;
	
	@Resource
	Mapper mapper;
	
	@RequestMapping( method = { RequestMethod.GET }, produces={"application/json","text/xml"} )
	@ResponseBody
	public ResponseEntity<List<InschrijvingX>> query( WebRequest request, @CookieValue(required=true, value="pwtid") String pwtid ) {
		
		List<InschrijvingX> inschrijvingen
			= new ArrayList<InschrijvingX>( );
		
		Organisatie organisatie
			= this.organisatie( request, pwtid );
		
		inschrijvingen.addAll( this.secretariaatsMedewerker.guard().actueleInschrijvingen( organisatie ) );
		
		return response( inschrijvingen, HttpStatus.OK );
		
	}
	
	@RequestMapping( method = { RequestMethod.POST } )
	@ResponseBody
	public ResponseEntity<InschrijvingX> post(
				@RequestBody InschrijvingX inschrijving, WebRequest request, @CookieValue(required=true, value="pwtid") String pwtid ) {
		
		Organisatie organisatie
			= this.organisatie( request, pwtid );
		
		inschrijving.setOrganisatie( organisatie );
		
		logger.info( "[{}]; request to create enrollment, provided reference [{}]", organisatie.getCode(), inschrijving.getReference() );
		
		InschrijvingX aangemaakt
			= this.secretariaatsMedewerker.guard().createEnrollment( inschrijving );
		
		if ( aangemaakt == null ) {
			throw new RuntimeException("create failed");
		}
		
		logger.info( "created enrollment with id [{}], reference [{}]", aangemaakt.getUuid(), aangemaakt.getReference() );
		
		return response( aangemaakt, HttpStatus.CREATED );
			
	}
	
	@RequestMapping( value="/download", method = { RequestMethod.GET }, produces={ "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet" } )
	public ResponseEntity<byte[]> download( @CookieValue(required=true, value="pwtid") String pwtid, @RequestParam(required=false) Status.Value status ) {
		

		Organisatie organisatie
			= this.buitenWipper.guard().whoHasID( pwtid  );
		
		// TODO: check organisatie != null
		List<InschrijvingX> inschrijvingen
			= null;
		
		if ( Status.Value.DRAFT.equals( status ) && ( PirlewietUtil.isPirlewiet( organisatie ) ) ) {
			inschrijvingen = this.secretariaatsMedewerker.guard().drafts();
		}
		else {
			inschrijvingen = this.secretariaatsMedewerker.guard().actueleInschrijvingen( organisatie );
		}
		
		List<String[]> mapped
			= this.mapper.asStrings( inschrijvingen, status );

		byte[] result 
			= this.mapper.asBytes( mapped );
	
		String disp
			= new StringBuilder("attachment; filename=_").append( "pirlewiet-digitaal" ).append( Timing.date(new Date(), Timing.dateFormat ) ).append( ".xlsx" ).toString();
	
		Map<String,String> headers
			= new HashMap<String,String>();
		
		headers.put( "Content-Disposition", disp.toString() );
	
		return response( result, HttpStatus.OK, headers );
		
	}
	
	@RequestMapping( method = { RequestMethod.GET }, produces={ MediaType.TEXT_HTML_VALUE } )
	public ModelAndView view( @CookieValue(required=true, value="pwtid") String pwtid ) {
		
		Organisatie organisatie
			= this.buitenWipper.guard().whoHasID( pwtid  );

		Map<String,Object> model
			= new HashMap<String,Object>();
	
		model.put( "organisation", organisatie );
	
		if ( PirlewietUtil.isPirlewiet( organisatie ) ) {
			
			List<InschrijvingX> inschrijvingen 
				= this.secretariaatsMedewerker.guard().actueleInschrijvingen( organisatie );
		
		}
		else {
			List<InschrijvingX> inschrijvingen 
				= this.secretariaatsMedewerker.guard().actueleInschrijvingen( organisatie );
			
			List<InschrijvingX> enrollments
				= new LinkedList<InschrijvingX>();
			
			for ( InschrijvingX enrollment : inschrijvingen ) {
				
				enrollments.add( enrollment );
				
				List<InschrijvingX> related
					= this.secretariaatsMedewerker.guard().findRelated( enrollment );
				
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
	
	protected Organisatie organisatie( WebRequest request, String pwtid ) {
		
		Organisatie organisatie
			= new Organisatie();
		organisatie.setUuid( pwtid );
		
		return organisatie;
		
	}
	
	

}

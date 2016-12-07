package be.pirlewiet.digitaal.web.controller;

import static be.occam.utils.spring.web.Controller.response;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.request.WebRequest;

import be.occam.utils.spring.web.Result;
import be.pirlewiet.digitaal.domain.people.DoorMan;
import be.pirlewiet.digitaal.domain.service.HolidayService;
import be.pirlewiet.digitaal.dto.HolidayDTO;
import be.pirlewiet.digitaal.model.Holiday;
import be.pirlewiet.digitaal.model.Organisation;

@Controller
@RequestMapping( {"/holidays"} )
public class HolidaysController {
	
	protected Logger logger 
		= LoggerFactory.getLogger( this.getClass() );
	
	@Resource
	HolidayService holidayService;
	
	@Resource
	DoorMan doorMan;
	
	@RequestMapping( method = { RequestMethod.GET }, produces={"application/json"} )
	@ResponseBody
	public ResponseEntity<Result<List<Result<HolidayDTO>>>> get( WebRequest request, @CookieValue(required=true, value="pwtid") String pwtid ) {
		
		Organisation actor
			= this.doorMan.whoHasID( pwtid );
		
		Result<List<Result<HolidayDTO>>> holidaysResult
			= this.holidayService.query( actor );
		
		return response( holidaysResult, HttpStatus.OK );
		
	}
	
	/*
	
	@RequestMapping( method = { RequestMethod.POST } )
	@ResponseBody
	public ResponseEntity<Holiday> post(
				@RequestBody Holiday vakantie, WebRequest request, @CookieValue(required=true, value="pwtid") String pwtid ) {
		
		Organisation organisatie
			= this.organisatie( request, pwtid );
		
		if ( ! PirlewietUtil.isPirlewiet( organisatie ) ) {
			throw new RuntimeException("only pirlewiet can add vakanties");
		}
		
		Holiday aangemaakt
			= this.secretariaatsMedewerker.maakVakantie( vakantie );
		
		if ( aangemaakt == null ) {
			throw new RuntimeException("create failed");
		}
		
		return response( aangemaakt, HttpStatus.CREATED );
			
	}
	
	@RequestMapping( method = { RequestMethod.GET }, produces={ MediaType.TEXT_HTML_VALUE } )
	public ModelAndView view( @CookieValue(required=true, value="pwtid") String pwtid ) {
		

		Organisation organisatie
			= this.buitenWipper.whoHasID(  pwtid  );
		
		// TODO: check organisatie != null
		
		List<Holiday> inschrijvingen 
			= this.secretariaatsMedewerker.actueleVakanties();
		
		Map<String,Object> model
			= new HashMap<String,Object>();
		
		model.put( "organisatie", organisatie );
		model.put( "pwt", PirlewietUtil.isPirlewiet( organisatie ) );
		model.put( "inschrijvingen", inschrijvingen );
		
		return new ModelAndView( "inschrijvingen", model );
		
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
	
	*/
	
}

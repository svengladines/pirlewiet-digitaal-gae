package be.pirlewiet.registrations.view.controllers.crud;

import static be.occam.utils.spring.web.Controller.response;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import be.pirlewiet.registrations.model.Contactpersoon;
import be.pirlewiet.registrations.model.Deelnemer;
import be.pirlewiet.registrations.model.Dienst;
import be.pirlewiet.registrations.model.Inschrijving;
import be.pirlewiet.registrations.model.Status;
import be.pirlewiet.registrations.services.ContactpersoonService;
import be.pirlewiet.registrations.services.DeelnemerService;
import be.pirlewiet.registrations.services.DienstService;
import be.pirlewiet.registrations.services.InschrijvingService;
import be.pirlewiet.registrations.services.VakantieProjectService;

@Controller
@RequestMapping(value = "/inschrijvingen")
public class InschrijvingCrudController {

	Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@Autowired
	private DienstService dienstService;
	
	@Autowired
	private DeelnemerService deelnemerService;
	
	@Autowired
	private InschrijvingService inschrijvingService;
	
	@Autowired
	private VakantieProjectService vakantieProjectService;
	
	@Autowired
	private ContactpersoonService contactpersoonService;
	
	@RequestMapping( method=RequestMethod.GET, produces=MediaType.TEXT_HTML_VALUE)
	public ModelAndView queryView(@RequestParam(required=false) String contact, @RequestParam(required=false) String dienst, @RequestParam(required=true) String context ) {
		
		Map<String,Object> resultaat = new HashMap<String, Object>();
		
		if ( contact != null && ( ! contact.isEmpty() ) ) {
			
			Contactpersoon contactPersoon
				= this.contactpersoonService.findById( Long.valueOf( contact ) );
			
			List<Inschrijving> inschrijvingen 
				= this.inschrijvingService.findActueleInschrijvingenByContactPersoon( contactPersoon );
			
			for ( Inschrijving inschrijving : inschrijvingen ) {
				inschrijving.setContactpersoon( contactPersoon );
			}
			resultaat.put("inschrijvingen", inschrijvingen );
		}
		else {
			
			List<Inschrijving> inschrijvingen 
				= this.inschrijvingService.findActueleInschrijvingen();
			logger.debug( "found [{}] inschrijvingen", inschrijvingen.size() );
			resultaat.put("inschrijvingen", inschrijvingen );
		}
		
		resultaat.put("context", context);
		
		return new ModelAndView( "/tables/inschrijvingenTable", resultaat );
		
	}
	
	@RequestMapping(method = RequestMethod.POST)
	@Transactional
	@ResponseBody
	public ResponseEntity<Inschrijving> post(@RequestParam String contact ) {
		
		Contactpersoon contactPersoon
			= this.contactpersoonService.findById( Long.valueOf( contact) );
		
		if ( contactPersoon != null ) {
			Inschrijving inschrijving
				= new Inschrijving();
			inschrijving.setContactpersoon( contactPersoon );
			inschrijving = this.inschrijvingService.create( inschrijving );
			logger.info( "created inschrijving met id [{}]", inschrijving.getId() );
			return response( inschrijving, HttpStatus.OK );
		}
		else {
			return response( HttpStatus.NOT_FOUND );
		}
		
		
	}
	
	@RequestMapping(value="{id}/vakantie", method = RequestMethod.POST)
	@ResponseBody
	public ResponseEntity<Inschrijving> setVakantie(
			@PathVariable String id,
			@RequestParam String vakantieId ) {
		
		logger.info( "[{}], set vakantie [{}]", id, vakantieId );
		
		Inschrijving inschrijving = inschrijvingService.updateVakantie( Long.valueOf(id), Long.valueOf(vakantieId) );
		
		return response( inschrijving, HttpStatus.OK );
		
	}
	
	@RequestMapping(value="{id}/opmerkingen", method = RequestMethod.POST)
	@ResponseBody
	public ResponseEntity<Inschrijving> setOpmerkingen(
			@PathVariable String id,
			@RequestParam String opmerkingen ) {
		
		logger.info( "[{}], set opmerkingen [{}]", id, opmerkingen );
		
		Inschrijving inschrijving = inschrijvingService.updateOpmerkingen( Long.valueOf(id), opmerkingen );
		
		return response( inschrijving, HttpStatus.OK );
		
	}
	
	@RequestMapping(value="{id}/deelnemer", method = RequestMethod.POST)
	@ResponseBody
	public ResponseEntity<Inschrijving> addDeelnemer(
			@PathVariable String id,
			@RequestParam String deelnemerId ) {
		
		logger.info( "[{}], set deelnemer [{}]", id, deelnemerId );
		
		Inschrijving inschrijving = inschrijvingService.updateDeelnemer( Long.valueOf(id), Long.valueOf( deelnemerId ) );
		
		return response( inschrijving, HttpStatus.OK );
		
	}
	
	@RequestMapping(value="{id}/status", method = RequestMethod.POST)
	@ResponseBody
	public ResponseEntity<Inschrijving> setStatus(
			@PathVariable String id,
			@RequestParam Status status ) {
		
		logger.info( "[{}], set status [{}]", id, status );
		
		Inschrijving inschrijving = inschrijvingService.updateStatus( Long.valueOf(id), status );
		
		return response( inschrijving, HttpStatus.OK );
		
	}
	
	@ExceptionHandler(value=Exception.class)
	public ResponseEntity<String> handleException( Exception e ) {
		logger.warn( "inschrijving crud operation failed", e );
		return response( e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR );
	}
	


}

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
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import be.pirlewiet.registrations.model.Deelnemer;
import be.pirlewiet.registrations.model.Dienst;
import be.pirlewiet.registrations.model.Inschrijving;
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
	
	private HashMap<String, Deelnemer> deelnemers = new HashMap<String, Deelnemer>();
	
	/**
	 * This method generates dummy data for a datatable. The only requirement is that the main object in JSON is of type 'aaData'.
	 * The logic in this method must be replaced with a service call to retrieve live data.
	 * @param req
	 * @param map
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "datatabledatagenerated", produces = "application/json")
	@ResponseBody
	public Map<String, Object> dataTableJson(ModelMap model) {
		
		Dienst loggedInDienst = dienstService.getLoggedInDienst();
		List<Deelnemer> lijstDeelnemersPerDienst = deelnemerService.getDeelnemersByDienst(loggedInDienst); //new ArrayList<Deelnemer>();
		logger.info("Dienst logged in: " + loggedInDienst);
		
		List<Object> b = new ArrayList<Object>();
		DateFormat df = new SimpleDateFormat("YYYY-MM-dd");
		for (Deelnemer deelnemer : lijstDeelnemersPerDienst) {
			List<String> a = new ArrayList<String>();
			a.add(deelnemer.getId() +"");
			a.add(deelnemer.getVoornaam());
			a.add(deelnemer.getFamilienaam());
			a.add(df.format(deelnemer.getGeboortedatum()));
			a.add(deelnemer.getGeslacht() + "");
			b.add(a);
		}
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("aaData", b);
		
		Map<String,Object> sessionData = (Map<String,Object>)model.get("deelnemersDatatableData");
		if (sessionData == null) {
			logger.info("deelnemersDatatableData op de sessie is null ");
			model.put("deelnemersDatatableData", map);
		} else {
			logger.info("Er is al een deelnemersDatatableTabel op de sessie geplaatst...");
			return sessionData;
		}
		return map;
	}
	
	@RequestMapping(method = RequestMethod.POST)
	@Transactional
	@ResponseBody
	public ResponseEntity<Inschrijving> post(@ModelAttribute("command") Inschrijving inschrijving ) {
		
		if ( inschrijving.getId() == 0L ) {
			inschrijving = this.inschrijvingService.create( inschrijving );
			logger.info( "created inschrijving met id [{}]", inschrijving.getId() );
		}
		else {
			inschrijving = inschrijvingService.update( inschrijving );
		}
		
		return response( inschrijving, HttpStatus.OK );
		
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
	
	@RequestMapping(value="{id}/deelnemers", method = RequestMethod.POST)
	@ResponseBody
	public ResponseEntity<Inschrijving> addDeelnemer(
			@PathVariable String id,
			@RequestParam String deelnemerId ) {
		
		logger.info( "[{}], set deelnemer [{}]", id, deelnemerId );
		
		Inschrijving inschrijving = inschrijvingService.addDeelnemer( Long.valueOf(id), Long.valueOf( deelnemerId ) );
		
		return response( inschrijving, HttpStatus.OK );
		
	}
	
	@ExceptionHandler(value=Exception.class)
	public ResponseEntity<String> handleException( Exception e ) {
		logger.warn( "inschrijving crud operation failed", e );
		return response( e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR );
	}
	


}

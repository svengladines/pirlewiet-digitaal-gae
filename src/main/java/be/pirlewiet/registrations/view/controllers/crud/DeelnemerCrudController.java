package be.pirlewiet.registrations.view.controllers.crud;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.ExtendedModelMap;
import org.springframework.ui.Model;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import be.pirlewiet.registrations.model.Deelnemer;
import be.pirlewiet.registrations.services.CredentialsService;
import be.pirlewiet.registrations.services.DeelnemerService;

@Controller
@RequestMapping(value="/deelnemers")
public class DeelnemerCrudController {

	Logger logger = Logger.getLogger(this.getClass());

	@Autowired
	private DeelnemerService deelnemerService;
	
	@Autowired
	private CredentialsService credentialsService;

	@InitBinder
	public void initBind(WebDataBinder binder) {
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		dateFormat.setLenient(false);
		CustomDateEditor cde = new CustomDateEditor(dateFormat, true);
		binder.registerCustomEditor(Date.class, cde);
	}
	
	@RequestMapping(value = "/{deelnemerId}/formulier", method = RequestMethod.GET)
	public ModelAndView getForm(@PathVariable String deelnemerId) {
		
		Model m = new ExtendedModelMap();
		
		Deelnemer deelnemer = null;
		
		if ( "nieuw".equals( deelnemerId ) ) {
			deelnemer = new Deelnemer();
		}
		else {
			deelnemer = deelnemerService.find( Long.valueOf( deelnemerId ) );
		}
		
		m.addAttribute("deelnemer", deelnemer );

		return new ModelAndView("forms/DeelnemerFormulier", m.asMap() );
	}

	@RequestMapping(method = RequestMethod.POST, produces = "application/json")
	@Transactional
	public @ResponseBody
	Map<String, Object> addDeelnemerFromDatatable(@ModelAttribute(value = "deelnemer") Deelnemer deelnemer, HttpSession session) {
		logger.info("Deelnemer-information was posted to the server");
		Map<String, Object> data = new HashMap<String, Object>();
		data.put("resultaat", "Deelnemer toegevoegd! " + deelnemer);
		logger.info("Added Deelnemer information: \n" + "Voornaam:" + deelnemer.getVoornaam() + "\n Familienaam:" + deelnemer.getFamilienaam() + "\n Tel:" + deelnemer.getTelefoonnr() +
				"\n Telefoonnr:" + deelnemer.getTelefoonnr() + " \n RijksRegisterNr:" + deelnemer.getRijksregisternr());
		deelnemerService.create(deelnemer);
		logger.info("DEELNEMER ADDED, AND THE SESSION-VARIABLE LIST OF DEELNEMERS IS AVAILABLE???? : " + session.getAttribute("deelnemersDatatableData"));
		return data;
	}

	@RequestMapping(value = "deelnemer/get/{deelnemerid}")
	public @ResponseBody Map<String,Object> getDeelnemerById(@PathVariable long deelnemerid) {
		Deelnemer geselecteerdeDeelnemer = deelnemerService.find(deelnemerid);
		logger.info("Geselecteerde deelnemer voor te editeren:" + geselecteerdeDeelnemer);
		
		Map<String,Object> resultaat = new HashMap<String, Object>();
		resultaat.put("voornaam", geselecteerdeDeelnemer.getVoornaam());
		resultaat.put("familienaam", geselecteerdeDeelnemer.getFamilienaam());
		resultaat.put("telefoonnr",geselecteerdeDeelnemer.getTelefoonnr());
		resultaat.put("rijksregisternummer", geselecteerdeDeelnemer.getRijksregisternr());
		resultaat.put("geboortedatum", geselecteerdeDeelnemer.getGeboortedatum());
		resultaat.put("geslacht",geselecteerdeDeelnemer.getGeslacht());
		
		return resultaat;
	}
	
	// @RequestMapping( method=RequestMethod.GET, produces=MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody List<Deelnemer> query(@RequestParam(required=false) String rrn, @RequestParam(required=false) String family ) {
		
		Set<Deelnemer> deelnemers
			= new HashSet<Deelnemer>();
		
		if ( rrn != null && (!rrn.isEmpty() ) ) {
			deelnemers.addAll( this.deelnemerService.findSmart( rrn ) );
		}
		else {
			deelnemers.addAll( this.deelnemerService.findSmart( family ) );
		}
		
		List<Deelnemer> sorted
			= new ArrayList<Deelnemer>( deelnemers.size() );
		
		sorted.addAll( deelnemers );
		
		// TODO, sorteren
		return sorted;
		
	}
	
	@RequestMapping( method=RequestMethod.GET, produces=MediaType.TEXT_HTML_VALUE)
	public ModelAndView queryView(@RequestParam(required=false) String rrn, @RequestParam(required=false) String family ) {
		
		List<Deelnemer> deelnemers
			= this.query( rrn, family );
		
		Map<String,Object> resultaat = new HashMap<String, Object>();
		resultaat.put("deelnemers", deelnemers );
		
		return new ModelAndView( "/forms/inschrijving_deelnemers", resultaat );
		
	}

}

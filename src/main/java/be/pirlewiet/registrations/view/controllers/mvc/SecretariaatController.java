package be.pirlewiet.registrations.view.controllers.mvc;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.ExtendedModelMap;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import be.pirlewiet.registrations.model.Contactpersoon;
import be.pirlewiet.registrations.model.Dienst;
import be.pirlewiet.registrations.model.DienstJsonObject;
import be.pirlewiet.registrations.model.Inschrijving;
import be.pirlewiet.registrations.model.SearchObject;
import be.pirlewiet.registrations.model.Secretariaatsmedewerker;
import be.pirlewiet.registrations.model.Status;
import be.pirlewiet.registrations.model.VakantieProject;
import be.pirlewiet.registrations.services.DienstService;
import be.pirlewiet.registrations.services.InschrijvingService;
import be.pirlewiet.registrations.services.SecretariaatsmedewerkerService;
import be.pirlewiet.registrations.services.VakantieProjectService;
import be.pirlewiet.registrations.services.VakantietypeService;
import be.pirlewiet.registrations.utils.JSONUtils;
import be.pirlewiet.registrations.utils.MenuUtils;
import be.pirlewiet.registrations.view.controllers.ScreenDropDownObject;

@Controller
@RequestMapping("secretariaat")
public class SecretariaatController {

	private static final Logger LOGGER = LoggerFactory.getLogger(SecretariaatController.class);

	@Autowired
	private VakantieProjectService vakantieprojectService;

	@Autowired
	private VakantietypeService vakantietypeService;
	
	@Autowired
	private InschrijvingService inschrijvingService;

	@Autowired
	private DienstService dienstService;
	
	@Autowired
	private SecretariaatsmedewerkerService secretariaatsmedewerkerService;

	@Autowired
	private JSONUtils jsonUtils;

	@Autowired
	private MenuUtils menuUtils;

	@RequestMapping(value = "/home", method = RequestMethod.GET)
	public ModelAndView home(@RequestParam("gebruiker") String userId ) {
		
		Model m = new ExtendedModelMap();

		Secretariaatsmedewerker medewerker
			= this.secretariaatsmedewerkerService.findById( Long.valueOf( userId ) );
		
		m.addAttribute("gebruiker", medewerker );

		return new ModelAndView("secretariaat/home", m.asMap() );
		
	}
	
	@RequestMapping(value = "/inschrijving/{id}", method = RequestMethod.GET)
	@Transactional
	public ModelAndView viewInschrijving(@PathVariable Long id ) {

		Inschrijving inschrijving
			= this.inschrijvingService.findInschrijvingById( id );
		
		Map<String, Object> model 
			= new HashMap<String, Object>();
	
		model.put( "inschrijving", inschrijving );

		return new ModelAndView( "secretariaat/inschrijving", model );

	}

	@RequestMapping("/vakanties.html")
	public ModelAndView navigateVakantieProjecten() {
		Model m = new ExtendedModelMap();
		m.addAttribute("vakanties", vakantieprojectService.getAllVakantieProjecten());
		m.addAttribute("vakantieproject", new VakantieProject());
		m.addAttribute("vakantieTypes", vakantietypeService.getAllVakantietypes());

		return new ModelAndView("secretariaat/vakantieprojecten", m.asMap());
	}
	
	@RequestMapping("/diensten.html")
	public ModelAndView navigateDiensten() {

		Model m = new ExtendedModelMap();
		return new ModelAndView("secretariaat/diensten", m.asMap());
		
	}

	@RequestMapping(value = "/searchDoorverwijzer", method = RequestMethod.POST)
	@Transactional
	public @ResponseBody
	Map<String, Object> searchDoorverwijzer(@ModelAttribute("searchterm") String searchterm, BindingResult result, Model model) {
		Map<String, Object> data = new HashMap<String, Object>();

		List<Dienst> list = dienstService.findDienstenThatContainsString(searchterm);
		List<DienstJsonObject> newList = jsonUtils.generateDienstJsonList(list);
		data.put("list", newList);
		return data;

	}

}

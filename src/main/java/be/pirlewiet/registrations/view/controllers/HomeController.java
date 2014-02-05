package be.pirlewiet.registrations.view.controllers;

import java.security.Principal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.ExtendedModelMap;
import org.springframework.ui.Model;
import org.springframework.web.bind.ServletRequestDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import be.pirlewiet.registrations.model.Inschrijving;
import be.pirlewiet.registrations.model.ContactType;
import be.pirlewiet.registrations.model.Contactpersoon;
import be.pirlewiet.registrations.model.Deelnemer;
import be.pirlewiet.registrations.model.Dienst;
import be.pirlewiet.registrations.model.Geslacht;
import be.pirlewiet.registrations.model.InschrijvingenForm;
import be.pirlewiet.registrations.model.Status;
import be.pirlewiet.registrations.model.VakantieProject;
import be.pirlewiet.registrations.services.ContactpersoonService;
import be.pirlewiet.registrations.services.DeelnemerService;
import be.pirlewiet.registrations.services.DienstService;
import be.pirlewiet.registrations.services.InschrijvingService;
import be.pirlewiet.registrations.services.VakantieProjectService;

@Controller
public class HomeController {
	@Autowired
	private InschrijvingService inschrijvingService;
	@Autowired
	private VakantieProjectService vakantieProjectService;
	@Autowired
	private DienstService dienstService;
	@Autowired
	private ContactpersoonService contactpersoonService;
	@Autowired
	private DeelnemerService deelnemerService;

	private HashMap<String, Deelnemer> deelnemers = new HashMap<String, Deelnemer>();

	private Logger logger = Logger.getLogger(this.getClass());

	//@RequestMapping(value = { "/dienst/home", "/" })
	public ModelAndView navigate(Principal principal) {
		Model m = new ExtendedModelMap();

		InschrijvingenForm inschrijvingenForm = new InschrijvingenForm();

		Contactpersoon contactpersoon = new Contactpersoon();
		contactpersoon.setDienst(dienstService.getLoggedInDienst());

		Inschrijving aanvraagInschrijving = new Inschrijving();
		aanvraagInschrijving.setContactpersoon(contactpersoon);
		aanvraagInschrijving.setVakantieproject(new VakantieProject());
		inschrijvingenForm.setAanvraagInschrijving(aanvraagInschrijving);

		// Setting the right command objects to connect back-end to front-end
		m.addAttribute("command", inschrijvingenForm);
		m.addAttribute("vakanties", vakantieProjectService.getAllVakantieProjecten());
		m.addAttribute("geslachten", Geslacht.values());
		m.addAttribute("contacttypes", ContactType.values());
		m.addAttribute("deelnemer", new Deelnemer());
		m.addAttribute("contactpersoon", new Contactpersoon());

		List<Contactpersoon> resultaat = dienstService.findByDienst(dienstService.getLoggedInDienst());
		m.addAttribute("contactpersonen", resultaat);

		if (principal != null) {
			m.addAttribute("currentUser", principal.getName());
		} else {
			m.addAttribute("currentUser", "GUEST");
		}

		return new ModelAndView("dienst/home", m.asMap());
	}

	@RequestMapping(value = "deelnemer/add/{rowID}", produces = "text/plain")
	public @ResponseBody
	String addDeelnemer(@ModelAttribute(value = "deelnemer") Deelnemer deelnemer, @PathVariable String rowID) {
		String voornaam = deelnemer.getVoornaam();
		String familienaam = deelnemer.getFamilienaam();

		String result = "FAIL";

		if (voornaam != null && familienaam != null && voornaam.length() > 0 && familienaam.length() > 0) {
			deelnemers.put(rowID, deelnemer);
			result = "OK";
		}

		return result;
	}

	@RequestMapping(value = "deelnemer/delete/{rowID}")
	public @ResponseBody
	Deelnemer deleteDeelnemer(@PathVariable String rowID) {
		Deelnemer removed = deelnemers.remove(rowID);

		return removed;
	}

	// Accept date input in standard text input fields (format dd/MM/yyyy)
	@InitBinder
	protected void initBinder(HttpServletRequest request, ServletRequestDataBinder binder) {
		SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
		dateFormat.setLenient(false);
		binder.registerCustomEditor(Date.class, new CustomDateEditor(dateFormat, true));
	}

}

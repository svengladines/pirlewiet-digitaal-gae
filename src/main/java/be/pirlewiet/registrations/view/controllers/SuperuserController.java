package be.pirlewiet.registrations.view.controllers;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import be.pirlewiet.registrations.model.Credentials;
import be.pirlewiet.registrations.model.Secretariaatsmedewerker;
import be.pirlewiet.registrations.services.SecretariaatsmedewerkerService;

@Controller
@RequestMapping(value = "/superuser")
public class SuperuserController {
	@Autowired
	private SecretariaatsmedewerkerService secretariaatsmedewerkerService;

	// @RequestMapping(value = "/home")
	// public ModelAndView showSuperuserDashboard() {
	// Map<String, Object> data = new HashMap<String, Object>();
	//
	// data.put("secretariaatsmedewerkers", secretariaatsmedewerkerService.findDisplayedSecretariaatMedewerker());
	// Secretariaatsmedewerker secr = new Secretariaatsmedewerker();
	// secr.setCredentials(new Credentials());
	// data.put("nieuwesecretariaatsmedewerker",secr);
	//
	// return new ModelAndView("superuser/home", data);
	// }

	@RequestMapping(value = "/home")
	public ModelAndView showSuperuserDashboard() {
		Map<String, Object> data = new HashMap<String, Object>();

		data.put("secretariaatsmedewerkers", secretariaatsmedewerkerService.findDisplayedSecretariaatMedewerker());
		Secretariaatsmedewerker secr = new Secretariaatsmedewerker();
		secr.setCredentials(new Credentials());
		data.put("nieuwesecretariaatsmedewerker", secr);

		return new ModelAndView("superuser/home", data);
	}

}

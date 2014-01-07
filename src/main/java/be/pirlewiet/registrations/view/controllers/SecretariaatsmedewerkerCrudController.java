package be.pirlewiet.registrations.view.controllers;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.MailSendException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import be.pirlewiet.registrations.model.Credentials;
import be.pirlewiet.registrations.model.Secretariaatsmedewerker;
import be.pirlewiet.registrations.services.CredentialsService;
import be.pirlewiet.registrations.services.SecretariaatsmedewerkerService;

@Controller
public class SecretariaatsmedewerkerCrudController {

	private static final Logger LOGGER = LoggerFactory.getLogger(SecretariaatsmedewerkerCrudController.class);

	@Autowired
	private SecretariaatsmedewerkerService secretariaatsmedewerkerService;
	@Autowired
	private CredentialsService credentialsService;

	@Value("#{myProps['add_new_secretariaats_medewerker']}")
	private String add_new_secretariaats_medewerker = "";

	@Value("#{myProps['activeer_secretariaats_medewerker']}")
	private String activeer_secretariaats_medewerker = "";

	@Value("#{myProps['deactiveer_secretariaats_medewerker']}")
	private String deactiveer_secretariaats_medewerker = "";

	@Value("#{myProps['delete_medewerker']}")
	private String delete_medewerker = "";

	@RequestMapping(value = "/superuser/secretariaatsmedewerker/add", method = RequestMethod.POST)
	@Transactional(propagation = Propagation.REQUIRED)
	public ModelAndView addSecretariaatsMedewerker(
			@ModelAttribute(value = "nieuwesecretariaatsmedewerker") Secretariaatsmedewerker secretariaatsmedewerker,
			Model model) {
		Map<String, Object> data = new HashMap<String, Object>();
		Secretariaatsmedewerker s = null;

		try {
			s = secretariaatsmedewerkerService.createAndSendPasswordAndAssignRoleSecretariaat(secretariaatsmedewerker);
		} catch (MailSendException e) {
			LOGGER.error(e.getMessage());
			throw new MailSendException("");
		}
		s.getCredentials().setPassword("hiddenpassword");

		if (s != null) {
			// remove password from result. How ???
			s.getCredentials().setAuthorities(null);
			s.getCredentials().setPassword("hiddenpassword");

			model.addAttribute("medewerkers", secretariaatsmedewerkerService.findDisplayedSecretariaatMedewerker());

			secretariaatsmedewerker = new Secretariaatsmedewerker();
			secretariaatsmedewerker.setCredentials(new Credentials());
		}

		return new ModelAndView("tables/medewerkersTable", model.asMap());
	}

	/**
	 * Returns all medewerkers where isDisplayed == true
	 * 
	 * @return
	 */
	@RequestMapping(value = "/superuser/secretariaatsmedewerker/getAllDisplayed", method = RequestMethod.GET)
	@Transactional(propagation = Propagation.NEVER)
	public ModelAndView getAllDisplayed(Model model) {

		model.addAttribute("medewerkers", secretariaatsmedewerkerService.findDisplayedSecretariaatMedewerker());

		return new ModelAndView("tables/medewerkersTable", model.asMap());
	}

	/**
	 * Change enable column form the Credentials Table
	 * 
	 * @param ID
	 * @return
	 */
	@RequestMapping(value = "/superuser/secretariaatsmedewerker/disable", produces = "application/json")
	@Transactional(propagation = Propagation.NEVER)
	public @ResponseBody
	Map<String, Object> disableSecretariaatsMedewerker(@ModelAttribute(value = "secretariaatsmedewerkerID") int ID) {
		Secretariaatsmedewerker s = secretariaatsmedewerkerService.findById(ID);
		s.getCredentials().setEnabled(false);
		Credentials c = credentialsService.update(s.getCredentials());
		c.setPassword("hiddenpassword");

		Map<String, Object> data = new HashMap<String, Object>();
		data.put("msg", s.getVoornaam() + " " + s.getFamilienaam() + " werd met succes gedeactiveerd");
		data.put("list", secretariaatsmedewerkerService.findDisplayedSecretariaatMedewerker());

		return data;
	}

	/**
	 * Change enable column form the Credentials Table
	 * 
	 * @param ID
	 * @return
	 */
	@RequestMapping(value = "/superuser/secretariaatsmedewerker/enable", produces = "application/json")
	@Transactional(propagation = Propagation.NEVER)
	public @ResponseBody
	Map<String, Object> enbleSecretariaatsMedewerker(@ModelAttribute(value = "secretariaatsmedewerkerID") long ID) {
		Secretariaatsmedewerker s = secretariaatsmedewerkerService.findById(ID);
		s.getCredentials().setEnabled(true);
		Credentials c = credentialsService.update(s.getCredentials());
		c.setPassword("hiddenpassword");

		Map<String, Object> data = new HashMap<String, Object>();
		data.put("msg", s.getVoornaam() + " " + s.getFamilienaam() + " werd met succes geactiveerd");
		data.put("list", secretariaatsmedewerkerService.findDisplayedSecretariaatMedewerker());

		return data;
	}

	// private Map<String, Object> enableDisableSecrMede(boolean oldval, long ID) {
	// Map<String, Object> data = new HashMap<String, Object>();
	// Credentials c2 = (Credentials) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
	// if (c2 == null) {
	// LOGGER.info("creds NULL"); // should not be true
	// }
	// String username = c2.getUsername();
	// Secretariaatsmedewerker s = null;
	// s = secretariaatsmedewerkerService.findById(ID);
	//
	// if (s.getCredentials().getUsername().equals(username)) {
	// data.put("resultaatCode", "FAIL");
	// data.put("resultaat", "Je kan jezelf niet " + (oldval ? "desactiveren." : "activeren."));
	// return data;
	// }
	// if (s.getCredentials().isEnabled() != oldval) {
	// data.put("resultaatCode", "FAIL");
	// data.put("resultaat", "De secretariaatsmedewerker was reeds " + (oldval ? "inactief." : "actief."));
	// return data;
	// }
	// s.getCredentials().setEnabled(!oldval);
	// Credentials c = credentialsService.update(s.getCredentials());
	// c.setPassword("hiddenpassword");
	// if (c != null) {
	// data.put("credentials", c);
	// data.put("resultaatCode", "SUCCESS");
	// data.put("resultaat", "De secretariaatsmedewerker werd aangepast.");
	// } else {
	// data.put("resultaatCode", "FAIL");
	// data.put("resultaat", "De secretariaatsmedewerker werd niet aangepast");
	// }
	//
	// return data;
	// }

	@RequestMapping(value = "/superuser/secretariaatsmedewerker/delete", produces = "application/json")
	@Transactional(propagation = Propagation.NEVER)
	public ModelAndView deleteMedewerker(@ModelAttribute(value = "id") long ID, Model model) {
		Credentials loggedInCredentials = (Credentials) SecurityContextHolder.getContext().getAuthentication()
				.getPrincipal();

		Secretariaatsmedewerker medewerker = secretariaatsmedewerkerService.setDisplayedFalse(ID);

		LOGGER.info("medewerker username" + medewerker.getCredentials().getUsername());
		LOGGER.info("logged in username" + loggedInCredentials.getUsername());
		if (medewerker.getCredentials().getUsername().equals(loggedInCredentials.getUsername())) {
			LOGGER.info("the same");
		}

		model.addAttribute("medewerkers", secretariaatsmedewerkerService.findDisplayedSecretariaatMedewerker());

		return new ModelAndView("tables/medewerkersTable", model.asMap());
	}

	@RequestMapping(value = "/superuser/secretariaatsmedewerker/reset", produces = "application/json")
	@Transactional(propagation = Propagation.NEVER)
	public @ResponseBody
	Map<String, Object> resetMedewerker(@ModelAttribute(value = "id") long ID) {
		Map<String, Object> data = new HashMap<String, Object>();
		Secretariaatsmedewerker secretariaatsmedewerker = secretariaatsmedewerkerService.findById(ID);

		data.put("msg", "Een nieuw wachtwoord werd verzonden.");
		try {
			secretariaatsmedewerkerService.reset(secretariaatsmedewerker);
		} catch (MailSendException e) {
			LOGGER.error(e.getMessage());
			throw new MailSendException("");

		} catch (Exception e) {
			LOGGER.error(e.getMessage());
			throw new MailSendException("");
		}

		return data;
	}

}

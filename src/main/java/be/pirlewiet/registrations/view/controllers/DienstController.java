package be.pirlewiet.registrations.view.controllers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import be.pirlewiet.registrations.model.Credentials;
import be.pirlewiet.registrations.model.Dienst;
import be.pirlewiet.registrations.repositories.DienstRepository;
import be.pirlewiet.registrations.services.CredentialsService;
import be.pirlewiet.registrations.services.DienstService;
import be.pirlewiet.registrations.utils.MenuUtils;
import be.pirlewiet.registrations.utils.PasswordUtils;

@Controller
public class DienstController {

	private final static Logger LOGGER = LoggerFactory.getLogger(DienstController.class);

	@Value("#{myProps['dienst_added']}")
	private String dienst_added_msg;

	@Value("#{myProps['dienst_already_in_db']}")
	private String dienst_already_in_db;

	@Autowired
	private DienstRepository dienstRepository;

	@Autowired
	private CredentialsService credentialsService;

	@Autowired
	private DienstService dienstService;

	@Autowired
	private PasswordUtils passwordUtils;

	@Autowired
	private MenuUtils menuUtils;

	// @RequestMapping(value = "/secretariaat/addNewDienst", method =
	// RequestMethod.POST)
	// @Transactional
	// public @ResponseBody Map<String,Object>
	// addNewDienst(@ModelAttribute("dienst") Dienst dienst, BindingResult
	// result, Model model) {
	// Map<String, Object> data = new HashMap<String, Object>();
	// List<Dienst> listDienst =
	// dienstRepository.findDienstByName(dienst.getNaam());
	//
	// // check if dienst already exists with this name
	// if (listDienst.size() == 0) {
	//
	// // It's a new dienst, add it to the database + create credentials
	//
	// createCredentialsAndPersistDienstr(dienst);
	// //return Json, e display this message to the user with Jquery
	// data.put("saved_in_db", dienst_added_msg);
	//
	// return data;
	//
	// }else {
	// //There are other Dienst objects found with this name
	//
	// //I tried to add the list in the data map like this:
	// data.put("dienstList", listDienst);
	// //But that didnt' work, i copied the data to a seperate list. This is a
	// work around
	// List<DienstJsonObject> dienstJsonList =
	// StaticMethods.generateDienstJsonList(listDienst);
	//
	// data.put("dienstList", dienstJsonList);
	// data.put("msg_dienst_already_in_db",dienst_already_in_db);
	//
	// return data;
	//
	// }
	// }
	@RequestMapping(value = "/secretariaat/addNewDienst", method = RequestMethod.POST)
	@Transactional
	public ModelAndView addNewDienst(@ModelAttribute("dienst") Dienst dienst, BindingResult result, Model model) {

		List<Dienst> listDienst = dienstRepository.findDienstByName(dienst.getNaam());

		// check if dienst already exists with this name
		if (listDienst.size() == 0) {

			// It's a new dienst, add it to the database + create credentials

			createCredentialsAndPersistDienstr(dienst);
			// return Json, e display this message to the user with Jquery
			model.addAttribute("duplicate", "no");

		} else {
			// There are other Dienst objects found with this name
			model.addAttribute("duplicate", "yes");
			model.addAttribute("diensten", listDienst);

		}
		return new ModelAndView("tables/bevestigMedewerkerMetZelfdeNaamTable", model.asMap());
	}

	/**
	 * Creates new credentials + persists the new dienst to the DB
	 * 
	 * @param dienst
	 */
	private void createCredentialsAndPersistDienstr(Dienst dienst) {
		Credentials credentials = new Credentials();
		dienst.setCredentials(credentials);

		credentials.setEmailadres(dienst.getEmailadres());
		credentials.setEnabled(true);
		ArrayList<String> roles = new ArrayList<String>();
		roles.add("ROLE_DIENST");
		credentials.setUsername(dienst.getNaam());
		credentials
				.setPassword(passwordUtils.generateAndEncodeAndSendPassword(dienst.getCredentials().getEmailadres()));
		credentialsService.create(credentials);

		dienstRepository.create(dienst);
	}

	/**
	 * User confirms that the new Dienst must be added to the database
	 * 
	 * @param newDienst
	 * @param result
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/secretariaat/bevestig")
	@Transactional
	public @ResponseBody
	Map<String, Object> persistDienst_New(@ModelAttribute("dienst_object") Dienst newDienst, BindingResult result,
			Model model) {
		Map<String, Object> data = new HashMap<String, Object>();

		createCredentialsAndPersistDienstr(newDienst);
		return data;

	}

	@RequestMapping(value = "/secretariaat/getDienstenTable", method = RequestMethod.GET)
	public ModelAndView getDienstenTable(HttpServletRequest request, Model model) {

		List<Dienst> allDiensten = dienstRepository.findAll();

		model.addAttribute("diensten", allDiensten);
		return new ModelAndView("tables/dienstenTable", model.asMap());

	}

	@RequestMapping(value = "/secretariaat/activeerDienst", method = RequestMethod.POST)
	@Transactional
	public @ResponseBody
	Map<String, Object> activeerDeactiveerDienst(@ModelAttribute(value = "id") long id) {

		Map<String, Object> map = new HashMap<String, Object>();

		Dienst tempDienst = dienstRepository.find(id);
		tempDienst.getCredentials().setEnabled(true);

		map.put("msg", "Dienst werd geactiveerd.");
		return map;
	}

	@RequestMapping(value = "/secretariaat/deactiveerDienst", method = RequestMethod.POST)
	@Transactional
	public @ResponseBody
	Map<String, Object> deactiveerDeactiveerDienst(@ModelAttribute(value = "id") long id) {

		Map<String, Object> map = new HashMap<String, Object>();

		Dienst tempDienst = dienstRepository.find(id);
		tempDienst.getCredentials().setEnabled(false);

		map.put("msg", "Dienst werd gedeactiveerd.");
		return map;
	}

	/**
	 * Shows all {@link Dienst} information of the {@link Dienst} with the given identifier.
	 * 
	 * @param dienstId
	 *            The identifier of the {@link Dienst} instance to show all information of.
	 * @param model
	 *            The model {@link Map} used on the view.
	 * @return The viewName of the view to resolve.
	 */
	@RequestMapping(value = "/secretariaat/diensten/dienst-{dienstId}", method = { RequestMethod.GET })
	public String getDienstById(@PathVariable("dienstId") Long dienstId, Map<String, Object> model) {
		Dienst dienst = dienstService.findDienstById(dienstId);
		ScreenDropDownObject screenDropDownObject = menuUtils.createDropDownObject();
		model.put("dienst", dienst);
		model.put("screenDropDownObject", screenDropDownObject);

		return "dienst/dienstDetail";
	}

}

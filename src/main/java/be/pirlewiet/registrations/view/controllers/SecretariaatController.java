package be.pirlewiet.registrations.view.controllers;

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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import be.pirlewiet.registrations.model.Dienst;
import be.pirlewiet.registrations.model.DienstJsonObject;
import be.pirlewiet.registrations.model.SearchObject;
import be.pirlewiet.registrations.model.VakantieProject;
import be.pirlewiet.registrations.services.DienstService;
import be.pirlewiet.registrations.services.VakantieProjectService;
import be.pirlewiet.registrations.services.VakantietypeService;
import be.pirlewiet.registrations.utils.JSONUtils;
import be.pirlewiet.registrations.utils.MenuUtils;

@Controller
@RequestMapping("secretariaat")
public class SecretariaatController {

	private static final Logger LOGGER = LoggerFactory.getLogger(SecretariaatController.class);

	@Autowired
	private VakantieProjectService vakantieprojectService;

	@Autowired
	private VakantietypeService vakantietypeService;

	@Autowired
	private DienstService dienstService;

	@Autowired
	private JSONUtils jsonUtils;

	@Autowired
	private MenuUtils menuUtils;

	@RequestMapping("/home")
	public ModelAndView navigate(@ModelAttribute(value = "screenDropDownObject") ScreenDropDownObject dropDownObject) {

		LOGGER.info("Screen: " + dropDownObject.getScreenName());
		ScreenDropDownObject newDropDownObject = menuUtils.createDropDownObject();
		Map<String, Object> data = new HashMap<String, Object>();

		if (dropDownObject.getScreenName() == null || dropDownObject.getScreenName().equals(MenuUtils.DEELNEMERS)) {
			data.put("screen", MenuUtils.DEELNEMERS);
			newDropDownObject.setScreenName(MenuUtils.DEELNEMERS);
		} else if (dropDownObject.getScreenName().equals(MenuUtils.DOORVERWIJZER)) {

			data.put("screen", MenuUtils.DOORVERWIJZER);
			data.put("diensten", dienstService.getAllDiensten());
			data.put("dienst_object", new Dienst());
			data.put("dienstList", null);
			data.put("searchObject", new SearchObject());
			newDropDownObject.setScreenName(MenuUtils.DOORVERWIJZER);
		} else if (dropDownObject.getScreenName().equals(MenuUtils.INSCHRIJVINGEN)) {
			data.put("screen", MenuUtils.INSCHRIJVINGEN);

			newDropDownObject.setScreenName(MenuUtils.INSCHRIJVINGEN);
		} else if (dropDownObject.getScreenName().equals(MenuUtils.VAKANTIEPROJECTEN)) {
			// Set the vars for vakantieprojecten on the model
			data.put("screen", MenuUtils.VAKANTIEPROJECTEN);
			data.put("vakanties", vakantieprojectService.getAllVakantieProjecten());
			data.put("vakantieproject", new VakantieProject());
			data.put("vakantieTypes", vakantietypeService.getAllVakantietypes());
			newDropDownObject.setScreenName(MenuUtils.VAKANTIEPROJECTEN);
		} else if (dropDownObject.getScreenName().equals(MenuUtils.DEELNEMERS)) {
			data.put("screen", MenuUtils.DEELNEMERS);
			newDropDownObject.setScreenName(MenuUtils.DEELNEMERS);
		}

		data.put("screenDropDownObject", newDropDownObject);

		return new ModelAndView("secretariaat/home", data);
	}

	@RequestMapping("/vakantieprojecten")
	public ModelAndView navigateVakantieProjecten() {
		Model m = new ExtendedModelMap();
		m.addAttribute("vakanties", vakantieprojectService.getAllVakantieProjecten());
		m.addAttribute("vakantieproject", new VakantieProject());
		m.addAttribute("vakantieTypes", vakantietypeService.getAllVakantietypes());

		return new ModelAndView("secretariaat/vakantieprojecten", m.asMap());
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

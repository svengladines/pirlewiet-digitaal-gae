package be.pirlewiet.registrations.view.controllers;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.ServletRequestDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import be.pirlewiet.registrations.model.VakantieProject;
import be.pirlewiet.registrations.services.VakantieProjectService;

@Controller
public class VakantieProjectController {

	@Autowired
	private VakantieProjectService vakantieProjectService;
	
//	@RequestMapping(value = "secretariaat/vakantieprojecten/add",produces="application/json")
//	@Transactional
//	public @ResponseBody Map<String,Object> createVakantieproject(@ModelAttribute("vakantieproject") VakantieProject vakantieproject, ModelMap modelMap) {
//		
//		Map<String, Object> data = new HashMap<String, Object>();
//		vakantieProjectService.createVakantieProject(vakantieproject);
//		data.put("resultaat", "Het nieuwe vakantieproject werd toegegvoegd!");
//		data.put("resultaatId", vakantieproject.getId());
//		data.put("vakanties", vakantieProjectService.getAllVakantieProjecten());
//		
//		return data;
//	}
	@RequestMapping(value = "secretariaat/vakantieprojecten/add",method = RequestMethod.POST)
	public ModelAndView createVakantieproject(@ModelAttribute("vakantieproject") VakantieProject vakantieproject, Model model) {
		
		Map<String, Object> data = new HashMap<String, Object>();
		vakantieProjectService.createVakantieProject(vakantieproject);
		model.addAttribute("resultaat", "Het nieuwe vakantieproject werd toegegvoegd!");
		model.addAttribute("resultaatId", vakantieproject.getId());
		model.addAttribute("vakanties", vakantieProjectService.getAllVakantieProjecten());
		
		return new ModelAndView("tables/vakantieProjectenTable", model.asMap());
	}

	@RequestMapping(value = "vakantieproject/get/{projectId}")
	public @ResponseBody
	Map<String, Object> getVakantieProjectById(@PathVariable long projectId) {
		VakantieProject geselecteerdVakantieProject = vakantieProjectService
				.findVakantieProjectById(projectId);
		Map<String, Object> data = new HashMap<String, Object>();
		

		if (geselecteerdVakantieProject == null) {
			return data;
		}

		//Format for date to work with IE
		SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
		
		data.put("beginInschrijving",sdf.format(geselecteerdVakantieProject.getBeginInschrijving()));
		data.put("eindInschrijving",sdf.format(geselecteerdVakantieProject.getEindInschrijving()));

		return data;
	}
	
//	@RequestMapping(value = "secretariaat/getAllVakanties", method = RequestMethod.POST)
//	@Transactional
//	public @ResponseBody Map<String,Object> getAllVakanties() {
//		Map<String, Object> data = new HashMap<String, Object>();
//		data.put("vakanties", vakantieProjectService.getAllVakantieProjecten());
//		
//		return data;
//		
//	}
	@RequestMapping(value = "secretariaat/getAllVakanties", method = RequestMethod.GET)
	public ModelAndView getAllVakanties(HttpServletRequest request, Model model) {

		model.addAttribute("vakanties", vakantieProjectService.getAllVakantieProjecten());
		
		return new ModelAndView("tables/vakantieProjectenTable", model.asMap());
		
	}
	
	//Accept date input in standard text input fields (format dd/MM/yyyy)
	@InitBinder
	protected void initBinder(HttpServletRequest request, ServletRequestDataBinder binder) {
		SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
		dateFormat.setLenient(false);
		binder.registerCustomEditor(Date.class, new CustomDateEditor(dateFormat, true));
	}
}

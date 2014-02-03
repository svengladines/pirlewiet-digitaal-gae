package be.pirlewiet.registrations.view.controllers;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.stereotype.Controller;
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

import be.pirlewiet.registrations.model.VakantieProject;
import be.pirlewiet.registrations.services.VakantieProjectService;
import be.pirlewiet.registrations.services.VakantietypeService;

@Controller
@RequestMapping( value = "/secretariaat/vakanties")
public class VakantieProjectController {
	
	protected final Logger logger
		= LoggerFactory.getLogger( this.getClass() );

	@Autowired
	private VakantieProjectService vakantieProjectService;
	
	@Autowired
	private VakantietypeService vakantietypeService;
	
	@RequestMapping(method = RequestMethod.POST)
	@ResponseBody
	public String createVakantieproject(@ModelAttribute("vakantieproject") VakantieProject vakantieproject ) {
		
		vakantieProjectService.createVakantieProject(vakantieproject);
		return "De vakantie werd toegevoegd";
		
	}

	@RequestMapping(value = "/{projectId}", method = RequestMethod.GET)
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
	
	@RequestMapping(value = "/{projectId}", method = RequestMethod.DELETE)
	public @ResponseBody String delete(@PathVariable long projectId) {
		VakantieProject geselecteerdVakantieProject = vakantieProjectService.findVakantieProjectById(projectId);
		Map<String, Object> data = new HashMap<String, Object>();

		return "Vakantie werd verwijderd";
	}
	
	@RequestMapping(value = "/{projectId}",method = RequestMethod.PUT)
	@ResponseBody
	public String editVakantieproject(@PathVariable long projectId, @ModelAttribute("vakantieproject") VakantieProject vakantieproject ) {
		
		VakantieProject geselecteerdVakantieProject = vakantieProjectService.findVakantieProjectById(projectId);
		// vakantieProjectService.createVakantieProject(vakantieproject);
		return "De vakantie werd gewijzigd";
		
	}
	
	@RequestMapping(value = "/{projectId}/formulier", method = RequestMethod.GET)
	public ModelAndView getVakantieForm(@PathVariable String projectId) {
		
		Model m = new ExtendedModelMap();
		
		VakantieProject vakantie = null;
		
		if ( "nieuw".equals( projectId ) ) {
			vakantie = new VakantieProject();
		}
		else {
			vakantie = vakantieProjectService.findVakantieProjectById( Long.valueOf( projectId ));
		}
		
		m.addAttribute("vakantie", vakantie );
		m.addAttribute("vakantieTypes", this.vakantietypeService.getAllVakantietypes() );

		return new ModelAndView("forms/VakantieprojectFormulier", m.asMap() );
	}
	
	@RequestMapping(method = RequestMethod.GET)
	public ModelAndView get(HttpServletRequest request, Model model) {

		model.addAttribute("vakanties", vakantieProjectService.getAllVakantieProjecten());
		
		logger.info( "vakanties: [{}]", model );
		
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

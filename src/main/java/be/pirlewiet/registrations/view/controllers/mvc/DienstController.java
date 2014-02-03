package be.pirlewiet.registrations.view.controllers.mvc;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import be.pirlewiet.registrations.model.Inschrijving;
import be.pirlewiet.registrations.model.Status;
import be.pirlewiet.registrations.model.VakantieProject;
import be.pirlewiet.registrations.services.InschrijvingService;
import be.pirlewiet.registrations.services.VakantieProjectService;

@Controller
@RequestMapping(value="/dienst")
public class DienstController {

	private final static Logger LOGGER = LoggerFactory.getLogger(DienstController.class);

	@Autowired
	private InschrijvingService inschrijvingService;
	
	@Autowired
	private VakantieProjectService vakantieService;
	
	@RequestMapping(value = "/inschrijving", method = RequestMethod.GET)
	public ModelAndView viewNieuweInschrijving() {

		Inschrijving inschrijving
			= new Inschrijving();
		
		inschrijving.setStatus( Status.NIEUW );
		
		List<VakantieProject> vakanties
			= vakantieService.getAllVakantieProjecten();
		
		Map<String, Object> model 
			= new HashMap<String, Object>();
		
		model.put( "inschrijving", inschrijving );
		model.put( "vakanties", vakanties );

		return new ModelAndView( "dienst/inschrijving", model );
	}

	@RequestMapping(value = "/inschrijving/{id}", method = RequestMethod.GET)
	@Transactional
	public ModelAndView viewInschrijving(@PathVariable Long id ) {

		Inschrijving inschrijving
			= this.inschrijvingService.findInschrijvingById( id );
		
		List<VakantieProject> vakanties
			= vakantieService.getAllVakantieProjecten();
	
		Map<String, Object> model 
			= new HashMap<String, Object>();
	
		model.put( "inschrijving", inschrijving );
		model.put( "vakanties", vakanties );

		return new ModelAndView( "dienst/inschrijving", model );

	}

}

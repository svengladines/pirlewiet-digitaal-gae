package be.pirlewiet.digitaal.web.controller.page.organisation;

import org.springframework.beans.factory.annotation.Autowired;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import be.pirlewiet.digitaal.domain.people.DoorMan;
import be.pirlewiet.digitaal.domain.service.OrganisationService;

@Controller
@RequestMapping(value="/registration.html")
public class OrganisationRegistrationPageController {
	
	private final static Logger logger = LoggerFactory.getLogger( OrganisationRegistrationPageController.class );
	
	@Autowired
	OrganisationService organisationService;
	
	@Autowired
	protected DoorMan doorMan;
	
	@RequestMapping( method = { RequestMethod.GET }, produces={ MediaType.TEXT_HTML_VALUE } ) 
	public ModelAndView view()  {
		
		return new ModelAndView( "registration" );	
			
	}
	
}

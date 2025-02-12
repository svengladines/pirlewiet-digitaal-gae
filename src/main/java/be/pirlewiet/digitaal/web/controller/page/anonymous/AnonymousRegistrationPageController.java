package be.pirlewiet.digitaal.web.controller.page.anonymous;

import org.springframework.beans.factory.annotation.Autowired;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import be.pirlewiet.digitaal.domain.people.DoorMan;
import be.pirlewiet.digitaal.domain.service.OrganisationService;

@Controller
@RequestMapping(value="/anonymous/registration.html")
public class AnonymousRegistrationPageController {
	
	private final static Logger logger = LoggerFactory.getLogger( AnonymousRegistrationPageController.class );
	
	@Autowired
	OrganisationService organisationService;
	
	@Autowired
	protected DoorMan doorMan;
	
	@RequestMapping( method = { RequestMethod.GET }, produces={ MediaType.TEXT_HTML_VALUE } ) 
	public String view()  {
		return "anonymous/registration";
	}
	
}

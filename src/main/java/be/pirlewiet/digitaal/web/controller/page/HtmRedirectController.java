package be.pirlewiet.digitaal.web.controller.page;

import be.pirlewiet.digitaal.domain.people.DoorMan;
import be.pirlewiet.digitaal.domain.people.OrganisationManager;
import be.pirlewiet.digitaal.model.Organisation;
import be.pirlewiet.digitaal.model.OrganisationType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequestMapping(value="/index.htm")
public class HtmRedirectController {
	
	private final static Logger logger = LoggerFactory.getLogger( HtmRedirectController.class );
	
	@Autowired
	OrganisationManager organisationManager;
	
	@Autowired
	protected DoorMan buitenWipper;
	
	@RequestMapping(method=RequestMethod.GET) 
	public String view()  {
		return "redirect:/index.html";
	}
	
}

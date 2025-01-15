package be.pirlewiet.digitaal.web.controller.page;

import be.pirlewiet.digitaal.model.OrganisationType;
import org.springframework.beans.factory.annotation.Autowired;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import be.pirlewiet.digitaal.domain.people.DoorMan;
import be.pirlewiet.digitaal.domain.people.OrganisationManager;
import be.pirlewiet.digitaal.model.Organisation;

@Controller
@RequestMapping(value="/start.html")
public class StartPageController {
	
	private final static Logger logger = LoggerFactory.getLogger( StartPageController.class );
	
	@Autowired
	OrganisationManager organisationManager;
	
	@Autowired
	protected DoorMan buitenWipper;
	
	@RequestMapping(method=RequestMethod.GET) 
	public String view( @CookieValue( required = false, value="pwtid" ) String pwtID )  {
		
		if ( pwtID == null ) {
			return "redirect:/login.html";
		}
		else {
			Organisation organisation = this.buitenWipper.guard().whoHasID(  pwtID  );
			if (organisation == null) {
				return "redirect:/login.html";
			}
			else if (OrganisationType.INDIVIDUAL.equals(organisation.getType())) {
				return "redirect:/profile.html";
			}
			else {
				return "redirect:/organisation/organisation.html";
			}
		}
		

	}
	
}

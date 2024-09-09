package be.pirlewiet.digitaal.web.controller.page;

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
	public ModelAndView view( @CookieValue( required = false, value="pwtid" ) String pwtID )  {
		
		ModelAndView mav
			= new ModelAndView();
		
		if ( pwtID == null ) {
			mav.setView( new RedirectView("login.htm") );
			return mav;
		}
		else {
			Organisation organisatie
				= this.buitenWipper.guard().whoHasID(  pwtID  );	
		
			if ( organisatie == null ) {
				mav.setView( new RedirectView("/login.htm") );
			}
			else {
				mav.setView( new RedirectView("/organisation.html") );
			}	
		}
		
		return mav;
		
	}
	
}

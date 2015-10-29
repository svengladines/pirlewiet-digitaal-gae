package be.pirlewiet.registrations.web.controllers;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import be.pirlewiet.registrations.domain.BuitenWipper;
import be.pirlewiet.registrations.domain.OrganisationManager;
import be.pirlewiet.registrations.model.Organisatie;

@Controller
@RequestMapping(value="/{page}.html")
public class PageController {
	
	private final static Logger logger = LoggerFactory.getLogger( PageController.class );
	
	@Resource
	OrganisationManager organisationManager;
	
	@Resource
	protected BuitenWipper buitenWipper;
	
	@RequestMapping(method=RequestMethod.GET) 
	public ModelAndView view( @PathVariable String page, @CookieValue( required = false, value="pwtid" ) String pwtID )  {
		
		ModelAndView mav
			= new ModelAndView();
		
		if ( pwtID == null ) {
			mav.setView( new RedirectView("code.htm") );
			return mav;
		} 
		
		
		Organisatie organisatie
			= this.buitenWipper.guard().whoHasID(  pwtID  );	
		
		if ( organisatie == null ) {
			mav.setView( new RedirectView("/code.htm") );
			return mav;
		}
		
		if ( "start".equals( page ) ) {
			
			if ( this.organisationManager.isInComplete( organisatie, true ) ) {
				mav.setView( new RedirectView("/rs/organisation.html") );
			}
			else {
				mav.setView( new RedirectView("/rs/inschrijvingen.html") );
			}
		
		}
		else {
			mav.setViewName( page );
		}
		
		return mav;
		
	}
	
}

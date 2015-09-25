package be.pirlewiet.registrations.web.controllers;

import static be.occam.utils.spring.web.Controller.*;
import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.condition.ProducesRequestCondition;
import org.springframework.web.servlet.view.RedirectView;

import be.occam.utils.spring.web.Client;
import be.pirlewiet.registrations.domain.BuitenWipper;
import be.pirlewiet.registrations.domain.OrganisationManager;
import be.pirlewiet.registrations.model.Organisatie;
import be.pirlewiet.registrations.web.util.PirlewietUtil;

@Controller
@RequestMapping(value="/pd/{page}.html")
public class PDController {
	
	private final static Logger logger = LoggerFactory.getLogger( PDController.class );
	
	@Resource
	OrganisationManager organisationManager;
	
	@Resource
	protected BuitenWipper buitenWipper;
	
	@Resource
	protected Organisatie pDiddy;
	
	@RequestMapping(method=RequestMethod.GET, produces={ MediaType.TEXT_HTML_VALUE } ) 
	@ResponseBody
	public ResponseEntity<String> view( @PathVariable String page, @CookieValue( required = true, value="pwtid" ) String pwtID )  {
		
		ModelAndView mav
			= new ModelAndView();
		
		Organisatie organisatie
			= this.buitenWipper.guard().whoHasID(  pwtID  );
		
		String html
			= "no es bueno";
		
		if ( ( organisatie == null ) || ( ! be.pirlewiet.registrations.web.util.PirlewietUtil.isPD( organisatie ) ) ){
			mav.setView( new RedirectView("/code.htm") );
			html = "get outta here!";
		}
		else if ( "organisations".equals( page ) ) {
			
			try {
				html = Client.getHTML( "http://localhost:8068/rs/organisations.html", PirlewietUtil.as( pDiddy ) ).getBody();
			}
			catch ( Exception e ) {
				logger.error( "could not get organisations as PD", e );
			}
		
		}
		
		return response( html, HttpStatus.OK );
		
	}
	
}

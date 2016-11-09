package be.pirlewiet.digitaal.web.controller.page;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import be.pirlewiet.digitaal.domain.people.DoorMan;

@Controller
@RequestMapping(value="/logout.html")
public class LogoutPageController {
	
	private final static Logger logger = LoggerFactory.getLogger( LogoutPageController.class );
	
	@Resource
	protected DoorMan buitenWipper;
	
	@RequestMapping(method=RequestMethod.GET) 
	public ModelAndView view( @CookieValue( required = false, value="pwtid" ) String pwtID )  {
		
		ModelAndView mav
			= new ModelAndView();
		
		mav.setViewName( "logout" );
		
		return mav;
		
	}
	
}

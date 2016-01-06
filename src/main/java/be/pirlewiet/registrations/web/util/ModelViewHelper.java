package be.pirlewiet.registrations.web.util;

import java.util.Map;

import org.springframework.web.servlet.ModelAndView;

import be.occam.utils.spring.configuration.ConfigurationProfiles;

public class ModelViewHelper {
	
	protected static final String ATTR_FRONTEND 
		= "be.pirlewiet.registrations.frontend.url";
	
	public static ModelAndView modelAndView( String view, Map<String,Object> model ) {
		
		if ( ConfigurationProfiles.isActive( ConfigurationProfiles.PRODUCTION ) ) {
			
			model.put( ATTR_FRONTEND, "http://pirlewiet-digitaal.appspot.com/digitaal" );
			
		}
		else {
			
			model.put( ATTR_FRONTEND, "http:localhost:8068" );
			
		}
		
		return new ModelAndView( view, model );
		
	}

}

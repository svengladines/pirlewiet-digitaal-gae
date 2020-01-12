package be.pirlewiet.digitaal.web.listener;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import com.googlecode.objectify.ObjectifyService;

import be.pirlewiet.digitaal.model.Organisation;

public class ObjectifyListener implements ServletContextListener {
	
	public void contextInitialized(ServletContextEvent event) {
		ObjectifyService.init();
		ObjectifyService.register( Organisation.class );
	}

	@Override
	public void contextDestroyed(ServletContextEvent arg0) {
		// TODO Auto-generated method stub
		
	}
	
}

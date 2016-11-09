package be.pirlewiet.digitaal.application.run;

import org.junit.Test;

import be.occam.test.jtest.JTest;
import be.occam.utils.spring.configuration.ConfigurationProfiles;

public class RunPirlewietRegistrations_Development extends JTest {
	
	public RunPirlewietRegistrations_Development() {
		super( "/", 8068, ConfigurationProfiles.DEV );
		
		System.setProperty( "pirlewiet.currentYear", "2017" );
	}
	
	@Test
	public void doesItSmoke() throws Exception {
		
		System.setSecurityManager( null );
		Thread.sleep( 10000000 );
		
	}

}

package be.pirlewiet.digitaal.application.run;

import org.junit.Test;

import be.occam.test.jtest.JTest;
import be.occam.utils.spring.configuration.ConfigurationProfiles;

public class RunPirlewietDigitaal_Development extends JTest {
	
	public RunPirlewietDigitaal_Development() {
		super( "/", 8068, ConfigurationProfiles.DEV );
		
		System.setProperty( "pirlewiet.currentYear", "2019" );
	}
	
	@Test
	public void doesItSmoke() throws Exception {
		
		System.setSecurityManager( null );
		Thread.sleep( 10000000 );
		
	}

}

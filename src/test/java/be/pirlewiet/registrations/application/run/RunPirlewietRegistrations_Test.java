package be.pirlewiet.registrations.application.run;

import org.junit.Test;

import be.occam.test.jtest.JTest;

public class RunPirlewietRegistrations_Test extends JTest {
	
	public RunPirlewietRegistrations_Test() {
		super( "/" );
		this.forcePort = 8068;
	}
	
	@Test
	public void doesItSmoke() throws Exception {
		
		System.setSecurityManager( null );
		Thread.sleep( 10000000 );
		
	}

}

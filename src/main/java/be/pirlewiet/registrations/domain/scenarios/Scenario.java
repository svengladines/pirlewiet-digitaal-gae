package be.pirlewiet.registrations.domain.scenarios;

import javax.annotation.Resource;

import be.pirlewiet.registrations.web.util.DataGuard;

public abstract class Scenario {
	
	public abstract void execute( String... parameters );
	
}

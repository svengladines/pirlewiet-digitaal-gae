package be.pirlewiet.digitaal.domain.scenario;

import javax.annotation.Resource;

import be.pirlewiet.digitaal.web.util.DataGuard;

public abstract class Scenario {
	
	public abstract void execute( String... parameters );
	
}

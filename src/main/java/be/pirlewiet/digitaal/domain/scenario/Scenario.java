package be.pirlewiet.digitaal.domain.scenario;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class Scenario {
	
	protected final Logger logger
		= LoggerFactory.getLogger( this.getClass() );
	
	public abstract void execute( String... parameters );
	
}

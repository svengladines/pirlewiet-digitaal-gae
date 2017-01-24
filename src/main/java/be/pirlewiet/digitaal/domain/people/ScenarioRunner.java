package be.pirlewiet.digitaal.domain.people;

import static be.occam.utils.javax.Utils.list;

import java.util.List;

import be.pirlewiet.digitaal.domain.scenario.Scenario;

public class ScenarioRunner {
	
	protected List<Scenario> scenariosToRun
		= list();
	
	public ScenarioRunner( Scenario...scenarios ) {
		
		for ( Scenario scenario : scenarios ) {
			scenariosToRun.add( scenario );
		}
		
	}
	
	public void runThem() {
		
		for ( Scenario scenario : scenariosToRun ) {
			scenario.execute();
		}
		
	}

}

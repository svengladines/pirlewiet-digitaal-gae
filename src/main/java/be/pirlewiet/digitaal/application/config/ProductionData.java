package be.pirlewiet.digitaal.application.config;

import javax.annotation.PostConstruct;

public class ProductionData {

	@PostConstruct
	public void injectData() {
		
		/*
		
			Holidays are no longer stored in datastore, but in 'ConfiguredVakantieRepository'
			This to limit data API calls, as those cost money...
		
		*/
		
	}
	
}

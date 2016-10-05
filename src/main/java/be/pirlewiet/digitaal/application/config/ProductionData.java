package be.pirlewiet.digitaal.application.config;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;

import be.occam.utils.timing.Timing;
import be.pirlewiet.digitaal.model.Period;
import be.pirlewiet.digitaal.model.Holiday;
import be.pirlewiet.digitaal.model.HolidayType;
import be.pirlewiet.digitaal.repositories.VakantieRepository;

import com.google.appengine.api.datastore.KeyFactory;

public class ProductionData {

	@Resource
	VakantieRepository vakantieRepository;
	
	@PostConstruct
	public void injectData() {
		
		/*
		
			Holidays are no longer stored in datastore, but in 'ConfiguredVakantieRepository'
			This to limit data API calls, as those cost money...
		
		*/
		
	}
	
}

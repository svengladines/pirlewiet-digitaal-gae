package be.pirlewiet.registrations.application.config;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;

import com.google.appengine.api.datastore.KeyFactory;

import be.occam.utils.timing.Timing;
import be.pirlewiet.registrations.model.Periode;
import be.pirlewiet.registrations.model.Vakantie;
import be.pirlewiet.registrations.model.VakantieType;
import be.pirlewiet.registrations.repositories.VakantieRepository;

public class ProductionData {

	@Resource
	VakantieRepository vakantieRepository;
	
	@PostConstruct
	public void injectData() {
		
	}
	
}

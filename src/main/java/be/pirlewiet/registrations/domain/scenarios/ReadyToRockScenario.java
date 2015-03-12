package be.pirlewiet.registrations.domain.scenarios;

import java.util.List;

import javax.annotation.Resource;

import be.pirlewiet.registrations.domain.OrganisationManager;
import be.pirlewiet.registrations.domain.SecretariaatsMedewerker;
import be.pirlewiet.registrations.model.Organisatie;
import be.pirlewiet.registrations.repositories.OrganisatieRepository;
import be.pirlewiet.registrations.web.util.DataGuard;

import com.google.appengine.api.datastore.KeyFactory;

public class ReadyToRockScenario extends Scenario {
	
	@Resource
	OrganisatieRepository organisatieRepository;
	
	@Resource
	SecretariaatsMedewerker secretariaatsMedewerker;
	
	@Resource
	DataGuard dataGuard;
	
	public ReadyToRockScenario guard() {
    	this.dataGuard.guard();
    	return this;
    }

	@Override
	public void execute( String... parameters ) {
		
		List<Organisatie> organisations
			= this.organisatieRepository.findAll();
		
		for ( Organisatie organisation : organisations ) {
			
			this.secretariaatsMedewerker.sendInitialCode( organisation );
			
		}
		
	}
	
	

}

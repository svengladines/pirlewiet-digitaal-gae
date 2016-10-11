package be.pirlewiet.digitaal.domain.scenario;

import java.util.List;

import javax.annotation.Resource;

import be.pirlewiet.digitaal.domain.people.OrganisationManager;
import be.pirlewiet.digitaal.domain.people.Secretary;
import be.pirlewiet.digitaal.model.Organisation;
import be.pirlewiet.digitaal.repositories.OrganisatieRepository;
import be.pirlewiet.digitaal.web.util.DataGuard;

import com.google.appengine.api.datastore.KeyFactory;

public class ReadyToRockScenario extends Scenario {
	
	@Resource
	OrganisatieRepository organisatieRepository;
	
	@Resource
	Secretary secretariaatsMedewerker;
	
	@Resource
	DataGuard dataGuard;
	
	public ReadyToRockScenario guard() {
    	this.dataGuard.guard();
    	return this;
    }

	@Override
	public void execute( String... parameters ) {
		
		List<Organisation> organisations
			= this.organisatieRepository.findAll();
		
		for ( Organisation organisation : organisations ) {
			
			this.secretariaatsMedewerker.sendInitialCode( organisation );
			
		}
		
	}
	
	

}
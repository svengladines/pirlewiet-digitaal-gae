package be.pirlewiet.digitaal.domain.scenario;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import be.pirlewiet.digitaal.domain.people.Secretary;
import be.pirlewiet.digitaal.model.Organisation;
import be.pirlewiet.digitaal.repository.OrganisationRepository;
import be.pirlewiet.digitaal.web.util.DataGuard;

public class ReadyToRockScenario extends Scenario {
	
	@Autowired
	OrganisationRepository organisatieRepository;
	
	@Autowired
	Secretary secretariaatsMedewerker;
	
	@Autowired
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
			
			// this.secretariaatsMedewerker.sendInitialCode( organisation );
			
		}
		
	}
	
	

}

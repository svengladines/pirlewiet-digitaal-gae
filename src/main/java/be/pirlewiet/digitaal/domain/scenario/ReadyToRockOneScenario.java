package be.pirlewiet.digitaal.domain.scenario;

import org.springframework.beans.factory.annotation.Autowired;

import be.pirlewiet.digitaal.domain.people.Secretary;
import be.pirlewiet.digitaal.model.Organisation;
import be.pirlewiet.digitaal.repository.OrganisationRepository;
import be.pirlewiet.digitaal.web.util.DataGuard;

public class ReadyToRockOneScenario extends Scenario {
	
	@Autowired
	OrganisationRepository organisatieRepository;
	
	@Autowired
	Secretary secretariaatsMedewerker;
	
	@Autowired
	DataGuard dataGuard;
	
	public ReadyToRockOneScenario guard() {
    	this.dataGuard.guard();
    	return this;
    }

	@Override
	public void execute( String... parameters ) {
		
		Organisation organisation 
			= this.organisatieRepository.findOneByEmail( parameters[ 0 ] );
		
		// this.secretariaatsMedewerker.sendInitialCode( organisation );
			
	}
	
	

}

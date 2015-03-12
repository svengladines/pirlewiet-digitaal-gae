package be.pirlewiet.registrations.domain.scenarios;

import javax.annotation.Resource;

import be.pirlewiet.registrations.domain.SecretariaatsMedewerker;
import be.pirlewiet.registrations.model.Organisatie;
import be.pirlewiet.registrations.repositories.OrganisatieRepository;
import be.pirlewiet.registrations.web.util.DataGuard;

public class ReadyToRockOneScenario extends Scenario {
	
	@Resource
	OrganisatieRepository organisatieRepository;
	
	@Resource
	SecretariaatsMedewerker secretariaatsMedewerker;
	
	@Resource
	DataGuard dataGuard;
	
	public ReadyToRockOneScenario guard() {
    	this.dataGuard.guard();
    	return this;
    }

	@Override
	public void execute( String... parameters ) {
		
		Organisatie organisation 
			= this.organisatieRepository.findOneByEmail( parameters[ 0 ] );
		
		this.secretariaatsMedewerker.sendInitialCode( organisation );
			
	}
	
	

}

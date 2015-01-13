package be.pirlewiet.registrations.domain.scenarios;

import java.util.List;

import javax.annotation.Resource;

import be.pirlewiet.registrations.domain.OrganisationManager;
import be.pirlewiet.registrations.model.Organisatie;
import be.pirlewiet.registrations.repositories.OrganisatieRepository;
import be.pirlewiet.registrations.web.util.DataGuard;

import com.google.appengine.api.datastore.KeyFactory;

public class SetOrganisationsUuidScenario extends Scenario {
	
	@Resource
	OrganisatieRepository organisatieRepository;
	
	@Resource
	DataGuard dataGuard;
	
	public SetOrganisationsUuidScenario guard() {
    	this.dataGuard.guard();
    	return this;
    }

	@Override
	public void execute() {
		
		List<Organisatie> organisations
			= this.organisatieRepository.findAll();
		
		for ( Organisatie organisation : organisations ) {
			
			if ( ( organisation.getUuid() == null ) || ( organisation.getUuid().isEmpty() ) ) {
				
				organisation.setUuid( KeyFactory.keyToString( organisation.getKey() ) );
				this.organisatieRepository.saveAndFlush( organisation );
				
			}
			
		}
		
	}
	
	

}

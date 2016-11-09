package be.pirlewiet.digitaal.domain.scenario;

import java.util.List;

import javax.annotation.Resource;

import be.pirlewiet.digitaal.model.Organisation;
import be.pirlewiet.digitaal.web.util.DataGuard;

import com.google.appengine.api.datastore.KeyFactory;

public class SetOrganisationsUUIDScenario extends Scenario {
	
	@Resource
	OrganisatieRepository organisatieRepository;
	
	@Resource
	DataGuard dataGuard;
	
	public SetOrganisationsUUIDScenario guard() {
    	this.dataGuard.guard();
    	return this;
    }

	@Override
	public void execute( String... parameters ) {
		
		List<Organisation> organisations
			= this.organisatieRepository.findAll();
		
		for ( Organisation organisation : organisations ) {
			
			if ( ( organisation.getUuid() == null ) || ( organisation.getUuid().isEmpty() ) ) {
				
				organisation.setUuid( KeyFactory.keyToString( organisation.getKey() ) );
				this.organisatieRepository.saveAndFlush( organisation );
				
			}
			
		}
		
	}
	
	

}

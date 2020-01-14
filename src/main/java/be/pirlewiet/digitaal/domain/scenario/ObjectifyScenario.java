package be.pirlewiet.digitaal.domain.scenario;

import java.util.List;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import be.pirlewiet.digitaal.model.Address;
import be.pirlewiet.digitaal.model.Organisation;
import be.pirlewiet.digitaal.repository.AddressRepository;
import be.pirlewiet.digitaal.repository.OrganisationRepository;
import be.pirlewiet.digitaal.web.util.DataGuard;

public class ObjectifyScenario extends Scenario {
	
	protected final Logger logger
		= LoggerFactory.getLogger( this.getClass() );

	@Resource
	OrganisationRepository organisationRepository;
	
	@Resource
	AddressRepository addressRepository;
	
	@Resource
	DataGuard dataGuard;
	
	public ObjectifyScenario guard() {
    	this.dataGuard.guard();
    	return this;
    }
	
	//@Transactional(readOnly=false)
	@Override
	public void execute( String... parameters ) {
		
		/*
		logger.info( "objectify organisations" );
		
		List<Organisation> all
			= this.organisationRepository.findOld();
		
		logger.info( "found [{}] old organisations", all.size() );

		for ( Organisation organisation : all ) {
			this.organisationRepository.saveAndFlush( organisation );
			logger.info( "objectified [{}]", organisation.getName() );
			break;
		}
		*/
		
		
		logger.info( "objectify addresses" );
		
		List<Address> all
			= this.addressRepository.findAll();
		
		//logger.info( "found [{}] old addresses", all.size() );

		for ( Address address : all ) {
			//address.setId( null );
			this.addressRepository.saveAndFlush( address );
			logger.info( "objectified [{}]", address.getUuid() );
			//break;
		}
				
	}
	
}

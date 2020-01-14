package be.pirlewiet.digitaal.repository.impl;

import static com.googlecode.objectify.ObjectifyService.ofy;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import com.googlecode.objectify.Key;

import be.pirlewiet.digitaal.model.Address;
import be.pirlewiet.digitaal.repository.AddressRepository;

@Repository
public class AddressRepositoryObjectify implements AddressRepository {
	
	private final Logger logger 
		= LoggerFactory.getLogger( this.getClass() );

	@Override
	public Address findByUuid(String uuid) {
		
		return ofy().load().type(Address.class).filter("uuid", uuid).first().now();
		
	}

	@Override
	public Address saveAndFlush(Address address) {

		logger.info( "store address with uuid [{}]", new Object[] { address.getUuid() } );
		Key<Address> key = ofy().save().entity( address ).now();
		Address stored = ofy().load().key( key ).now();
		logger.info( "stored address has id [{}]", key.getId() );
		return stored;
	}

	@Override
	public List<Address> findAll() {
		logger.info( "load all addresses");
		return ofy().load().type(Address.class).list();
	}
	
	@Override
	public List<Address> findOld() {
		return ofy().load().type(Address.class).filter("id",0).list();
	}

	@Override
	public void delete(Address enrollment) {
		// TODO Auto-generated method stub
		
	}

}

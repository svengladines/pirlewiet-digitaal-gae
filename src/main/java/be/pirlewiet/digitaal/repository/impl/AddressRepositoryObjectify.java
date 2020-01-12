package be.pirlewiet.digitaal.repository.impl;

import static com.googlecode.objectify.ObjectifyService.ofy;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.googlecode.objectify.Key;

import be.pirlewiet.digitaal.model.Address;
import be.pirlewiet.digitaal.repository.AddressRepository;

@Repository
public class AddressRepositoryObjectify implements AddressRepository {

	@Override
	public Address findByUuid(String uuid) {
		
		return ofy().load().key(Key.create(Address.class, uuid)).now();
		
	}

	@Override
	public Address saveAndFlush(Address address) {

		Key<Address> key = ofy().save().entity( address ).now();
		return ofy().load().key( key ).now();
		
	}

	@Override
	public List<Address> findAll() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void delete(Address enrollment) {
		// TODO Auto-generated method stub
		
	}

}

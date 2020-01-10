package be.pirlewiet.digitaal.repository;

import java.util.List;

import be.pirlewiet.digitaal.model.Address;
import be.pirlewiet.digitaal.model.Enrollment;

public interface AddressRepository {
	
	public Address findByUuid( String id );
	
	public Address saveAndFlush( Address address );
	public List<Address> findAll();
	
	public void delete( Address enrollment );

}

package be.pirlewiet.digitaal.domain.people;

import static be.occam.utils.javax.Utils.isEmpty;

import javax.annotation.Resource;

import be.pirlewiet.digitaal.domain.exception.ErrorCodes;
import be.pirlewiet.digitaal.domain.exception.IncompleteObjectException;
import be.pirlewiet.digitaal.domain.exception.PirlewietException;
import be.pirlewiet.digitaal.model.Address;
import be.pirlewiet.digitaal.repositories.AddressRepository;

import com.google.appengine.api.datastore.KeyFactory;

public class AddressManager {
	
	@Resource
	protected DoorMan doorMan;
	
	@Resource
	AddressRepository addressRepository;
	
	public Address create( Address address ) {
		
		if ( isEmpty( address.getZipCode() ) ) {
			throw new IncompleteObjectException( ErrorCodes.ADDRESS_ZIPCODE_MISSING );
		}
		
		if ( isEmpty( address.getCity() ) ) {
			throw new IncompleteObjectException( ErrorCodes.ADDRESS_CITY_MISSING );
		}
		
		if ( isEmpty( address.getStreet() ) ) {
			throw new IncompleteObjectException( ErrorCodes.ADDRESS_STREET_MISSING );
		}
		
		if ( isEmpty( address.getNumber() ) ) {
			throw new IncompleteObjectException( ErrorCodes.ADDRESS_NUMBER_MISSING );
		}
		
		Address saved 
			= this.addressRepository.saveAndFlush( address );
	
		saved.setUuid( KeyFactory.keyToString( saved.getKey() ) );
	
		saved 
			= this.addressRepository.saveAndFlush( saved );
		
		return saved;
		
	}
	
	public Address update( Address toUpdate, Address update ) {
		
		// TODO, move check to protected method & reuse. prio 2
		if ( isEmpty( update.getZipCode() ) ) {
			throw new IncompleteObjectException( ErrorCodes.ADDRESS_ZIPCODE_MISSING );
		}
		
		if ( isEmpty( update.getCity() ) ) {
			throw new IncompleteObjectException( ErrorCodes.ADDRESS_CITY_MISSING );
		}
		
		if ( isEmpty( update.getStreet() ) ) {
			throw new IncompleteObjectException( ErrorCodes.ADDRESS_STREET_MISSING );
		}
		
		if ( isEmpty( update.getNumber() ) ) {
			throw new IncompleteObjectException( ErrorCodes.ADDRESS_NUMBER_MISSING );
		}
		
		Address updated 
			= this.addressRepository.saveAndFlush( toUpdate );
	
		return updated;
		
	}
	
	public Address findOneByUuid( String uuid ) {
		return this.addressRepository.findByUuid( uuid );
	}
	
	public void delete( Address address ) {
		this.addressRepository.delete( address );
	}
	
	public void checkComplete( String uuid ) {
		
		Address address
			= this.addressRepository.findByUuid( uuid );
		
		if ( address == null ) {
			throw new PirlewietException( ErrorCodes.INTERNAL, String.format("no address with uuid [%s]", uuid ) );
		}
		
		if ( isEmpty( address.getCity() ) ) {
			throw new IncompleteObjectException( ErrorCodes.ADDRESS_CITY_MISSING );
		}
		
		if ( isEmpty( address.getStreet() ) ) {
			throw new IncompleteObjectException( ErrorCodes.ADDRESS_STREET_MISSING );
		}
		
		if ( isEmpty( address.getNumber() ) ) {
			throw new IncompleteObjectException( ErrorCodes.ADDRESS_NUMBER_MISSING );
		}
		
		if ( isEmpty( address.getZipCode() ) ) {
			throw new IncompleteObjectException( ErrorCodes.ADDRESS_ZIPCODE_MISSING );
		}
		
	}
	
	public boolean isComplete( String uuid ) {
		
		try {
			this.checkComplete( uuid );
			return true;
		}
		catch( IncompleteObjectException e ) {
			return false;
		}
		
	}
	
}

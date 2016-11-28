package be.pirlewiet.digitaal.domain.people;

import static be.occam.utils.javax.Utils.isEmpty;

import javax.annotation.Resource;

import be.pirlewiet.digitaal.domain.exception.ErrorCodes;
import be.pirlewiet.digitaal.domain.exception.IncompleteObjectException;
import be.pirlewiet.digitaal.model.Address;
import be.pirlewiet.digitaal.repositories.AddressRepository;

import com.google.appengine.api.datastore.KeyFactory;

public class AddressManager {
	
	@Resource
	protected DoorMan doorMan;
	
	@Resource
	AddressRepository addressRepository;
	
	public Address createOrUpdate( Address address ) {
		
		Address saved 
			= this.addressRepository.saveAndFlush( address );
	
		saved.setUuid( KeyFactory.keyToString( saved.getKey() ) );
	
		saved 
			= this.addressRepository.saveAndFlush( saved );
		
		return saved;
		
	}
	
	public Address update( Address toUpdate, Address update ) {
		
		toUpdate.setCity( update.getCity() );
		toUpdate.setStreet( update.getStreet() );
		toUpdate.setNumber( update.getNumber() );
		toUpdate.setZipCode( update.getZipCode() );
		
		Address updated 
			= this.addressRepository.saveAndFlush( toUpdate );
	
		return updated;
		
	}
	
	public Address findOneByUuid( String uuid ) {
		return this.addressRepository.findByUuid( uuid );
	}
	
	public void checkComplete( String uuid ) {
		
		Address address
			= this.addressRepository.findByUuid( uuid );
		
		if ( address == null ) {
			// TODO, throw exception
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

package be.pirlewiet.digitaal.domain.people;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.appengine.api.datastore.KeyFactory;

import be.pirlewiet.digitaal.domain.HeadQuarters;
import be.pirlewiet.digitaal.model.Organisation;
import be.pirlewiet.digitaal.model.Person;
import be.pirlewiet.digitaal.repositories.PersonRepository;

public class PersonManager {
	
	@Resource
	HeadQuarters headQuarters;
	
	@Resource
	protected Organisation pDiddy;
	
	@Resource
	protected AddressManager addressManager;
	
	protected final Logger logger
		= LoggerFactory.getLogger( this.getClass() );
	
	@Resource
	protected PersonRepository personRepository;
	
	@Resource
	MailMan postBode;
	
    public PersonManager() {
    	
    	
    }
    
    public Person template() {
    	return new Person();
    }
    
    public Person findOneByUuid( String uuid ) {
    	
    	Person person 
    		= this.personRepository.findByUuid( uuid );
    	
    	return person;
    	
    }
    
    public Person update( Person toUpdate, Person update ) {
    	
    	toUpdate.setGivenName( update.getGivenName() );
    	toUpdate.setFamilyName( update.getFamilyName() );
    	toUpdate.setPhone( update.getPhone() );
    	toUpdate.setEmail( update.getEmail() );
    	
    	Person updated
    		= this.personRepository.saveAndFlush( toUpdate );
    	
    	if ( toUpdate.getUuid() == null ) {
    	
    		updated.setUuid( KeyFactory.keyToString( updated.getKey() ) );
    		updated = this.personRepository.saveAndFlush( updated );
    		
    	}
    	
    	return updated;
    		
    	
    }
       
}
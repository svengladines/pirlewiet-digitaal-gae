package be.pirlewiet.digitaal.domain.people;

import java.util.List;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.appengine.api.datastore.KeyFactory;

import be.pirlewiet.digitaal.domain.HeadQuarters;
import be.pirlewiet.digitaal.model.Organisation;
import be.pirlewiet.digitaal.model.Person;
import be.pirlewiet.digitaal.repository.PersonRepository;

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
    
    public List<Person> findAll() {
    	
    	return this.personRepository.findAll();
    	
    }
    
  public Person create( Person toCreate ) {
    	
    	Person created
    		= this.personRepository.saveAndFlush( toCreate );
    	
    	created.setUuid( KeyFactory.keyToString( created.getKey() ) );
    	created = this.personRepository.saveAndFlush( created );
    		
    	return created;
    		
    	
    }
    
    public Person update( Person toUpdate, Person update ) {
    	
    	toUpdate.setGivenName( update.getGivenName() );
    	toUpdate.setFamilyName( update.getFamilyName() );
    	toUpdate.setPhone( update.getPhone() );
    	toUpdate.setEmail( update.getEmail() );
    	
    	if ( update.getBirthDay() != null ) {
    		toUpdate.setBirthDay( update.getBirthDay() );
    	}
    	
    	if ( update.getGender() != null ) {
    		toUpdate.setGender( update.getGender() );
    	}
    	
    	Person updated
    		= this.personRepository.saveAndFlush( toUpdate );
    	
    	if ( toUpdate.getUuid() == null ) {
    	
    		updated.setUuid( KeyFactory.keyToString( updated.getKey() ) );
    		updated = this.personRepository.saveAndFlush( updated );
    		
    	}
    	
    	return updated;
    		
    	
    }
    
    public void delete( Person person ) {
    	this.personRepository.delete( person );
    }
       
}
package be.pirlewiet.digitaal.domain.people;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
    
    public Person findOneByUuid( String uuid ) {
    	
    	Person person 
    		= this.personRepository.findByUuid( uuid );
    	
    	return person;
    	
    }
    
       
}
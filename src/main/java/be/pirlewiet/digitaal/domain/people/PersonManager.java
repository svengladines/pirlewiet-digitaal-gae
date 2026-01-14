package be.pirlewiet.digitaal.domain.people;

import java.util.List;
import java.util.UUID;

import be.occam.utils.timing.Timing;
import be.pirlewiet.digitaal.infrastructure.salesforce.Contact;
import be.pirlewiet.digitaal.infrastructure.salesforce.SalesforceClient;
import be.pirlewiet.digitaal.model.Address;
import be.pirlewiet.digitaal.model.Gender;
import com.sun.jdi.BooleanValue;
import org.springframework.beans.factory.annotation.Autowired;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import be.pirlewiet.digitaal.domain.HeadQuarters;
import be.pirlewiet.digitaal.model.Organisation;
import be.pirlewiet.digitaal.model.Person;
import be.pirlewiet.digitaal.repository.PersonRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import static java.util.Optional.ofNullable;

@Component
public class PersonManager {
	
	@Autowired
	HeadQuarters headQuarters;
	
	@Autowired
	protected Organisation pDiddy;
	
	@Autowired
	protected AddressManager addressManager;
	
	protected final Logger logger = LoggerFactory.getLogger( this.getClass() );
	
	@Autowired
	protected PersonRepository personRepository;

	@Value("${salesforce.enabled}")
	protected boolean salesforceEnabled;
	
	@Autowired
	MailMan mailMan;

	@Autowired(required = false)
	SalesforceClient salesforceClient;
	
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
	  
	  	toCreate.setUuid( UUID.randomUUID().toString() );
    	
    	Person created
    		= this.personRepository.saveAndFlush( toCreate );
    	
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

    		toUpdate.setUuid( UUID.randomUUID().toString() );
    		updated = this.personRepository.saveAndFlush( updated );
    		
    	}
    	return updated;
    }
    
    public void delete( Person person ) {
    	this.personRepository.delete( person );
    }

	public void touch(Person person, Address address) {
		logger.info("person [{}]; touch", person.getUuid());
		ofNullable(person.getExternalId()).ifPresentOrElse(id -> {
			if (salesforceEnabled) {
				logger.info("person [{}], is SF contact with id [{}]; update...", person.getUuid(),id);
				this.salesforceClient.updateContact(id, toContact(person, address)).ifPresent(contact -> {
					logger.info("person [{}]; saved SF contact with id {}", person.getUuid(), contact.id());
					this.personRepository.saveAndFlush(person);
				});
			}
		}, () -> {
			if (salesforceEnabled) {
				logger.info("person [{}]; is no SF contact yet, create", person.getUuid());
				this.salesforceClient.createContact(toContact(person, address)).ifPresent(contact -> {
					person.setExternalId(contact.id());
					logger.info("person [{}]; saved as new SF contact with id {}", person.getUuid(), contact.id());
					this.personRepository.saveAndFlush(person);
				});
			}
		});
	}

	protected static Contact toContact(Person person,Address address ) {
		return new Contact()
			// TODO: allow other types
			.type("Deelnemer")
			.firstName(person.getGivenName())
			.lastName(person.getFamilyName())
			.mobilePhone(person.getPhone())
			.birthDate(Timing.date(person.getBirthDay(),"yyyy-MM-dd"))
			.city(address.getCity())
			.street("%s %s".formatted(address.getStreet(),address.getNumber()))
			.postalCode(address.getZipCode())
				.gender(switch (person.getGender()) {
					case Gender.M -> "Man";
					case Gender.F -> "Vrouw";
					default -> "X";
				});

	}
       
}
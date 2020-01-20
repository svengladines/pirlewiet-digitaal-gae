package be.pirlewiet.digitaal.repository.impl;

import static com.googlecode.objectify.ObjectifyService.ofy;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.googlecode.objectify.Key;

import be.pirlewiet.digitaal.model.Holiday;
import be.pirlewiet.digitaal.model.Person;
import be.pirlewiet.digitaal.repository.PersonRepository;

public class PersonRepositoryObjectify implements PersonRepository {

	protected final Logger logger 
		= LoggerFactory.getLogger( this.getClass() );

	@Override
	public Person findByUuid(String uuid) {
		//logger.info( "load person with uuid [{}]", uuid );
		return ofy().load().type(Person.class).filter("uuid", uuid).first().now();
	}

	@Override
	public List<Person> findAll() {
		return ofy().load().type(Person.class).list();
	}

	@Override
	public Person saveAndFlush(Person person) {
		//logger.info( "store person with uuid [{}]", new Object[] { person.getUuid() } );
		Key<Person> key = ofy().save().entity( person ).now();
		Person stored = ofy().load().key( key ).now();
		return stored;
	}

	@Override
	public void delete(Person person) {
		// TODO Auto-generated method stub

	}

}

package be.pirlewiet.digitaal.repository.impl;

import static com.googlecode.objectify.ObjectifyService.ofy;

import be.pirlewiet.digitaal.model.Enrollment;
import be.pirlewiet.digitaal.model.Person;
import be.pirlewiet.digitaal.repository.ContactRepository;

public class ContactRepositoryObjectify implements ContactRepository {

	@Override
	public Person findByUuid(String uuid) {
		
		return ofy().load().type(Person.class).filter("uuid", uuid).first().now();
		
	}

}

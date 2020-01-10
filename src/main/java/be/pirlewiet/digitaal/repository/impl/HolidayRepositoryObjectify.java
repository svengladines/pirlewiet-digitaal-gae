package be.pirlewiet.digitaal.repository.impl;

import static com.googlecode.objectify.ObjectifyService.ofy;

import com.googlecode.objectify.Key;

import be.pirlewiet.digitaal.model.Enrollment;
import be.pirlewiet.digitaal.model.Holiday;
import be.pirlewiet.digitaal.repository.HolidayRepository;

public class HolidayRepositoryObjectify implements HolidayRepository {

	@Override
	public Holiday findByUuid(String uuid) {
		return ofy().load().key(Key.create(Holiday.class, uuid)).now();
	}

	@Override
	public Holiday findOneByName(String name) {
		
		return ofy().load().type(Holiday.class).filter("name", name).first().now();
		
	}

}

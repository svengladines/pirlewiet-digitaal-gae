package be.pirlewiet.digitaal.repository.impl;

import static com.googlecode.objectify.ObjectifyService.ofy;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.googlecode.objectify.Key;

import be.pirlewiet.digitaal.model.Enrollment;
import be.pirlewiet.digitaal.model.Holiday;
import be.pirlewiet.digitaal.repository.HolidayRepository;

@Repository
public class HolidayRepositoryObjectify implements HolidayRepository {

	@Override
	public Holiday findByUuid(String uuid) {
		return ofy().load().key(Key.create(Holiday.class, uuid)).now();
	}

	@Override
	public Holiday findOneByName(String name) {
		
		return ofy().load().type(Holiday.class).filter("name", name).first().now();
		
	}

	@Override
	public List<Holiday> findAll() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Holiday saveAndFlush(Holiday holiday) {
		// TODO Auto-generated method stub
		return null;
	}

}

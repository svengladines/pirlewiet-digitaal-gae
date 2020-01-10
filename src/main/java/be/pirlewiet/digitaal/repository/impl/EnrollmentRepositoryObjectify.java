package be.pirlewiet.digitaal.repository.impl;

import static com.googlecode.objectify.ObjectifyService.ofy;

import java.util.List;

import com.googlecode.objectify.Key;

import be.pirlewiet.digitaal.model.Enrollment;
import be.pirlewiet.digitaal.repository.EnrollmentRepository;

public class EnrollmentRepositoryObjectify implements EnrollmentRepository {

	@Override
	public Enrollment findByUuid(String uuid) {
		
		return ofy().load().key(Key.create(Enrollment.class, uuid)).now();
		
	}

	@Override
	public List<Enrollment> findByApplicationUuid(String applicationUuid) {
		
		return ofy().load().type(Enrollment.class).filter("applicationUuid", applicationUuid).list();
		
	}

}

package be.pirlewiet.digitaal.repository.impl;

import static com.googlecode.objectify.ObjectifyService.ofy;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.googlecode.objectify.Key;

import be.pirlewiet.digitaal.model.Enrollment;
import be.pirlewiet.digitaal.repository.EnrollmentRepository;

@Repository
public class EnrollmentRepositoryObjectify implements EnrollmentRepository {

	@Override
	public Enrollment findByUuid(String uuid) {
		
		return ofy().load().key(Key.create(Enrollment.class, uuid)).now();
		
	}

	@Override
	public List<Enrollment> findByApplicationUuid(String applicationUuid) {
		
		return ofy().load().type(Enrollment.class).filter("applicationUuid", applicationUuid).list();
		
	}

	@Override
	public List<Enrollment> findAll() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Enrollment saveAndFlush(Enrollment enrollment) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void delete(Enrollment enrollment) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void deleteAll() {
		// TODO Auto-generated method stub
		
	}

}

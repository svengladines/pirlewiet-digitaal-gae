package be.pirlewiet.digitaal.repository.impl;

import static com.googlecode.objectify.ObjectifyService.ofy;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import com.googlecode.objectify.Key;

import be.pirlewiet.digitaal.model.Enrollment;
import be.pirlewiet.digitaal.model.Holiday;
import be.pirlewiet.digitaal.model.Organisation;
import be.pirlewiet.digitaal.repository.EnrollmentRepository;

@Repository
public class EnrollmentRepositoryObjectify implements EnrollmentRepository {
	
	protected final Logger logger
		= LoggerFactory.getLogger( this.getClass() );

	@Override
	public Enrollment findByUuid(String uuid) {
		
		logger.info( "load enrollment with uuid [{}]", uuid );
		return ofy().load().type(Enrollment.class).filter("uuid", uuid).first().now();
		
	}

	@Override
	public List<Enrollment> findByApplicationUuid(String applicationUuid) {
		
		return ofy().load().type(Enrollment.class).filter("applicationUuid", applicationUuid).list();
		
	}

	@Override
	public List<Enrollment> findAll() {
		logger.info( "load all enrollments");
		return ofy().load().type(Enrollment.class).list();
	}

	@Override
	public Enrollment saveAndFlush(Enrollment enrollment) {
		logger.info( "store enrollment with uuid [{}]", new Object[] { enrollment.getUuid() } );
		Key<Enrollment> key = ofy().save().entity( enrollment ).now();
		Enrollment stored = ofy().load().key( key ).now();
		logger.info( "stored enrollment has id [{}] and uuid [{}]", key.getId(), stored.getUuid() );
		return stored;
	}

	@Override
	public void delete(Enrollment enrollment) {
		ofy().delete().entity( enrollment ).now();
	}

	@Override
	public void deleteAll() {
		// TODO Auto-generated method stub
	}

}

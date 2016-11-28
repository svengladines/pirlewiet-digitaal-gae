package be.pirlewiet.digitaal.domain.people;

import java.util.List;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.appengine.api.datastore.KeyFactory;

import be.pirlewiet.digitaal.model.Enrollment;
import be.pirlewiet.digitaal.repositories.EnrollmentRepository;

/*
 * 
 * 
 */
public class EnrollmentManager {

	protected final Logger logger
		= LoggerFactory.getLogger( this.getClass() );
	
	@Resource
	protected EnrollmentRepository enrollmentRepository;
	
	public EnrollmentManager( ) {
	}
	
	public List<Enrollment> findByApplicationUuid( String applicationUuid ) {
		
		List<Enrollment> enrollments
			= this.enrollmentRepository.findByApplicationUuid( applicationUuid );
		
		// TODO, sort ...
		
		return enrollments;
		
	}
	
	public Enrollment findOneByUuid( String uuid ) {
		return this.enrollmentRepository.findByUuid( uuid );
	}
	
	public Enrollment create( Enrollment toCreate ) {
		
		Enrollment created
			= this.enrollmentRepository.saveAndFlush( toCreate );
		
		created.setUuid( KeyFactory.keyToString( created.getKey() ) );
		created = this.enrollmentRepository.saveAndFlush( created );
		
		return created;
		
	}


	public Enrollment update( Enrollment toUpdate, Enrollment update ) {
		
		// TODO
		toUpdate.setHolidayName( update.getHolidayName() );
		toUpdate.setHolidayUuid( update.getHolidayUuid() );
		if ( update.getStatus() != null ) {
			toUpdate.setStatus( update.getStatus() );
		}
		
		Enrollment updated
			= this.enrollmentRepository.saveAndFlush( toUpdate );
		
		return updated;
		
	}
	
	public void delete( Enrollment enrollment ) {
		
		this.enrollmentRepository.delete( enrollment );
		
	}
	
	public Enrollment template() {
		
		Enrollment enrollment 
			= new Enrollment();
		
		return enrollment;
		
	}
	

}
 
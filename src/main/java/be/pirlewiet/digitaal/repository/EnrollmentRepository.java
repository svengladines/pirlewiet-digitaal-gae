package be.pirlewiet.digitaal.repository;

import java.util.List;

import org.springframework.stereotype.Component;

import be.pirlewiet.digitaal.model.Enrollment;

@Component
public interface EnrollmentRepository {
	
	
	public Enrollment findByUuid( String uuid );
	
	public List<Enrollment> findByApplicationUuid( String applicationUuid );
	public List<Enrollment> findAll();
	
	public Enrollment saveAndFlush( Enrollment enrollment ); 
	
	public void delete( Enrollment enrollment );
	public void deleteAll();
}

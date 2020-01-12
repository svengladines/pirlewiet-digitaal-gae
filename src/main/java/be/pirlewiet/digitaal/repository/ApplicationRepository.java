package be.pirlewiet.digitaal.repository;

import java.util.List;

import org.springframework.stereotype.Component;

import be.pirlewiet.digitaal.model.Application;
import be.pirlewiet.digitaal.model.Enrollment;

@Component
public interface ApplicationRepository {
	
	public Application findByUuid( String uuid );
	
	public List<Application> findByYear( int year );
	public List<Application> findByOrganisationUuid( String organisationUuid );
	public List<Application> findByOrganisationUuidAndYear( String organisationUuid, int year );
	public List<Application> findAll();
	
	public Application saveAndFlush( Application application );
	
	public void delete( Application application );
	public void deleteAll();

}

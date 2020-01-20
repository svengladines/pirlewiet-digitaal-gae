package be.pirlewiet.digitaal.repository.impl;

import static com.googlecode.objectify.ObjectifyService.ofy;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import com.googlecode.objectify.Key;

import be.pirlewiet.digitaal.model.Application;
import be.pirlewiet.digitaal.model.Organisation;
import be.pirlewiet.digitaal.repository.ApplicationRepository;
@Repository
public class ApplicationRepositoryObjectify implements ApplicationRepository {
	
	protected final Logger logger 
		= LoggerFactory.getLogger( this.getClass() );
	
	@Override
	public Application findByUuid(String uuid) {
		
		//logger.info( "load application with uuid [{}]", uuid );
		return ofy().load().type(Application.class).filter("uuid", uuid).first().now();
		
	}

	@Override
	public List<Application> findByYear(int year) {
		
		return ofy().load().type(Application.class).filter("year", year).list();
		
	}

	@Override
	public List<Application> findByOrganisationUuid(String organisationUuid) {
		
		return ofy().load().type(Application.class).filter("organisationuuid", organisationUuid).list();
		
	}
	
	@Override
	public List<Application> findByOrganisationUuidAndYear(String organisationUuid, int year) {

		return ofy().load().type(Application.class).filter("organisationUuid", organisationUuid).filter("year", year).list();
		
	}

	@Override
	public Application saveAndFlush( Application application ) {
		logger.info( "store application with uuid [{}]", new Object[] { application.getUuid() } );
		Key<Application> key = ofy().save().entity( application ).now();
		Application stored = ofy().load().key( key ).now();
		return stored;
	}

	@Override
	public void delete( Application application ) {
		ofy().delete().entity( application ).now();
	}

	@Override
	public void deleteAll() {
		// TODO Auto-generated method stub
	}

	@Override
	public List<Application> findAll() {
		return ofy().load().type(Application.class).list();
	}

}

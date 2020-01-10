package be.pirlewiet.digitaal.repository.impl;

import static com.googlecode.objectify.ObjectifyService.ofy;

import java.util.List;

import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ApplicationEventMulticaster;
import org.springframework.core.ResolvableType;

import com.googlecode.objectify.Key;

import be.pirlewiet.digitaal.model.Address;
import be.pirlewiet.digitaal.model.Application;
import be.pirlewiet.digitaal.repository.ApplicationRepository;

public class ApplicationRepositoryObjectify implements ApplicationRepository {
	
	@Override
	public Application findByUuid(String uuid) {
		
		return ofy().load().key(Key.create(Application.class, uuid)).now();
		
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

		return ofy().load().type(Application.class).filter("organisationuuid", organisationUuid).filter("year", year).list();
		
	}

}

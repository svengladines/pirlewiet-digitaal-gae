package be.pirlewiet.digitaal.domain.service;

import java.util.Arrays;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import be.pirlewiet.digitaal.domain.people.ApplicationManager;
import be.pirlewiet.digitaal.domain.people.DoorMan;
import be.pirlewiet.digitaal.model.Application;
import be.pirlewiet.digitaal.model.Organisation;

@Service
public class ApplicationService {
	
	@Resource
	protected DoorMan doorMan;
	
	@Resource
	ApplicationManager applicationManager;
	
	public Application retrieve( String id, Organisation actor ) {
		return new Application();
	}
	
	public Application create( Application application, Organisation actor ) {
		return application;
	}
	
	public Application update( Application application, Organisation actor ) {
		return application;
	}
	
	public Application delete( Application application, Organisation actor ) {
		return application;
	}
	
	public List<Application> query( Organisation actor ) {
		return Arrays.asList( new Application() );
	}

}

package be.pirlewiet.digitaal.repository.impl;

import com.googlecode.objectify.ObjectifyFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.googlecode.objectify.ObjectifyService;

import be.pirlewiet.digitaal.model.Address;
import be.pirlewiet.digitaal.model.Application;
import be.pirlewiet.digitaal.model.Enrollment;
import be.pirlewiet.digitaal.model.Holiday;
import be.pirlewiet.digitaal.model.Organisation;
import be.pirlewiet.digitaal.model.Person;
import be.pirlewiet.digitaal.model.QuestionAndAnswer;

public class Objectify {
	
	protected final Logger logger
		= LoggerFactory.getLogger( this.getClass() );
	
	public Objectify(ObjectifyFactory factory) {
		ObjectifyService.init(factory);
		ObjectifyService.register( Organisation.class );
		ObjectifyService.register( Address.class );
		ObjectifyService.register( Application.class );
		ObjectifyService.register( QuestionAndAnswer.class );
		ObjectifyService.register( Enrollment.class );
		ObjectifyService.register( Holiday.class );
		ObjectifyService.register( Person.class );
		logger.info( "objectify@pwt initialized");
	}

}

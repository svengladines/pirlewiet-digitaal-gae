package be.pirlewiet.digitaal.repository.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.googlecode.objectify.ObjectifyService;

import be.pirlewiet.digitaal.model.Organisation;

public class Objectify {
	
	protected final Logger logger
		= LoggerFactory.getLogger( this.getClass() );
	
	public Objectify() {
		ObjectifyService.init();
		ObjectifyService.register( Organisation.class );
		logger.info( "objectify@pwt initialized");
	}

}

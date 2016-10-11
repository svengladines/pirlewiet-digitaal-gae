package be.pirlewiet.digitaal.domain.exception;

import be.occam.utils.spring.web.ErrorCode;

@SuppressWarnings("serial")
public class ObjectNotFoundException extends PirlewietException {
	
	public ObjectNotFoundException( ErrorCode errorCode, String message ) {
		super( errorCode, message );
	}
	
	public ObjectNotFoundException( String message ) {
		super( message );
	}

}

package be.pirlewiet.digitaal.domain.exception;

import be.occam.utils.spring.web.ErrorCode;

@SuppressWarnings("serial")
public class IncompleteObjectException extends PirlewietException {
	
	public IncompleteObjectException( ErrorCode errorCode ) {
		super( errorCode );
	}
	
	public IncompleteObjectException( String message ) {
		super( message );
	}

}

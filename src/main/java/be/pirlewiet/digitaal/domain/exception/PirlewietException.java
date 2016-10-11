package be.pirlewiet.digitaal.domain.exception;

import be.occam.utils.spring.web.ErrorCode;

@SuppressWarnings("serial")
public class PirlewietException extends RuntimeException {
	
	protected ErrorCode errorCode;
	
	public PirlewietException( String message ) {
		super( message );
	}
	
	public PirlewietException( ErrorCode errorCode ) {
		super();
		this.errorCode = errorCode;
	}
	
	public PirlewietException( ErrorCode errorCode, String message ) {
		super( message );
		this.errorCode = errorCode;
	}

}

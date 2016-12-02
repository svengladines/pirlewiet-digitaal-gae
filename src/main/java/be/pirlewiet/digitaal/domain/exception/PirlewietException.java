package be.pirlewiet.digitaal.domain.exception;

import be.occam.utils.spring.web.ErrorCode;

@SuppressWarnings("serial")
public class PirlewietException extends RuntimeException {
	
	protected ErrorCode errorCode;
	protected String actor;
	
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
	
	public PirlewietException( String actor, ErrorCode errorCode ) {
		super();
		this.actor = actor;
		this.errorCode = errorCode;
	}

	public ErrorCode getErrorCode() {
		return errorCode;
	}

	public void setErrorCode(ErrorCode errorCode) {
		this.errorCode = errorCode;
	}

}

package be.pirlewiet.registrations.web;

import be.pirlewiet.registrations.domain.exception.ErrorCode;

public class ResultDTO<T> {
	
	public static enum Value {
		OK,
		NOK,
		PARTIAL
	}
	
	protected ErrorCode errorCode;
	protected T object;
	protected Value value;
	
	public ErrorCode getErrorCode() {
		return errorCode;
	}
	
	public void setErrorCode(ErrorCode errorCode) {
		this.errorCode = errorCode;
	}
	
	public T getObject() {
		return object;
	}
	
	public void setObject(T object) {
		this.object = object;
	}

	public Value getValue() {
		return value;
	}

	public void setValue(Value value) {
		this.value = value;
	}
	

}

package be.occam.utils.spring.web;

public class Result<T> {
	
	public static enum Value {
		OK,
		NOK,
		PARTIAL
	}
	
	protected ErrorCode errorCode;
	protected T object;
	protected Value value;
	protected String message;
	
	public Result( ) {
	}
	
	public Result(T t ) {
		this.object = t;
	}
	
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

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	
}

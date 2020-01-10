package be.pirlewiet.digitaal.model;

public class CodeRequest {
	
	public static enum Status {
		INPROGRESS, REJECTED, ERROR, OK
	};

	protected String email;
	protected String code;
	protected Status status;
	
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public Status getStatus() {
		return status;
	}
	public void setStatus(Status status) {
		this.status = status;
	}
	
}

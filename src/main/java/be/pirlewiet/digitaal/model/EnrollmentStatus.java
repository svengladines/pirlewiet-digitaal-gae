package be.pirlewiet.digitaal.model;

public class EnrollmentStatus {

	public enum Value {
   
		TRANSIT, ACCEPTED, CONFIRMED, WAITINGLIST, VISIT, REJECTED, CANCELLED
		
	}
	
	protected Value value;
	
	protected String comment;
	protected Boolean emailMe;
	
	public EnrollmentStatus( ) {
	}
	
	public EnrollmentStatus( Value value ) {
		this.value = value;
	}
	
	public Value getValue() {
		return value;
	}
	public void setValue(Value value) {
		this.value = value;
	}
	public String getComment() {
		return comment;
	}
	public void setComment(String comment) {
		this.comment = comment;
	}

	public Boolean getEmailMe() {
		return emailMe;
	}

	public void setEmailMe(Boolean emailMe) {
		this.emailMe = emailMe;
	}

	@Override
	public String toString() {
		if ( this.value == null ) {
			return "";
		}
		else {
			return this.value.name();
		}
		
	}
	

}

package be.pirlewiet.digitaal.model;

import javax.persistence.Embeddable;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;

@Embeddable
public class ApplicationStatus {

	public enum Value {
   
		DRAFT, SUBMITTED, TRANSIT, COMPLETE, CANCELLED, AUTO
		
	}
	
	@Enumerated(value=EnumType.STRING)
	protected Value value;
	
	protected String comment;
	protected Boolean emailMe;
	
	public ApplicationStatus( ) {
	}
	
	public ApplicationStatus( Value value ) {
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

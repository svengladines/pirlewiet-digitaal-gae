package be.pirlewiet.digitaal.model;

import java.util.Date;

import javax.persistence.Id;

import com.google.appengine.api.datastore.Key;

import be.pirlewiet.digitaal.web.dto.EnrollmentDTO;

public class Enrollment {

	@Id
    private String uuid;
    
    protected String applicationUuid;
    
    protected String participantUuid;
    
    protected String participantName;
    
    protected String holidayUuid;
    
    protected String holidayName;
    
    protected String addressUuid;
    
    private Date createdDate;
    
    private Date modifiedDate;

    private EnrollmentStatus status;
    	
    
    public Enrollment() {
    	this.status = new EnrollmentStatus();
    }
    
	public String getUuid() {
		return uuid;
	}

	public void setUuid(String uuid) {
		this.uuid = uuid;
	}

	public String getParticipantUuid() {
		return participantUuid;
	}

	public void setParticipantUuid(String participantUuid) {
		this.participantUuid = participantUuid;
	}

	public String getParticipantName() {
		return participantName;
	}

	public void setParticipantName(String participantName) {
		this.participantName = participantName;
	}

	public EnrollmentStatus getStatus() {
		return status;
	}

	public void setStatus(EnrollmentStatus status) {
		this.status = status;
	}

	public String getHolidayUuid() {
		return holidayUuid;
	}

	public void setHolidayUuid(String holidayUuid) {
		this.holidayUuid = holidayUuid;
	}

	public String getHolidayName() {
		return holidayName;
	}

	public void setHolidayName(String holidayName) {
		this.holidayName = holidayName;
	}

	public String getApplicationUuid() {
		return applicationUuid;
	}

	public void setApplicationUuid(String applicationUuid) {
		this.applicationUuid = applicationUuid;
	}
	
	
	public String getAddressUuid() {
		return addressUuid;
	}

	public void setAddressUuid(String addressUuid) {
		this.addressUuid = addressUuid;
	}

	public static Enrollment from ( EnrollmentDTO f ) {
		
		Enrollment t
			= new Enrollment();
		
		t.setUuid( f.getUuid() );
		t.setApplicationUuid( f.getApplicationUuid() );
		t.setStatus( f.getStatus() );
		t.setParticipantUuid( f.getParticipantUuid() );
		t.setParticipantName( f.getParticipantName () );
		t.setHolidayUuid( f.getHolidayUuid() );
		t.setHolidayName( f.getHolidayName() );
		t.setAddressUuid( f.getAddressUuid() );
		
		return t;
		
	}
	
}
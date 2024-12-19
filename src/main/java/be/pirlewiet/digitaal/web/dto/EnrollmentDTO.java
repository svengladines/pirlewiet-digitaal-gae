package be.pirlewiet.digitaal.web.dto;

import be.pirlewiet.digitaal.model.Enrollment;
import be.pirlewiet.digitaal.model.EnrollmentStatus;

public class EnrollmentDTO {
	
    protected String uuid;
    protected String applicationUuid;
    protected String participantUuid;
    protected String participantName;
    protected String holidayUuid;
    protected String holidayName;
    protected PersonDTO participant;
    protected AddressDTO address;
    protected String addressUuid;
    
    protected EnrollmentStatus status;
    
	public EnrollmentStatus getStatus() {
		return status;
	}

	public void setStatus(EnrollmentStatus status) {
		this.status = status;
	}

	public String getUuid() {
		return uuid;
	}

	public void setUuid(String uuid) {
		this.uuid = uuid;
	}

	public String getParticipantName() {
		return participantName;
	}

	public void setParticipantName(String participantName) {
		this.participantName = participantName;
	}
	
	public String getParticipantUuid() {
		return participantUuid;
	}

	public void setParticipantUuid(String participantUuid) {
		this.participantUuid = participantUuid;
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
	
	public PersonDTO getParticipant() {
		return participant;
	}

	public void setParticipant(PersonDTO participant) {
		this.participant = participant;
	}
	
	public String getApplicationUuid() {
		return applicationUuid;
	}

	public void setApplicationUuid(String applicationUuid) {
		this.applicationUuid = applicationUuid;
	}
	
	public AddressDTO getAddress() {
		return address;
	}

	public void setAddress(AddressDTO address) {
		this.address = address;
	}
	
	

	public String getAddressUuid() {
		return addressUuid;
	}

	public void setAddressUuid(String addressUuid) {
		this.addressUuid = addressUuid;
	}

	public static EnrollmentDTO from ( Enrollment f ) {
		EnrollmentDTO t = new EnrollmentDTO();
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
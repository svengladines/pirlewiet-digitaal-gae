package be.pirlewiet.digitaal.web.dto;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import be.pirlewiet.digitaal.model.Application;
import be.pirlewiet.digitaal.model.ApplicationStatus;

public class ApplicationDTO {
	
    private String uuid;
    
    protected String reference;
    
    protected String contactPersonUuid;
    protected String contactPersonName;
    
    protected String organisationUuid;
    
    protected String description;
    
    private List<HolidayDTO> holidays
    	= new ArrayList<HolidayDTO>();
    
    protected String holidayUuids;
    protected String holidayNames;
    
    private int year;
    
    protected Date submitted;

    private ApplicationStatus status
    	= new ApplicationStatus();
    
    protected List<EnrollmentDTO> enrollments
    	= new ArrayList<EnrollmentDTO>();

	public String getReference() {
		return reference;
	}

	public void setReference(String reference) {
		this.reference = reference;
	}

	public ApplicationStatus getStatus() {
		return status;
	}

	public void setStatus(ApplicationStatus status) {
		this.status = status;
	}

	public String getUuid() {
		return uuid;
	}

	public void setUuid(String uuid) {
		this.uuid = uuid;
	}

	public int getYear() {
		return year;
	}

	public void setYear(int year) {
		this.year = year;
	}

	public String getOrganisationUuid() {
		return organisationUuid;
	}

	public void setOrganisationUuid(String organisationUuid) {
		this.organisationUuid = organisationUuid;
	}
	
	public String getContactPersonName() {
		return contactPersonName;
	}

	public void setContactPersonName(String contactPersonName) {
		this.contactPersonName = contactPersonName;
	}
	
	public String getContactPersonUuid() {
		return contactPersonUuid;
	}

	public void setContactPersonUuid(String contactPersonUuid) {
		this.contactPersonUuid = contactPersonUuid;
	}
	
	public List<HolidayDTO> getHolidays() {
		return holidays;
	}
	
	public List<EnrollmentDTO> getEnrollments() {
		return enrollments;
	}
	
	public String getHolidayNames() {
		return holidayNames;
	}

	public void setHolidayNames(String holidayNames) {
		this.holidayNames = holidayNames;
	}
	
	public String getHolidayUuids() {
		return holidayUuids;
	}

	public void setHolidayUuids(String holidayUuids) {
		this.holidayUuids = holidayUuids;
	}
	
	public Date getSubmitted() {
		return submitted;
	}

	public void setSubmitted(Date submitted) {
		this.submitted = submitted;
	}

	public static ApplicationDTO from ( Application f ) {
		
		ApplicationDTO t
			= new ApplicationDTO();
		
		t.setUuid( f.getUuid() );
		t.setStatus( f.getStatus() );
		t.setReference( f.getReference() );
		t.setContactPersonUuid( f.getContactPersonUuid() );
		t.setContactPersonName( f.getContactPersonName () );
		t.setHolidayNames( f.getHolidayNames() );
		t.setHolidayUuids( f.getHolidayUuids() );
		t.setSubmitted( f.getSubmitted() );
		
		return t;
		
	}
	
}
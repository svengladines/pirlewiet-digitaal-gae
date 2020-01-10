package be.pirlewiet.digitaal.model;

import java.util.Date;

import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;

import be.pirlewiet.digitaal.web.dto.ApplicationDTO;

/**
 * Application is submitted by the organisation. It has a status, indicates the desired holidays, references the requesting organisation, contact person.
 * 
 * 
 * SGL| We no longer do the one-to-many stuff. Too many entities loaded per fetch ...
 * 
 * @author sven
 *
 */
@Entity
public class Application {
	
	@Id
	private String uuid;
    
    protected String reference;
    
    protected String contactPersonUuid;
    
    protected String contactPersonName;
    
    protected String organisationUuid;
    
    protected String description;
    
    private String holidayNames = "";
    
    private String holidayUuids = "";
    
    private int year;
    
    private Date submitted;
    
    private Date created;
    
    
    private ApplicationStatus status
    	= new ApplicationStatus();
    
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

	public String getContactPersonUuid() {
		return contactPersonUuid;
	}

	public void setContactPersonUuid(String contactPersonUuid) {
		this.contactPersonUuid = contactPersonUuid;
	}

	public String getContactPersonName() {
		return contactPersonName;
	}

	public void setContactPersonName(String contactPersonName) {
		this.contactPersonName = contactPersonName;
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
	
	public Date getCreated() {
		return created;
	}

	public void setCreated(Date created) {
		this.created = created;
	}

	public static Application from ( ApplicationDTO f ) {
		
		Application t
			= new Application();
		
		t.setUuid( f.getUuid() );
		t.setStatus( f.getStatus() );
		t.setReference( f.getReference() );
		t.setContactPersonUuid( f.getContactPersonUuid() );
		t.setContactPersonName( f.getContactPersonName () );
		t.setHolidayNames( f.getHolidayNames() );
		t.setHolidayUuids( f.getHolidayUuids() );
		
		return t;
		
	}
	
}
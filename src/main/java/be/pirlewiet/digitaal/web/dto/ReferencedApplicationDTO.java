package be.pirlewiet.digitaal.web.dto;

import java.util.ArrayList;
import java.util.List;

public class ReferencedApplicationDTO {
    protected String reference;
	protected PersonDTO applicant;

    protected List<EnrollmentDTO> enrollments = new ArrayList<EnrollmentDTO>();
	public String getReference() {
		return reference;
	}
	public void setReference(String reference) {
		this.reference = reference;
	}
	public PersonDTO applicant() {
		return this.applicant;
	}

}
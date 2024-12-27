package be.pirlewiet.digitaal.web.dto;

import java.util.ArrayList;
import java.util.List;

public class ReferencedApplicationDTO {
    protected String reference;
	protected PersonDTO applicant;

	public String getReference() {
		return reference;
	}
	public void setReference(String reference) {
		this.reference = reference;
	}
	public PersonDTO getApplicant() {
		return this.applicant;
	}
	public void setApplicant(PersonDTO applicant) {
		this.applicant = applicant;
	}

}
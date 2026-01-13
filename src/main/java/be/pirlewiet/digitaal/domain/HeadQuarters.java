package be.pirlewiet.digitaal.domain;

import java.util.Date;
import java.util.GregorianCalendar;

public class HeadQuarters {
	
	protected final String email;
	protected final Integer year;
	protected final Date applicationsStartDate;
	
	public HeadQuarters(String email, Integer year) {
		this.email = email;
        this.year = year;
		GregorianCalendar cal = new GregorianCalendar();
		// applications start from 15/1
		cal.set(year,0,15);
		applicationsStartDate = cal.getTime();
    }

	public String getEmail() {
		return email;
	}

	public Integer getYear() {
		return year;
	}

	public Date getApplicationsStartDate() {
		return applicationsStartDate;
	}

	public void initialize() {
	}

}

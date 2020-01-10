/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package be.pirlewiet.digitaal.web.dto;

import be.occam.utils.timing.Timing;
import be.pirlewiet.digitaal.model.Gender;
import be.pirlewiet.digitaal.model.Person;


public class PersonDTO {
	
	protected String uuid;
	private String givenName;
	private String familyName;
	private String phone;
	private String email;
	private String birthDay;
	private Gender gender;
	
	public String getUuid() {
		return uuid;
	}

	public void setUuid(String uuid) {
		this.uuid = uuid;
	}

	public String getGivenName() {
		return givenName;
	}

	public void setGivenName(String givenName) {
		this.givenName = givenName;
	}

	public String getFamilyName() {
		return familyName;
	}

	public void setFamilyName(String familyName) {
		this.familyName = familyName;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}
	
	public String getName() {
		
		return String.format( "%s %s", this.givenName, this.familyName );
		
	}
	
	public String getBirthDay() {
		return birthDay;
	}

	public void setBirthDay(String birthDay) {
		this.birthDay = birthDay;
	}

	public Gender getGender() {
		return gender;
	}

	public void setGender(Gender gender) {
		this.gender = gender;
	}

	public static PersonDTO from( Person f ) {
		
		PersonDTO t
			= new PersonDTO();
		
		t.setUuid( f.getUuid() );
		t.setGivenName( f.getGivenName() );
		t.setFamilyName( f.getFamilyName() );
		t.setPhone( f.getPhone() );
		t.setEmail( f.getEmail() );
		
		if ( f.getBirthDay() != null ) {
			t.setBirthDay( Timing.date( f.getBirthDay() ));
		}
		if ( f.getGender() != null ) {
			t.setGender( f.getGender() );
		}
		
		return t;
		
	}
	
	

}

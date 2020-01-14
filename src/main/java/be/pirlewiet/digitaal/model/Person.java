package be.pirlewiet.digitaal.model;

import static be.occam.utils.javax.Utils.isEmpty;

import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;

import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;
import com.googlecode.objectify.annotation.Index;

import be.occam.utils.timing.Timing;
import be.pirlewiet.digitaal.web.dto.PersonDTO;

@Entity
public class Person {
	
    @Id
    private Long id;
    
    @Index
	private String uuid;
	
	protected String givenName;
	
	protected String familyName;
	
	private String phone;
	
	@Index
	private String email;
	
	@DateTimeFormat(pattern="dd/MM/yyyy")
	private Date birthDay;
	
	private Gender gender;
	
	private String stateNumber;
	
	public Person() {
	}

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

	public Date getBirthDay() {
		return birthDay;
	}

	public void setBirthDay(Date birthDay) {
		this.birthDay = birthDay;
	}

	public Gender getGender() {
		return gender;
	}

	public void setGender(Gender gender) {
		this.gender = gender;
	}
	
	public String getStateNumber() {
		return stateNumber;
	}

	public void setStateNumber(String stateNumber) {
		this.stateNumber = stateNumber;
	}

	public static Person from( PersonDTO f ) {
		
		Person t
			= new Person();
		
		t.setUuid( f.getUuid() );
		t.setGivenName( f.getGivenName() );
		t.setFamilyName( f.getFamilyName() );
		t.setPhone( f.getPhone() );
		t.setEmail( f.getEmail() );
		t.setGender( f.getGender() );	
		t.setStateNumber( f.getStateNumber() );
		if ( ! isEmpty( f.getBirthDay() ) ) {
			t.setBirthDay( Timing.date( f.getBirthDay() ));
		}
		
		return t;
		
	}
	
}
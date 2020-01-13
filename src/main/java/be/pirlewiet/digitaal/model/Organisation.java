package be.pirlewiet.digitaal.model;

import java.util.Date;

import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;

import be.pirlewiet.digitaal.web.dto.OrganisationDTO;

@Entity
public class Organisation {

	@Id
	private long kee;
	
	private String uuid;
	
	protected String name;
	
	protected String addressUuid;
	
	protected String phone;
	protected String cellPhone;
	protected String code;
	protected String email;
	protected String city;
	
	protected Date updated;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getAddressUuid() {
		return this.addressUuid;
	}

	public void setAddressUuid(String uuid) {
		this.addressUuid = uuid;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public Date getUpdated() {
		return updated;
	}

	public void setUpdated(Date updated) {
		this.updated = updated;
	}
	
	public String getUuid() {
		return uuid;
	}

	public void setUuid(String uuid) {
		this.uuid = uuid;
	}
	
	public static Organisation from( OrganisationDTO f ) {
		
		Organisation t
			= new Organisation();
		
		t.setUuid( f.getUuid() );
		t.setName( f.getName() );
		t.setEmail( f.getEmail() );
		t.setPhone( f.getPhone() );
		t.setAddressUuid( f.getAddressUuid() );
		t.setCity( f.getCity() );
		
		return t;
	}

}

package be.pirlewiet.digitaal.web.dto;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import be.pirlewiet.digitaal.model.Organisation;

public class OrganisationDTO {
	
	protected final Logger logger
		= LoggerFactory.getLogger( this.getClass() );
	
	private String uuid;
	protected String name;
	protected String phone;
	protected String code;
	protected String email;
	protected String alternativeEmail;
	protected boolean inComplete;
	protected String addressUuid;
	protected String city;
	protected AddressDTO address;
	
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

	public String getAlternativeEmail() {
		return alternativeEmail;
	}

	public void setAlternativeEmail(String alternativeEmail) {
		this.alternativeEmail = alternativeEmail;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}
	
	public String getUuid() {
		return uuid;
	}

	public void setUuid(String uuid) {
		this.uuid = uuid;
	}
	
	public boolean getInComplete() {
		return inComplete;
	}
	
	public String getAddressUuid() {
		return addressUuid;
	}

	public void setAddressUuid(String addressUuid) {
		this.addressUuid = addressUuid;
	}

	public void setInComplete(boolean inComplete) {
		this.inComplete = inComplete;
	}
	
	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public static OrganisationDTO from( Organisation f ) {
		
		OrganisationDTO t
			= new OrganisationDTO();
		
		t.setUuid( f.getUuid() );
		t.setName( f.getName() );
		t.setEmail( f.getEmail() );
		t.setPhone( f.getPhone() );
		t.setAddressUuid( f.getAddressUuid() );
		t.setCity( f.getCity() );
		t.setCode( f.getCode() );
		
		return t;
	}
	
	
	
}

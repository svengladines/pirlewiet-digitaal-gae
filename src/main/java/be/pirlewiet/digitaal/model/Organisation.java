package be.pirlewiet.digitaal.model;

import java.util.Date;

import be.pirlewiet.digitaal.web.dto.PersonDTO;
import com.googlecode.objectify.annotation.AlsoLoad;
import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;
import com.googlecode.objectify.annotation.Index;

import be.pirlewiet.digitaal.web.dto.OrganisationDTO;

@Entity
public class Organisation {

	@Id
	private Long id;
	
	@Index
	private String uuid;
	
	@Index
	protected String name;
	
	protected String address;
	
	protected String phone;
	protected String cellPhone;
	
	@Index
	protected String code;
	
	@Index
	protected String email;
	protected String city;
	
	protected Date updated;

	@Index
	protected OrganisationType type = OrganisationType.NON_PROFIT;

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
		return this.address;
	}

	public void setAddressUuid(String uuid) {
		this.address = uuid;
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

	public OrganisationType getType() {
		return type;
	}

	public void setType(OrganisationType type) {
		this.type = type;
	}

	public static Organisation from(OrganisationDTO f ) {
		Organisation t = new Organisation();
		t.setUuid( f.getUuid() );
		t.setName( f.getName() );
		t.setEmail( f.getEmail() );
		t.setPhone( f.getPhone() );
		t.setAddressUuid( f.getAddressUuid() );
		if (f.getAddress() != null) {
			t.setCity(f.getAddress().getCity());
		}
		return t;
	}

	public static Organisation fromPerson(PersonDTO f) {
		Organisation t = new Organisation();
		t.setName( f.getName() );
		t.setEmail( f.getEmail() );
		t.setPhone( f.getPhone() );
		return t;
	}

}

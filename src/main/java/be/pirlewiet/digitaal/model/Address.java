/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package be.pirlewiet.digitaal.model;

import com.googlecode.objectify.annotation.AlsoLoad;
import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;
import com.googlecode.objectify.annotation.Index;

import be.pirlewiet.digitaal.web.dto.AddressDTO;

@Entity
public class Address {

	@Id
	private Long key;
	
	@Index
	private String uuid;
	private String street;
	private String number;
	private String zipCode;
	private String city;
	
	@Index
	@AlsoLoad("id")
	protected Long oldId;
	
	public String getZipCode() {
		return zipCode;
	}

	public void setZipCode(String zipCode) {
		this.zipCode = zipCode;
	}

	public String getStreet() {
		return street;
	}

	public void setStreet(String street) {
		this.street = street;
	}

	public String getNumber() {
		return number;
	}

	public void setNumber(String number) {
		this.number = number;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}
	
	public String getUuid() {
		return uuid;
	}

	public void setUuid(String uuid) {
		this.uuid = uuid;
	}
	
	public static Address from( AddressDTO f ) {
		
		Address t
			= new Address();
		
		t.setCity( f.getCity() );
		t.setStreet( f.getStreet() );
		t.setNumber( f.getNumber() );
		t.setZipCode( f.getZipCode() );
		t.setUuid( f.getUuid() );
		
		return t;
		
	}
	
	

}

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package be.pirlewiet.digitaal.web.dto;

import be.pirlewiet.digitaal.model.Address;
import be.pirlewiet.digitaal.model.Organisation;


public class AddressDTO {
	
	protected String uuid;
	private String street = "";
	private String number = "";
	private String zipCode = "";
	private String city = "";
	
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

	public AddressDTO setCity(String city) {
		this.city = city;
		return this;
	}
	
	public String getUuid() {
		return uuid;
	}

	public AddressDTO setUuid(String uuid) {
		this.uuid = uuid;
		return this;
	}

	public static AddressDTO from( Address f ) {
		
		AddressDTO t
			= new AddressDTO();
		
		t.setCity( f.getCity() );
		t.setStreet( f.getStreet() );
		t.setNumber( f.getNumber() );
		t.setZipCode( f.getZipCode() );
		t.setUuid( f.getUuid() );
		
		return t;
		
	}
	
	

}

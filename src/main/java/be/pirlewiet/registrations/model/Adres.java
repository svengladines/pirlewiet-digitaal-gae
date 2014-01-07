/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package be.pirlewiet.registrations.model;

import java.io.Serializable;

import javax.persistence.Embeddable;
import javax.persistence.Transient;

import org.apache.log4j.Logger;

/**
 * 
 * @author bgri978
 */
@SuppressWarnings("serial")
@Embeddable
public class Adres implements Serializable {
	private String straat;
	private String postcode;
	private String gemeente;
	private String nummer;
	
	@Transient
	Logger logger = Logger.getLogger(this.getClass());

	public String getPostcode() {
		return postcode;
	}

	public void setPostcode(String postcode) {
		this.postcode = postcode;
	}

	public String getGemeente() {
		return gemeente;
	}

	public void setGemeente(String gemeente) {
		this.gemeente = gemeente;
	}

	public String getStraat() {
		return straat;
	}

	public void setStraat(String straat) {
		this.straat = straat;
	}

	public String getNummer() {
		return nummer;
	}

	public void setNummer(String nummer) {
		this.nummer = nummer;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append(straat!=null?straat + " ":"");
		sb.append(nummer!=null?nummer + " ,":"");
		sb.append(postcode!=null?postcode + " ":"");
		sb.append(gemeente!=null?gemeente + " ":"");
		return sb.toString();
	}
}

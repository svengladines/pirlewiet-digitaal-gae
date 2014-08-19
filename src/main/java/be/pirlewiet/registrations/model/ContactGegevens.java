package be.pirlewiet.registrations.model;

import javax.persistence.Embeddable;
import javax.xml.bind.annotation.XmlRootElement;

@Embeddable
@XmlRootElement
public class ContactGegevens {
	
	protected String naam;
	protected String telefoonNummer;
	protected String gsmNummer;
	protected String email;
	
	public String getNaam() {
		return naam;
	}
	public void setNaam(String naam) {
		this.naam = naam;
	}
	public String getTelefoonNummer() {
		return telefoonNummer;
	}
	public void setTelefoonNummer(String telefoonNummer) {
		this.telefoonNummer = telefoonNummer;
	}
	public String getGsmNummer() {
		return gsmNummer;
	}
	public void setGsmNummer(String gsmNummer) {
		this.gsmNummer = gsmNummer;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	
}

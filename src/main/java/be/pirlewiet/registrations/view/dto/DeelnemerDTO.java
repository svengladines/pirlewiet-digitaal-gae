package be.pirlewiet.registrations.view.dto;

import java.io.Serializable;
import java.util.Date;

public class DeelnemerDTO implements Serializable {
	private int id;
	private String voornaam;
	private String achternaam;
	private Date geboortedatum;
	private String geslacht;

	public DeelnemerDTO() {
	}

	public DeelnemerDTO(String voornaam, String achternaam, Date geboortedatum,
			String geslacht) {
		super();
		this.voornaam = voornaam;
		this.achternaam = achternaam;
		this.geboortedatum = geboortedatum;
		this.geslacht = geslacht;
	}

	public String getVoornaam() {
		return voornaam;
	}

	public void setVoornaam(String voornaam) {
		this.voornaam = voornaam;
	}

	public String getAchternaam() {
		return achternaam;
	}

	public void setAchternaam(String achternaam) {
		this.achternaam = achternaam;
	}

	public Date getGeboortedatum() {
		return geboortedatum;
	}

	public void setGeboortedatum(Date geboortedatum) {
		this.geboortedatum = geboortedatum;
	}

	public String getGeslacht() {
		return geslacht;
	}

	public void setGeslacht(String geslacht) {
		this.geslacht = geslacht;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

}

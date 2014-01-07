package be.pirlewiet.registrations.model;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@SuppressWarnings("serial")
@Entity
public class Vakantietype implements Serializable {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id;

	private String naam;

	private String displaynaam;

	public long getId() {
		return id;
	}

	public Vakantietype() {
	}

	public Vakantietype(String naam, String displaynaam) {
		this.naam = naam;
		this.displaynaam = displaynaam;
	}

	public String getNaam() {
		return naam;
	}

	public String getDisplaynaam() {
		return displaynaam;
	}

	public void setId(long id) {
		this.id = id;
	}

	public void setNaam(String naam) {
		this.naam = naam;
	}

	public void setDisplaynaam(String displaynaam) {
		this.displaynaam = displaynaam;
	}

	@Override
	public String toString() {
		return this.displaynaam;
	}
}
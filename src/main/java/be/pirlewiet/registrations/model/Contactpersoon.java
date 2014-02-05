package be.pirlewiet.registrations.model;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.ManyToOne;
import javax.persistence.Transient;

@Entity
@SuppressWarnings("serial")
@DiscriminatorValue(value="C") 
public class Contactpersoon extends Persoon implements Serializable {

	private String functie;
	private String email;

	@ManyToOne(cascade = CascadeType.ALL, optional=true)
	private Dienst dienst;

	// @OneToMany(mappedBy="contactpersoon")
	// @OneToMany
	@Transient
	private Set<Inschrijving> aanvragen = new HashSet<Inschrijving>(0);
	
	// Nog in dienst bij 'Dienst' instance of niet?
	private Boolean isPassive = false;

	public Contactpersoon(String voornaam, String familienaam) {
		super(voornaam, familienaam);
	}
	
	public Contactpersoon() {
	}

	public String getFunctie() {
		return functie;
	}

	public String getEmail() {
		return email;
	}

	public void setFunctie(String functie) {
		this.functie = functie;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public Dienst getDienst() {
		return dienst;
	}

	public Set<Inschrijving> getAanvragen() {
		return aanvragen;
	}

	public void setDienst(Dienst dienst) {
		this.dienst = dienst;
	}

	public boolean isPassive() {
		return isPassive;
	}

	public void setPassive(boolean isPassive) {
		this.isPassive = isPassive;
	}
	
	@Override
	public String getVoornaam() {
		return super.getVoornaam();
	}

	@Override
	public String getFamilienaam() {
		return super.getFamilienaam();
	}

	@Override
	public String toString() {
		return getVoornaam() + " " + getFamilienaam();
	}
}
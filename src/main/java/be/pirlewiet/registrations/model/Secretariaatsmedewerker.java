package be.pirlewiet.registrations.model;

import java.io.Serializable;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;

@Entity
@SuppressWarnings("serial")
@DiscriminatorValue(value = "S")
public class Secretariaatsmedewerker extends Persoon implements Serializable {
    @OneToOne
    @JoinColumn(unique=true)
    private Credentials credentials;
    //boolean display in admin table
    
    //Boolean determines if this Secretariaatsmedewerker is shown in the overview. You cannot delete a Secretariaatsmedewerker (it can be linked to other objects) but instead boolean will be then put to false.
    private boolean displayed;

    public Credentials getCredentials() {
        return credentials;
    }

    public void setCredentials(Credentials credentials) {
        this.credentials = credentials;
    }

    public Secretariaatsmedewerker(String voornaam, String familienaam) {
        super(voornaam, familienaam);
    }

    public Secretariaatsmedewerker() {
    }

    public boolean isDisplayed() {
		return displayed;
	}

	public void setDisplayed(boolean displayed) {
		this.displayed = displayed;
	}

	@Override
    public String toString() {
        return getVoornaam() + " " + getFamilienaam();
    }
}
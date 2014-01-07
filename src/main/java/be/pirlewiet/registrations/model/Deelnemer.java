package be.pirlewiet.registrations.model;

import java.io.Serializable;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.ManyToMany;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.apache.ldap.common.util.EqualsBuilder;
import org.apache.ldap.common.util.HashCodeBuilder;

@SuppressWarnings("serial")
@Entity
@DiscriminatorValue(value="D")
public class Deelnemer extends Persoon implements Serializable {

	private String rijksregisternr;
	
//	@Column(nullable = false)
	@Temporal(TemporalType.DATE)
	private Date geboortedatum;
	
	private String gsmnr;

//	@Column(nullable = false)	
	private Geslacht geslacht;
	
	@ManyToMany(mappedBy = "deelnemers")
	private Set<AanvraagInschrijving> inschrijvingen = new HashSet<AanvraagInschrijving>(0);
	
	public Deelnemer(String rijksregisternr, String voornaam, String familienaam, Date geboortedatum) {
		this(voornaam, familienaam, geboortedatum);
		this.rijksregisternr = rijksregisternr;
	}

	public Deelnemer(String voornaam, String familienaam, Date geboortedatum) {
		super(voornaam, familienaam);
		this.geboortedatum = geboortedatum;
	}
	
	public Deelnemer() {
		// TODO Auto-generated constructor stub
	}

	public Geslacht getGeslacht() {
		if (geslacht == null)
			return Geslacht.Onbekend;
		return geslacht;
	}

	public Set<AanvraagInschrijving> getInschrijvingen() {
		return inschrijvingen;
	}


	public void setGeslacht(Geslacht geslacht) {
		this.geslacht = geslacht;
	}

	public void setInschrijvingen(Set<AanvraagInschrijving> inschrijvingen) {
		this.inschrijvingen = inschrijvingen;
	}

	public String getGsmnr() {
		return gsmnr;
	}

	public void setGsmnr(String gsmnr) {
		this.gsmnr = gsmnr;
	}

	public String getRijksregisternr() {
		return rijksregisternr;
	}

	public Date getGeboortedatum() {
		return geboortedatum;
	}

	public void setRijksregisternr(String rijksregisternr) {
		this.rijksregisternr = rijksregisternr;
	}

	public void setGeboortedatum(Date geboortedatum) {
		this.geboortedatum = geboortedatum;
	}
	
	@Override
	public int hashCode() {
		return (isNullOrEmpty(getRijksregisternr()) 
		?   new HashCodeBuilder().append(getFamilienaam()).append(getGeboortedatum()).append(getGeslacht()).append(getVoornaam()).toHashCode()
		: 	new HashCodeBuilder().append(getRijksregisternr()).toHashCode());
	}

	@Override
	public boolean equals(final Object other) {
	    if (this == other){
	        return true;
	    }
	    if (!(other instanceof Deelnemer)) {
	        return false;
	    }
	    Deelnemer castOther = (Deelnemer) other;

	    return !(isNullOrEmpty(getRijksregisternr()) || isNullOrEmpty(castOther.getRijksregisternr()))
		    ? new EqualsBuilder().append(getRijksregisternr(), castOther.getRijksregisternr()).isEquals()
	    	: new EqualsBuilder().
    			append(getFamilienaam(), castOther.getFamilienaam()).
    			append(getGeboortedatum(), castOther.getGeboortedatum()).
    			append(getGeslacht(), castOther.getGeslacht()).
    			append(getVoornaam(), castOther.getVoornaam()).
    			isEquals();
	}

	private boolean isNullOrEmpty(String string) {
		return (string == null || string.isEmpty());
	}
	
	@Override
	public String toString() {
		return "[Deelnemer:"+getVoornaam()+ " " + getFamilienaam() + "]";
	}
}
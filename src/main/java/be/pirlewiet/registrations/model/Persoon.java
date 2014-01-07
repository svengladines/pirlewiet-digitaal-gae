package be.pirlewiet.registrations.model;

import java.io.Serializable;

import javax.persistence.DiscriminatorColumn;
import javax.persistence.DiscriminatorType;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.Table;

import org.apache.ldap.common.util.EqualsBuilder;
import org.apache.ldap.common.util.HashCodeBuilder;

@SuppressWarnings("serial")
@Entity
@Table(name = "PERSOON") 
@Inheritance(strategy=InheritanceType.SINGLE_TABLE) 
@DiscriminatorColumn(name="discriminator", discriminatorType=DiscriminatorType.CHAR)
@DiscriminatorValue(value="P")
public class Persoon implements Serializable {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id;

	private String voornaam;

//	@Column(nullable = false)
	private String familienaam;

	private String telefoonnr;

	
	
	public void setId(long id) {
		this.id = id;
	}

	public long getId() {
		return id;
	}

	public Persoon(String voornaam, String familienaam) {
		this.voornaam = voornaam;
		this.familienaam = familienaam;
	}
	
	public Persoon() {
		// TODO Auto-generated constructor stub
	}

	public String getVoornaam() {
		return voornaam;
	}
	public String getFamilienaam() {
		return familienaam;
	}

	public String getTelefoonnr() {
		return telefoonnr;
	}

	public void setVoornaam(String voornaam) {
		this.voornaam = voornaam;
	}

	public void setFamilienaam(String familienaam) {
		this.familienaam = familienaam;
	}

	public void setTelefoonnr(String telefoonnr) {
		this.telefoonnr = telefoonnr;
	}

	@Override
	public int hashCode() {
		return new HashCodeBuilder().append(getFamilienaam()).append(getVoornaam()).toHashCode();
	}

	@Override
	public boolean equals(Object other) {
	    if (this == other){
	        return true;
	    }
	    if (!(other instanceof Persoon)) {
	        return false;
	    }
	    Persoon castOther = (Persoon) other;

	    return new EqualsBuilder().
    			append(getFamilienaam(), castOther.getFamilienaam()).
    			append(getVoornaam(), castOther.getVoornaam()).
    			isEquals();
	}
}
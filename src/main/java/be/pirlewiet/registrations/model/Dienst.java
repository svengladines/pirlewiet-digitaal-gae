package be.pirlewiet.registrations.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.hibernate.annotations.NamedQueries;
import org.hibernate.annotations.NamedQuery;

@SuppressWarnings("serial")
@Entity
@NamedQueries({ @NamedQuery(name = Dienst.FIND_DIENSTEN_THAT_CONTAINS_STRING, query = "SELECT d FROM Dienst d WHERE (UPPER(d.afdeling) LIKE :searchterm OR UPPER(d.naam) LIKE :searchterm)") })
public class Dienst implements Serializable {

	public static final String FIND_DIENSTEN_THAT_CONTAINS_STRING = "Dienst.findDienstenThatContainsString";

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id;

	private String naam;
	private String telefoonnummer;
	private String faxnummer;
	private String emailadres;
	private String afdeling;

	@OneToMany(cascade = CascadeType.ALL, mappedBy = "dienst")
	private Set<Contactpersoon> contactpersonen = new HashSet<Contactpersoon>(0);

	public Dienst() {
		// TODO Auto-generated constructor stub
	}

	@Embedded
	private Adres adres;
	@OneToOne
	@JoinColumn(unique = true)
	private Credentials credentials;

	public Adres getAdres() {
		return adres;
	}

	public void setAdres(Adres adres) {
		this.adres = adres;
	}

	public String getNaam() {
		return naam;
	}

	public void setNaam(String naam) {
		this.naam = naam;
	}

	public String getTelefoonnummer() {
		return telefoonnummer;
	}

	public void setTelefoonnummer(String telefoonnummer) {
		this.telefoonnummer = telefoonnummer;
	}

	public String getFaxnummer() {
		return faxnummer;
	}

	public void setFaxnummer(String faxnummer) {
		this.faxnummer = faxnummer;
	}

	public String getEmailadres() {
		return emailadres;
	}

	public void setEmailadres(String emailadres) {
		this.emailadres = emailadres;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	/**
	 * Geeft ALLE contactpersonen terug van deze Dienst, ongeacht hun isPassive-boolean.
	 * 
	 * @return
	 */
	public Set<Contactpersoon> getContactpersonen() {
		return contactpersonen;
	}

	public Credentials getCredentials() {
		List<String> roles = new ArrayList<String>();
		roles.add("ROLE_DIENST");
		credentials.setRoles(roles);
		return credentials;
	}

	public void setCredentials(Credentials credentials) {
		this.credentials = credentials;
	}

	public void setContactpersonen(Set<Contactpersoon> contactpersonen) {
		this.contactpersonen = contactpersonen;
	}

	public String getAfdeling() {
		return afdeling;
	}

	public void setAfdeling(String afdeling) {
		this.afdeling = afdeling;
	}

	public void addContactpersoon(Contactpersoon contactpersoon) {
		if (this.getContactpersonen() == null) {
			this.setContactpersonen(new HashSet<Contactpersoon>());
		}
		this.getContactpersonen().add(contactpersoon);
	}

	@Override
	public String toString() {
		ToStringBuilder toStringBuilder = new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE);
		toStringBuilder.append(getId());
		toStringBuilder.append(getNaam());
		toStringBuilder.append(getTelefoonnummer());
		toStringBuilder.append(getFaxnummer());
		toStringBuilder.append(getEmailadres());
		toStringBuilder.append(getAfdeling());

		return toStringBuilder.toString();
	}

}

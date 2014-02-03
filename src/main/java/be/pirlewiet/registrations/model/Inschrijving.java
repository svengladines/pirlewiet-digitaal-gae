package be.pirlewiet.registrations.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.PrePersist;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import org.apache.ldap.common.util.EqualsBuilder;
import org.apache.ldap.common.util.HashCodeBuilder;

@SuppressWarnings("serial")
@Entity
public class Inschrijving implements Serializable {
	
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    private String opmerkingen;

    private String gezinsnummer;

    @ManyToOne(cascade = CascadeType.PERSIST)
    private Contactpersoon contactpersoon;

    @ManyToMany(cascade = CascadeType.ALL,fetch=FetchType.LAZY)
//	@IndexColumn(name="deelnemers_col")
    private List<Deelnemer> deelnemers
    	= new ArrayList<Deelnemer>();

    @Embedded
    private Adres deelnemersAdres;

    // (cascade = CascadeType.PERSIST, optional=false)
    @ManyToOne
    private VakantieProject vakantieproject;

    @Enumerated
    private ContactType contactType;

    @Temporal(TemporalType.DATE)
    private Date inschrijvingsdatum;

    @Enumerated
    private Status status;

    @PrePersist
    public void prePersist() {
        // set default value, by invoking getter(does not get called by jpa, persist).
        getInschrijvingsdatum();
    }

    public Date getInschrijvingsdatum() {
        if (inschrijvingsdatum == null) {
            inschrijvingsdatum = new Date();  // TODAY.
        }
        return inschrijvingsdatum;
    }

    public void setInschrijvingsdatum(Date inschrijvingsdatum) {
        this.inschrijvingsdatum = inschrijvingsdatum;
    }

    public String getOpmerkingen() {
        return opmerkingen;
    }

    public void setOpmerkingen(String opmerkingen) {
        this.opmerkingen = opmerkingen;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public List<Deelnemer> getDeelnemers() {
        return deelnemers;
    }

    public void setDeelnemers(List<Deelnemer> deelnemers) {
        this.deelnemers = deelnemers;
    }

    public void addDeelnemer(Deelnemer deelnemer) {
        if (this.deelnemers == null){
            this.deelnemers = new ArrayList<Deelnemer>();
        }
        this.deelnemers.add(deelnemer);
    }

    public Contactpersoon getContactpersoon() {
        return contactpersoon;
    }

    public void setContactpersoon(Contactpersoon contactpersoon) {
        this.contactpersoon = contactpersoon;
    }

    public String getGezinsnummer() {
        return gezinsnummer;
    }

    public void setGezinsnummer(String gezinsnummer) {
        this.gezinsnummer = gezinsnummer;
    }

    public VakantieProject getVakantieproject() {
        return vakantieproject;
    }

    public void setVakantieproject(VakantieProject vakantieproject) {
        this.vakantieproject = vakantieproject;
    }

    public ContactType getContactType() {
        return contactType;
    }

    public void setContactType(ContactType contactType) {
        this.contactType = contactType;
    }

    public Adres getDeelnemersAdres() {
        return deelnemersAdres;
    }

    public void setDeelnemersAdres(Adres deelnemersAdres) {
        this.deelnemersAdres = deelnemersAdres;
    }

    public Status getStatus() {
		return status;
	}

	public void setStatus(Status status) {
		this.status = status;
	}

	@Override
    public int hashCode() {
        return new HashCodeBuilder().append(getVakantieproject()).
                append(getDeelnemers()).append(getContactpersoon()).toHashCode();
    }

    @Override
    public boolean equals(Object other) {
        if (this == other) {
            return true;
        }
        if (!(other instanceof Inschrijving)) {
            return false;
        }
        Inschrijving castOther = (Inschrijving) other;

        return new EqualsBuilder().append(getVakantieproject(),
                castOther.getVakantieproject()).
                append(getDeelnemers(), castOther.getDeelnemers())
                .append(getContactpersoon(), castOther.getContactpersoon())
                .append(getInschrijvingsdatum(), castOther.getInschrijvingsdatum())
                .isEquals();
    }
}
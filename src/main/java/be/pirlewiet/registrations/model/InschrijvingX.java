package be.pirlewiet.registrations.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlRootElement;

@Entity
@XmlRootElement
public class InschrijvingX {
	
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
    
    protected String opmerking;
    
    @Embedded
    protected ContactGegevens contactGegevens;

    /**
     * GAE does not support Many2Many relationships well. 
     * As long as we do not reverse the relationship, One2Many is OK
     */
    @OneToMany(fetch=FetchType.LAZY)
    protected List<Deelnemer> deelnemers = new ArrayList<Deelnemer>();
    
    @ManyToOne
    protected Organisatie organisatie;
    
    @ManyToOne
    private Vakantie vakantie;
    
    @ManyToOne
    private Vakantie alternatief;

    @Temporal(TemporalType.DATE)
    private Date inschrijvingsdatum;

    @Enumerated
    private Status status;
    
    @Embedded
    protected Adres adres;
    
    @OneToMany(fetch=FetchType.LAZY)
    protected List<Vraag> vragen 
    	= new ArrayList<Vraag>();

    public Date getInschrijvingsdatum() {
        if (inschrijvingsdatum == null) {
            inschrijvingsdatum = new Date();  // TODAY.
        }
        return inschrijvingsdatum;
    }

    public void setInschrijvingsdatum(Date inschrijvingsdatum) {
        this.inschrijvingsdatum = inschrijvingsdatum;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

	public Vakantie getVakantie() {
		return vakantie;
	}

	public void setVakantie(Vakantie vakantie) {
		this.vakantie = vakantie;
	}

	public List<Deelnemer> getDeelnemers() {
		return deelnemers;
	}

	public void setDeelnemers(List<Deelnemer> deelnemers) {
		this.deelnemers = deelnemers;
	}

	public Organisatie getOrganisatie() {
		return organisatie;
	}

	public void setOrganisatie(Organisatie organisatie) {
		this.organisatie = organisatie;
	}

	public ContactGegevens getContactGegevens() {
		return contactGegevens;
	}

	public void setContactGegevens(ContactGegevens contactGegevens) {
		this.contactGegevens = contactGegevens;
	}

	public Adres getAdres() {
		return adres;
	}

	public void setAdres(Adres adres) {
		this.adres = adres;
	}

	public List<Vraag> getVragen() {
		return vragen;
	}

	public String getOpmerking() {
		return opmerking;
	}

	public void setOpmerking(String opmerking) {
		this.opmerking = opmerking;
	}

	public Status getStatus() {
		return status;
	}

	public void setStatus(Status status) {
		this.status = status;
	}

	public Vakantie getAlternatief() {
		return alternatief;
	}

	public void setAlternatief(Vakantie alternatief) {
		this.alternatief = alternatief;
	}
	
	
}
package be.pirlewiet.registrations.model;

import java.io.Serializable;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Temporal;
import javax.persistence.Transient;

import org.apache.ldap.common.util.EqualsBuilder;
import org.apache.ldap.common.util.HashCodeBuilder;

@SuppressWarnings("serial")
@Entity
public class Vakantie implements Serializable {
	
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
    
    private int jaar;
    
    protected String naam;
    
    @Enumerated(value=EnumType.STRING)
    protected Periode periode;

    @Enumerated(value=EnumType.STRING)
    protected VakantieType type;

    @Temporal(javax.persistence.TemporalType.DATE)
    private Date beginDatum;
    @Temporal(javax.persistence.TemporalType.DATE)
    private Date eindDatum;
    @Temporal(javax.persistence.TemporalType.DATE)
    private Date beginInschrijving;
    @Temporal(javax.persistence.TemporalType.DATE)
    private Date eindInschrijving;
    
	private Long maxDeelnemers;
	
	private Long minLeeftijd;

	private Long maxLeeftijd;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Date getEindDatum() {
        return eindDatum;
    }

    public void setEindDatum(Date eindDatum) {
        this.eindDatum = eindDatum;
    }

    public Date getBeginDatum() {
        return beginDatum;
    }
	
    public void setBeginDatum(Date beginDatum) {
        this.beginDatum = beginDatum;
    }

    public VakantieType getType() {
        return type;
    }

    public void setType(VakantieType type) {
        this.type = type;
    }

    public Date getBeginInschrijving() {
        return beginInschrijving;
    }

    public void setBeginInschrijving(Date beginInschrijving) {
        this.beginInschrijving = beginInschrijving;
    }

    public Date getEindInschrijving() {
        return eindInschrijving;
    }

    public void setEindInschrijving(Date eindInschrijving) {
        this.eindInschrijving = eindInschrijving;
    }
    
    public Long getMaxDeelnemers() {
		return maxDeelnemers;
	}

	public void setMaxDeelnemers(Long maxDeelnemers) {
		this.maxDeelnemers = maxDeelnemers;
	}

	public Long getMinLeeftijd() {
		return minLeeftijd;
	}

	public void setMinLeeftijd(Long minLeeftijd) {
		this.minLeeftijd = minLeeftijd;
	}

	public Long getMaxLeeftijd() {
		return maxLeeftijd;
	}

	public void setMaxLeeftijd(Long maxLeeftijd) {
		this.maxLeeftijd = maxLeeftijd;
	}
	
    public Periode getPeriode() {
		return periode;
	}

	public void setPeriode(Periode periode) {
		this.periode = periode;
	}

    @Override
    public String toString() {
        return this.getNaam();
    }

	public int getJaar() {
		return jaar;
	}

	public void setJaar(int jaar) {
		this.jaar = jaar;
	}

	public String getNaam() {
		return naam;
	}

	public void setNaam(String naam) {
		this.naam = naam;
	}
	
	
    
}
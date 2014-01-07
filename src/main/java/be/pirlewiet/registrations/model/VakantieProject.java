package be.pirlewiet.registrations.model;

import java.io.Serializable;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.Temporal;

import org.apache.ldap.common.util.EqualsBuilder;
import org.apache.ldap.common.util.HashCodeBuilder;

@SuppressWarnings("serial")
@Entity
public class VakantieProject implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @OneToOne(targetEntity = Vakantietype.class)
    private Vakantietype vakantietype;

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

    public Vakantietype getVakantietype() {
        return vakantietype;
    }

    public void setVakantietype(Vakantietype vakantietype) {
        this.vakantietype = vakantietype;
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

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(getVakantietype()).append(getBeginDatum()).append(getEindDatum()).toHashCode();
    }

    @Override
    public boolean equals(Object other) {
        if (this == other) {
            return true;
        }
        if (!(other instanceof VakantieProject)) {
            return false;
        }
        VakantieProject castOther = (VakantieProject) other;

        boolean ret = new EqualsBuilder().append(getVakantietype(), castOther.getVakantietype()).append(getBeginDatum(), castOther.getBeginDatum()).append(getEindDatum(), castOther.getEindDatum()).isEquals();
        return ret;
    }

    @Override
    public String toString() {
        DateFormat dfDagMaandJaar = new SimpleDateFormat("d MMMM yyyy");
        String result = vakantietype + " van ";
        if (beginDatum != null) {
            result += dfDagMaandJaar.format(beginDatum) + " t/m ";
        } else {
            result += "null" + " t/m ";
        }
        if (eindDatum != null) {
            result += dfDagMaandJaar.format(eindDatum);
        } else {
            result += "null";
        }
        return result;
    }
}
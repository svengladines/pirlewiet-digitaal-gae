package be.pirlewiet.registrations.model;

import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.DiscriminatorColumn;
import javax.persistence.DiscriminatorType;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.format.annotation.DateTimeFormat;

@Entity
@Table(name = "PERSOON") 
@Inheritance(strategy=InheritanceType.SINGLE_TABLE) 
@DiscriminatorColumn(name="discriminator", discriminatorType=DiscriminatorType.STRING)
public abstract class Persoon {
	
	@Transient
	protected final Logger logger
		= LoggerFactory.getLogger( this.getClass() );
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id;

	protected String voorNaam;
	protected String familieNaam;

	private String telefoonNummer;
	private String mobielNummer;
	private String rijksRegisterNummer;
	private String email;
	
	@Temporal(TemporalType.DATE)
	@DateTimeFormat(pattern="dd/MM/yyyy")
	private Date geboorteDatum;
	
//	@Column(nullable = false)
	@Enumerated(EnumType.STRING)
	private Geslacht geslacht;
	
	@Column(name="ffn")
	private String ffn;
	
	@Embedded
	Adres adres;
	
	public void setId(long id) {
		this.id = id;
	}

	public long getId() {
		return id;
	}

	public Persoon(String voornaam, String familienaam) {
	}
	
	public Persoon() {
	}

	public String getVoorNaam() {
		return voorNaam;
	}

	public void setVoorNaam(String voorNaam) {
		this.voorNaam = voorNaam;
	}

	public String getFamilieNaam() {
		return familieNaam;
	}

	public void setFamilieNaam(String familieNaam) {
		this.familieNaam = familieNaam;
	}

	public String getTelefoonNummer() {
		return telefoonNummer;
	}

	public void setTelefoonNummer(String telefoonNummer) {
		this.telefoonNummer = telefoonNummer;
	}

	public String getMobielNummer() {
		return mobielNummer;
	}

	public void setMobielNummer(String mobielNummer) {
		this.mobielNummer = mobielNummer;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public Date getGeboorteDatum() {
		return geboorteDatum;
	}

	public void setGeboorteDatum(Date geboorteDatum) {
		this.geboorteDatum = geboorteDatum;
	}

	public String getRijksRegisterNummer() {
		return rijksRegisterNummer;
	}

	public void setRijksRegisterNummer(String rijksRegisterNummer) {
		this.rijksRegisterNummer = rijksRegisterNummer;
	}

	public Geslacht getGeslacht() {
		return geslacht;
	}

	public void setGeslacht(Geslacht geslacht) {
		this.geslacht = geslacht;
	}

	public String getFfn() {
		return ffn;
	}

	public void setFfn(String ffn) {
		this.ffn = ffn;
	}

	public Adres getAdres() {
		return adres;
	}

	public void setAdres(Adres adres) {
		this.adres = adres;
	}
	
	

}
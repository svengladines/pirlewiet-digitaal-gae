package be.pirlewiet.registrations.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Transient;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Entity
public class Organisatie {
	
	@Transient
	protected final Logger logger
		= LoggerFactory.getLogger( this.getClass() );
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id;
	
	protected String naam;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getNaam() {
		return naam;
	}

	public void setNaam(String naam) {
		this.naam = naam;
	}
	
}

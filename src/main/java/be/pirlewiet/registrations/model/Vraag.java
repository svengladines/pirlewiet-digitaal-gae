package be.pirlewiet.registrations.model;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.xml.bind.annotation.XmlRootElement;

@Entity
public class Vraag {
	
	public static enum Type {
		YesNo, Text, Area, Label
	}
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id;
	
	protected String tag;
	protected String vraag;
	protected String antwoord;
	
	private Vraag ( ) {
	}
	
	public Vraag ( Type type, String tag, String vraag ) {
		this.type = type;
		this.tag = tag;
		this.vraag = vraag;
	}
	
	@Enumerated(EnumType.STRING)
	protected Type type;
	
	public String getVraag() {
		return vraag;
	}
	
	public void setVraag(String vraag) {
		this.vraag = vraag;
	}
	
	public String getAntwoord() {
		return antwoord;
	}
	
	public void setAntwoord(String antwoord) {
		this.antwoord = antwoord;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getTag() {
		return tag;
	}

	public void setTag(String tag) {
		this.tag = tag;
	}

	public Type getType() {
		return type;
	}

	public void setType(Type type) {
		this.type = type;
	}
	
}

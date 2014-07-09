package be.pirlewiet.registrations.model;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;

@Entity
@DiscriminatorValue(value="D")
public class Deelnemer extends Persoon {
	
	protected enum Rol {
		Kind, Ouder
	}
	
	@Enumerated(EnumType.STRING)
	protected Rol rol;

	@Override
	public String toString() {
		return new StringBuilder( this.getVoorNaam() ).append(" ").append( this.getFamilieNaam() ).toString();
	}
	
}
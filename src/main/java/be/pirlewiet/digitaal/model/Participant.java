package be.pirlewiet.digitaal.model;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity
@DiscriminatorValue(value="P")
public class Participant extends Person {
	
}
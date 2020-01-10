package be.pirlewiet.digitaal.repository;

import java.util.List;

import be.pirlewiet.digitaal.model.Person;

public interface PersonRepository {
	
	public Person findByUuid( String uuid );
	public List<Person> findAll();
	
	public Person saveAndFlush( Person person);
	public void delete( Person person );

}

package be.pirlewiet.registrations.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import be.pirlewiet.registrations.model.Persoon;

public interface PersoonRepository extends JpaRepository<Persoon, Long>{
	
	public Persoon findById( Long id );

}

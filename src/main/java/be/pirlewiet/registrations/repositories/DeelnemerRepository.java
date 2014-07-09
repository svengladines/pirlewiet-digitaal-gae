package be.pirlewiet.registrations.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import be.pirlewiet.registrations.model.Deelnemer;

public interface DeelnemerRepository extends JpaRepository<Deelnemer, Long>{
	
	public Deelnemer findById( Long id );

}

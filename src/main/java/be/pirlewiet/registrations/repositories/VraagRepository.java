package be.pirlewiet.registrations.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import be.pirlewiet.registrations.model.Vraag;

public interface VraagRepository extends JpaRepository<Vraag, Long>{
	
}

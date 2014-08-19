package be.pirlewiet.registrations.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import be.pirlewiet.registrations.model.Organisatie;

public interface OrganisatieRepository extends JpaRepository<Organisatie, Long>{
	
	public Organisatie findById( Long id );

}

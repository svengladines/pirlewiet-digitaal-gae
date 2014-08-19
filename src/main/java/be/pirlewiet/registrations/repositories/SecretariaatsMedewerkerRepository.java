package be.pirlewiet.registrations.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import be.pirlewiet.registrations.model.Deelnemer;
import be.pirlewiet.registrations.model.SecretariaatsMedewerker;

public interface SecretariaatsMedewerkerRepository extends JpaRepository<SecretariaatsMedewerker, Long>{
	
	public SecretariaatsMedewerker findById( Long id );

}

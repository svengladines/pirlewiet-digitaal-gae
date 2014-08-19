package be.pirlewiet.registrations.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;

import be.pirlewiet.registrations.model.InschrijvingX;
import be.pirlewiet.registrations.model.Organisatie;

@Component
public interface InschrijvingXRepository extends JpaRepository<InschrijvingX, Long>{
	
	public InschrijvingX findById( Long id );
	public List<InschrijvingX> findByOrganisatie( Organisatie organisatie );

}

package be.pirlewiet.registrations.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import be.pirlewiet.registrations.model.Periode;
import be.pirlewiet.registrations.model.Vakantie;
import be.pirlewiet.registrations.model.VakantieType;

public interface VakantieRepository extends JpaRepository<Vakantie, Long>{
	
	public Vakantie findById( Long id );
	
	public List<Vakantie> findByJaarAndPeriodeAndType( int jaar, Periode periode, VakantieType type );

}

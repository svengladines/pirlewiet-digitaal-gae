package be.pirlewiet.registrations.repositories;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Query;

import org.springframework.stereotype.Repository;

import be.pirlewiet.registrations.model.Vakantietype;

@Repository
public class VakantietypeRepository extends AbstractRepository<Vakantietype> {
	
	@SuppressWarnings("unchecked")
	public List<Vakantietype> findVakantietypeForNaam(String naam) {
		String query = "SELECT DISTINCT vt FROM Vakantietype vt WHERE vt.naam = :naam ";

		Query q = em.createQuery(query);
		if (naam != null) {
			q.setParameter("naam", naam);
		}
		return new ArrayList<Vakantietype> (q.getResultList());
	}
}

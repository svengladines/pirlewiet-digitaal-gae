package be.pirlewiet.registrations.repositories;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Query;

import org.springframework.stereotype.Repository;

import be.pirlewiet.registrations.model.Deelnemer;
import be.pirlewiet.registrations.model.Dienst;

@Repository
public class DeelnemerRepository extends AbstractRepository<Deelnemer> {

	@SuppressWarnings("unchecked")
	public List<Deelnemer> findDeelnemersByDienstID(Dienst dienst) {
		Query q = em.createQuery("SELECT DISTINCT a.deelnemers FROM AanvraagInschrijving a WHERE a.contactpersoon IN (SELECT c FROM Contactpersoon c WHERE c.dienst = :dienst)");
//		Query q = em.createQuery("SELECT d FROM Deelnemer d JOIN d.inschrijvingen a WHERE a.contactpersoon IN (SELECT c FROM Contactpersoon c WHERE c.dienst = :dienst)");
		q.setParameter("dienst",dienst);
		
		return new ArrayList<Deelnemer>(q.getResultList());
	}
	
	public Deelnemer findIdenticalDeelnemer(String rijksregisternr){
		Query q = em.createQuery("SELECT d FROM Deelnemer d WHERE d.rijksregisternr = :rijksregisternr");
		
		q.setParameter("rijksregisternr", rijksregisternr);
		List<Deelnemer> resultList = q.getResultList();
		Deelnemer result = null;
		
		if (resultList.size() > 0) {
			result = resultList.get(0);
		}
		
		return result;
	}
}

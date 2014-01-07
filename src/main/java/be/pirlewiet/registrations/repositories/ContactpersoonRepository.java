package be.pirlewiet.registrations.repositories;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Query;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Repository;

import be.pirlewiet.registrations.model.Contactpersoon;
import be.pirlewiet.registrations.model.Dienst;

@Repository
public class ContactpersoonRepository extends
		AbstractRepository<Contactpersoon> {
	
	private Logger logger = Logger.getLogger(this.getClass());
	
	public List<Contactpersoon> findByDienst(Dienst dienst) {
		Query q = em.createQuery("SELECT c FROM Contactpersoon c WHERE c.dienst = :dienst");
		q.setParameter("dienst",dienst);
		
		@SuppressWarnings("unchecked")
		List<Contactpersoon> contactpersonen = q.getResultList();
		if (contactpersonen.size() == 0) {
			logger.error("No Contactperson-objects found for this user, please check your database.");
			return new ArrayList<Contactpersoon>();
		}
		return contactpersonen;
	}
	
	public List<Contactpersoon> findActiveByDienst(Dienst dienst) {
		Query q = em.createQuery("SELECT c FROM Contactpersoon c WHERE c.dienst = :dienst AND c.isPassive = false");
		q.setParameter("dienst",dienst);
		
		@SuppressWarnings("unchecked")
		List<Contactpersoon> contactpersonen = q.getResultList();
		logger.info(contactpersonen.size() + " contactpersonen gevonden na call met passive is false...");
		return contactpersonen;
	}
	
	

}

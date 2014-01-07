package be.pirlewiet.registrations.repositories;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Query;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Repository;

import be.pirlewiet.registrations.model.Secretariaatsmedewerker;

@Repository
public class SecretariaatsmedewerkerRepository extends
		AbstractRepository<Secretariaatsmedewerker> {

	private Logger logger = Logger.getLogger(this.getClass());

	public List<Secretariaatsmedewerker> findByNaam(String naam) {
		Query q = em.createQuery("SELECT d from Secretariaatsmedewerker d WHERE d.naam LIKE :naam");
		q.setParameter("naam",naam);
        return new ArrayList<Secretariaatsmedewerker>(q.getResultList());
	}
	
	/**
	 * Find all SecretariaatMedewerker where displayed == 1
	 * @return
	 */
	public List<Secretariaatsmedewerker> findDisplayedSecretariaatMedewerker() {
		Query q = em.createQuery("SELECT d from Secretariaatsmedewerker d WHERE d.displayed = 1");
		return new ArrayList<Secretariaatsmedewerker>(q.getResultList());
	}
	
	public Secretariaatsmedewerker setDisplayedFalse(long id){
		Secretariaatsmedewerker medewerker = find(id);
		medewerker.setDisplayed(false);
		return medewerker;
	}

}

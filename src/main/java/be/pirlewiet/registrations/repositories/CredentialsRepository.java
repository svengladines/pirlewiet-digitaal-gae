package be.pirlewiet.registrations.repositories;

import javax.persistence.Query;

import org.springframework.stereotype.Repository;

import be.pirlewiet.registrations.model.Credentials;

@Repository
public class CredentialsRepository extends AbstractRepository<Credentials> {
	
	public Credentials findCredentialsByUsername(String username) {
		Query q = em.createQuery("SELECT c from Credentials c WHERE c.username LIKE :username");
		q.setParameter("username", username);

		Credentials resultaat = (Credentials) q.getSingleResult();
		return resultaat;
	}
//
//	public List<Credentials> findAllSecretariaatCredentials() {
//		Query q = em.createQuery("SELECT DISTINCT c FROM Credentials c WHERE 'ROLE_SECRETARIAAT' IN ELEMENTS(c.roles) AND 'ROLE_SUPERUSER' NOT IN ELEMENTS(c.roles)");
//		List<Credentials> resultaat = q.getResultList();
//		return resultaat;
//	}
}

package be.pirlewiet.registrations.repositories;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Query;
import javax.persistence.TypedQuery;

import org.springframework.stereotype.Repository;

import be.pirlewiet.registrations.model.Dienst;

@Repository
public class DienstRepository extends AbstractRepository<Dienst> {

	public Dienst findDienstByUsername(String username) {
		Query q = em.createQuery("SELECT d from Dienst d WHERE d.credentials.username LIKE :username");
		q.setParameter("username", username);

		List<Dienst> result = q.getResultList();
		Dienst returnDienst = null;

		if (!result.isEmpty()) {
			returnDienst = result.get(0);
		}

		return returnDienst;
	}

	public List<Dienst> findDienstByName(String dienstname) {
		Query q = em.createQuery("SELECT d from Dienst d WHERE d.naam LIKE :dienstname");
		q.setParameter("dienstname", dienstname);

		return new ArrayList<Dienst>(q.getResultList());
	}

	public Dienst deactiveerDienst(Dienst d) {
		List<String> roles = new ArrayList<String>();
		roles.add("ROLE_DIENST");

		d.getCredentials().setEnabled(false);
		d.getCredentials().setRoles(roles);
		return d;
	}

	public Dienst activeerDienst(Dienst d) {
		List<String> roles = new ArrayList<String>();
		roles.add("ROLE_DIENST");
		d.getCredentials().setEnabled(true);
		d.getCredentials().setRoles(roles);
		return d;
	}

	public List<Dienst> findDienstenThatContainsString(String searchterm) {
		TypedQuery<Dienst> query = em.createNamedQuery(Dienst.FIND_DIENSTEN_THAT_CONTAINS_STRING, Dienst.class)
				.setParameter("searchterm", "%" + searchterm.toUpperCase().trim() + "%");

		return query.getResultList();
	}

}

package be.pirlewiet.registrations.repository;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import be.pirlewiet.registrations.AbstractTransactionalTest;
import be.pirlewiet.registrations.model.Credentials;
import be.pirlewiet.registrations.model.Dienst;
import be.pirlewiet.registrations.repositories.CredentialsRepository;
import be.pirlewiet.registrations.repositories.DienstRepository;


public class DienstRepositoryTest extends AbstractTransactionalTest {

	@Autowired
	private DienstRepository dienstRepository;

	@Autowired
	private CredentialsRepository credentialsRepo;

	Dienst dnst1 = new Dienst();
	Credentials credentials = new Credentials();

	@Before
	public void init() {
		credentials.setUsername("dienst1");
		credentials.setPassword("test");

		dnst1.setCredentials(credentials);
		credentialsRepo.create(credentials);
		dienstRepository.create(dnst1);

		Dienst dienst = new Dienst();
		dienst.setNaam("OCMW Heist-op-den-Berg");
		dienstRepository.create(dienst);

	}

	@Test
	public void findDienstByUsernameTest() {
		Dienst result = dienstRepository.findDienstByUsername("dienst1");

		assertTrue(result != null);
		// assertTrue(result.equals(dnst1));

		result = dienstRepository.findDienstByUsername("notInDB");
		assertTrue(result == null);
	}

	@Test
	public void findDienstByNaam_found() {
		List<Dienst> findDienstByName = dienstRepository.findDienstByName("OCMW Heist-op-den-Berg");

		assertTrue(findDienstByName.size() >= 1);

	}

	@Test
	public void findDienstByNaam_not_found() {
		List<Dienst> findDienstByName = dienstRepository.findDienstByName("onbestaande dienst");

		assertTrue(findDienstByName.size() == 0);

	}

	@Test
	public void activeerDienst() {
		Dienst dienst = dienstRepository.find(1L);
		assertTrue(dienst.getCredentials().isEnabled());

		Dienst newDienst = dienstRepository.deactiveerDienst(dienst);
		assertFalse(newDienst.getCredentials().isEnabled());

	}

	@Test
	public void deactiveerDienst() {
		Dienst dienst = dienstRepository.find(1L);
		Dienst newDienst = dienstRepository.activeerDienst(dienst);

		assertTrue(newDienst.getCredentials().isEnabled());
	}

	@Test
	public void findDienstenThatContainsString1() {

		List<Dienst> list = dienstRepository.findDienstenThatContainsString("Brussel");

		assertTrue(list.size() == 2);

	}

	@Test
	public void findDienstenThatContains_OnlyAfdeling() {
		String param = "afdeling";
		List<Dienst> list = dienstRepository.findDienstenThatContainsString(param);

		assertTrue(list.size() > 0);
		for (Dienst d : list) {

			if (StringUtils.containsIgnoreCase(d.getNaam(), param)) {
				assertTrue(d.getNaam().toUpperCase().contains(param.toUpperCase()));
			} else {
				assertTrue(d.getAfdeling().toUpperCase().contains(param.toUpperCase()));
			}

		}
	}

	@Test
	public void findDienstenThatContains_OnlyNaam() {
		String param = "Antwerpen";
		List<Dienst> list = dienstRepository.findDienstenThatContainsString(param);

		assertTrue(list.size() > 0);
		for (Dienst d : list) {

			if (StringUtils.containsIgnoreCase(d.getNaam(), param)) {
				assertTrue(d.getNaam().toUpperCase().contains(param.toUpperCase()));
			} else {
				assertTrue(d.getAfdeling().toUpperCase().contains(param.toUpperCase()));
			}

		}
	}

	@Test
	public void findDienstenThatContains_Both() {
		String param = "kruis";
		List<Dienst> list = dienstRepository.findDienstenThatContainsString(param);

		assertTrue(list.size() > 0);
		for (Dienst d : list) {

			if (StringUtils.containsIgnoreCase(d.getNaam(), param)) {
				assertTrue(d.getNaam().toUpperCase().contains(param.toUpperCase()));
			} else {
				assertTrue(d.getAfdeling().toUpperCase().contains(param.toUpperCase()));
			}

		}
	}

}

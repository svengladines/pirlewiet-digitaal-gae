package be.pirlewiet.registrations.repository;

import java.util.ArrayList;
import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import be.pirlewiet.registrations.AbstractTransactionalTest;
import be.pirlewiet.registrations.model.Credentials;
import be.pirlewiet.registrations.repositories.CredentialsRepository;


public class CredentialsRepositoryTest extends AbstractTransactionalTest {

	@Autowired
	private CredentialsRepository credentialsRepository;

	Credentials c = new Credentials();
	Credentials c2 = new Credentials();
	Credentials c3 = new Credentials();
	List<String> roles = new ArrayList<String>();
	List<String> roles3 = new ArrayList<String>();

	@Before
	public void init() {

		roles.add("ROLE_SECRETARIAAT");
		c.setUsername("secretariaatsUser1");
		c.setPassword("sec1");
		c.setRoles(roles);
		credentialsRepository.create(c);

		c2.setUsername("secretariaatsUser2");
		c2.setPassword("sec2");
		c2.setRoles(roles);
		credentialsRepository.create(c2);

		roles3.add("ROLE_SOMETHINGELSE");
		c3.setUsername("secretariaatsUser3");
		c3.setPassword("sec3");
		c3.setRoles(roles3);
		credentialsRepository.create(c3);
	}

	@Test
	public void testFindCredentialsByUsername() {
		Credentials c = new Credentials();
		List<String> roles = new ArrayList<String>();
		roles.add("ROLE_TESTROLE");
		c.setUsername("testingUser");
		c.setPassword("testingPassword");
		c.setRoles(roles);
		credentialsRepository.create(c);

		Credentials resultaat = credentialsRepository.findCredentialsByUsername("testingUser");
		Assert.assertEquals(resultaat,c);
		Assert.assertTrue(resultaat.getRoles().contains("ROLE_TESTROLE"));
		}
}

package be.pirlewiet.registrations.repository;

import static org.junit.Assert.assertTrue;

import java.util.List;

import org.apache.log4j.Logger;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import be.pirlewiet.registrations.AbstractTransactionalTest;
import be.pirlewiet.registrations.model.Secretariaatsmedewerker;
import be.pirlewiet.registrations.repositories.SecretariaatsmedewerkerRepository;


public class SecretariaatMedewerkerRepositoryTest extends AbstractTransactionalTest {

	@Autowired
	private SecretariaatsmedewerkerRepository secretariaatMedewerkerRepository;

	
	private Logger logger = Logger.getLogger(this.getClass());

	
	@Before
	public void init(){

	}
	
	@Test
	public void findDisplayedSecretariaatMedewerker() {	
		List<Secretariaatsmedewerker> list = secretariaatMedewerkerRepository.findDisplayedSecretariaatMedewerker();
		
		for(Secretariaatsmedewerker s : list){
			assertTrue(s.isDisplayed());

		}
	}
	

}

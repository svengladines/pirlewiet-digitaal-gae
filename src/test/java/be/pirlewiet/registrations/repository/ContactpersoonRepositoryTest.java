package be.pirlewiet.registrations.repository;

import static org.junit.Assert.assertTrue;

import java.util.List;

import org.apache.log4j.Logger;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import be.pirlewiet.registrations.AbstractTransactionalTest;
import be.pirlewiet.registrations.model.Contactpersoon;
import be.pirlewiet.registrations.model.Dienst;
import be.pirlewiet.registrations.repositories.ContactpersoonRepository;
import be.pirlewiet.registrations.repositories.DienstRepository;


public class ContactpersoonRepositoryTest extends AbstractTransactionalTest {

	@Autowired
	private ContactpersoonRepository contactpersoonRepository;

	@Autowired
	private DienstRepository dienstRepository;
	
	private Logger logger = Logger.getLogger(this.getClass());

	Dienst dnst1 = new Dienst();
	
	Contactpersoon ctp1 = new Contactpersoon();
	Contactpersoon ctp2 = new Contactpersoon();
	
	
	@Before
	public void init(){
		ctp1.setDienst(dnst1);
		ctp1.setVoornaam("ctp1");
		ctp1.setPassive(false);
		
		ctp2.setDienst(dnst1);
		ctp2.setVoornaam("ctp2");
		ctp2.setPassive(true);
		
		contactpersoonRepository.create(ctp1);
		contactpersoonRepository.create(ctp2);
	}
	
	@Test
	public void getActiveContactpersonenByDienstTest() {
		List<Contactpersoon> result = contactpersoonRepository.findByDienst(dnst1);
		assertTrue(result.contains(ctp1));
		assertTrue(result.contains(ctp2));
		assertTrue(result.get(0).getDienst().equals(dnst1));
		assertTrue(result.get(1).getDienst().equals(dnst1));
	}
	
	@Test
	public void noContactsFromDienstTest(){
		Dienst newDnst = new Dienst();
		dienstRepository.create(newDnst);
		
		List<Contactpersoon> result = contactpersoonRepository.findByDienst(newDnst);
		
		assertTrue(result != null);
		assertTrue(result.size() == 0);
	}
	
	@Test
	public void noPassiveContactpersonenInGetContactpersonenByDienst() {
		List<Contactpersoon> result = contactpersoonRepository.findActiveByDienst(dnst1);
		assertTrue(result.contains(ctp1));
		assertTrue(!result.contains(ctp2));
	}
}

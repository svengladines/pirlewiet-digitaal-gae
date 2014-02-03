package be.pirlewiet.registrations.repository;

import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import be.pirlewiet.registrations.AbstractTransactionalTest;
import be.pirlewiet.registrations.model.Inschrijving;
import be.pirlewiet.registrations.model.Contactpersoon;
import be.pirlewiet.registrations.model.Deelnemer;
import be.pirlewiet.registrations.model.Dienst;
import be.pirlewiet.registrations.model.Geslacht;
import be.pirlewiet.registrations.repositories.DeelnemerRepository;
import be.pirlewiet.registrations.repositories.InschrijvingRepository;


public class DeelnemerRepositoryTest extends AbstractTransactionalTest{

	@Autowired
	private DeelnemerRepository deelnemerRepository;
	@Autowired
	private InschrijvingRepository inschrijvingRepository;
	
	Dienst dnst1 = new Dienst();
	Dienst dnst2 = new Dienst();
	
	Contactpersoon ctp1 = new Contactpersoon();
	Contactpersoon ctp2 = new Contactpersoon();
	
	Deelnemer dnr1 = new Deelnemer("John","Doe",new Date());
	Deelnemer dnr2 = new Deelnemer("Jane","Doe",new Date());
	Deelnemer dnr3 = new Deelnemer("John","Foe",new Date());
	Deelnemer dnr4 = new Deelnemer("Jane","Foe",new Date());
	Deelnemer dnr5 = new Deelnemer("John","Boe",new Date());
	Deelnemer compleetIngevuldeDeelnemer = new Deelnemer();
	
	
	Inschrijving inschr1 = new Inschrijving();
	Inschrijving inschr2 = new Inschrijving();
	Inschrijving inschr3 = new Inschrijving();
	Inschrijving inschr4 = new Inschrijving();
	Inschrijving inschr5 = new Inschrijving();
	
	@Before
	public void init(){
		ctp1.setDienst(dnst1);
		ctp2.setDienst(dnst2);
		
		inschr1.setDeelnemers(new ArrayList<Deelnemer>(Arrays.asList(dnr1)));
		inschr1.setContactpersoon(ctp1);
		
		inschr2.setDeelnemers(new ArrayList<Deelnemer>(Arrays.asList(dnr2)));
		inschr2.setContactpersoon(ctp2);
		
		inschr3.setDeelnemers(new ArrayList<Deelnemer>(Arrays.asList(dnr3)));
		inschr3.setContactpersoon(ctp1);
		
		inschr4.setDeelnemers(new ArrayList<Deelnemer>(Arrays.asList(dnr4)));
		inschr4.setContactpersoon(ctp2);
		
		inschr5.setDeelnemers(new ArrayList<Deelnemer>(Arrays.asList(dnr5)));
		inschr5.setContactpersoon(ctp1);
		
		inschrijvingRepository.create(inschr1);
		inschrijvingRepository.create(inschr2);
		inschrijvingRepository.create(inschr3);
		inschrijvingRepository.create(inschr4);
		inschrijvingRepository.create(inschr5);
		
		compleetIngevuldeDeelnemer.setVoornaam("TestVN");
		compleetIngevuldeDeelnemer.setFamilienaam("TestFN");
		compleetIngevuldeDeelnemer.setGeslacht(Geslacht.Man);
		compleetIngevuldeDeelnemer.setGsmnr("0123/456789");
		compleetIngevuldeDeelnemer.setRijksregisternr("200-10 11.22.33");
		compleetIngevuldeDeelnemer.setTelefoonnr("015/987654");
		compleetIngevuldeDeelnemer.setGeboortedatum(new Date());
		compleetIngevuldeDeelnemer.setInschrijvingen(null);
	}
	
	@Test
	public void getDeelnemersPerDienstTest() {
		List<Deelnemer> result = deelnemerRepository.findDeelnemersByDienstID(dnst1);
	
		assertTrue(result.contains(dnr1));
		assertTrue(result.contains(dnr3));
		assertTrue(result.contains(dnr5));
		assertTrue(result.size() == 3);
		
		result = deelnemerRepository.findDeelnemersByDienstID(dnst2);
		
		assertTrue(result.contains(dnr2));
		assertTrue(result.contains(dnr4));
		assertTrue(result.size() == 2);
	}
	
	@Test
	public void createDeelnemerTest() {
		Deelnemer gepersisteerdeDeelnemer = deelnemerRepository.create(compleetIngevuldeDeelnemer);
		
		assertTrue(gepersisteerdeDeelnemer.getId() > 0);
		assertTrue(gepersisteerdeDeelnemer.getVoornaam().equals("TestVN"));
	}
	
	@Test
	public void findDeelnemerWithRrnrTest(){
		Deelnemer gepersisteerdeDeelnemer = deelnemerRepository.create(compleetIngevuldeDeelnemer);
		
		//search for deelnemer with rrnr = rrnr van compleetIngevuldeDeelnemer
		Deelnemer gevondenDeelnemer = deelnemerRepository.findIdenticalDeelnemer("200-10 11.22.33");
		assertTrue(gevondenDeelnemer != null);
		assertTrue(gevondenDeelnemer.equals(gepersisteerdeDeelnemer));
		
		//Searching for not existing participant
		gevondenDeelnemer = deelnemerRepository.findIdenticalDeelnemer("200-10 11.22.00");
		assertTrue(gevondenDeelnemer == null);
	}
	
	@Test
	public void findDeelnemerWithoutRrnrTest(){
		Deelnemer gepersisteerdeDeelnemer = deelnemerRepository.create(compleetIngevuldeDeelnemer);
		
		//Searching for a participant without rrnr returns null
		Deelnemer gevondenDeelnemer = deelnemerRepository.findIdenticalDeelnemer("");
		assertTrue(gevondenDeelnemer == null);
		
		//Searching for a participant without rrnr=null returns null
		gevondenDeelnemer = deelnemerRepository.findIdenticalDeelnemer(null);
		assertTrue(gevondenDeelnemer == null);
	}
}

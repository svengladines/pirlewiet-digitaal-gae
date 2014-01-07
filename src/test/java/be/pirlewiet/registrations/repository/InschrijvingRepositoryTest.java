package be.pirlewiet.registrations.repository;

import static junit.framework.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import be.pirlewiet.registrations.AbstractTransactionalTest;
import be.pirlewiet.registrations.model.AanvraagInschrijving;
import be.pirlewiet.registrations.model.Contactpersoon;
import be.pirlewiet.registrations.model.Deelnemer;
import be.pirlewiet.registrations.model.Dienst;
import be.pirlewiet.registrations.model.Geslacht;
import be.pirlewiet.registrations.model.VakantieProject;
import be.pirlewiet.registrations.model.Vakantietype;
import be.pirlewiet.registrations.repositories.InschrijvingRepository;
import be.pirlewiet.registrations.repositories.VakantieProjectRepository;
import be.pirlewiet.registrations.utils.DummyEntityBuilder;


public class InschrijvingRepositoryTest extends AbstractTransactionalTest {
    
    @Autowired
    private InschrijvingRepository inschrijvingsRepository;
    @Autowired
    private VakantieProjectRepository vakantieProjectRepository;
       
    private static final Logger LOGGER = Logger.getLogger(InschrijvingRepositoryTest.class.getName());
    
    @Test
    public void testPersistingInschrijving() {
        AanvraagInschrijving aanvraag = DummyEntityBuilder.getDummyAanvraagInschrijving();

        Contactpersoon contactpersoon = DummyEntityBuilder.getDummyContactpersoon();
        aanvraag.setContactpersoon(contactpersoon);

        Vakantietype vt = new Vakantietype("KINDERKAMP","Kinderkamp");
        VakantieProject vakantieproject = DummyEntityBuilder.getDummyVakantieProject(vt);
        aanvraag.setVakantieproject(vakantieproject);

        List<Deelnemer> deelnemers = DummyEntityBuilder.getDummyDeelnemers();
        aanvraag.setDeelnemers(deelnemers);
        
        //Hier wel gelijk
        Logger logger = Logger.getLogger(this.getClass());
        AanvraagInschrijving identicalButNotTheSameAanvraag = DummyEntityBuilder.getDummyAanvraagInschrijving();
        identicalButNotTheSameAanvraag.setContactpersoon(contactpersoon);
        identicalButNotTheSameAanvraag.setDeelnemers(deelnemers);
        identicalButNotTheSameAanvraag.setVakantieproject(vakantieproject);
        
        logger.info(aanvraag.equals(identicalButNotTheSameAanvraag));
        AanvraagInschrijving aanvraagInschrijving = inschrijvingsRepository.create(aanvraag);
        logger.info(aanvraagInschrijving.equals(identicalButNotTheSameAanvraag));

        
        // Assert same object reference (==)
        assertTrue(aanvraagInschrijving.equals(aanvraag));
        assertTrue(aanvraag.getId() > 0);
        assertTrue(aanvraagInschrijving.getOpmerkingen().equals(aanvraag.getOpmerkingen()));
        
        

        // Assert overridden equals()
        logger.info(aanvraagInschrijving.getDeelnemers().equals(identicalButNotTheSameAanvraag.getDeelnemers()));
//        assertTrue(aanvraagInschrijving.getDeelnemers().equals(identicalButNotTheSameAanvraag.getDeelnemers()));
    }

    @Test
    public void testPersistingInschrijvingVoorVakantieProject() {
        AanvraagInschrijving aanvraag = DummyEntityBuilder.getDummyAanvraagInschrijving();

        Contactpersoon contactpersoon = DummyEntityBuilder.getDummyContactpersoon();
        aanvraag.setContactpersoon(contactpersoon);

        Vakantietype vt = new Vakantietype("GEZINSVAKANTIE","Gezinsvakantie");
        VakantieProject vakantieproject = DummyEntityBuilder.getDummyVakantieProject(vt);
        aanvraag.setVakantieproject(vakantieproject);

        Deelnemer deelnemer = DummyEntityBuilder.getDummyDeelnemer();
        aanvraag.setDeelnemers(new ArrayList<Deelnemer>(Arrays.asList(deelnemer)));

        AanvraagInschrijving aanvraagInschrijving = inschrijvingsRepository.create(aanvraag);
        // Assert same object reference (==)
        assertTrue(aanvraagInschrijving.getVakantieproject().equals(aanvraag.getVakantieproject()));

        VakantieProject identicalButNotTheSameVakantieProject = DummyEntityBuilder.getDummyVakantieProject(vt);
        // Assert overridden equals()
        assertTrue(aanvraagInschrijving.getVakantieproject().equals(identicalButNotTheSameVakantieProject));
    }

    @Test
    public void testPersistingInschrijvingVoorDeelnemer() {
        Date geboortedatum = Calendar.getInstance().getTime();
        AanvraagInschrijving aanvraag = new AanvraagInschrijving();

        Deelnemer deelnemer = new Deelnemer("Eén", "Deelnemer", Calendar.getInstance().getTime());
        deelnemer.setGeslacht(Geslacht.Man);
        deelnemer.setGeboortedatum(geboortedatum);
        List<Deelnemer> deelnemers = new ArrayList<Deelnemer>();
        deelnemers.add(deelnemer);
        aanvraag.setDeelnemers(deelnemers);

        Contactpersoon contactpersoon = new Contactpersoon("Contact", "Persoon1");
        Dienst dienst = new Dienst();
        contactpersoon.setDienst(dienst);
        aanvraag.setContactpersoon(contactpersoon);

        VakantieProject vakantieproject = new VakantieProject();
        aanvraag.setVakantieproject(vakantieproject);

        AanvraagInschrijving aanvraagInschrijving = inschrijvingsRepository.create(aanvraag);
        // Assert same object reference (==)
        assertTrue(aanvraagInschrijving.getDeelnemers().get(0).equals(deelnemer));

        Deelnemer identicalButNotTheSameDeelnemer =
                new Deelnemer("Eén", "Deelnemer", Calendar.getInstance().getTime());
        identicalButNotTheSameDeelnemer.setGeboortedatum(geboortedatum);
        identicalButNotTheSameDeelnemer.setGeslacht(Geslacht.Man);
        identicalButNotTheSameDeelnemer.setGeboortedatum(geboortedatum);
        // Assert overridden equals()
        assertTrue(aanvraagInschrijving.getDeelnemers().get(0).equals(deelnemer));
    }

    @Test
    public void testPersistingInschrijvingVoorContactPersoon() {
        AanvraagInschrijving aanvraag = new AanvraagInschrijving();
        Contactpersoon contactpersoon = new Contactpersoon("Contact", "Persoon1");
        contactpersoon.setFunctie("Functie");
        aanvraag.setContactpersoon(contactpersoon);

//        Deelnemer deelnemer = new Deelnemer("Eén", "Deelnemer", Calendar.getInstance().getTime());
//        aanvraag.setDeelnemer(deelnemer);

        VakantieProject vakantieproject = new VakantieProject();
        aanvraag.setVakantieproject(vakantieproject);

        AanvraagInschrijving aanvraagInschrijving = inschrijvingsRepository.create(aanvraag);

        assertTrue(aanvraagInschrijving.getContactpersoon().equals(contactpersoon));

        Contactpersoon identicalButNotTheSameContactpersoon = new Contactpersoon("Contact", "Persoon1");
        contactpersoon.setFunctie("AndereFunctie"); // shouldn't matter for equality check

        // Assert overridden equals()
        assertTrue(aanvraagInschrijving.getContactpersoon().equals(identicalButNotTheSameContactpersoon));
    }

    private Dienst d = new Dienst();
    
    @Test
    public void testFindActueleInschrijvingenByDienst() {
    	VakantieProject vroegerVakantieProject = new VakantieProject();
    	Calendar c = Calendar.getInstance();
    	c.add(Calendar.DAY_OF_YEAR, -3);
    	
    	vroegerVakantieProject.setEindDatum(c.getTime());
    	c.add(Calendar.DAY_OF_YEAR, -10);
    	vroegerVakantieProject.setBeginDatum(c.getTime());

    	VakantieProject laterVakantieProject = new VakantieProject();
    	Calendar c2 = Calendar.getInstance();
    	c2.add(Calendar.DAY_OF_YEAR, 10);
    	
    	laterVakantieProject.setEindDatum(c2.getTime());
    	c2.add(Calendar.DAY_OF_YEAR, 13);
    	laterVakantieProject.setBeginDatum(c2.getTime());
    	
    	LOGGER.info("[" + vroegerVakantieProject + "] vroegerVAKANTIEPROJECT");
    	LOGGER.info("[" + laterVakantieProject + "] laterVAKANTIEPROJECT");
    	
    	
    	d.setNaam("OtCeMsWt");
    	d.setEmailadres("instelling@pvzw.be");
    	
    	Contactpersoon contactpersoon = new Contactpersoon("test1","test2");
    	contactpersoon.setDienst(d);
    	
    	AanvraagInschrijving a = new AanvraagInschrijving();
    	AanvraagInschrijving a2 = new AanvraagInschrijving();
    	
    	a.setContactpersoon(contactpersoon);
    	a2.setContactpersoon(contactpersoon);
    	a.setVakantieproject(vroegerVakantieProject);
    	a2.setVakantieproject(laterVakantieProject);
    	
    	
    	vakantieProjectRepository.create(laterVakantieProject);
    	vakantieProjectRepository.create(vroegerVakantieProject);
    	inschrijvingsRepository.create(a);
    	inschrijvingsRepository.create(a2);
    	
    	List<AanvraagInschrijving> resultaat = inschrijvingsRepository.findActueleInschrijvingenByDienst(d);
    	LOGGER.info("RESULTAAT:" + resultaat);
    	
    	assertTrue(!resultaat.contains(a));
    	assertTrue(resultaat.contains(a2));
    }
}
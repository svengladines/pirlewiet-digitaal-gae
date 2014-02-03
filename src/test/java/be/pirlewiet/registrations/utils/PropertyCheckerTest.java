package be.pirlewiet.registrations.utils;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import be.pirlewiet.registrations.model.Inschrijving;
import be.pirlewiet.registrations.model.Adres;
import be.pirlewiet.registrations.model.ContactType;
import be.pirlewiet.registrations.model.Contactpersoon;
import be.pirlewiet.registrations.model.Deelnemer;
import be.pirlewiet.registrations.model.Dienst;
import be.pirlewiet.registrations.utils.PropertyChecker;



public class PropertyCheckerTest {
    
    private static final Logger LOGGER = LoggerFactory.getLogger(PropertyCheckerTest.class);
        
    @Test
    public void testGetAsString(){
        Adres adresDeelnemer = createAdres();
        adresDeelnemer.setGemeente("GemeenteDeelnemer");
        Adres adresDienst = createAdres();
        adresDienst.setGemeente("GemeenteDienst");
        Dienst dienst = createDienst(adresDienst);
        Contactpersoon contactPersoon = createContactpersoon(dienst);
        Deelnemer deelnemer = createDeelnemer();
        Inschrijving aanvraagInschrijving = createAanvraagInschrijving(contactPersoon, deelnemer, adresDeelnemer);        
        String testResult = PropertyChecker.getAsString(aanvraagInschrijving, "contactpersoon.dienst.adres.gemeente", "");
        assertEquals("GemeenteDienst", testResult);        
    }
    
    @Test
    public void testGetAsStringWithAdresDienstNull(){
        Adres adresDeelnemer = createAdres();
        adresDeelnemer.setGemeente("GemeenteDeelnemer");
        Adres adresDienst = null;
        Dienst dienst = createDienst(adresDienst);
        Contactpersoon contactPersoon = createContactpersoon(dienst);
        Deelnemer deelnemer = createDeelnemer();
        Inschrijving aanvraagInschrijving = createAanvraagInschrijving(contactPersoon, deelnemer, adresDeelnemer);        
        String testResult = PropertyChecker.getAsString(aanvraagInschrijving, "contactpersoon.dienst.adres.gemeente", "");
        assertEquals("", testResult);        
    }
    
    @Test
    public void testGetAsStringWithAdresAanvraagInschrijvingNull(){
        Adres adresDeelnemer = null;
        Adres adresDienst = createAdres();
        adresDienst.setGemeente("GemeenteDeelnemer");
        Dienst dienst = createDienst(adresDienst);
        Contactpersoon contactPersoon = createContactpersoon(dienst);
        Deelnemer deelnemer = createDeelnemer();
        Inschrijving aanvraagInschrijving = createAanvraagInschrijving(contactPersoon, deelnemer, adresDeelnemer);        
        String testResult = PropertyChecker.getAsString(aanvraagInschrijving, "deelnemersAdres.straat", "");
        assertEquals("", testResult);  
    }  
       
     private List<Inschrijving> createAanvraagInschrijvingen(Inschrijving aanvraagInschrijving){
        ArrayList<Inschrijving> aanvraagInschrijvingen = new ArrayList<Inschrijving>();
        aanvraagInschrijvingen.add(aanvraagInschrijving);
        
        return aanvraagInschrijvingen;
    }
    
    private Inschrijving createAanvraagInschrijving(Contactpersoon contactPersoon, Deelnemer deelnemer, Adres adresDeelnemers){
        
       Inschrijving aanvraagInschrijving = new Inschrijving();
              
       aanvraagInschrijving.setDeelnemers(new ArrayList<Deelnemer>(Arrays.asList(deelnemer)));     
       aanvraagInschrijving.setContactpersoon(contactPersoon);
       aanvraagInschrijving.setContactType(ContactType.Dienst);
       aanvraagInschrijving.setDeelnemersAdres(adresDeelnemers);
       
       return aanvraagInschrijving;
        
    }
    
    private Deelnemer createDeelnemer(){
       Deelnemer deelnemer = new Deelnemer();
       deelnemer.setVoornaam("Baron");
       deelnemer.setFamilienaam("Van Stiepelsteen");
       deelnemer.setGeboortedatum(new Date());
       
       return deelnemer;
    }
    
    private Adres createAdres(){
       Adres adres = new Adres();
       adres.setStraat("Bommelweg");
       adres.setNummer("12");
       adres.setPostcode("3210");
       adres.setGemeente("Zonnedorp");
       
       return adres;
    }
    
    private Dienst createDienst(Adres adres){
       Dienst dienst = new Dienst();
       dienst.setAdres(adres);
       dienst.setEmailadres("flup@flap.com");
       dienst.setNaam("OCMW Zaffelare");
       dienst.setTelefoonnummer("023456789");
       dienst.setFaxnummer("098765432");
       
       return dienst;
    }
    
    private Contactpersoon createContactpersoon(Dienst dienst){
        Contactpersoon contactPersoon = new Contactpersoon();
       contactPersoon.setVoornaam("Jan");
       contactPersoon.setFamilienaam("Haring");
       contactPersoon.setDienst(dienst);
       return contactPersoon;
    }

}

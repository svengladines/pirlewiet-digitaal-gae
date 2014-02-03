package be.pirlewiet.registrations.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;

import be.pirlewiet.registrations.model.Inschrijving;
import be.pirlewiet.registrations.model.Adres;
import be.pirlewiet.registrations.model.Contactpersoon;
import be.pirlewiet.registrations.model.Deelnemer;
import be.pirlewiet.registrations.model.Dienst;
import be.pirlewiet.registrations.model.VakantieProject;
import be.pirlewiet.registrations.model.Vakantietype;

public class DummyEntityBuilder {
    
    private static final Logger LOGGER = Logger.getLogger(DummyEntityBuilder.class.getName());

    public static Inschrijving getDummyAanvraagInschrijving() {
        Inschrijving aanvraag = new Inschrijving();
        String opmerking = "Lorem ipsum dolor sit amet, consectetur...";
        aanvraag.setOpmerkingen(opmerking);
        aanvraag.setDeelnemersAdres(getDummyAdres());
        return aanvraag;
    }

    public static Contactpersoon getDummyContactpersoon() {
        Contactpersoon contactpersoon = new Contactpersoon("Contact", "Persoon1");
        Dienst dienst = new Dienst();
        contactpersoon.setDienst(dienst);
        return contactpersoon;
    }


    public static Date createDate(int year, int month, int day) {
        String date = year + "/" + month + "/" + day;
        Date thedate = null;
        try {
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy/MM/dd");
            thedate = formatter.parse(date);
        } catch (ParseException e) {
            LOGGER.info(e.toString(), e);
        }
        return thedate;
    }

    public static VakantieProject getDummyVakantieProject(Vakantietype vt) {
        VakantieProject vakantieproject = new VakantieProject();
        vakantieproject.setVakantietype(vt);
        vakantieproject.setBeginDatum(createDate(2000,1,1));
        vakantieproject.setEindDatum(createDate(2010,1,1));
        return vakantieproject;
    }

    private static Adres getDummyAdres(){
       Adres adres = new Adres();
       adres.setStraat("Bommelweg");
       adres.setNummer("12");
       adres.setPostcode("3210");
       adres.setGemeente("Zonnedorp");

       return adres;
    }

    private static Dienst getDummyDienst(Adres adres){
       Dienst dienst = new Dienst();
       dienst.setAdres(adres);
       dienst.setEmailadres("flup@flap.com");
       dienst.setNaam("OCMW Zaffelare");
       dienst.setTelefoonnummer("023456789");
       return dienst;
    }
    public static Deelnemer getDummyDeelnemer() {
        //Deelnemer deelnemer = new Deelnemer("Eén", "Deelnemer", createDate(1977,7,7));
        //deelnemer.setGeslacht(Geslacht.Man);
       Deelnemer deelnemer = new Deelnemer();
       deelnemer.setVoornaam("Baron");
       deelnemer.setFamilienaam("Van Stiepelsteen");
       deelnemer.setGeboortedatum(new Date());
       return deelnemer;
    }
    
    public static List<Deelnemer> getDummyDeelnemers() {
        //Deelnemer deelnemer = new Deelnemer("Eén", "Deelnemer", createDate(1977,7,7));
        //deelnemer.setGeslacht(Geslacht.Man);
       Deelnemer deelnemer = new Deelnemer();
       deelnemer.setVoornaam("Baron");
       deelnemer.setFamilienaam("Van Stiepelsteen");
       deelnemer.setGeboortedatum(new Date());
       return Arrays.asList(deelnemer);
    }
    
}

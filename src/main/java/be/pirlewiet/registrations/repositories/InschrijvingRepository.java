package be.pirlewiet.registrations.repositories;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.Query;

import org.springframework.stereotype.Repository;

import be.pirlewiet.registrations.model.AanvraagInschrijving;
import be.pirlewiet.registrations.model.Adres;
import be.pirlewiet.registrations.model.ContactType;
import be.pirlewiet.registrations.model.Contactpersoon;
import be.pirlewiet.registrations.model.Dienst;
import be.pirlewiet.registrations.model.VakantieProject;
import be.pirlewiet.registrations.model.Vakantietype;

@Repository
public class InschrijvingRepository extends AbstractRepository<AanvraagInschrijving> {
    public List<AanvraagInschrijving> findActueleInschrijvingenByDienst(Dienst dienst) {
        Query q = em.createQuery("SELECT DISTINCT a FROM AanvraagInschrijving a WHERE a.contactpersoon IN (SELECT c FROM Contactpersoon c WHERE c.dienst = :dienst) AND a.vakantieproject.eindDatum >= CURRENT_DATE");
        q.setParameter("dienst", dienst);

        return new ArrayList<AanvraagInschrijving>(q.getResultList());
    }

    @SuppressWarnings("unchecked")
    public List<AanvraagInschrijving> findAanvraagInschrijvingByGezinsNummer(String gezinsnummer) {
        Query q = em.createQuery("SELECT DISTINCT a FROM AanvraagInschrijving a WHERE a.gezinsnummer like :gezinsnummer");
        q.setParameter("gezinsnummer", gezinsnummer);
        return new ArrayList<AanvraagInschrijving>(q.getResultList());
    }

    @SuppressWarnings("unchecked")
    public List<AanvraagInschrijving> findAanvraagInschrijvingByContactVakprojAdrGezinsnrConttypeOpmDatuminschr(Contactpersoon c, VakantieProject vp,Adres a, String gezinsnummer,  ContactType contacttype, String opmerkingen, Date datumInschrijving) {
        Query q = em.createQuery("SELECT DISTINCT a FROM AanvraagInschrijving a WHERE "
                + " a.deelnemersAdres.straat = :adresstraat "
                + " AND a.deelnemersAdres.postcode = :adrespostcode "
                + " AND a.deelnemersAdres.gemeente = :adresgemeente "
                + " AND a.deelnemersAdres.nummer = :adresnr "
                + " AND a.gezinsnummer like :gezinsnummer "
                + " AND a.vakantieproject = :vakproj "
                + " AND a.contactpersoon = :contactpersoon "
                + " AND a.contactType = :contacttype "
                + " AND a.opmerkingen = :opmerkingen "
                + " AND a.inschrijvingsdatum = :datumIn "
                );
        q.setParameter("adresnr", a.getNummer());
        q.setParameter("adresstraat", a.getStraat());
        q.setParameter("adresgemeente", a.getGemeente());
        q.setParameter("adrespostcode", a.getPostcode());
        q.setParameter("vakproj", vp);
        q.setParameter("contactpersoon", c);
        q.setParameter("gezinsnummer", gezinsnummer);
        q.setParameter("contacttype", contacttype);
        q.setParameter("opmerkingen", opmerkingen);
        q.setParameter("datumIn", datumInschrijving);

        return new ArrayList<AanvraagInschrijving>(q.getResultList());
    }


    @SuppressWarnings("unchecked")
    public List<AanvraagInschrijving> findAanvraagInschrijvingByGezinsNummerJaar(String gezinsnummer, int jaar) {
        int centJaar = jaar;
        if ((centJaar > 0) && (centJaar < 100)) {
            centJaar += 2000;
        }
        String jr = String.valueOf(jaar);
        Query q = em.createQuery("SELECT DISTINCT a.AanvraagInschrijving FROM AanvraagInschrijving a WHERE a.gezinsnummer like :gezinsnummer and a.vakantieproject.begindatum >= :beginjaar and a.vakantieproject.einddatum <= :eindjaar");
        q.setParameter("gezinsnummer", gezinsnummer);
        q.setParameter("beginjaar", "01-01-" + jr);
        q.setParameter("eindjaar", "31-12-" + jr);
        return new ArrayList<AanvraagInschrijving>(q.getResultList());
    }

    @SuppressWarnings("unchecked")
    public List<AanvraagInschrijving> findAanvraagInschrijvingByGezinsNummerJaarType(String gezinsnummer, int jaar, Vakantietype vt) {
        int centJaar = jaar;
        if ((centJaar > 0) && (centJaar < 100)) {
            centJaar += 2000;
        }
        String jr = String.valueOf(jaar);
        Query q = em.createQuery("SELECT DISTINCT a.AanvraagInschrijving FROM AanvraagInschrijving a WHERE a.gezinsnummer like :gezinsnummer and a.vakantieproject.begindatum >= :beginjaar and a.vakantieproject.einddatum <= :eindjaar and a.vakantieproject.vakantietype = :vt");
        q.setParameter("gezinsnummer", gezinsnummer);
        q.setParameter("beginjaar", "01-01-" + jr);
        q.setParameter("eindjaar", "31-12-" + jr);
        q.setParameter("vt", vt);
        return new ArrayList<AanvraagInschrijving>(q.getResultList());
    }
}

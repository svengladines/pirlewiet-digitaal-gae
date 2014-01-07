package be.pirlewiet.registrations.services;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import be.pirlewiet.registrations.model.AanvraagInschrijving;
import be.pirlewiet.registrations.model.Adres;
import be.pirlewiet.registrations.model.ContactType;
import be.pirlewiet.registrations.model.Contactpersoon;
import be.pirlewiet.registrations.model.Dienst;
import be.pirlewiet.registrations.model.VakantieProject;
import be.pirlewiet.registrations.repositories.InschrijvingRepository;

@Service
@Transactional(propagation = Propagation.REQUIRED)
public class InschrijvingService {

	@Autowired
	private InschrijvingRepository inschrijvingRepository;

	public AanvraagInschrijving createInschrijving(AanvraagInschrijving aanvraagInschrijving) {
		AanvraagInschrijving i = inschrijvingRepository.create(aanvraagInschrijving);
		return i;
	}

	public AanvraagInschrijving updateInschrijving(AanvraagInschrijving aanvraagInschrijving) {
		AanvraagInschrijving i = inschrijvingRepository.update(aanvraagInschrijving);
		return i;
	}

	public AanvraagInschrijving findInschrijvingById(long id) {
		AanvraagInschrijving i = inschrijvingRepository.find(id);
		return i;
	}
    public List<AanvraagInschrijving>  findInschrijvingByContactVakprojAdrGezinsnrConttypeOpmDatuminschr(Contactpersoon c, VakantieProject vp,Adres a, String gezinsnummer, ContactType contacttype, String opmerkingen, Date datumInschrijving) {
		List<AanvraagInschrijving>  i = inschrijvingRepository.findAanvraagInschrijvingByContactVakprojAdrGezinsnrConttypeOpmDatuminschr(c,vp,a,gezinsnummer,contacttype,opmerkingen,datumInschrijving);
		return i;
	}

	public List<AanvraagInschrijving> findInschrijvingenByGezinsNummer(String gezinsnummer) {
		List<AanvraagInschrijving> il = inschrijvingRepository.findAanvraagInschrijvingByGezinsNummer(gezinsnummer);
		return il;
	}
	public List<AanvraagInschrijving> findInschrijvingenByGezinsNummerJaar(String gezinsnummer,int jaar) {
		List<AanvraagInschrijving> il = inschrijvingRepository.findAanvraagInschrijvingByGezinsNummerJaar(gezinsnummer,jaar);
		return il;
	}

	public List<AanvraagInschrijving> getInschrijvingen() {
		return inschrijvingRepository.findAll();
	}

	public List<AanvraagInschrijving> findActueleInschrijvingenByDienst(Dienst dienst) {
		return inschrijvingRepository.findActueleInschrijvingenByDienst(dienst);
	}


}

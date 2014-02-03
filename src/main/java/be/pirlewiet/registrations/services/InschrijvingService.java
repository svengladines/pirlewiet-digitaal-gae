package be.pirlewiet.registrations.services;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import be.pirlewiet.registrations.model.Adres;
import be.pirlewiet.registrations.model.ContactType;
import be.pirlewiet.registrations.model.Contactpersoon;
import be.pirlewiet.registrations.model.Deelnemer;
import be.pirlewiet.registrations.model.Dienst;
import be.pirlewiet.registrations.model.Inschrijving;
import be.pirlewiet.registrations.model.Status;
import be.pirlewiet.registrations.model.VakantieProject;
import be.pirlewiet.registrations.repositories.InschrijvingRepository;

@Service
@Transactional(propagation = Propagation.REQUIRED)
public class InschrijvingService {

	@Autowired
	private InschrijvingRepository inschrijvingRepository;
	
	@Autowired
	private VakantieProjectService vakantieProjectService;
	
	@Autowired
	private DeelnemerService deelnemerService;

	public Inschrijving create(Inschrijving inschrijving) {
		inschrijving.setStatus( Status.NIEUW );
		Inschrijving i = inschrijvingRepository.create(inschrijving);
		return i;
	}
	
	public Inschrijving update(Inschrijving inschrijving) {
		Inschrijving i = inschrijvingRepository.update(inschrijving);
		return i;
	}

	@Transactional(readOnly=false)
	public Inschrijving updateVakantie(long id, long vakantieId ) {
		Inschrijving i = this.findInschrijvingById( id );
		VakantieProject vakantie = this.vakantieProjectService.findVakantieProjectById( vakantieId );
		i.setVakantieproject( vakantie );
		i.setDeelnemers( new ArrayList<Deelnemer>() );
		i.setStatus( Status.VAKANTIEGEKOZEN );
		return this.inschrijvingRepository.update( i );
	}
	
	@Transactional(readOnly=false)
	public Inschrijving addDeelnemer(long id, long deelnemerId ) {
		Inschrijving i = this.findInschrijvingById( id );
		Deelnemer deelnemer = this.deelnemerService.find( deelnemerId );
		i.getDeelnemers().add( deelnemer );
		i.setStatus( Status.DEELNEMERTOEGEVOEGD );
		return this.inschrijvingRepository.update( i );
	}

	public Inschrijving findInschrijvingById(long id) {
		Inschrijving i = inschrijvingRepository.find(id);
		return i;
	}
    public List<Inschrijving>  findInschrijvingByContactVakprojAdrGezinsnrConttypeOpmDatuminschr(Contactpersoon c, VakantieProject vp,Adres a, String gezinsnummer, ContactType contacttype, String opmerkingen, Date datumInschrijving) {
		List<Inschrijving>  i = inschrijvingRepository.findAanvraagInschrijvingByContactVakprojAdrGezinsnrConttypeOpmDatuminschr(c,vp,a,gezinsnummer,contacttype,opmerkingen,datumInschrijving);
		return i;
	}

	public List<Inschrijving> findInschrijvingenByGezinsNummer(String gezinsnummer) {
		List<Inschrijving> il = inschrijvingRepository.findAanvraagInschrijvingByGezinsNummer(gezinsnummer);
		return il;
	}
	public List<Inschrijving> findInschrijvingenByGezinsNummerJaar(String gezinsnummer,int jaar) {
		List<Inschrijving> il = inschrijvingRepository.findAanvraagInschrijvingByGezinsNummerJaar(gezinsnummer,jaar);
		return il;
	}

	public List<Inschrijving> getInschrijvingen() {
		return inschrijvingRepository.findAll();
	}

	public List<Inschrijving> findActueleInschrijvingenByDienst(Dienst dienst) {
		return inschrijvingRepository.findActueleInschrijvingenByDienst(dienst);
	}


}

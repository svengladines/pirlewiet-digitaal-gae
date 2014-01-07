package be.pirlewiet.registrations.services;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import be.pirlewiet.registrations.model.Contactpersoon;
import be.pirlewiet.registrations.model.Dienst;
import be.pirlewiet.registrations.repositories.ContactpersoonRepository;
import be.pirlewiet.registrations.repositories.DienstRepository;

@Service
@Transactional(propagation = Propagation.REQUIRED)
public class DienstService {

	private static final Logger LOG = LoggerFactory.getLogger(DienstService.class);

	@Autowired
	private DienstRepository dienstRepository;

	@Autowired
	private CredentialsService credentialsService;

	@Autowired
	private ContactpersoonRepository contactpersoonRepository;

	public List<Contactpersoon> findByDienst(Dienst dienst) {
		return contactpersoonRepository.findActiveByDienst(dienst);
	}

	public Dienst findDienstById(Long id) {
		Dienst dienst = dienstRepository.find(id);
		LOG.debug("findDienstById: {}", dienst.toString());

		return dienst;
	}

	public Dienst findDienstByUsername(String username) {
		return dienstRepository.findDienstByUsername(username);
	}

	public List<Dienst> findDienstByName(String dienstname) {
		return dienstRepository.findDienstByName(dienstname);
	}

	public Dienst getLoggedInDienst() {
		String username = credentialsService.getLoggedInUsername();
		Dienst findDienstByUsername = dienstRepository.findDienstByUsername(username);

		return findDienstByUsername;
	}

	public Dienst create(Dienst dienst) {
		return dienstRepository.create(dienst);
	}

	public Dienst update(Dienst dienst) {
		return dienstRepository.update(dienst);
	}

	public Dienst updateDienst(Dienst dienst) {
		Dienst d = dienstRepository.update(dienst);
		return d;
	}

	public List<Dienst> getAllDiensten() {
		return dienstRepository.findAll();
	}

	public Dienst activeerDienst(Dienst d) {
		return dienstRepository.activeerDienst(d);
	}

	public Dienst deactiveerDienst(Dienst d) {
		return dienstRepository.deactiveerDienst(d);
	}

	public List<Dienst> findDienstenThatContainsString(String searchterm) {
		return dienstRepository.findDienstenThatContainsString(searchterm);
	}
}

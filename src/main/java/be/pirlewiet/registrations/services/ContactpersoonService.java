package be.pirlewiet.registrations.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import be.pirlewiet.registrations.model.Contactpersoon;
import be.pirlewiet.registrations.repositories.ContactpersoonRepository;

@Service
@Transactional(propagation=Propagation.REQUIRED)
public class ContactpersoonService {

	@Autowired
	private ContactpersoonRepository contactpersoonRepository;

	public Contactpersoon findById(long id) {
		return contactpersoonRepository.find(id);
	}

	public Contactpersoon findByName(long id) {
		return contactpersoonRepository.find(id);
	}

	public Contactpersoon create(Contactpersoon c) {
		return contactpersoonRepository.create(c);
	}
	public Contactpersoon update(Contactpersoon c) {
		return contactpersoonRepository.update(c);
	}
}

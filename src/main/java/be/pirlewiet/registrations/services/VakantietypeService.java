package be.pirlewiet.registrations.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import be.pirlewiet.registrations.model.Vakantietype;
import be.pirlewiet.registrations.repositories.VakantietypeRepository;

@Service
@Transactional(propagation = Propagation.SUPPORTS)
public class VakantietypeService {
	@Autowired
	private VakantietypeRepository vakantietypeRepository;

	public List<Vakantietype> getAllVakantietypes() {
		return vakantietypeRepository.findAll();
	}

	public Vakantietype findVakantietypeById(long id) {
		return vakantietypeRepository.find(id);
	}

	public Vakantietype createVakantieProject(Vakantietype vakantietype) {
		Vakantietype i = vakantietypeRepository.create(vakantietype);
		return i;
	}
	
	public List<Vakantietype> findVakantietypeForNaam(String naam) {
		return vakantietypeRepository.findVakantietypeForNaam(naam);
	}
}

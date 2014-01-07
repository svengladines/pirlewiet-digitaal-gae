package be.pirlewiet.registrations.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import be.pirlewiet.registrations.model.Deelnemer;
import be.pirlewiet.registrations.model.Dienst;
import be.pirlewiet.registrations.repositories.DeelnemerRepository;

@Service
@Transactional(propagation = Propagation.REQUIRED)
public class DeelnemerService {
	@Autowired
	private DeelnemerRepository deelnemerRepository;

	public List<Deelnemer> getDeelnemersByDienst(Dienst dienst) {
		return deelnemerRepository.findDeelnemersByDienstID(dienst);
	}

	public Deelnemer findDienstById(long deelnemerID) {
		return deelnemerRepository.find(deelnemerID);
	}

	public Deelnemer find(long deelnemerid) {
		return deelnemerRepository.find(deelnemerid);
	}
	
	public Deelnemer create(Deelnemer deelnemer) {
		return deelnemerRepository.create(deelnemer);
	}
	
	public Deelnemer findIdenticalDeelnemer(String rijksregisternr){
		return deelnemerRepository.findIdenticalDeelnemer(rijksregisternr);
	}
}

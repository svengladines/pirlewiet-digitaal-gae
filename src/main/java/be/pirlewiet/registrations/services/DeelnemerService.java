package be.pirlewiet.registrations.services;

import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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
	
	public Set<Deelnemer> findSmart(String q) {
		
		String normalized
			= q.trim().replaceAll("-","").replaceAll("'", "").toLowerCase();
		
		Set<Deelnemer> deelnemers
			= new HashSet<Deelnemer>();
		
		boolean byRRN
			= false;
		
		try {
			Integer.parseInt( normalized );
			byRRN = true;
		}
		catch( NumberFormatException e ) {
			// not a number, no search by RRN
		}
		
		if ( byRRN ) {
			deelnemers.add( this.deelnemerRepository.findOneByRRN( normalized ) );
		}
		else {
			List<Deelnemer> list
				= this.deelnemerRepository.findByFlatFamilyName( normalized );
			deelnemers.addAll( list );
		}
		
		return deelnemers;
		
	}
	
	public Deelnemer create(Deelnemer deelnemer) {
		return deelnemerRepository.create(deelnemer);
	}

}

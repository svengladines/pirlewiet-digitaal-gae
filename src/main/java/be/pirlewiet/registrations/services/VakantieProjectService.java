package be.pirlewiet.registrations.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import be.pirlewiet.registrations.model.VakantieProject;
import be.pirlewiet.registrations.repositories.VakantieProjectRepository;

@Service
@Transactional(propagation = Propagation.REQUIRES_NEW)
public class VakantieProjectService {
	@Autowired
	private VakantieProjectRepository vakantieProjectRepository;

	public List<VakantieProject>  getAllVakantieProjecten() {
		return vakantieProjectRepository.findAll();
	}

	public VakantieProject findVakantieProjectById(long id) {
		return vakantieProjectRepository.find(id);
	}

        public List<VakantieProject> findVakantieProjectWithBegindateEinddateVakantietype(VakantieProject vp) {
		return vakantieProjectRepository.findVakantieProjectWithBegindateEinddateVakantietype(vp);
	}

	public VakantieProject createVakantieProject(VakantieProject vakantieProject) {
		VakantieProject i = vakantieProjectRepository.create(vakantieProject);
		return i;
	}
}

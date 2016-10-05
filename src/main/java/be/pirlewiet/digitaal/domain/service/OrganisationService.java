package be.pirlewiet.digitaal.domain.service;

import java.util.Arrays;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import be.pirlewiet.digitaal.domain.people.DoorMan;
import be.pirlewiet.digitaal.domain.people.OrganisationManager;
import be.pirlewiet.digitaal.dto.OrganisationDTO;
import be.pirlewiet.digitaal.model.Organisation;

@Service
public class OrganisationService extends be.pirlewiet.digitaal.domain.service.Service<OrganisationDTO,Organisation> {
	
	@Resource
	protected DoorMan doorMan;
	
	@Resource
	OrganisationManager organisationManager;
	
	@Override
	public OrganisationService guard() {
		super.guard();
		return this;
	}

	@Override
	public OrganisationDTO create(OrganisationDTO dto, OrganisationDTO actor) {
		return super.create(dto, actor);
	}

	@Override
	public OrganisationDTO update(OrganisationDTO organisation,
			OrganisationDTO actor) {
		return super.update(organisation, actor);
	}

	@Override
	public OrganisationDTO delete(OrganisationDTO organisation,
			OrganisationDTO actor) {
		return super.delete(organisation, actor);
	}

	@Override
	public List<OrganisationDTO> query(OrganisationDTO actor) {
		return super.query(actor);
	}	

}

package be.pirlewiet.digitaal.domain.service;

import static be.occam.utils.javax.Utils.*;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import be.occam.utils.spring.web.Result;
import be.occam.utils.spring.web.Result.Value;
import be.pirlewiet.digitaal.domain.people.ApplicationManager;
import be.pirlewiet.digitaal.domain.people.DoorMan;
import be.pirlewiet.digitaal.dto.ApplicationDTO;
import be.pirlewiet.digitaal.model.Application;
import be.pirlewiet.digitaal.model.Organisation;

@Service
public class ApplicationService extends be.pirlewiet.digitaal.domain.service.Service<ApplicationDTO,Application> {
	
	@Resource
	protected DoorMan doorMan;
	
	@Resource
	ApplicationManager applicationManager;
	
	@Override
	public ApplicationService guard() {
		super.guard();
		return this;
	}

	@Override
	@Transactional(readOnly=true)
	public Result<List<ApplicationDTO>> query( Organisation actor ) {
		
		Result<List<ApplicationDTO>> result
			= new Result<List<ApplicationDTO>>();
		
		List<Application> applications
			= this.guard().applicationManager.findByOrganisation( actor );
		
		List<ApplicationDTO> dtos
			= list();
		
		for ( Application application : applications ) {
			ApplicationDTO dto
				= ApplicationDTO.from( application );
			this.extend( dto );
			dtos.add ( dto );
		}
		
		result.setValue( Value.OK );
		result.setObject( dtos );
		
		return result;
		
	}
	
	protected void extend( ApplicationDTO dto ) {
		// TODO
	}
	
	
	
}

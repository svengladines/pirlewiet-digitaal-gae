package be.pirlewiet.digitaal.domain.service;

import static be.occam.utils.javax.Utils.list;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import be.occam.utils.spring.web.Result;
import be.occam.utils.spring.web.Result.Value;
import be.pirlewiet.digitaal.domain.people.DoorMan;
import be.pirlewiet.digitaal.domain.people.EnrollmentManager;
import be.pirlewiet.digitaal.dto.ApplicationDTO;
import be.pirlewiet.digitaal.dto.EnrollmentDTO;
import be.pirlewiet.digitaal.model.Enrollment;
import be.pirlewiet.digitaal.model.Organisation;

@Service
public class EnrollmentService extends be.pirlewiet.digitaal.domain.service.Service<EnrollmentDTO,Enrollment> {
	
	@Resource
	protected DoorMan doorMan;
	
	@Resource
	EnrollmentManager enrollmentManager;
	
	@Override
	public EnrollmentService guard() {
		super.guard();
		return this;
	}

	@Transactional(readOnly=true)
	public Result<List<EnrollmentDTO>> query( String applicationUuid, Organisation actor ) {
		
		Result<List<EnrollmentDTO>> result
			= new Result<List<EnrollmentDTO>>();
		
		List<Enrollment> enrollments
			= this.guard().enrollmentManager.findByApplicationUuid( applicationUuid );
		
		List<EnrollmentDTO> dtos
			= list();
		
		for ( Enrollment enrollment : enrollments ) {
			
			EnrollmentDTO dto
				= EnrollmentDTO.from( enrollment );
			
			this.extend( dto );
			
			dtos.add ( dto );
		}
		
		result.setValue( Value.OK );
		result.setObject( dtos );
		
		return result;
		
	}
	
	protected void extend( EnrollmentDTO dto ) {
		// TODO
	}
	
	
	
}
package be.pirlewiet.digitaal.domain.service;

import static be.occam.utils.javax.Utils.list;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import be.occam.utils.spring.web.Result;
import be.occam.utils.spring.web.Result.Value;
import be.pirlewiet.digitaal.domain.exception.ErrorCodes;
import be.pirlewiet.digitaal.domain.exception.PirlewietException;
import be.pirlewiet.digitaal.domain.people.DoorMan;
import be.pirlewiet.digitaal.domain.people.EnrollmentManager;
import be.pirlewiet.digitaal.domain.people.PersonManager;
import be.pirlewiet.digitaal.dto.EnrollmentDTO;
import be.pirlewiet.digitaal.dto.PersonDTO;
import be.pirlewiet.digitaal.model.Enrollment;
import be.pirlewiet.digitaal.model.Organisation;
import be.pirlewiet.digitaal.model.Person;

@Service
public class EnrollmentService extends be.pirlewiet.digitaal.domain.service.Service<EnrollmentDTO,Enrollment> {
	
	@Resource
	protected DoorMan doorMan;
	
	@Resource
	EnrollmentManager enrollmentManager;
	
	@Resource
	PersonManager personManager;
	
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
	
	public Result<EnrollmentDTO> findOneByUuid( String uuid ) {
		
		Result<EnrollmentDTO> result
			= new Result<EnrollmentDTO>();
		
		Enrollment enrollment
			= this.enrollmentManager.findOneByUuid( uuid );
		
		if ( enrollment == null ) {
			result.setValue( Value.NOK );
			result.setErrorCode( ErrorCodes.INTERNAL );
		}
		else {
			EnrollmentDTO dto
				= EnrollmentDTO.from( enrollment );
			result.setValue( Value.OK);
			result.setObject( dto );
		}
		
		return result;
		
	}
	
	public Result<EnrollmentDTO> create( EnrollmentDTO enrollment ) {
		
		Result<EnrollmentDTO> result
			= new Result<EnrollmentDTO>();
		
		
		Enrollment toCreate
			= Enrollment.from( enrollment );
		
		try {
			
			PersonDTO participant
				= enrollment.getParticipant();
			
			Person toCreateParticipant
				= Person.from( participant );
			
			Person createdParticipant
				= this.personManager.create( toCreateParticipant );
			
			toCreate.setParticipantUuid( createdParticipant.getUuid() );
		
			Enrollment created
				= this.enrollmentManager.create( toCreate );
			
			EnrollmentDTO dto
				= EnrollmentDTO.from( created );
		
			result.setValue( Value.OK);
			result.setObject( dto );
		
		}
		catch( PirlewietException e ) {
			result.setValue( Value.NOK );
			//result.setErrorCode(  );
		}
		catch( Exception e ) {
			result.setValue( Value.NOK );
			result.setErrorCode( ErrorCodes.INTERNAL );	
		}
		
		return result;
		
	}
	
	
	public Result<EnrollmentDTO> template( ) {
		Enrollment enrollment
			= this.enrollmentManager.template();
		
		EnrollmentDTO dto
			= EnrollmentDTO.from( enrollment );
		
		Result<EnrollmentDTO> result
			= new Result<EnrollmentDTO>();
		result.setValue( Value.OK );
		result.setObject( dto );
	
		return result;
	}

	protected void extend( EnrollmentDTO dto ) {
		// TODO
	}
	
}

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
import be.pirlewiet.digitaal.domain.people.AddressManager;
import be.pirlewiet.digitaal.domain.people.DoorMan;
import be.pirlewiet.digitaal.domain.people.EnrollmentManager;
import be.pirlewiet.digitaal.domain.people.PersonManager;
import be.pirlewiet.digitaal.dto.AddressDTO;
import be.pirlewiet.digitaal.dto.EnrollmentDTO;
import be.pirlewiet.digitaal.dto.PersonDTO;
import be.pirlewiet.digitaal.model.Address;
import be.pirlewiet.digitaal.model.Enrollment;
import be.pirlewiet.digitaal.model.EnrollmentStatus;
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
	
	@Resource
	AddressManager addressManager;
	
	@Override
	public EnrollmentService guard() {
		super.guard();
		return this;
	}

	@Transactional(readOnly=true)
	public Result<List<Result<EnrollmentDTO>>> query( String applicationUuid, Organisation actor ) {
		
		Result<List<Result<EnrollmentDTO>> > result
			= new Result<List<Result<EnrollmentDTO>> >();
		
		List<Enrollment> enrollments
			= this.guard().enrollmentManager.findByApplicationUuid( applicationUuid );
		
		List<Result<EnrollmentDTO>> individualResults
			= list();
		
		for ( Enrollment enrollment : enrollments ) {
			
			Result<EnrollmentDTO> individualResult
				= new Result<EnrollmentDTO>();
			
			individualResult.setValue( Value.OK );
			
			EnrollmentDTO dto
				= EnrollmentDTO.from( enrollment );
			
			this.extend( dto );
			
			individualResult.setObject( dto );
			individualResults.add( individualResult );
			
		}
		
		if ( individualResults.isEmpty() ) {
			result.setValue( Value.NOK );
			result.setErrorCode( ErrorCodes.APPLICATION_NO_ENROLLMENTS );
		}
		else {
			result.setValue( Value.OK );
			result.setObject( individualResults );
		}
		
		
		
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
			this.extend( dto );
			result.setValue( Value.OK);
			result.setObject( dto );
		}
		
		return result;
		
	}
	
	@Override
	public Result<EnrollmentDTO> create( EnrollmentDTO enrollment, Organisation actor ) {
		
		Result<EnrollmentDTO> result
			= new Result<EnrollmentDTO>();
		
		
		Enrollment toCreate
			= Enrollment.from( enrollment );
		toCreate.setStatus( new EnrollmentStatus( EnrollmentStatus.Value.TRANSIT ) );
		
		try {
			
			PersonDTO participant
				= enrollment.getParticipant();
			
			Person toCreateParticipant
				= Person.from( participant );
			
			Person createdParticipant
				= this.personManager.create( toCreateParticipant );
			
			toCreate.setParticipantUuid( createdParticipant.getUuid() );
			
			Address toCreateAddress
				= Address.from( enrollment.getAddress() );
		
			Address createdAddress 
				= this.addressManager.createOrUpdate( toCreateAddress );
			
			toCreate.setAddressUuid( createdAddress.getUuid() );
		
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
			logger.warn( "bugger", e );
		}
		
		return result;
		
	}
	
	@Override
	@Transactional(readOnly=false)
	public Result<EnrollmentDTO> update( EnrollmentDTO enrollment, Organisation actor ) {
		
		Result<EnrollmentDTO> result
			= new Result<EnrollmentDTO>();
		
		
		Enrollment update
			= Enrollment.from( enrollment );
		
		try {
			
			Enrollment toUpdate
				= this.enrollmentManager.findOneByUuid( enrollment.getUuid() );
			
			PersonDTO participant
				= enrollment.getParticipant();
			
			Person updateParticipant
				= Person.from( participant );
			
			Person toUpdateParticipant 
				= this.personManager.findOneByUuid( toUpdate.getParticipantUuid() );
			
			Person updatedParticipant
				= this.personManager.update( toUpdateParticipant, updateParticipant );
			
			Address toUpdateAddress
				= this.addressManager.findOneByUuid( toUpdate.getAddressUuid() );
			
			Address updateAddress
				= Address.from( enrollment.getAddress() );
			
			this.addressManager.update( toUpdateAddress, updateAddress );
			
			Enrollment updated
				= this.enrollmentManager.update( toUpdate, update );
			
			EnrollmentDTO dto
				= EnrollmentDTO.from( updated );
		
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
			logger.warn("bugger", e);
		}
		
		return result;
		
	}
	
	
	@Override
	@Transactional(readOnly=false)
	public Result<EnrollmentDTO> delete( String enrollmentUuid, Organisation actor) {
		
		Result<EnrollmentDTO> result
			= new Result<EnrollmentDTO>();
		
		Enrollment toDelete
			= this.enrollmentManager.findOneByUuid( enrollmentUuid );
		
		Address toDeleteAddress
			= this.addressManager.findOneByUuid( toDelete.getAddressUuid() );
		this.addressManager.delete( toDeleteAddress );
		
		Person toDeletePerson
			= this.personManager.findOneByUuid( toDelete.getParticipantUuid() );
		
		this.personManager.delete( toDeletePerson );
		
		this.enrollmentManager.delete( toDelete );
		
		result.setValue( Value.OK );
		result.setObject( EnrollmentDTO.from( toDelete ) );
		
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
		
		Person participant
			= this.personManager.findOneByUuid( dto.getParticipantUuid() );
		
		dto.setParticipant( PersonDTO.from( participant ) );
		
		Address address
			= this.addressManager.findOneByUuid( dto.getAddressUuid() );
		dto.setAddress( AddressDTO.from( address ) );
		
		
	}
	
}

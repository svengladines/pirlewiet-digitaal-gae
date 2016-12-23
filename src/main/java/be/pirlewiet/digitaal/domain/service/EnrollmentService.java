package be.pirlewiet.digitaal.domain.service;

import static be.occam.utils.javax.Utils.list;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import be.occam.utils.spring.web.ErrorCode;
import be.occam.utils.spring.web.Result;
import be.occam.utils.spring.web.Result.Value;
import be.pirlewiet.digitaal.domain.exception.ErrorCodes;
import be.pirlewiet.digitaal.domain.exception.PirlewietException;
import be.pirlewiet.digitaal.domain.people.AddressManager;
import be.pirlewiet.digitaal.domain.people.ApplicationManager;
import be.pirlewiet.digitaal.domain.people.DoorMan;
import be.pirlewiet.digitaal.domain.people.EnrollmentManager;
import be.pirlewiet.digitaal.domain.people.PersonManager;
import be.pirlewiet.digitaal.domain.people.QuestionAndAnswerManager;
import be.pirlewiet.digitaal.domain.people.Secretary;
import be.pirlewiet.digitaal.dto.AddressDTO;
import be.pirlewiet.digitaal.dto.ApplicationDTO;
import be.pirlewiet.digitaal.dto.EnrollmentDTO;
import be.pirlewiet.digitaal.dto.HolidayDTO;
import be.pirlewiet.digitaal.dto.PersonDTO;
import be.pirlewiet.digitaal.dto.QuestionAndAnswerDTO;
import be.pirlewiet.digitaal.model.Address;
import be.pirlewiet.digitaal.model.Application;
import be.pirlewiet.digitaal.model.Enrollment;
import be.pirlewiet.digitaal.model.EnrollmentStatus;
import be.pirlewiet.digitaal.model.Holiday;
import be.pirlewiet.digitaal.model.Organisation;
import be.pirlewiet.digitaal.model.Person;
import be.pirlewiet.digitaal.model.QuestionAndAnswer;
import be.pirlewiet.digitaal.model.Tags;

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
	
	@Resource
	ApplicationManager applicationManager;
	
	@Resource
	QuestionAndAnswerManager questionAndAnswerManager;
	
	@Resource
	Secretary secretary;
	
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
			
			StringBuilder errorCodes = new StringBuilder();
			
			List<QuestionAndAnswer> medicals
				= this.questionAndAnswerManager.findByEntityAndTag( enrollment.getUuid(), Tags.TAG_MEDIC );
			
			Result<List<Result<QuestionAndAnswer>>> medicalResult
				= this.secretary.checkEnrollmentQuestionList( enrollment.getUuid(), medicals, Tags.TAG_MEDIC );
			
			// assume all good untill proven otherwise
			individualResult.setValue( Value.OK );
			
			if ( ! Result.Value.OK.equals( medicalResult.getValue() ) ) {
				individualResult.setValue( Result.Value.NOK );
				errorCodes.append( ErrorCodes.PARTICIPANT_MEDIC_QUESTION_MISSING.getCode() ).append( "|" );
			}
			
			List<QuestionAndAnswer> history
				= this.questionAndAnswerManager.findByEntityAndTag( enrollment.getUuid(), Tags.TAG_HISTORY );
		
			Result<List<Result<QuestionAndAnswer>>> historyResult
				= this.secretary.checkEnrollmentQuestionList( enrollment.getUuid(), history, Tags.TAG_HISTORY );
		
			if ( ! Result.Value.OK.equals( historyResult.getValue() ) ) {
				individualResult.setValue( Result.Value.NOK );
				errorCodes.append( ErrorCodes.PARTICIPANT_HISTORY_QUESTION_MISSING.getCode() ).append( "|" );
			}
			
			if ( ! Value.OK.equals( individualResult.getValue() ) ) {
				logger.info( "error codes are [{}]", errorCodes.toString() );
				individualResult.setErrorCode( new ErrorCode( errorCodes.toString() ) );
			}
			
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
				= this.addressManager.create( toCreateAddress );
			
			toCreate.setAddressUuid( createdAddress.getUuid() );
			
			Application application
				= this.applicationManager.findOne( enrollment.getApplicationUuid() );
			
			toCreate.setHolidayUuid( application.getHolidayUuids() );
			
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
	
	@Transactional(readOnly=false)
	public Result<EnrollmentDTO> updateHolidays ( String uuid, List<HolidayDTO> holidays, Organisation actor ) {
		
		logger.info("enrollment.updateHolidays");
		
		Result<EnrollmentDTO> result
			= new Result<EnrollmentDTO>();
		
		List<Holiday> holidayz
			= list();
		
		for ( HolidayDTO dto : holidays ) {
			
			Holiday holiday 
				=  Holiday.from( dto );
			
			holidayz.add( holiday );
		}
		
		Enrollment updated
			= this.enrollmentManager.updateHolidays( uuid, holidayz );
		
		result.setValue( Value.OK );
		result.setObject( EnrollmentDTO.from( updated ) );
		
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

	@Transactional(readOnly=false)
	public Result<EnrollmentDTO> updateQList ( String uuid, List<QuestionAndAnswerDTO> qList, Organisation actor ) {
		
		logger.info("[{}]; enrollment.updateQList", actor.getName() );
		
		Result<EnrollmentDTO> result
			= new Result<EnrollmentDTO>();
		
		List<QuestionAndAnswer> list
			= list();
		
		for ( QuestionAndAnswerDTO dto : qList ) {
			
			QuestionAndAnswer holiday 
				=  QuestionAndAnswer.from( dto );
			
			list.add( holiday );
		}
		
		Enrollment updated
			= this.enrollmentManager.updateQList( uuid, list );
		
		result.setValue( Value.OK );
		result.setObject( EnrollmentDTO.from( updated ) );
		
		return result;
		
	}
	
}

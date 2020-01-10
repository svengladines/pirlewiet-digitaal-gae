package be.pirlewiet.digitaal.domain.service;

import static be.occam.utils.javax.Utils.list;
import static be.occam.utils.javax.Utils.map;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import be.occam.utils.javax.Utils;
import be.occam.utils.spring.web.ErrorCode;
import be.occam.utils.spring.web.Result;
import be.occam.utils.spring.web.Result.Value;
import be.pirlewiet.digitaal.domain.Mapper;
import be.pirlewiet.digitaal.domain.exception.ErrorCodes;
import be.pirlewiet.digitaal.domain.exception.PirlewietException;
import be.pirlewiet.digitaal.domain.people.AddressManager;
import be.pirlewiet.digitaal.domain.people.ApplicationManager;
import be.pirlewiet.digitaal.domain.people.DoorMan;
import be.pirlewiet.digitaal.domain.people.EnrollmentManager;
import be.pirlewiet.digitaal.domain.people.OrganisationManager;
import be.pirlewiet.digitaal.domain.people.PersonManager;
import be.pirlewiet.digitaal.domain.people.QuestionAndAnswerManager;
import be.pirlewiet.digitaal.domain.people.Secretary;
import be.pirlewiet.digitaal.model.Address;
import be.pirlewiet.digitaal.model.Application;
import be.pirlewiet.digitaal.model.Enrollment;
import be.pirlewiet.digitaal.model.EnrollmentStatus;
import be.pirlewiet.digitaal.model.Holiday;
import be.pirlewiet.digitaal.model.Organisation;
import be.pirlewiet.digitaal.model.Person;
import be.pirlewiet.digitaal.model.QuestionAndAnswer;
import be.pirlewiet.digitaal.model.Tags;
import be.pirlewiet.digitaal.web.dto.AddressDTO;
import be.pirlewiet.digitaal.web.dto.ApplicationDTO;
import be.pirlewiet.digitaal.web.dto.EnrollmentDTO;
import be.pirlewiet.digitaal.web.dto.HolidayDTO;
import be.pirlewiet.digitaal.web.dto.PersonDTO;
import be.pirlewiet.digitaal.web.dto.QuestionAndAnswerDTO;
import be.pirlewiet.digitaal.web.util.PirlewietUtil;

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
	OrganisationManager organisationManager;
	
	@Resource
	QuestionAndAnswerManager questionAndAnswerManager;
	
	@Resource
	Secretary secretary;
	
	@Resource
	Mapper mapper;
	
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
		
		
		boolean allOK 
			= true;
		
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
			
			if ( Result.Value.NOK.equals( individualResult.getValue() ) ) {
				allOK = false;
			}
			
		}
		
		if ( individualResults.isEmpty() ) {
			result.setValue( Value.NOK );
			result.setErrorCode( ErrorCodes.APPLICATION_NO_ENROLLMENTS );
		}
		else {
			result.setValue( allOK ? Value.OK : Value.NOK );
			result.setObject( individualResults );
		}
		
		
		
		return result;
		
	}
	
	@Transactional(readOnly=true)
	public Result<Map<String,List<Result<EnrollmentDTO>>>> mapped( List<Result<ApplicationDTO>> apps, Organisation actor ) {
		
		Result<Map<String,List<Result<EnrollmentDTO>>>>  result
			= new Result<Map<String,List<Result<EnrollmentDTO>>>>();
		
		Map<String,List<Result<EnrollmentDTO>>> map
			= map();
		
		List<Enrollment> enrollments
			= this.guard().enrollmentManager.findAll();
		
		boolean allOK 
			= true;
		
		for ( Enrollment enrollment : enrollments ) {
			
			Result<EnrollmentDTO> individualResult
				= new Result<EnrollmentDTO>();
			
			String applicationUuid
				= enrollment.getApplicationUuid();
			
			List<Result<EnrollmentDTO>> applicationEnrollments
				= map.get( applicationUuid );
			
			if ( applicationEnrollments == null ) {
				
				applicationEnrollments = list();
				map.put( applicationUuid, applicationEnrollments );
				
			}
			
			individualResult.setValue( Value.OK );
			individualResult.setObject( EnrollmentDTO.from( enrollment ) );
			
			applicationEnrollments.add( individualResult );
			
			if ( Result.Value.NOK.equals( individualResult.getValue() ) ) {
				allOK = false;
			}
			
		}
		
		result.setValue( allOK ? Value.OK : Value.NOK );
		result.setObject( map );
		
		return result;
		
	}
	
	@Transactional(readOnly=true)
	public byte[] download( Organisation actor ) {
		
		byte[] bytes
			= new byte[] {};
		
		List<String[]> rows
			= list();
		
		if ( PirlewietUtil.isPirlewiet( actor ) ) {
			
			logger.info( "start download...");
			
			List<Application> applications
				= this.applicationManager.findActiveByYear();
			
			logger.info( "download - applications loaded");
			
			List<Enrollment> allEnrollments
				= this.enrollmentManager.findAll();
			
			logger.info( "download - enrollments loaded");
			
			Map<String, List<Enrollment>> enrollmentMap
				= map();
			
			for ( Enrollment enrollment : allEnrollments ) {
				
				List<Enrollment> appedEnrollments
					= enrollmentMap.get( enrollment.getApplicationUuid() );
				
				if ( appedEnrollments == null ) {
					appedEnrollments = list();
					enrollmentMap.put( enrollment.getApplicationUuid(), appedEnrollments );
				}
				
				appedEnrollments.add( enrollment );
				
			}
			
			logger.info( "download - application/enrollment mapping done" );
			
			Map<String,Address> addressMap
				= Utils.map();
		
			List<Address> allAddresses
				= this.addressManager.findAll();
			
			for ( Address address : allAddresses ) {
				
				addressMap.put( address.getUuid(), address );
				
			}
			
			logger.info( "download - address mapping done" );
		
			Map<String,Person> personMap
				= Utils.map();
		
			List<Person> allPersons
				= this.personManager.findAll();
			
			for ( Person person : allPersons ) {
				
				personMap.put( person.getUuid(), person );
				
			}
			
			logger.info( "download - address mapping done" );
			
			Map<String,Organisation> organisationMap
				= Utils.map();
		
			List<Organisation> allOrganisations
				= this.organisationManager.all();
			
			for ( Organisation organisation : allOrganisations ) {
				
				organisationMap.put( organisation.getUuid(), organisation );
				
			}
			
			logger.info( "download - organisation mapping done" );
			
			Map<String,Map<String,List<QuestionAndAnswer>>> allMap
				= Utils.map();
			
			List<QuestionAndAnswer> allQna 
				= this.questionAndAnswerManager.findAll();
			
			for ( QuestionAndAnswer qna : allQna ) {
				
				String entityUuid
					 = qna.getEntityUuid();
				
				String tag 
					= qna.getTag();
				
				Map<String,List<QuestionAndAnswer>> entityMap
					= allMap.get( entityUuid );
				
				if ( entityMap == null ) {
					entityMap = map();
					allMap.put( entityUuid, entityMap );
				}
				
				List<QuestionAndAnswer> tagList
					= entityMap.get( tag );
				
				if ( tagList == null ) {
					
					tagList = list();
					entityMap.put( tag, tagList );
				}
				
				tagList.add( qna );
				
			}
			
			logger.info( "download - qna mapping done" );
			
			
			for ( Application application : applications ) {
				
				logger.info( "download - application [{}]", application.getUuid() );
				
				List<Enrollment> enrollments
					= enrollmentMap.get( application.getUuid() );
				
				if ( enrollments != null ) {
					logger.info( "[{}]; found [{}] enrollments for this application", application.getUuid(), enrollments.size() );
				}
				else {
					logger.info( "[{}]; found no related enrollments" );
					continue;
				}
				
				logger.info( "download - application [{}], now map", application.getUuid() );
				
				List<String[]> mapped
					= this.mapper.asStrings( 
							application, 
							enrollments,
							null,
							addressMap,
							personMap,
							organisationMap,
							allMap );
				
				if ( mapped != null ) {
					
					rows.addAll( mapped );
					
				}
				
				logger.info( "download - application [{}], mapped", application.getUuid() );
			
			}
			
		}
		
		bytes = this.mapper.asBytes( rows );
		
		return bytes;
		
	}
	
	@Transactional(readOnly=true)
	public byte[] downloadOneHundred( Organisation actor ) {
		
		byte[] bytes
			= new byte[] {};
		
		List<String[]> rows
			= list();
		
		if ( PirlewietUtil.isPirlewiet( actor ) ) {
			
			logger.info( "start download-100...");
			
			List<Application> applications
				= this.applicationManager.findActiveByYear();
			
			logger.info( "download - applications loaded");
			
			List<Enrollment> mostRecentEnrollments
				= this.enrollmentManager.findMostRecent();
			
			logger.info( "download - enrollments loaded");
			
			Map<String, List<Enrollment>> enrollmentMap
				= map();
			
			for ( Enrollment enrollment : mostRecentEnrollments ) {
				
				List<Enrollment> appedEnrollments
					= enrollmentMap.get( enrollment.getApplicationUuid() );
				
				if ( appedEnrollments == null ) {
					appedEnrollments = list();
					enrollmentMap.put( enrollment.getApplicationUuid(), appedEnrollments );
				}
				
				appedEnrollments.add( enrollment );
				
			}
			
			logger.info( "download - application/enrollment mapping done" );
			
			Map<String,Address> addressMap
				= Utils.map();
		
			List<Address> allAddresses
				= this.addressManager.findAll();
			
			for ( Address address : allAddresses ) {
				
				addressMap.put( address.getUuid(), address );
				
			}
			
			logger.info( "download - address mapping done" );
		
			Map<String,Person> personMap
				= Utils.map();
		
			List<Person> allPersons
				= this.personManager.findAll();
			
			for ( Person person : allPersons ) {
				
				personMap.put( person.getUuid(), person );
				
			}
			
			logger.info( "download - address mapping done" );
			
			Map<String,Organisation> organisationMap
				= Utils.map();
		
			List<Organisation> allOrganisations
				= this.organisationManager.all();
			
			for ( Organisation organisation : allOrganisations ) {
				
				organisationMap.put( organisation.getUuid(), organisation );
				
			}
			
			logger.info( "download - organisation mapping done" );
			
			Map<String,Map<String,List<QuestionAndAnswer>>> allMap
				= Utils.map();
			
			List<QuestionAndAnswer> qnaList 
				 = list();
			
			for ( String applicationUuid : enrollmentMap.keySet() ) {
				
				List<QuestionAndAnswer> forApplication
					= this.questionAndAnswerManager.findByEntity( applicationUuid );
				
				qnaList.addAll( forApplication );
				
				List<Enrollment> enrollments
					= enrollmentMap.get( applicationUuid );
				
				if ( enrollments != null ) {
					
					
					
				}
				
			}
			
			List<QuestionAndAnswer> allQna 
				= this.questionAndAnswerManager.findAll();
			
			for ( QuestionAndAnswer qna : allQna ) {
				
				String entityUuid
					 = qna.getEntityUuid();
				
				String tag 
					= qna.getTag();
				
				Map<String,List<QuestionAndAnswer>> entityMap
					= allMap.get( entityUuid );
				
				if ( entityMap == null ) {
					entityMap = map();
					allMap.put( entityUuid, entityMap );
				}
				
				List<QuestionAndAnswer> tagList
					= entityMap.get( tag );
				
				if ( tagList == null ) {
					
					tagList = list();
					entityMap.put( tag, tagList );
				}
				
				tagList.add( qna );
				
			}
			
			logger.info( "download - qna mapping done" );
			
			
			for ( Application application : applications ) {
				
				logger.info( "download - application [{}]", application.getUuid() );
				
				List<Enrollment> enrollments
					= enrollmentMap.get( application.getUuid() );
				
				if ( enrollments != null ) {
					logger.info( "[{}]; found [{}] enrollments for this application", application.getUuid(), enrollments.size() );
				}
				else {
					logger.info( "[{}]; found no related enrollments" );
					continue;
				}
				
				logger.info( "download - application [{}], now map", application.getUuid() );
				
				List<String[]> mapped
					= this.mapper.asStrings( 
							application, 
							enrollments,
							null,
							addressMap,
							personMap,
							organisationMap,
							allMap );
				
				if ( mapped != null ) {
					
					rows.addAll( mapped );
					
				}
				
				logger.info( "download - application [{}], mapped", application.getUuid() );
			
			}
			
		}
		
		bytes = this.mapper.asBytes( rows );
		
		return bytes;
		
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
			toCreate.setParticipantName( String.format("%s %s", toCreateParticipant.getGivenName(), toCreateParticipant.getFamilyName() ) );
			
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
	public Result<EnrollmentDTO> updateStatus ( String uuid, EnrollmentStatus enrollmentStatus, Organisation actor ) {
		
		logger.info("application.updateStatus");
		
		Result<EnrollmentDTO> result
			= new Result<EnrollmentDTO>();
		
		Enrollment updated
			= this.enrollmentManager.updateStatus( uuid, enrollmentStatus, true );
		
		result.setValue( Value.OK );
		result.setObject( EnrollmentDTO.from( updated ) );
		
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
	
	@Transactional(readOnly=false)
	public Result<EnrollmentDTO> updateHolidays ( String uuid, String holidayUuids, Organisation actor ) {
		
		logger.info("enrollment.updateHolidays");
		
		Result<EnrollmentDTO> result
			= new Result<EnrollmentDTO>();
		
		Enrollment updated
			= this.enrollmentManager.updateHolidays( uuid, holidayUuids, true );
		
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

package be.pirlewiet.digitaal.domain.service;

import static be.occam.utils.javax.Utils.list;

import java.util.List;
import java.util.Set;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import be.occam.utils.spring.web.Result;
import be.occam.utils.spring.web.Result.Value;
import be.pirlewiet.digitaal.domain.people.ApplicationManager;
import be.pirlewiet.digitaal.domain.people.DoorMan;
import be.pirlewiet.digitaal.domain.people.HolidayManager;
import be.pirlewiet.digitaal.domain.people.QuestionAndAnswerManager;
import be.pirlewiet.digitaal.domain.people.Secretary;
import be.pirlewiet.digitaal.model.Application;
import be.pirlewiet.digitaal.model.ApplicationStatus;
import be.pirlewiet.digitaal.model.Holiday;
import be.pirlewiet.digitaal.model.Organisation;
import be.pirlewiet.digitaal.model.Person;
import be.pirlewiet.digitaal.model.QuestionAndAnswer;
import be.pirlewiet.digitaal.model.Tags;
import be.pirlewiet.digitaal.web.dto.ApplicationDTO;
import be.pirlewiet.digitaal.web.dto.HolidayDTO;
import be.pirlewiet.digitaal.web.dto.PersonDTO;
import be.pirlewiet.digitaal.web.dto.QuestionAndAnswerDTO;
import be.pirlewiet.digitaal.web.util.PirlewietUtil;

@Service
public class ApplicationService extends be.pirlewiet.digitaal.domain.service.Service<ApplicationDTO,Application> {
	
	@Resource
	protected DoorMan doorMan;
	
	@Resource
	ApplicationManager applicationManager;
	
	@Resource
	QuestionAndAnswerManager questionAndAnswerManager;
	
	@Resource
	HolidayManager holidayManager;
	
	@Resource
	Secretary secretary;
	
	@Override
	public ApplicationService guard() {
		super.guard();
		return this;
	}
	
	@Override
	@Transactional(readOnly=false)
	public Result<ApplicationDTO> create(ApplicationDTO dto, Organisation actor) {
		
		Result<ApplicationDTO> result
			= new Result<ApplicationDTO>();
		
		Application application
			= Application.from( dto );
		
		Application created
			= this.applicationManager.create(application,actor);
		
		result.setValue( Value.OK );
		result.setObject( ApplicationDTO.from( created ) );
		
		return result;
		
	}
	
	public List<ApplicationDTO> findAll() {
		
		List<Application> all
			= this.applicationManager.findAll();
		
		List<ApplicationDTO> result
			= list();
		
		for ( Application application : all ) {
			result.add( ApplicationDTO.from( application ) );
		}
		
		return result;
		
	}

	@Override
	@Transactional(readOnly=true)
	public Result<List<Result<ApplicationDTO>>> query( Organisation actor ) {
		
		Result<List<Result<ApplicationDTO>>>result
			= new Result<List<Result<ApplicationDTO>>>();
		
		// TODO, remove hardcoded year
		List<Application> applications
			= PirlewietUtil.isPirlewiet( actor ) ? this.guard().applicationManager.findActiveByYear( ) : this.guard().applicationManager.findByOrganisation( actor );
		
		List<Result<ApplicationDTO>> individualResults
			= list();
		
		for ( Application application : applications ) {
			
			Result<ApplicationDTO> individualResult
				= new Result<ApplicationDTO>();
			
			ApplicationDTO dto
				= ApplicationDTO.from( application );
			this.extend( dto, application );
			
			individualResult.setValue( Value.OK );
			individualResult.setObject( dto );
			
			individualResults.add( individualResult );
			
		}
		
		result.setValue( Value.OK );
		result.setObject( individualResults );
		
		return result;
		
	}
	
	@Transactional(readOnly=true)
	public Result<ApplicationDTO> findOne( String uuid, Organisation actor ) {
		
		Result<ApplicationDTO> result
			= new Result<ApplicationDTO>();
		
		Application application
			= this.guard().applicationManager.findOne( uuid );
		
		ApplicationDTO dto
			= ApplicationDTO.from( application );
		
		this.extend( dto, application );
		
		result.setValue( Value.OK );
		result.setObject( dto );
		
		return result;
		
	}
	
	@Transactional(readOnly=false)
	public Result<ApplicationDTO> updateHolidays ( String uuid, List<HolidayDTO> holidays, Organisation actor ) {
		
		logger.info("application.updateHolidays");
		
		Result<ApplicationDTO> result
			= new Result<ApplicationDTO>();
		
		List<Holiday> holidayz
			= list();
		
		for ( HolidayDTO dto : holidays ) {
			
			Holiday holiday 
				=  Holiday.from( dto );
			
			holidayz.add( holiday );
		}
		
		Application updated
			= this.applicationManager.updateHolidays( uuid, holidayz );
		
		result.setValue( Value.OK );
		result.setObject( ApplicationDTO.from( updated ) );
		
		return result;
		
	}
	
	@Transactional(readOnly=false)
	public Result<ApplicationDTO> updateContact ( String uuid, PersonDTO contact, Organisation actor ) {
		
		logger.info("application.updateHolidays");
		
		Result<ApplicationDTO> result
			= new Result<ApplicationDTO>();
		
		List<Holiday> holidayz
			= list();
		
		Person c
			= Person.from( contact );
		
		Application updated
			= this.applicationManager.updateContact( uuid, c );
		
		result.setValue( Value.OK );
		result.setObject( ApplicationDTO.from( updated ) );
		
		return result;
		
	}
	
	@Transactional(readOnly=false)
	public Result<ApplicationDTO> updateQList ( String uuid, List<QuestionAndAnswerDTO> qList, Organisation actor ) {
		
		logger.info("application.updateHolidays");
		
		Result<ApplicationDTO> result
			= new Result<ApplicationDTO>();
		
		List<QuestionAndAnswer> list
			= list();
		
		for ( QuestionAndAnswerDTO dto : qList ) {
			
			QuestionAndAnswer holiday 
				=  QuestionAndAnswer.from( dto );
			
			list.add( holiday );
		}
		
		Application updated
			= this.applicationManager.updateQList( uuid, list );
		
		result.setValue( Value.OK );
		result.setObject( ApplicationDTO.from( updated ) );
		
		return result;
		
	}
	
	@Transactional(readOnly=false)
	public Result<ApplicationDTO> updateStatus ( String uuid, ApplicationStatus applicationStatus, Organisation actor ) {
		
		logger.info("application.updateStatus");
		
		Result<ApplicationDTO> result
			= new Result<ApplicationDTO>();
		
		Application updated
			= this.applicationManager.updateStatus( uuid, applicationStatus );
		
		result.setValue( Value.OK );
		result.setObject( ApplicationDTO.from( updated ) );
		
		return result;
		
	}
	
	@Transactional(readOnly=true)
	public Result<List<Result<QuestionAndAnswerDTO>>> checkApplicationQuestionList( ApplicationDTO application, List<QuestionAndAnswerDTO> list ) {
		
		List<QuestionAndAnswer> qnaList
			= this.questionAndAnswerManager.findByEntityAndTag( application.getUuid(), Tags.TAG_APPLICATION );
		 
		 Result<List<Result<QuestionAndAnswer>>> secretaryResult
			= this.secretary.checkApplicationQuestionList( application.getUuid(), qnaList );
		 
		 Result<List<Result<QuestionAndAnswerDTO>>> result
		 	= new Result<List<Result<QuestionAndAnswerDTO>>>();
		 
		 List<Result<QuestionAndAnswerDTO>> listResult
		 	= list();
		 
		 result.setValue( secretaryResult.getValue() );
		 result.setErrorCode( secretaryResult.getErrorCode() );
		 
		 for ( Result<QuestionAndAnswer> qna : secretaryResult.getObject() ) {
			 
			Result<QuestionAndAnswerDTO> dtoResult
				= new Result<QuestionAndAnswerDTO>();
			
			dtoResult.setValue( qna.getValue() );
			dtoResult.setErrorCode( qna.getErrorCode() );
				
			QuestionAndAnswerDTO a
				= QuestionAndAnswerDTO.from( qna.getObject() );
			
			dtoResult.setObject( a );
			
			listResult.add( dtoResult );
				
		}
		 
		 result.setObject( listResult );
		  
		 return result;
	    	
	}
	
	protected void extend( ApplicationDTO dto, Application application ) {
		
		String holidayString
			= application.getHolidayUuids();
		
		Set<Holiday> holidays
			= this.holidayManager.holidaysFromUUidString( holidayString );
		
		for ( Holiday holiday : holidays ) {
			
			HolidayDTO holidayDTO
				= HolidayDTO.from( holiday );
			
			dto.getHolidays().add( holidayDTO );
			
		}
		
		
	}
	
	
	
}

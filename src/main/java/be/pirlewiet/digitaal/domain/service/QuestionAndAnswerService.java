package be.pirlewiet.digitaal.domain.service;

import static be.occam.utils.javax.Utils.*;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import be.occam.utils.spring.web.Result;
import be.occam.utils.spring.web.Result.Value;
import be.pirlewiet.digitaal.domain.exception.ErrorCodes;
import be.pirlewiet.digitaal.domain.people.DoorMan;
import be.pirlewiet.digitaal.domain.people.QuestionAndAnswerManager;
import be.pirlewiet.digitaal.dto.PersonDTO;
import be.pirlewiet.digitaal.dto.QuestionAndAnswerDTO;
import be.pirlewiet.digitaal.model.QuestionAndAnswer;

@Service
public class QuestionAndAnswerService extends be.pirlewiet.digitaal.domain.service.Service<QuestionAndAnswerDTO,QuestionAndAnswer> {
	
	@Resource
	protected DoorMan doorMan;
	
	@Resource
	QuestionAndAnswerManager questionAndAnswerManager;
	
	public Result<List<QuestionAndAnswerDTO>> findByEntityAndTag( String entityUuid, String tag ) {
		
		Result<List<QuestionAndAnswerDTO>> result 
			= new Result<List<QuestionAndAnswerDTO>>();
		
		List<QuestionAndAnswer> list
			= this.questionAndAnswerManager.findByEntityAndTag( entityUuid, tag );
		
		if ( list == null  ) {
			result.setValue( Value.NOK );
			result.setErrorCode( ErrorCodes.INTERNAL );
		}
		else {
			
			List<QuestionAndAnswerDTO> dtos
				= list();
			
			for ( QuestionAndAnswer answer : list ) {
			
				QuestionAndAnswerDTO dto
					= QuestionAndAnswerDTO.from( answer );
				
				dtos.add( dto );
			}
				
			result.setValue( Value.OK );
			result.setObject( dtos );
			
		}
		
		return result;
		
	}
	
	
}

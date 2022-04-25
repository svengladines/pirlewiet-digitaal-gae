package be.pirlewiet.digitaal.domain.service;

import static be.occam.utils.javax.Utils.list;

import java.util.List;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import be.occam.utils.spring.web.Result;
import be.occam.utils.spring.web.Result.Value;
import be.pirlewiet.digitaal.domain.exception.ErrorCodes;
import be.pirlewiet.digitaal.domain.people.DoorMan;
import be.pirlewiet.digitaal.domain.people.QuestionAndAnswerManager;
import be.pirlewiet.digitaal.model.QuestionAndAnswer;
import be.pirlewiet.digitaal.web.dto.QuestionAndAnswerDTO;

@Service
public class QuestionAndAnswerService extends be.pirlewiet.digitaal.domain.service.Service<QuestionAndAnswerDTO,QuestionAndAnswer> {
	
	@Resource
	protected DoorMan doorMan;
	
	@Resource
	QuestionAndAnswerManager questionAndAnswerManager;
	
	protected final Logger logger = LoggerFactory.getLogger( this.getClass() );
	
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
	
	public Result<List<QuestionAndAnswerDTO>> findByEntityAndTag( String entityUuid, String... tags ) {
		
		Result<List<QuestionAndAnswerDTO>> result 
			= new Result<List<QuestionAndAnswerDTO>>();
		
		result.setObject( list() );
		
		for ( String tag : tags ) {
		
			List<QuestionAndAnswer> list
				= this.questionAndAnswerManager.findByEntityAndTag( entityUuid, tag );
			
			if ( list == null  ) {
				result.setValue( Value.NOK );
				result.setErrorCode( ErrorCodes.INTERNAL );
				logger.info( "qna-list for entity [{}] and tag [{}] is null", new Object[] { entityUuid, tag } );
			}
			else {
				
				logger.info( "found [{}] qnas for entity [{}] and tag [{}]", new Object[] { list.size(), entityUuid, tag } );
				
				List<QuestionAndAnswerDTO> dtos
					= list();
				
				for ( QuestionAndAnswer answer : list ) {
				
					QuestionAndAnswerDTO dto
						= QuestionAndAnswerDTO.from( answer );
					
					dtos.add( dto );
				}
					
				result.setValue( Value.OK );
				result.getObject().addAll( dtos );
				
				//logger.info( "added [{}] qnas for entity [{}] and tag [{}]", dtos.size(), entityUuid, tag );
				
			}
		}
		
		return result;
		
	}
	
	
}

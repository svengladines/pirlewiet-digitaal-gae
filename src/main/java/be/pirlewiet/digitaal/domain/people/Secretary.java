package be.pirlewiet.digitaal.domain.people;

import static be.occam.utils.javax.Utils.isEmpty;
import static be.occam.utils.javax.Utils.list;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.LocaleResolver;

import be.occam.utils.spring.web.Result;
import be.occam.utils.spring.web.Result.Value;
import be.pirlewiet.digitaal.model.QuestionAndAnswer;
import be.pirlewiet.digitaal.model.QuestionType;
import be.pirlewiet.digitaal.model.Tags;
import be.pirlewiet.digitaal.repository.EnrollmentRepository;

@Component
public class Secretary {
	
	protected final Logger logger
		= LoggerFactory.getLogger( this.getClass() );

	@Autowired
    public Secretary( ) {
    }
    
  public Result<List<Result<QuestionAndAnswer>>> checkApplicationQuestionList( String applicationUUID, List<QuestionAndAnswer> list ) {
    	
	  	Result<List<Result<QuestionAndAnswer>>> result
    		= new Result<List<Result<QuestionAndAnswer>>>();
	  	
    	result.setValue( Result.Value.OK );
	
    	// check list
    	List<Result<QuestionAndAnswer>> individualResults 
			= this.areAllMandatoryQuestionsAnswered( applicationUUID, list, Tags.TAG_APPLICATION );
    	
    	boolean allOK = true;
		boolean allNOK = true;
		
    	for ( Result<QuestionAndAnswer> individualResult : individualResults ) {
    		
	    	if ( ! Result.Value.OK.equals( individualResult.getValue() ) ) {
	    		allOK = false;
	    	}
	    	else {
	    		allNOK = false;
	    	}
	    	
    	}
    	
    	if ( !allOK ) {
    		
    		result.setValue( allNOK ? Result.Value.NOK : Result.Value.PARTIAL );
    		
    	}
    	
    	result.setObject( individualResults );
	    	
    	return result;
    	
    }
  
  public Result<List<Result<QuestionAndAnswer>>> checkEnrollmentQuestionList( String enrollmentUUID, List<QuestionAndAnswer> list, String tag ) {
  	
	Result<List<Result<QuestionAndAnswer>>> result = new Result<List<Result<QuestionAndAnswer>>>();
	result.setValue( Result.Value.OK );
	
  	// check list
  	List<Result<QuestionAndAnswer>> individualResults = this.areAllMandatoryQuestionsAnswered( enrollmentUUID, list, tag );
  	
  	boolean allOK = true;
	boolean allNOK = true;
		
  	for ( Result<QuestionAndAnswer> individualResult : individualResults ) {
		if ( ! Result.Value.OK.equals( individualResult.getValue() ) ) {
			allOK = false;
		}
		else {
			allNOK = false;
		}
  	}
  	
  	if ( !allOK ) {
  		result.setValue( allNOK ? Result.Value.NOK : Result.Value.PARTIAL );
  	}
  	
  	result.setObject( individualResults );
  	return result;
  	
  }
  
  public  List<Result<QuestionAndAnswer>> areAllMandatoryQuestionsAnswered( String entityUuid, List<QuestionAndAnswer> list, String tag ) {
  	
	  List<Result<QuestionAndAnswer>> individualResults
	  	= list();

		for ( QuestionAndAnswer questionAndAnswer : list ) {
			
			Result<QuestionAndAnswer> individualResult
				= new Result<QuestionAndAnswer>();
			
			individualResult.setValue( Value.OK );
			individualResult.setObject( questionAndAnswer );
			
			if ( ! QuestionType.Label.equals( questionAndAnswer.getType() ) ) {
				if ( isEmpty( questionAndAnswer.getAnswer() ) ) {
					individualResult.setValue( Value.NOK );
					logger.debug( "[{}]; mandatory question [{}] was not answered", entityUuid, questionAndAnswer.getQuestion() );
				}
			}
			
			individualResults.add( individualResult );
			
		}
		return individualResults;
  }
  
}
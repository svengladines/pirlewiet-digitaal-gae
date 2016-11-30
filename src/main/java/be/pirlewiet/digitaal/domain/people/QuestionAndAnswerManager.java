package be.pirlewiet.digitaal.domain.people;

import java.util.List;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.appengine.api.datastore.KeyFactory;

import be.pirlewiet.digitaal.model.QuestionAndAnswer;
import be.pirlewiet.digitaal.repositories.QuestionAndAnswerRepository;

public class QuestionAndAnswerManager {
	
	protected final Logger logger
		= LoggerFactory.getLogger( this.getClass() );
	
	@Resource
	protected QuestionAndAnswerRepository questionAndAnswerRepository;
	
    public QuestionAndAnswerManager() {
    	
    	
    }
    
    public QuestionAndAnswer findOneByUuid( String uuid ) {
    	return this.questionAndAnswerRepository.findOneByUuid( uuid );
    }
    
    public List<QuestionAndAnswer> findByEntityAndTag( String entityUuid, String tag ) {
    	
    	return this.questionAndAnswerRepository.findByEntityUuidAndTag( entityUuid, tag );
    	
    }
    
  public QuestionAndAnswer create( QuestionAndAnswer toCreate ) {
    	
	  QuestionAndAnswer created 
	  	= this.questionAndAnswerRepository.saveAndFlush( toCreate );
	  
	  created.setUuid( KeyFactory.keyToString( created.getKey() ) );
	  created = this.questionAndAnswerRepository.saveAndFlush( created );
    	
	  return created;
    	
    }
    
    public QuestionAndAnswer update( QuestionAndAnswer toUpdate, QuestionAndAnswer update ) {
    	
    	// currently only update answer
    	toUpdate.setAnswer( update.getAnswer() );
    	
    	toUpdate = this.questionAndAnswerRepository.saveAndFlush( toUpdate );
    	
    	return toUpdate;
    	
    }
    
       
}
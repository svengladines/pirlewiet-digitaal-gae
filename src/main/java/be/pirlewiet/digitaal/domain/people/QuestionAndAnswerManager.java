package be.pirlewiet.digitaal.domain.people;

import static be.occam.utils.javax.Utils.*;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import be.pirlewiet.digitaal.model.QuestionAndAnswer;
import be.pirlewiet.digitaal.model.QuestionType;
import be.pirlewiet.digitaal.repositories.QuestionAndAnswerRepository;

import com.google.appengine.api.datastore.KeyFactory;

public class QuestionAndAnswerManager {
	
	protected final Logger logger
		= LoggerFactory.getLogger( this.getClass() );
	
	protected final Comparator<QuestionAndAnswer> orderById = new Comparator<QuestionAndAnswer> () {

		@Override
		public int compare(QuestionAndAnswer o1, QuestionAndAnswer o2) {
			return Long.valueOf( o1.getId() ).compareTo( Long.valueOf( o2.getId() ) );
		}
		
	};
	
	@Resource
	protected QuestionAndAnswerRepository questionAndAnswerRepository;
	
    public QuestionAndAnswerManager() {
    	
    	
    }
    
    public QuestionAndAnswer findOneByUuid( String uuid ) {
    	return this.questionAndAnswerRepository.findOneByUuid( uuid );
    }
    
    public List<QuestionAndAnswer> findByEntityAndTag( String entityUuid, String tag ) {
    	
    	List<QuestionAndAnswer> l 
    		= this.questionAndAnswerRepository.findByEntityUuidAndTag( entityUuid, tag );
    	
    	Collections.sort( l, this.orderById );
    	
    	return l;
    	
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
    	if ( QuestionType.Text.equals( toUpdate.getType() ) || ( QuestionType.Area.equals( toUpdate.getType() ) ) ) {
    		if ( isEmpty( toUpdate.getAnswer() ) && (isEmpty( update.getAnswer() ) ) ) {
    			update.setAnswer( "Niet van toepassing" );
    		}
    	}
    	
    	toUpdate.setAnswer( update.getAnswer() );
    	
    	toUpdate = this.questionAndAnswerRepository.saveAndFlush( toUpdate );
    	
    	return toUpdate;
    	
    }
    
       
}
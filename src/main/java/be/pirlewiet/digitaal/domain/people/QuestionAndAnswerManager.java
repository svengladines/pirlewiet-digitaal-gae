package be.pirlewiet.digitaal.domain.people;

import static be.occam.utils.javax.Utils.isEmpty;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.StringTokenizer;
import java.util.UUID;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import be.occam.utils.javax.Utils;
import be.pirlewiet.digitaal.model.QuestionAndAnswer;
import be.pirlewiet.digitaal.model.QuestionType;
import be.pirlewiet.digitaal.repository.impl.QuestionAndAnswerRepositoryObjectify;

public class QuestionAndAnswerManager {
	
	protected final Logger logger
		= LoggerFactory.getLogger( this.getClass() );
	
	protected final Comparator<QuestionAndAnswer> orderById = new Comparator<QuestionAndAnswer> () {

		@Override
		public int compare(QuestionAndAnswer o1, QuestionAndAnswer o2) {
			return Long.valueOf( o1.getUuid() ).compareTo( Long.valueOf( o2.getUuid() ) );
		}
		
	};
	
	@Resource
	protected QuestionAndAnswerRepositoryObjectify questionAndAnswerRepository;
	
    public QuestionAndAnswerManager() {
    	
    	
    }
    
    public List<QuestionAndAnswer> findAll( ) {
    	List<QuestionAndAnswer> found 
    		= this.questionAndAnswerRepository.findAll();
    	for ( QuestionAndAnswer questionAndAnswer : found ) {
    		deserialize( questionAndAnswer );
    	}
    	return found;
    }
    
    public QuestionAndAnswer findOneByUuid( String uuid ) {
    	QuestionAndAnswer found = this.questionAndAnswerRepository.findOneByUuid( uuid );
    	if ( found != null ) {
    		deserialize( found );
    	}
    	return found;
    }
    
    public List<QuestionAndAnswer> findByEntityAndTag( String entityUuid, String tag ) {

    	List<QuestionAndAnswer> found 
			= this.questionAndAnswerRepository.findByEntityUuidAndTag( entityUuid, tag );
		for ( QuestionAndAnswer questionAndAnswer : found ) {
			deserialize( questionAndAnswer );
		}
    	
    	Collections.sort( found, this.orderById );
    	
    	return found;
    	
    }
    
    public List<QuestionAndAnswer> findByEntity( String entityUuid ) {

    	List<QuestionAndAnswer> found 
			= this.questionAndAnswerRepository.findByEntityUuid( entityUuid );
		for ( QuestionAndAnswer questionAndAnswer : found ) {
			deserialize( questionAndAnswer );
		}
    	
    	Collections.sort( found, this.orderById );
    	
    	return found;
    	
    }
    
  public QuestionAndAnswer create( QuestionAndAnswer toCreate ) {
    	
	  serialize( toCreate );
	  
	  toCreate.setUuid( UUID.randomUUID().toString() );
	  
	  QuestionAndAnswer created 
	  	= this.questionAndAnswerRepository.saveAndFlush( toCreate );
	  
	  if ( QuestionType.MC.equals( created.getType() ) ) {
		  logger.info( "created MC question with [{}] options", created.getOptions() );
	  }
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
    
    protected void deserialize( QuestionAndAnswer questionAndAnswer ) {
    	
    	if ( QuestionType.MC.equals( questionAndAnswer.getType() ) ) {
    		
    		if ( ! Utils.isEmpty( questionAndAnswer.getOptionString() ) ) {
    			
    			StringTokenizer tok
    				= new StringTokenizer( questionAndAnswer.getOptionString() , "," );
    			
    			while (tok.hasMoreTokens() ) {
    				
    				String option 
    					= tok.nextToken();
    				
    				questionAndAnswer.getOptions().add( option );
    				
    			}
    			
    		}
    		
    	}
    	
    }
    
    protected void serialize( QuestionAndAnswer questionAndAnswer ) {
    	
    	if ( QuestionType.MC.equals( questionAndAnswer.getType() ) ) {
    		
    		List<String> options
    			= questionAndAnswer.getOptions();
    		
    		if ( ! options.isEmpty() ) {
    			
    			StringBuilder b
    				= new StringBuilder();
    			
    			int index = 0;
    			for ( String option : options ) {
    				
    				b.append( option );
    				if ( index < options.size() - 1 ) {
    					b.append( "," );
    				}
    				index++;
    				
    			}
    			questionAndAnswer.setOptionString( b.toString() );
    			
    		}
    		
    	}
    	
    }
    
       
}
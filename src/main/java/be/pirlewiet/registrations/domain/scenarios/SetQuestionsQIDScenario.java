package be.pirlewiet.registrations.domain.scenarios;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import be.pirlewiet.registrations.domain.q.QList;
import be.pirlewiet.registrations.model.Vraag;
import be.pirlewiet.registrations.repositories.VraagRepository;
import be.pirlewiet.registrations.web.util.DataGuard;
import be.pirlewiet.registrations.web.util.Utils;

public class SetQuestionsQIDScenario extends Scenario {
	
	protected final Logger logger
		= LoggerFactory.getLogger( this.getClass() );
	
	@Resource
	VraagRepository questionRepository;
	
	@Resource
	DataGuard dataGuard;
	
	public SetQuestionsQIDScenario guard() {
    	this.dataGuard.guard();
    	return this;
    }

	@Override
	public void execute( String... parameters ) {
		
		List<Vraag> questions
			= this.questionRepository.findAll();
		
		QList templateQuestions
			= QList.template();
		
		for ( Vraag loaded : questions ) {
			
			if ( Utils.isEmpty( loaded.getQID() ) ) {
				
				Vraag matched
					= this.matched( loaded , templateQuestions );
				
				if ( matched != null ) {
					
					logger.info( "matched question [{}]", loaded.getUuid() );
					logger.info( "old question tag is [{}]", loaded.getTag() );
					logger.info( "new question tag is [{}]", matched.getTag() );
					logger.info( "new question QID is [{}]", matched.getQID() );
					logger.info( "update & save questions");
					
					loaded.setQID( matched.getQID() );
					loaded.setTag( matched.getTag() );
					
					//this.questionRepository.saveAndFlush( loaded );
					
				}
				else {
					logger.warn( "found not match for question [{}] : [{}]", loaded.getUuid(), loaded.getVraag() );
				}
				
				
			}
			
		}
		
	}
	
	protected Vraag matched( Vraag key, QList list ) {
		
		Vraag found
			= null;
		
		Map<String, List<Vraag>> questionsToMatch
			= list.getVragen();
		
		for ( List<Vraag> l : questionsToMatch.values() ) {
			
			for ( Vraag target : l ) {
				
				if ( key.getVraag().equals( target.getVraag() ) ) {
					found = target;
					break;
				}
				
			}
			
			if ( found != null ) {
				break;
			}
			
		}
		
		return found;
		
	}
	
	

}

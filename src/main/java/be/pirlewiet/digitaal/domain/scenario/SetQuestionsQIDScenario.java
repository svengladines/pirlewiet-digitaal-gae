package be.pirlewiet.digitaal.domain.scenario;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import be.pirlewiet.digitaal.domain.q.QList;
import be.pirlewiet.digitaal.model.QnA;
import be.pirlewiet.digitaal.web.util.DataGuard;
import be.pirlewiet.digitaal.web.util.Utils;

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
		
		this.guard();
		
		List<QnA> questions
			= this.questionRepository.findAll();
		
		QList templateQuestions
			= QList.template();
		
		int max = 100;
		int done = 0;
		int todo = 0;
		
		for ( QnA loaded : questions ) {
			
			if ( Utils.isEmpty( loaded.getQID() ) ) {
				
				QnA matched
					= this.matched( loaded , templateQuestions );
				
				if ( matched != null ) {
					
					if ( done >= max ) {
						todo++;
						continue;
					}
					
					logger.info( "matched question [{}]", loaded.getUuid() );
					logger.info( "old question q [{}]", loaded.getVraag() );
					logger.info( "matched question q [{}]", loaded.getVraag() );
					logger.info( "old question tag is [{}]", loaded.getTag() );
					logger.info( "new question tag is [{}]", matched.getTag() );
					logger.info( "new question QID is [{}]", matched.getQID() );
					logger.info( "update & save questions");
					
					loaded.setQID( matched.getQID() );
					loaded.setTag( matched.getTag() );
					
					try {
					
						this.questionRepository.saveAndFlush( loaded );
						done++;
						
					}
					catch( Exception e ) {
						logger.warn( "failed to update question" ,e );
						break;
					}
					
					
				}
				else {
					logger.warn( "found not match for question [{}] : [{}]", loaded.getUuid(), loaded.getVraag() );
				}
			
			}
			
		}
		
		logger.info( "scenario ended with [{}] updates, todo: [{}]", done, todo );
		
	}
	
	protected QnA matched( QnA key, QList list ) {
		
		QnA found
			= null;
		
		Map<String, List<QnA>> questionsToMatch
			= list.getVragen();
		
		for ( List<QnA> l : questionsToMatch.values() ) {
			
			for ( QnA target : l ) {
				
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

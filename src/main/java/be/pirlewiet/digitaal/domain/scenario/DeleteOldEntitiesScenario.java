package be.pirlewiet.digitaal.domain.scenario;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.transaction.annotation.Transactional;

import be.pirlewiet.digitaal.domain.people.Secretary;
import be.pirlewiet.digitaal.model.Organisation;
import be.pirlewiet.digitaal.model.QuestionAndAnswer;
import be.pirlewiet.digitaal.repositories.OrganisationRepository;
import be.pirlewiet.digitaal.repositories.QuestionAndAnswerRepository;
import be.pirlewiet.digitaal.web.util.DataGuard;

public class DeleteOldEntitiesScenario extends Scenario {
	
	@Resource
	QuestionAndAnswerRepository questionAndAnswerRepository;
	
	@Resource
	DataGuard dataGuard;
	
	public DeleteOldEntitiesScenario guard() {
    	this.dataGuard.guard();
    	return this;
    }

	@Override
	//@Transactional
	public void execute( String... parameters ) {
		
		List<QuestionAndAnswer> qnas
			= this.questionAndAnswerRepository.findAll();
		
		logger.info( "about to delete [{}] qnas", qnas.size() );
		
		this.questionAndAnswerRepository.deleteAllInBatch();
		
	}
	

}

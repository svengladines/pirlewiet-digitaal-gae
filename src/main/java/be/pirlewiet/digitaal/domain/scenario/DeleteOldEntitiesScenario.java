package be.pirlewiet.digitaal.domain.scenario;

import java.util.List;

import javax.annotation.Resource;

import be.pirlewiet.digitaal.model.Application;
import be.pirlewiet.digitaal.model.Enrollment;
import be.pirlewiet.digitaal.model.QuestionAndAnswer;
import be.pirlewiet.digitaal.repository.ApplicationRepository;
import be.pirlewiet.digitaal.repository.EnrollmentRepository;
import be.pirlewiet.digitaal.repository.impl.QuestionAndAnswerRepositoryObjectify;
import be.pirlewiet.digitaal.web.util.DataGuard;

public class DeleteOldEntitiesScenario extends Scenario {
	
	@Resource
	QuestionAndAnswerRepositoryObjectify questionAndAnswerRepository;
	
	@Resource
	EnrollmentRepository enrollmentRepository;
	
	@Resource
	ApplicationRepository applicationRepository;
	
	@Resource
	DataGuard dataGuard;
	
	public DeleteOldEntitiesScenario guard() {
    	this.dataGuard.guard();
    	return this;
    }

	@Override
	//@Transactional
	public void execute( String... parameters ) {

	
		try {
			List<QuestionAndAnswer> qnas
				= this.questionAndAnswerRepository.findAll();
			
			logger.info( "about to delete [{}] qnas", qnas.size() );
			
			for ( QuestionAndAnswer qna : qnas ) {
				this.questionAndAnswerRepository.delete( qna );
			}
			
		}
		catch( Exception e ) {
			logger.warn( "exception while removing old qnas", e  );
		}
		
		/*
		try {
			List<Enrollment> all
				= this.enrollmentRepository.findAll();
			
			logger.info( "about to delete [{}] enrollments", all.size() );
			
			for ( Enrollment enrollment : all ) {
				this.enrollmentRepository.delete( enrollment );
			}
		}
		
		catch( Exception e ) {
			logger.warn( "exception while removing old enrollments", e  );
		}
		*/
		
		try {
			List<Application> all
				= this.applicationRepository.findAll();
			
			logger.info( "about to delete [{}] applications", all.size() );
			
			for ( Application application : all ) {
				this.applicationRepository.delete( application );
			}
		}
		
		catch( Exception e ) {
			logger.warn( "exception while removing old applications", e  );
		}
		
	}
	

}

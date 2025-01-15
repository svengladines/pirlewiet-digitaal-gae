package be.pirlewiet.digitaal.domain.scenario;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import be.pirlewiet.digitaal.model.Application;
import be.pirlewiet.digitaal.model.Enrollment;
import be.pirlewiet.digitaal.model.Person;
import be.pirlewiet.digitaal.model.QuestionAndAnswer;
import be.pirlewiet.digitaal.repository.ApplicationRepository;
import be.pirlewiet.digitaal.repository.EnrollmentRepository;
import be.pirlewiet.digitaal.repository.PersonRepository;
import be.pirlewiet.digitaal.repository.impl.QuestionAndAnswerRepositoryObjectify;
import be.pirlewiet.digitaal.web.util.DataGuard;

public class DeleteOldEntitiesScenario extends Scenario {
	
	@Autowired
	QuestionAndAnswerRepositoryObjectify questionAndAnswerRepository;
	
	@Autowired
	EnrollmentRepository enrollmentRepository;
	
	@Autowired
	ApplicationRepository applicationRepository;
	
	@Autowired
	PersonRepository personRepository;
	
	@Autowired
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
		
		try {
			List<Person> all
				= this.personRepository.findAll();
			
			logger.info( "about to delete [{}] persons", all.size() );
			
			for ( Person one : all ) {
				this.personRepository.delete( one );
			}
		}
		
		catch( Exception e ) {
			logger.warn( "exception while removing old persons", e  );
		}
		
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

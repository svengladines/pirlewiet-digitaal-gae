package be.pirlewiet.digitaal.repository.impl;

import static com.googlecode.objectify.ObjectifyService.ofy;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import com.googlecode.objectify.Key;

import be.pirlewiet.digitaal.model.Application;
import be.pirlewiet.digitaal.model.Organisation;
import be.pirlewiet.digitaal.model.QuestionAndAnswer;
import be.pirlewiet.digitaal.repository.QuestionAndAnswerRepository;

@Repository
public class QuestionAndAnswerRepositoryObjectify implements QuestionAndAnswerRepository {
	
	protected final Logger logger 
		= LoggerFactory.getLogger( this.getClass() );

	@Override
	public List<QuestionAndAnswer> findByEntityUuid(String entityUuid) {
		
		return ofy().load().type(QuestionAndAnswer.class).filter("entityUuid", entityUuid).list();
		
	}

	@Override
	public List<QuestionAndAnswer> findByEntityUuidAndTag(String entityUuid, String tag) {
		
		return ofy().load().type(QuestionAndAnswer.class).filter("entityUuid", entityUuid).filter("tag", tag).list();
		
	}

	@Override
	public QuestionAndAnswer findOneByUuid(String uuid) {
		return ofy().load().type(QuestionAndAnswer.class).filter("uuid", uuid).first().now();
	}

	@Override
	public List<QuestionAndAnswer> findAll() {
		logger.info( "load all qnas");
		return ofy().load().type(QuestionAndAnswer.class).list();
	}

	@Override
	public QuestionAndAnswer saveAndFlush(QuestionAndAnswer qna) {
		logger.info( "store qna with uuid [{}]", new Object[] { qna.getUuid() } );
		Key<QuestionAndAnswer> key = ofy().save().entity( qna ).now();
		QuestionAndAnswer stored = ofy().load().key( key ).now();
		return stored;
	}

	@Override
	public void delete(QuestionAndAnswer qna) {
		ofy().delete().entity(qna);//.now();
		logger.info( "qna with uuid [{}] deleted", qna.getUuid() );
	}

	@Override
	public void deleteAll() {
		
	}
	
}

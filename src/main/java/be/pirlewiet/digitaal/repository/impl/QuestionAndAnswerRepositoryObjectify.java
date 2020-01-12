package be.pirlewiet.digitaal.repository.impl;

import static com.googlecode.objectify.ObjectifyService.ofy;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.googlecode.objectify.Key;

import be.pirlewiet.digitaal.model.QuestionAndAnswer;
import be.pirlewiet.digitaal.repository.QuestionAndAnswerRepository;

@Repository
public class QuestionAndAnswerRepositoryObjectify implements QuestionAndAnswerRepository {

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
		return ofy().load().key(Key.create(QuestionAndAnswer.class, uuid)).now();
	}

	@Override
	public List<QuestionAndAnswer> findAll() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public QuestionAndAnswer saveAndFlush(QuestionAndAnswer qna) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void delete(QuestionAndAnswer qna) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void deleteAll() {
		// TODO Auto-generated method stub
		
	}
	
}

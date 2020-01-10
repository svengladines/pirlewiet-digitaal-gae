package be.pirlewiet.digitaal.repository.impl;

import static com.googlecode.objectify.ObjectifyService.ofy;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.googlecode.objectify.Key;

import be.pirlewiet.digitaal.model.Application;
import be.pirlewiet.digitaal.model.Organisation;
import be.pirlewiet.digitaal.model.QuestionAndAnswer;
import be.pirlewiet.digitaal.repository.QuestionAndAnswerRepository;

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
	
}

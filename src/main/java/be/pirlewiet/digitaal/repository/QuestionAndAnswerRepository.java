package be.pirlewiet.digitaal.repository;

import java.util.List;

import be.pirlewiet.digitaal.model.QuestionAndAnswer;

public interface QuestionAndAnswerRepository {
	
	public QuestionAndAnswer findOneByUuid( String uuid );
	
	public List<QuestionAndAnswer> findByEntityUuid( String uuid );
	public List<QuestionAndAnswer> findByEntityUuidAndTag( String uuid, String tag );
	public List<QuestionAndAnswer> findAll();
	
	public QuestionAndAnswer saveAndFlush( QuestionAndAnswer qna );
	
	public void delete(QuestionAndAnswer qna );
	public void deleteAll();
	
}

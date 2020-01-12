package be.pirlewiet.digitaal.web.dto;

import static be.occam.utils.javax.Utils.list;

import java.util.List;

import be.pirlewiet.digitaal.model.QuestionAndAnswer;
import be.pirlewiet.digitaal.model.QuestionType;


public class QuestionAndAnswerDTO {
	
	private String uuid;
	
	protected String tag;
	
	protected String question;
	
	protected String answer;
	
	protected String entityUuid;
	
	private String qid;
	
	protected QuestionType type;
	
	protected List<String> options
		= list();
	
	private QuestionAndAnswerDTO ( ) {
		
	}
	
	public String getUuid() {
		return uuid;
	}

	public void setUuid(String uuid) {
		this.uuid = uuid;
	}

	public String getTag() {
		return tag;
	}

	public void setTag(String tag) {
		this.tag = tag;
	}

	public String getQuestion() {
		return question;
	}

	public void setQuestion(String question) {
		this.question = question;
	}

	public String getAnswer() {
		return answer;
	}

	public void setAnswer(String answer) {
		this.answer = answer;
	}

	public String getEntityUuid() {
		return entityUuid;
	}

	public void setEntityUuid(String entityUuid) {
		this.entityUuid = entityUuid;
	}

	public String getQid() {
		return qid;
	}

	public void setQid(String qid) {
		this.qid = qid;
	}

	public QuestionType getType() {
		return type;
	}

	public void setType(QuestionType type) {
		this.type = type;
	}
	


	public List<String> getOptions() {
		return options;
	}

	public static QuestionAndAnswerDTO from( QuestionAndAnswer f ) {
		
		QuestionAndAnswerDTO t
			= new QuestionAndAnswerDTO();
		
		t.setUuid( f.getUuid() );
		t.setQuestion( f.getQuestion() );
		t.setAnswer( f.getAnswer() );
		t.setTag( f.getTag() );
		t.setType( f.getType() );
		t.setEntityUuid( f.getEntityUuid() );
		for ( String option : f.getOptions() ) {
			t.getOptions().add( option );
		}
		
		return t;
		
	}
	
	

}

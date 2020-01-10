package be.pirlewiet.digitaal.model;

import static be.occam.utils.javax.Utils.list;

import java.util.List;

import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;

import be.pirlewiet.digitaal.web.dto.QuestionAndAnswerDTO;

@Entity
public class QuestionAndAnswer {
	
	@Id
	private String uuid;
	
	protected String tag;
	
	protected String question;
	
	protected String answer;
	
	protected String entityUuid;
	
	private String qid;
	
	protected QuestionType type;
	
	private String optionString;
		
	protected List<String> options
		= list();
	
	
	private QuestionAndAnswer ( ) {
	}
	
	public QuestionAndAnswer ( String uuid, QuestionType type, String tag, String qid, String question, String... options ) {
		this.uuid = uuid;
		this.type = type;
		this.tag = tag;
		this.qid = qid;
		this.question = question;
		for ( String option : options ) {
			this.options.add( option );
		}
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
	
	public String getOptionString() {
		return optionString;
	}

	public void setOptionString(String optionString) {
		this.optionString = optionString;
	}

	public static QuestionAndAnswer from( QuestionAndAnswerDTO f ) {
		
		QuestionAndAnswer t
			= new QuestionAndAnswer();
		
		t.setUuid( f.getUuid() );
		t.setQuestion( f.getQuestion() );
		t.setAnswer( f.getAnswer() );
		t.setTag( f.getTag() );
		t.setType( f.getType() );
		t.setEntityUuid( f.getEntityUuid() );
		
		return t;
		
	}
		
}

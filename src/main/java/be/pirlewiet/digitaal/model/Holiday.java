package be.pirlewiet.digitaal.model;

import java.util.Date;

import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;
import com.googlecode.objectify.annotation.Index;

import be.pirlewiet.digitaal.web.dto.HolidayDTO;

@Entity
public class Holiday {
	
	@Id
	private Long id;
	
	@Index
	private String uuid;
    
	@Index
    private int year;
    
    protected String name;
    
    protected Period period;

    protected HolidayType type;

    private Date start;
    private Date end;
    private Date deadLine;

    public String getUuid() {
		return uuid;
	}
	public void setUuid(String uuid) {
		this.uuid = uuid;
	}
	public int getYear() {
		return year;
	}
	public void setYear(int year) {
		this.year = year;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Period getPeriod() {
		return period;
	}
	public void setPeriod(Period period) {
		this.period = period;
	}
	public HolidayType getType() {
		return type;
	}
	public void setType(HolidayType type) {
		this.type = type;
	}
	public Date getStart() {
		return start;
	}
	public void setStart(Date start) {
		this.start = start;
	}
	public Date getEnd() {
		return end;
	}
	public void setEnd(Date end) {
		this.end = end;
	}
	public Date getDeadLine() {
		return deadLine;
	}
	public void setDeadLine(Date deadLine) {
		this.deadLine = deadLine;
	}
	
	public static Holiday from ( HolidayDTO f ) {
		
		Holiday t
			= new Holiday();
		
		t.setUuid( f.getUuid() );
		t.setName( f.getName() );
		t.setYear( f.getYear() );
		t.setPeriod( f.getPeriod() );
		t.setStart( f.getStart() );
		t.setEnd( f.getEnd() );
		t.setDeadLine( f.getDeadLine() );
		
		return t;
		
	}
    
}
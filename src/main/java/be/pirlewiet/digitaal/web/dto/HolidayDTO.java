package be.pirlewiet.digitaal.web.dto;

import static be.occam.utils.javax.Utils.*;
import java.util.Date;
import java.util.List;

import be.pirlewiet.digitaal.model.Holiday;
import be.pirlewiet.digitaal.model.HolidayType;
import be.pirlewiet.digitaal.model.Period;

public class HolidayDTO {
	
    protected String uuid;
    
    protected String name;
    
    protected Date start;
    
    protected Date end;
    
    protected Date deadLine;

    protected Period period;
    
    protected HolidayType type;
    
    protected int year;
    
    protected String holidayNames;
    
    protected Boolean isApplicationHoliday;
    
    protected List<HolidayDTO> holidays
    	= list();
    	
	public String getUuid() {
		return uuid;
	}

	public void setUuid(String uuid) {
		this.uuid = uuid;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
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
	
	public int getYear() {
		return year;
	}

	public void setYear(int year) {
		this.year = year;
	}

	public String getHolidayNames() {
		return holidayNames;
	}

	public void setHolidayNames(String holidayNames) {
		this.holidayNames = holidayNames;
	}
	
	public List<HolidayDTO> getHolidays() {
		return holidays;
	}
	
	public Boolean getIsApplicationHoliday() {
		return isApplicationHoliday;
	}

	public HolidayDTO setIsApplicationHoliday(Boolean isApplicationHoliday) {
		this.isApplicationHoliday = isApplicationHoliday;
		return this;
	}

	public static HolidayDTO from ( Holiday f ) {
		
		HolidayDTO t
			= new HolidayDTO();
		
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
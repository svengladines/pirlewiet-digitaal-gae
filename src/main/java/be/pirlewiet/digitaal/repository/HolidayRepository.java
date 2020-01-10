package be.pirlewiet.digitaal.repository;

import java.util.List;

import be.pirlewiet.digitaal.model.Holiday;

public interface HolidayRepository {
	
	public Holiday findByUuid( String id );
	public Holiday findOneByName( String name );
	
	public List<Holiday> findAll();
	
	public Holiday saveAndFlush( Holiday holiday );
	
}

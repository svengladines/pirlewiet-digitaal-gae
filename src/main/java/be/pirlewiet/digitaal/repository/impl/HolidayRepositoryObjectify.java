package be.pirlewiet.digitaal.repository.impl;

import static com.googlecode.objectify.ObjectifyService.ofy;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import com.googlecode.objectify.Key;

import be.pirlewiet.digitaal.model.Application;
import be.pirlewiet.digitaal.model.Enrollment;
import be.pirlewiet.digitaal.model.Holiday;
import be.pirlewiet.digitaal.repository.HolidayRepository;

@Repository
public class HolidayRepositoryObjectify implements HolidayRepository {
	
	protected final Logger logger 
		= LoggerFactory.getLogger( this.getClass() );

	@Override
	public Holiday findByUuid(String uuid) {
		logger.info( "load holiday with uuid [{}]", uuid );
		return ofy().load().type(Holiday.class).filter("uuid", uuid).first().now();
	}

	@Override
	public Holiday findOneByName(String name) {
		
		return ofy().load().type(Holiday.class).filter("name", name).first().now();
		
	}

	@Override
	public List<Holiday> findAll() {
		return ofy().load().type(Holiday.class).list();
	}

	@Override
	public Holiday saveAndFlush(Holiday holiday) {
		logger.info( "store holiday with uuid [{}]", new Object[] { holiday.getUuid() } );
		Key<Holiday> key = ofy().save().entity( holiday ).now();
		Holiday stored = ofy().load().key( key ).now();
		return stored;
	}

}

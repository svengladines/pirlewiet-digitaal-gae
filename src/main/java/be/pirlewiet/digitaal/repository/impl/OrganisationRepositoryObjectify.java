package be.pirlewiet.digitaal.repository.impl;

import static com.googlecode.objectify.ObjectifyService.ofy;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import com.googlecode.objectify.ObjectifyFactory;
import com.googlecode.objectify.ObjectifyService;

import be.pirlewiet.digitaal.model.Organisation;
import be.pirlewiet.digitaal.repository.OrganisationRepository;

@Repository
public class OrganisationRepositoryObjectify implements OrganisationRepository {
	
	protected final Logger logger 
		= LoggerFactory.getLogger( this.getClass() );

	@Override
	public Organisation findByUuid(String uuid) {
		ObjectifyFactory factory = ObjectifyService.factory();
		System.out.println( String.format("ofyx - load from factory [%s]", factory ) );
		logger.info( "load organisation with uuid [{}]", uuid );
		return ofy().load().type(Organisation.class).filter("uuid", uuid).first().now();
	}

	@Override
	public Organisation findOneByCode(String code) {
		return ofy().load().type(Organisation.class).filter("code", code).first().now();
	}

	@Override
	public Organisation findOneByEmail(String email) {
		return ofy().load().type(Organisation.class).filter("email", email).first().now();
	}

	@Override
	public List<Organisation> findAll() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Organisation saveAndFlush(Organisation organisation) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void delete(Organisation organisation) {
		// TODO Auto-generated method stub
		
	}

}

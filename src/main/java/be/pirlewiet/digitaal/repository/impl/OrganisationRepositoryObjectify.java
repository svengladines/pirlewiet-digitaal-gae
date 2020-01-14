package be.pirlewiet.digitaal.repository.impl;

import static com.googlecode.objectify.ObjectifyService.ofy;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import com.googlecode.objectify.Key;
import com.googlecode.objectify.ObjectifyFactory;
import com.googlecode.objectify.ObjectifyService;

import be.pirlewiet.digitaal.model.Address;
import be.pirlewiet.digitaal.model.Entitty;
import be.pirlewiet.digitaal.model.Organisation;
import be.pirlewiet.digitaal.repository.OrganisationRepository;

@Repository
public class OrganisationRepositoryObjectify implements OrganisationRepository {
	
	protected final Logger logger 
		= LoggerFactory.getLogger( this.getClass() );

	@Override
	public Organisation findByUuid(String uuid) {
		logger.info( "load organisation with uuid [{}]", uuid );
		return ofy().load().type(Organisation.class).filter("uuid", uuid).first().now();
	}

	@Override
	public Organisation findOneByCode(String code) {
		logger.info( "load organisation with code [{}]", code );
		Organisation loaded = ofy().load().type(Organisation.class).filter("code", code).first().now();
		if (loaded != null ) {
			logger.info( "loaded organisation has uuid [{}]", loaded.getUuid() );
		}
		else {
			logger.info( "no organisation with code [{}]", code );
		}
		return loaded;
	}

	@Override
	public Organisation findOneByEmail(String email) {
		return ofy().load().type(Organisation.class).filter("email", email).first().now();
	}

	@Override
	public List<Organisation> findAll() {
		logger.info( "load all organisations");
		return ofy().load().type(Organisation.class).list();
	}
	
	@Override
	public List<Organisation> findOld() {
		return ofy().load().type(Organisation.class).filter("id",0).list();
	}

	@Override
	public Organisation saveAndFlush(Organisation organisation) {
		// TODO new ?
		logger.info( "store organisation with uuid [{}], email [{}] and code [{}]", new Object[] { organisation.getUuid(), organisation.getEmail(), organisation.getCode() } );
		Key<Organisation> key = ofy().save().entity( organisation ).now();
		Organisation stored = ofy().load().key( key ).now();
		logger.info( "stored organisation has id [{}] and uuid [{}]", key.getId(), stored.getUuid() );
		
		/*
		Entitty entitty
			= new Entitty();
		entitty.setX( "1" );
		entitty.setY( "A");
		
		ofy().save().entity( entitty ).now();
		*/
		
		return stored;

	}

	@Override
	public void delete(Organisation organisation) {
		ofy().delete().entity( organisation );
	}

}

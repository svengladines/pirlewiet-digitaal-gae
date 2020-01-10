package be.pirlewiet.digitaal.repository;

import java.util.List;

import be.pirlewiet.digitaal.model.Organisation;

public interface OrganisationRepository {
	
	public Organisation findByUuid( String id );
	public Organisation findOneByCode( String code );
	public Organisation findOneByEmail( String email );
	
	public List<Organisation> findAll();
	
	public Organisation saveAndFlush( Organisation organisation );
	
	public void delete( Organisation organisation );

}

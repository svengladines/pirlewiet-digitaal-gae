package be.pirlewiet.registrations.domain;

import javax.annotation.Resource;

import be.pirlewiet.registrations.model.Organisatie;
import be.pirlewiet.registrations.model.Persoon;
import be.pirlewiet.registrations.repositories.OrganisatieRepository;
import be.pirlewiet.registrations.web.util.DataGuard;

public class BuitenWipper extends Persoon {
	
	@Resource
	protected OrganisatieRepository organisatieRepository;
	
	@Resource
	protected DataGuard dataGuard;
	
	public Organisatie whoHasCode( String code ) {
		dataGuard.guard();
		return this.organisatieRepository.findOneByCode( code.replaceAll("\"", "") );
	}
	
	public Organisatie whoHasID( Long id ) {
		dataGuard.guard();
		return this.organisatieRepository.findOneById( id );
	}

}

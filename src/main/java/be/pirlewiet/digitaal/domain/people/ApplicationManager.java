package be.pirlewiet.digitaal.domain.people;

import java.util.List;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.mail.javamail.JavaMailSender;

import be.pirlewiet.digitaal.domain.HeadQuarters;
import be.pirlewiet.digitaal.domain.Mapper;
import be.pirlewiet.digitaal.model.Application;
import be.pirlewiet.digitaal.model.Organisation;
import be.pirlewiet.digitaal.repositories.ApplicationRepository;
import be.pirlewiet.digitaal.web.util.DataGuard;

/*
 * Receives applications, checks them and passes them on to the secretaries, notifying them and the applicant via e-mail.
 * 
 */
public class ApplicationManager {

	protected final Logger logger
		= LoggerFactory.getLogger( this.getClass() );
	
	protected final int currentYear;
	
	@Resource
	protected ApplicationRepository applicationRepository;
	
	public ApplicationManager( int currentYear ) {
		this.currentYear = currentYear;
	}
	
	public List<Application> findByOrganisation( Organisation actor ) {
		
		List<Application> byOrganisationAndYear
			= this.applicationRepository.findByOrganisationUuidAndYear( actor.getUuid(), 2017 );//.findByYear(2017);//.findAll();//.findByOrganisationUuidAndYear( actor.getUuid(), this.currentYear );
		
		// TODO, sort ...
		
		return byOrganisationAndYear;
		
	}
	

}
 
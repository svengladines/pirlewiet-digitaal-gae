package be.pirlewiet.digitaal.application.run;

import java.util.Date;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import be.pirlewiet.digitaal.application.config.ConfiguredVakantieRepository;
import be.pirlewiet.digitaal.domain.people.Secretary;
import be.pirlewiet.digitaal.model.Address;
import be.pirlewiet.digitaal.model.Participant;
import be.pirlewiet.digitaal.model.Gender;
import be.pirlewiet.digitaal.model.Enrollment;
import be.pirlewiet.digitaal.model.Organisation;
import be.pirlewiet.digitaal.model.EnrollmentStatus;
import be.pirlewiet.digitaal.repositories.EnrollmentRepository;
import be.pirlewiet.digitaal.repositories.OrganisatieRepository;
import be.pirlewiet.digitaal.repositories.PersoonRepository;
import be.pirlewiet.digitaal.web.util.PirlewietUtil;

import com.google.appengine.api.datastore.KeyFactory;

public class DevData {
	
	protected final Logger logger
		= LoggerFactory.getLogger( this.getClass() );

	@Resource
	ConfiguredVakantieRepository configuredVakantieRepository;
	
	@Resource
	PersoonRepository persoonRepository;
	
	@Resource
	Secretary secretariaatsMedewerker;
	
	@Resource
	OrganisatieRepository organsiatieRepository;
	
	@PostConstruct
	public void injectData() {
		
		Participant lisa
			= new Participant();
		
		// sarah.setUuid(  Ids.SARAH );
		lisa.setVoorNaam( "Lisa");
		lisa.setFamilieNaam( "Simpson" );
		lisa.setEmail("lisa.simpson@springfield.net");
		lisa.setGeslacht( Gender.F );
		lisa.setGeboorteDatum( new Date() );
		lisa.setTelefoonNummer( "x" );
		
		
		// this.persoonRepository.saveAndFlush( sarah );
		
		Organisation pirlewiet
			= new Organisation();

		pirlewiet.setNaam("Pirlewiet VZW");
		pirlewiet.setCode( "pwt001" );
		pirlewiet.setEmail( PirlewietUtil.PIRLEWIET_EMAIL );
		pirlewiet.setTelefoonNummer( "09020123456" );
		pirlewiet.setAdres( new Address() );
		pirlewiet.setEmail( "info@pirlewiet.be");
		pirlewiet.getAdres().setGemeente( "Gent" );
		pirlewiet.getAdres().setZipCode( "6000" );
		pirlewiet.getAdres().setStraat( "Sint-X" );
		pirlewiet.getAdres().setNummer( "61" );
		pirlewiet = this.organsiatieRepository.saveAndFlush( pirlewiet );
		pirlewiet.setUuid( "pwt-uuid" );
		// pirlewiet.setUuid( KeyFactory.keyToString( pirlewiet.getKey() ) );
		this.organsiatieRepository.saveAndFlush( pirlewiet );
		
		{
		Organisation ocmw
			= new Organisation();
	
		ocmw.setNaam("OCMW Leuven");
		ocmw.setCode( "abc123" );
		ocmw.setEmail( "info@ocmw.be" );
		ocmw.setTelefoonNummer( "016123456" );
		ocmw.setAdres( new Address() );
		ocmw.getAdres().setGemeente( "Leuven" );
		ocmw.getAdres().setStraat( "Oude Markt" );
		ocmw.getAdres().setZipCode( "3000" );
		ocmw.getAdres().setNummer("1");
	
		ocmw = this.organsiatieRepository.saveAndFlush( ocmw );
		ocmw.setUuid( KeyFactory.keyToString( ocmw.getKey() ) );
		this.organsiatieRepository.saveAndFlush( ocmw );
		
		Enrollment lisaSimpson
			= new Enrollment();
		
		lisaSimpson.setVks( this.configuredVakantieRepository.findAll().get( 0 ).getUuid() );
		lisaSimpson.setOrganisatie( ocmw );
		lisaSimpson.getStatus().setValue( EnrollmentStatus.Value.DRAFT );
		lisaSimpson.getContactGegevens().setName( "x" );
		lisaSimpson.getContactGegevens().setPhone( "x" );
		lisaSimpson.getContactGegevens().setEmail( "sven@x" );
		lisaSimpson.getAdres().setGemeente( "x");
		lisaSimpson.getAdres().setZipCode( "6000");
		lisaSimpson.getAdres().setStraat( "x" );
		lisaSimpson.getAdres().setNummer( "x" );
		lisaSimpson.setYear( 2016 );
		
	
		Enrollment created 
			= this.secretariaatsMedewerker.createEnrollment( lisaSimpson );
		
		this.secretariaatsMedewerker.updateDeelnemer( created.getUuid(), lisa ); 
				
		this.logger.info( "lisa id is [{}]", lisa.getUuid() );
		}
		
		{
			Organisation ocmw
				= new Organisation();
		
			ocmw.setNaam("VZW Svekke");
			ocmw.setCode( "hfu608" );
			ocmw.setEmail( "sven.gladines@foo.bar" );
			ocmw.setTelefoonNummer( "016123456" );
			ocmw.setAdres( new Address() );
			ocmw.getAdres().setGemeente( "Leuven" );
			ocmw.getAdres().setStraat( "Oude Markt" );
			ocmw.getAdres().setZipCode( "3000" );
			ocmw.getAdres().setNummer("1");
		
			ocmw = this.organsiatieRepository.saveAndFlush( ocmw );
			ocmw.setUuid( KeyFactory.keyToString( ocmw.getKey() ) );
			this.organsiatieRepository.saveAndFlush( ocmw );
		}
		
	}
	
}

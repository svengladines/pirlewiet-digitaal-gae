package be.pirlewiet.registrations.application.run;

import java.util.Date;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import be.pirlewiet.registrations.application.config.ConfiguredVakantieRepository;
import be.pirlewiet.registrations.model.Adres;
import be.pirlewiet.registrations.model.Deelnemer;
import be.pirlewiet.registrations.model.Gender;
import be.pirlewiet.registrations.model.InschrijvingX;
import be.pirlewiet.registrations.model.Organisatie;
import be.pirlewiet.registrations.model.Status;
import be.pirlewiet.registrations.repositories.EnrollmentRepository;
import be.pirlewiet.registrations.repositories.OrganisatieRepository;
import be.pirlewiet.registrations.repositories.PersoonRepository;
import be.pirlewiet.registrations.web.util.PirlewietUtil;

import com.google.appengine.api.datastore.KeyFactory;

public class DevData {
	
	protected final Logger logger
		= LoggerFactory.getLogger( this.getClass() );

	@Resource
	ConfiguredVakantieRepository configuredVakantieRepository;
	
	@Resource
	PersoonRepository persoonRepository;
	
	@Resource
	EnrollmentRepository inschrijvingXRepository;
	
	@Resource
	OrganisatieRepository organsiatieRepository;
	
	@PostConstruct
	public void injectData() {
		
		Deelnemer lisa
			= new Deelnemer();
		
		// sarah.setUuid(  Ids.SARAH );
		lisa.setVoorNaam( "Lisa");
		lisa.setFamilieNaam( "Simpson" );
		lisa.setEmail("lisa.simpson@springfield.net");
		lisa.setGeslacht( Gender.F );
		lisa.setGeboorteDatum( new Date() );
		lisa.setTelefoonNummer( "x" );
		
		
		// this.persoonRepository.saveAndFlush( sarah );
		
		Organisatie pirlewiet
			= new Organisatie();

		pirlewiet.setNaam("Pirlewiet VZW");
		pirlewiet.setCode( "pwt001" );
		pirlewiet.setEmail( PirlewietUtil.PIRLEWIET_EMAIL );
		pirlewiet.setTelefoonNummer( "09020123456" );
		pirlewiet.setAdres( new Adres() );
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
		Organisatie ocmw
			= new Organisatie();
	
		ocmw.setNaam("OCMW Leuven");
		ocmw.setCode( "abc123" );
		ocmw.setEmail( "info@ocmw.be" );
		ocmw.setTelefoonNummer( "016123456" );
		ocmw.setAdres( new Adres() );
		ocmw.getAdres().setGemeente( "Leuven" );
		ocmw.getAdres().setStraat( "Oude Markt" );
		ocmw.getAdres().setZipCode( "3000" );
		ocmw.getAdres().setNummer("1");
	
		ocmw = this.organsiatieRepository.saveAndFlush( ocmw );
		ocmw.setUuid( KeyFactory.keyToString( ocmw.getKey() ) );
		this.organsiatieRepository.saveAndFlush( ocmw );
		
		InschrijvingX lisaSimpson
			= new InschrijvingX();
		lisaSimpson.setVks( this.configuredVakantieRepository.findAll().get( 0 ).getUuid() );
		lisaSimpson.setOrganisatie( ocmw );
		lisaSimpson.getStatus().setValue( Status.Value.DRAFT );
		lisaSimpson.getContactGegevens().setName( "x" );
		lisaSimpson.getContactGegevens().setPhone( "x" );
		lisaSimpson.getContactGegevens().setEmail( "sven@x" );
		lisaSimpson.getAdres().setGemeente( "x");
		lisaSimpson.getAdres().setZipCode( "6000");
		lisaSimpson.getAdres().setStraat( "x" );
		lisaSimpson.getAdres().setNummer( "x" );
		lisaSimpson.setYear( 2016 );
		
	
		lisaSimpson = this.inschrijvingXRepository.saveAndFlush( lisaSimpson );
		
		lisaSimpson.getDeelnemers().add( lisa );
		
		this.inschrijvingXRepository.saveAndFlush( lisaSimpson );
		
		lisa.setUuid( KeyFactory.keyToString( lisa.getKey() ) );
		lisaSimpson.setUuid( KeyFactory.keyToString( lisaSimpson.getKey() ) );
		
		this.inschrijvingXRepository.saveAndFlush( lisaSimpson );
		
		this.logger.info( "lisa id is [{}]", lisa.getUuid() );
		}
		
		{
			Organisatie ocmw
				= new Organisatie();
		
			ocmw.setNaam("VZW Svekke");
			ocmw.setCode( "hfu608" );
			ocmw.setEmail( "sven.gladines@foo.bar" );
			ocmw.setTelefoonNummer( "016123456" );
			ocmw.setAdres( new Adres() );
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

package be.pirlewiet.registrations.application.run;

import java.util.Date;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import be.occam.utils.timing.Timing;
import be.pirlewiet.registrations.application.config.ConfiguredVakantieRepository;
import be.pirlewiet.registrations.model.Adres;
import be.pirlewiet.registrations.model.Deelnemer;
import be.pirlewiet.registrations.model.Geslacht;
import be.pirlewiet.registrations.model.InschrijvingX;
import be.pirlewiet.registrations.model.Organisatie;
import be.pirlewiet.registrations.model.Periode;
import be.pirlewiet.registrations.model.Status;
import be.pirlewiet.registrations.model.Vakantie;
import be.pirlewiet.registrations.model.VakantieType;
import be.pirlewiet.registrations.repositories.InschrijvingXRepository;
import be.pirlewiet.registrations.repositories.OrganisatieRepository;
import be.pirlewiet.registrations.repositories.PersoonRepository;
import be.pirlewiet.registrations.repositories.VakantieRepository;

import com.google.appengine.api.datastore.KeyFactory;

public class DevData {
	
	protected final Logger logger
		= LoggerFactory.getLogger( this.getClass() );

	@Resource
	ConfiguredVakantieRepository configuredVakantieRepository;
	
	@Resource
	PersoonRepository persoonRepository;
	
	@Resource
	InschrijvingXRepository inschrijvingXRepository;
	
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
		lisa.setGeslacht( Geslacht.V );
		lisa.setGeboorteDatum( new Date() );
		lisa.setTelefoonNummer( "x" );
		
		
		// this.persoonRepository.saveAndFlush( sarah );
		
		Organisatie pirlewiet
			= new Organisatie();

		pirlewiet.setNaam("Pirlewiet VZW");
		pirlewiet.setCode( "pwt001" );
		pirlewiet.setEmail( "secretariaat@pirlewiet.be" );
		pirlewiet.setTelefoonNummer( "09020123456" );
		pirlewiet.setAdres( new Adres() );
		pirlewiet.getAdres().setGemeente( "Gent" );
		pirlewiet.getAdres().setZipCode( "6000" );
		pirlewiet.getAdres().setStraat( "Sint-X" );
		pirlewiet.getAdres().setNummer( "61" );
		pirlewiet = this.organsiatieRepository.saveAndFlush( pirlewiet );
		pirlewiet.setUuid( "pwt-uuid" );
		// pirlewiet.setUuid( KeyFactory.keyToString( pirlewiet.getKey() ) );
		this.organsiatieRepository.saveAndFlush( pirlewiet );
		
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
		lisaSimpson.getContactGegevens().setNaam( "x" );
		lisaSimpson.getContactGegevens().setTelefoonNummer( "x" );
		lisaSimpson.getContactGegevens().setEmail( "sven@x" );
		lisaSimpson.getAdres().setGemeente( "x");
		lisaSimpson.getAdres().setZipCode( "6000");
		lisaSimpson.getAdres().setStraat( "x" );
		lisaSimpson.getAdres().setNummer( "x" );
		
	
		lisaSimpson = this.inschrijvingXRepository.saveAndFlush( lisaSimpson );
		
		lisaSimpson.getDeelnemers().add( lisa );
		
		this.inschrijvingXRepository.saveAndFlush( lisaSimpson );
		
		lisa.setUuid( KeyFactory.keyToString( lisa.getKey() ) );
		lisaSimpson.setUuid( KeyFactory.keyToString( lisaSimpson.getKey() ) );
		
		this.inschrijvingXRepository.saveAndFlush( lisaSimpson );
		
		this.logger.info( "lisa id is [{}]", lisa.getUuid() );
		
	}
	
}

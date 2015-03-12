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
		
		Deelnemer sarah
			= new Deelnemer();
		
		// sarah.setUuid(  Ids.SARAH );
		sarah.setVoorNaam( "Lisa");
		sarah.setFamilieNaam( "Simpson" );
		sarah.setEmail("lisa.simpson@springfield.net");
		sarah.setGeslacht( Geslacht.V );
		sarah.setGeboorteDatum( new Date() );
		sarah.setTelefoonNummer( "x" );
		
		
		// this.persoonRepository.saveAndFlush( sarah );
		
		Organisatie pirlewiet
			= new Organisatie();

		pirlewiet.setNaam("Pirlewiet VZW");
		pirlewiet.setCode( "pwt001" );
		pirlewiet.setEmail( "info@pirlewiet.be" );
		pirlewiet.setTelefoonNummer( "09020123456" );
		pirlewiet.setAdres( new Adres() );
		pirlewiet.getAdres().setGemeente( "Gent" );
		pirlewiet.getAdres().setStraat( "Sint-X" );
		pirlewiet.getAdres().setNummer( "61" );
		pirlewiet = this.organsiatieRepository.saveAndFlush( pirlewiet );
		pirlewiet.setUuid( KeyFactory.keyToString( pirlewiet.getKey() ) );
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
		ocmw.getAdres().setNummer("1");
	
		ocmw = this.organsiatieRepository.saveAndFlush( ocmw );
		ocmw.setUuid( KeyFactory.keyToString( ocmw.getKey() ) );
		this.organsiatieRepository.saveAndFlush( ocmw );
		
		InschrijvingX sarahKika1
			= new InschrijvingX();
		sarahKika1.setVks( this.configuredVakantieRepository.findAll().get( 0 ).getUuid() );
		sarahKika1.setOrganisatie( ocmw );
		sarahKika1.getStatus().setValue( Status.Value.SUBMITTED );
		sarahKika1.getContactGegevens().setNaam( "x" );
		sarahKika1.getContactGegevens().setTelefoonNummer( "x" );
		sarahKika1.getContactGegevens().setEmail( "sven@x" );
		sarahKika1.getAdres().setGemeente( "x");
		sarahKika1.getAdres().setStraat( "x" );
		sarahKika1.getAdres().setNummer( "x" );
	
		sarahKika1 = this.inschrijvingXRepository.saveAndFlush( sarahKika1 );
		
		sarahKika1.getDeelnemers().add( sarah );
		
		this.inschrijvingXRepository.saveAndFlush( sarahKika1 );
		
		sarah.setUuid( KeyFactory.keyToString( sarah.getKey() ) );
		sarahKika1.setUuid( KeyFactory.keyToString( sarahKika1.getKey() ) );
		
		this.inschrijvingXRepository.saveAndFlush( sarahKika1 );
		
		this.logger.info( "sarah's id is [{}]", sarah.getUuid() );
		
	}
	
}

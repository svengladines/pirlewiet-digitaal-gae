package be.pirlewiet.digitaal.jtests;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import be.pirlewiet.digitaal.model.Address;
import be.pirlewiet.digitaal.model.Organisation;
import be.pirlewiet.digitaal.repositories.AddressRepository;
import be.pirlewiet.digitaal.repositories.OrganisationRepository;

import com.google.appengine.api.datastore.KeyFactory;

public class DevData {
	
	protected final Logger logger
		= LoggerFactory.getLogger( this.getClass() );

	@Resource
	OrganisationRepository organsiationRepository;
	
	@Resource
	AddressRepository addressRepository;
	
	@PostConstruct
	public void injectData() {
		
		/*
		
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
		*/
		
		{
			Organisation vzwSvekke
				= new Organisation();
		
			vzwSvekke.setName("VZW Svekke");
			vzwSvekke.setCode( "svk013" );
			vzwSvekke.setEmail( "sven.gladines@foo.bar" );
			vzwSvekke.setPhone( "016123456" );
		
			vzwSvekke = this.organsiationRepository.saveAndFlush( vzwSvekke );
			vzwSvekke.setUuid( KeyFactory.keyToString( vzwSvekke.getKey() ) );
			
			Address vzwSvekkeAddress = new Address();
			vzwSvekkeAddress.setZipCode( "3370" );
			vzwSvekkeAddress.setCity("Neervelp");
			vzwSvekkeAddress.setStreet( "Vertrijksestraat" );
			//vzwSvekkeAddress.setNumber( "33" );
			vzwSvekkeAddress = this.addressRepository.saveAndFlush( vzwSvekkeAddress );
			vzwSvekkeAddress.setUuid( KeyFactory.keyToString( vzwSvekke.getKey() ) );
			vzwSvekkeAddress = this.addressRepository.saveAndFlush( vzwSvekkeAddress );
			
			vzwSvekke.setAddressUuid( vzwSvekkeAddress.getUuid() );
			
			this.organsiationRepository.saveAndFlush( vzwSvekke );
		}
		
	}
	
}

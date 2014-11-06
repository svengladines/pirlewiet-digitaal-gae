package be.pirlewiet.registrations.domain;

import javax.annotation.Resource;
import javax.persistence.Transient;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Transactional;

import be.pirlewiet.registrations.model.Adres;
import be.pirlewiet.registrations.model.Organisatie;
import be.pirlewiet.registrations.model.Vragen;
import be.pirlewiet.registrations.repositories.OrganisatieRepository;
import be.pirlewiet.registrations.web.util.DataGuard;

public class OrganisationManager {
	
	protected final Logger logger
		= LoggerFactory.getLogger( this.getClass() );
	
	@Resource
	protected OrganisatieRepository organisatieRepository;
	
	@Transient
	@Resource
	protected Vragen vragen;
	
	@Resource
	BuitenWipper buitenWipper;
	
	@Resource
	DataGuard dataGuard;
	
    public OrganisationManager() {
    }
    
    public OrganisationManager guard() {
    	this.dataGuard.guard();
    	return this;
    }

    @Transactional(readOnly=false)
    public Organisatie updateAdres( long id, Adres adres ) {
    	
    	Organisatie organisatie
    		= this.organisation( id );
    	
    	if ( isEmpty( adres.getGemeente() ) ) {
    		throw new RuntimeException("Geef de gemeente op");
    	}
    	
    	if ( isEmpty( adres.getStraat() ) ) {
    		throw new RuntimeException("Geef de straat op");
    	}
    	
    	if ( isEmpty( adres.getNummer() ) ) {
    		throw new RuntimeException("Geef huis- en eventueel busnummer op");
    	}
    	
    	organisatie.setAdres( adres );
		
		this.organisatieRepository.saveAndFlush( organisatie );
    	
    	return organisatie;
    	
    }
    
    @Transactional( readOnly=false )
    public Organisatie update( long id, Organisatie organisation ) {
    	
    	Organisatie loaded 
    		= organisation( id );
    	
    	if ( loaded == null ) {
    		return null;
    	}
    	
    	if ( this.isEmpty( organisation.getNaam() ) ) {
    		throw new RuntimeException("Geef de naam van de organisatie op");
    	}
    	
    	if ( this.isEmpty( organisation.getEmail() ) ) {
    		throw new RuntimeException("Geef het e-mailadres van de organisatie op");
    	}
    	
    	if ( this.isEmpty( organisation.getTelefoonNummer() ) && this.isEmpty( organisation.getGsmNummer() ) ) {
    		throw new RuntimeException("Geef ofwel een telefoonnummer ofwel een gsm-nummer op");
    	}
    	
    	loaded.setTelefoonNummer( organisation.getTelefoonNummer() );
    	loaded.setGsmNummer( organisation.getGsmNummer() );
    	loaded.setNaam( organisation.getNaam() );
    	loaded.setEmail( organisation.getEmail() );
    	loaded.setAlternativeEmail( organisation.getAlternativeEmail() );
    	
    	Organisatie saved
    		= this.organisatieRepository.saveAndFlush( loaded );
    	
    	saved.setAdres( loaded.getAdres() );
    	
    	logger.info( "updated organisation with id [{}]", saved.getId() );
    	
    	return saved;
    	
    }
    
    public boolean isInComplete( Organisatie organisation ) {
    	
    	boolean incomplete 
    		= false;
    	
    	incomplete |= isEmpty( organisation.getNaam() ) ;
    	incomplete |= isEmpty( organisation.getTelefoonNummer() ) ;
    	incomplete |= isEmpty( organisation.getEmail() ) ;
    	if ( organisation.getAdres() != null ) {
	    	// incomplete |= isEmpty( organisation.getAdres().getZipCode() );
	    	incomplete |= isEmpty( organisation.getAdres().getGemeente() );
	    	incomplete |= isEmpty( organisation.getAdres().getStraat() );
	    	incomplete |= isEmpty( organisation.getAdres().getNummer() );
    	}
    	
    	return incomplete;
    	
    }
    
    protected boolean isEmpty( String x ) {
    	
    	return ( x == null ) || ( x.isEmpty() );
    	
    }
    
    @Transactional(readOnly=true)
    public Organisatie organisation( Long id ) {
    	
    	Organisatie organsiatie
    		= this.organisatieRepository.findOneById( id );
    	
    	if ( organsiatie != null ) {
    		logger.info( "found organsiatie with id []", id );
    	}
    	
    	return organsiatie;
    	
    	
    }
    
    public boolean isOrganisationOutDated( Organisatie organisation ) {
    	
    	return ( organisation.getUpdated() == null );
    	
    }
    
}
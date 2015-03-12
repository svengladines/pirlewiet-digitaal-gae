package be.pirlewiet.registrations.domain;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;
import javax.persistence.Transient;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Transactional;

import be.pirlewiet.registrations.model.Adres;
import be.pirlewiet.registrations.model.Organisatie;
import be.pirlewiet.registrations.model.Vragen;
import be.pirlewiet.registrations.repositories.OrganisatieRepository;
import be.pirlewiet.registrations.utils.PirlewietUtil;
import be.pirlewiet.registrations.web.util.DataGuard;

public class OrganisationManager {
	
	protected final Logger logger
		= LoggerFactory.getLogger( this.getClass() );
	
	protected final Comparator<Organisatie> lastUpdatedFirst
		= new Comparator<Organisatie>() {

			@Override
			public int compare(Organisatie o1, Organisatie o2) {
				if ( o1.getUpdated() == null ) {
					return 1;
				}
				else if ( o2.getUpdated() == null ) {
					return -1;
				}
				else {
					return o1.getUpdated().after( o2.getUpdated() ) ? -1 : 1;
				}
			}
		
		};
	
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
    public Organisatie updateAdres( String id, Adres adres ) {
    	
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
    	organisatie.setUpdated( new Date() );
		
		this.organisatieRepository.saveAndFlush( organisatie );
    	
    	return organisatie;
    	
    }
    
    @Transactional( readOnly=false )
    public Organisatie update( String id, Organisatie organisation ) {
    	
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
    	loaded.setUpdated( new Date() );
    	
    	Organisatie saved
    		= this.organisatieRepository.saveAndFlush( loaded );
    	
    	saved.setAdres( loaded.getAdres() );
    	
    	logger.info( "updated organisation with uiid [{}]", saved.getUuid() );
    	
    	return saved;
    	
    }
    
    public boolean isInComplete( Organisatie organisation, boolean checkAddress ) {
    	
    	boolean incomplete 
    		= false;
    	
    	incomplete |= isEmpty( organisation.getNaam() ) ;
    	incomplete |= isEmpty( organisation.getTelefoonNummer() ) ;
    	incomplete |= isEmpty( organisation.getEmail() ) ;
    	if ( checkAddress && ( organisation.getAdres() != null ) ) {
	    	incomplete |= isEmpty( organisation.getAdres().getZipCode() );
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
    public Organisatie organisation( String id ) {
    	
    	Organisatie organsiatie
    		= this.organisatieRepository.findByUuid( id );
    	
    	if ( organsiatie != null ) {
    		logger.info( "found organsiatie with id []", id );
    	}
    	
    	return organsiatie;
    	
    }
    
    @Transactional(readOnly=true)
    public List<Organisatie> all( ) {
    	
    	List<Organisatie> all
    		= this.organisatieRepository.findAll();
    	
    	List<Organisatie> filtered
			= new ArrayList<Organisatie>();
    	
    	for ( Organisatie organisation : all ) {
    		organisation.getAdres().hashCode();
    		
    		if ( ! PirlewietUtil.isPirlewiet( organisation ) ) {
    			filtered.add( organisation );
    		}
    	}
    	
    	
    	Collections.sort( filtered , this.lastUpdatedFirst );
    	
    	return filtered;
    	
    	
    }
    
    public boolean isOrganisationOutDated( Organisatie organisation ) {
    	
    	return ( organisation.getUpdated() == null );
    	
    }
    
}
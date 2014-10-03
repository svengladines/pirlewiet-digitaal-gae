package be.pirlewiet.registrations.domain;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import javax.annotation.Resource;
import javax.persistence.Transient;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Transactional;

import be.pirlewiet.registrations.model.Adres;
import be.pirlewiet.registrations.model.ContactGegevens;
import be.pirlewiet.registrations.model.Deelnemer;
import be.pirlewiet.registrations.model.InschrijvingX;
import be.pirlewiet.registrations.model.Organisatie;
import be.pirlewiet.registrations.model.Status;
import be.pirlewiet.registrations.model.Vakantie;
import be.pirlewiet.registrations.model.Vraag;
import be.pirlewiet.registrations.model.Vragen;
import be.pirlewiet.registrations.repositories.DeelnemerRepository;
import be.pirlewiet.registrations.repositories.InschrijvingVakantieRepository;
import be.pirlewiet.registrations.repositories.InschrijvingXRepository;
import be.pirlewiet.registrations.repositories.PersoonRepository;
import be.pirlewiet.registrations.repositories.VakantieRepository;
import be.pirlewiet.registrations.repositories.VraagRepository;
import be.pirlewiet.registrations.utils.PirlewietUtil;
import be.pirlewiet.registrations.web.util.DataGuard;

public class SecretariaatsMedewerker {
	
	protected final Logger logger
		= LoggerFactory.getLogger( this.getClass() );
	
	@Resource
	protected InschrijvingXRepository inschrijvingXRepository;
	
	@Resource
	protected InschrijvingVakantieRepository inschrijvingVakantieRepository;
	
	@Transient
	@Resource
	protected PersoonRepository persoonRepository;
	
	@Transient
	@Resource
	protected DeelnemerRepository deelnemerRepository;
	
	@Transient
	@Resource
	protected VakantieRepository vakantieRepository;
	
	@Transient
	@Resource
	protected VraagRepository vraagRepository;
	
	@Transient
	@Resource
	protected Vragen vragen;
	
	@Resource
	BuitenWipper buitenWipper;
	
	@Resource
	DataGuard dataGuard;
	
    public SecretariaatsMedewerker() {
    }
    
    public SecretariaatsMedewerker guard() {
    	this.dataGuard.guard();
    	return this;
    }

    @Transactional(readOnly=false)
    public InschrijvingX ontvangInschrijving( InschrijvingX inschrijving ) {
    	
    	if ( inschrijving.getId() != 0 ) {
    		return null;
    	}
    	
    	inschrijving.setStatus( Status.NIEUW );
    	
    	Organisatie organisatie 
    		= this.buitenWipper.whoHasID( inschrijving.getOrganisatie().getId() );
    	
    	inschrijving.setOrganisatie( organisatie );
    	
    	InschrijvingX saved
    		= this.inschrijvingXRepository.saveAndFlush( inschrijving );

    	// stupid GAE ... set id manually
    	saved.setId( saved.getKey().getId() );
    	this.inschrijvingXRepository.saveAndFlush( saved );
    	
    	Deelnemer deelnemer
			= new Deelnemer();
    	
    	saved = this.addDeelnemer( saved , deelnemer );
    	
    	/*
    	for ( String key : vragen.getVragen().keySet() ) {
    		
    		List<Vraag> list
    			= vragen.getVragen().get( key );
    		
    		for ( Vraag vraag : list ) {
    			Vraag savedVraag = this.vraagRepository.save( vraag );
    			savedVraag.setId( savedVraag.getId() );
    			this.vraagRepository.save( vraag );
    			saved.getVragen().add( savedVraag );
    		}
    		
    	}
    	*/
    	
    	return saved;
    }
    
    @Transactional(readOnly=false)
    public Vakantie maakVakantie( Vakantie vakantie ) {
    	
    	if ( vakantie.getId() != 0 ) {
    		return null;
    	}
    	
    	Vakantie saved
    		= this.vakantieRepository.saveAndFlush( vakantie );

    	// stupid GAE ... set id manually
    	saved.setId( saved.getKey().getId() );
    	this.vakantieRepository.saveAndFlush( saved );
    	
    	/*
    	for ( String key : vragen.getVragen().keySet() ) {
    		
    		List<Vraag> list
    			= vragen.getVragen().get( key );
    		
    		for ( Vraag vraag : list ) {
    			Vraag savedVraag = this.vraagRepository.save( vraag );
    			saved.getVragen().add( savedVraag );
    		}
    		
    	}
    	*/
    	
    	return saved;
    }
    
    
    
    @Transactional(readOnly=true)
    public List<InschrijvingX> actueleInschrijvingen( Organisatie organisatie ) {
    	
    	List<InschrijvingX> inschrijvingen
    		= new ArrayList<InschrijvingX>( );
    	
    	List<InschrijvingX> all
    		= this.inschrijvingXRepository.findAll();// = PirlewietUtil.isPirlewiet( organisatie ) ? this.inschrijvingXRepository.findAll() : this.inschrijvingXRepository.findByOrganisatie( organisatie );
    		// SGL|| findByOrganisatie does not work in GAE ... at least not out-of-the-box
    	
    	Iterator<InschrijvingX> it
    		= all.iterator();
    	
    	boolean isPirlewiet
    		= PirlewietUtil.isPirlewiet( organisatie );
    	
    	while ( it.hasNext() ) {
    		
    		InschrijvingX inschrijving
    			= it.next();
    		
    		if ( (!isPirlewiet) && ( inschrijving.getOrganisatie().getId() != organisatie.getId() ) ) {
    			continue;
    		}
    		
    		// needed to lazily load the collections (need to be initialized to transfer to json)
    		inschrijving.getDeelnemers().size();
    		inschrijving.getVragen().size();
    		
    		for ( Long vk : inschrijving.getVakanties() ) {
    			Vakantie vakantie = this.vakantieRepository.findById( vk );
    			
    			if ( inschrijving.getVakantieDetails() == null ) {
    				inschrijving.setVakantieDetails( vakantie.getNaam() );
    			}
    			else {
    				inschrijving.setVakantieDetails( new StringBuilder( inschrijving.getVakantieDetails() ).append( ", " ).append( vakantie.getNaam() ).toString()  );
    			}
    			
    		}
    		
    		inschrijving.getDeelnemers().size();
			inschrijving.getVragen().size();
			inschrijving.getVakanties().size();
			// same for GAE for embedded
			inschrijving.getContactGegevens().hashCode();
    		
    		inschrijvingen.add( inschrijving );
    		
    		/**
    		// enkel toevoegen als de vakantie nog niet voorbij is
    		if ( inschrijving.getVakantie().getEindDatum().after( new Date() ) ) {
    			inschrijvingen.add( inschrijving );
    		} 
    		**/
    		
    	}
    	
    	return inschrijvingen;
    	
    }
    
    @Transactional(readOnly=true)
    public InschrijvingX inschrijving( Long id ) {
    	
    	InschrijvingX inschrijving
    		= this.inschrijvingXRepository.findOneById( id );
    	
    	if ( inschrijving != null ) {
    	
    		logger.info( "found inschrijving with id []", id );
	    	// needed to lazily load the lists (can't load them eagerly, unfortunately)
			inschrijving.getDeelnemers().size();
			inschrijving.getVragen().size();
			inschrijving.getVakanties().size();
			// same for GAE for embedded
			inschrijving.getContactGegevens().hashCode();
			logger.info( "found inschrijving with adres hash []", inschrijving.getAdres().hashCode()  );
			
    	}
    	
    	return inschrijving;
    	
    	
    }
    
    public Deelnemer deelnemer( Long id ) {
    	
    	Deelnemer deelnemer
			= this.deelnemerRepository.findById( id );
    	
    	return deelnemer;    	
    	
    }
    
    @Transactional(readOnly=false)
    public  InschrijvingX pasAan( InschrijvingX inschrijving ) {
    	
    	this.inschrijvingXRepository.saveAndFlush( inschrijving );
    	
    	return inschrijving;
    	
    }
    
    @Transactional(readOnly=false)
    public InschrijvingX addVraag( long inschrijvingID, Vraag vraag ) {
    	
    	InschrijvingX inschrijving
    		= this.inschrijving( inschrijvingID );
    	
    	vraag.setId( this.vraagRepository.saveAndFlush( vraag ).getId() );
    	
    	inschrijving.getVragen().add( vraag );
    	
    	this.inschrijvingXRepository.saveAndFlush( inschrijving );
    	
    	return inschrijving;
    	
    }
    
    @Transactional(readOnly=false)
    public InschrijvingX updateVakanties( long inschrijvingID, Long[] vakanties ) {
    	
    	InschrijvingX inschrijving
    		= this.inschrijving( inschrijvingID );
    	
    	if ( vakanties.length == 0 ) {
    		throw new RuntimeException( "Selecteer minstens 1 vakantie" );
    	}
    	
		inschrijving.getVakanties().clear();
		
		for ( Long vk : vakanties ) {
			
			Vakantie v
    			= this.vakantieRepository.findById( vk );
    	
			if ( v == null ) {
				throw new RuntimeException("vakantie not found");
			}
			
			inschrijving.getVakanties().add( vk );
			
		}
    	
		this.inschrijvingXRepository.saveAndFlush( inschrijving );
    	
    	return inschrijving;
    	
    }
    
    @Transactional(readOnly=false)
    public InschrijvingX updateContact( long inschrijvingID, ContactGegevens contactGegevens ) {
    	
    	InschrijvingX inschrijving
    		= this.inschrijving( inschrijvingID );
    	
    	if ( contactGegevens.getNaam().isEmpty() ) {
    		throw new RuntimeException("Geef de naam van de contactpersoon op");
    	}
    	
    	if ( contactGegevens.getTelefoonNummer().isEmpty() ) {
    		throw new RuntimeException("Geef de GSM-nummer of een telefoonnummer van de contactpersoon op");
    	}
    	
    	if ( contactGegevens.getEmail().isEmpty() ) {
    		throw new RuntimeException("Geef het e-mailadres van de contactpersoon op");
    	}
    	
    	inschrijving.setContactGegevens( contactGegevens );
		
		this.inschrijvingXRepository.saveAndFlush( inschrijving );
    	
    	return inschrijving;
    	
    }
    
    @Transactional(readOnly=false)
    public InschrijvingX updateAdres( long inschrijvingID, Adres adres ) {
    	
    	InschrijvingX inschrijving
    		= this.inschrijving( inschrijvingID );
    	
    	if ( isEmpty( adres.getGemeente() ) ) {
    		throw new RuntimeException("Geef de gemeente op");
    	}
    	
    	if ( isEmpty( adres.getStraat() ) ) {
    		throw new RuntimeException("Geef de straat op");
    	}
    	
    	if ( isEmpty( adres.getNummer() ) ) {
    		throw new RuntimeException("Geef huis- en eventueel busnummer op");
    	}
    	
    	inschrijving.setAdres( adres );
		
		this.inschrijvingXRepository.saveAndFlush( inschrijving );
    	
    	return inschrijving;
    	
    }
    
    @Transactional(readOnly=false)
    public  Deelnemer updateDeelnemer( Long id, Deelnemer deelnemer ) {
    	
    	Deelnemer d
    		= this.deelnemerRepository.findById( id );
    	
    	if ( d != null ) {
    		
    		if ( isEmpty(  deelnemer.getVoorNaam() ) ) {
    			throw new RuntimeException("Geef de voornaam van de deelnemer op");
    		}
    		if ( isEmpty( deelnemer.getFamilieNaam() ) ) {
    			throw new RuntimeException("Geef de familienaam van de deelnemer op");
    		}
    		
    		if ( deelnemer.getGeslacht() == null ) {
    			throw new RuntimeException("Geef het geslacht van de deelnemer op");
    		}
    		if ( deelnemer.getGeboorteDatum() == null ) {
    			throw new RuntimeException("Geef de geboortedatum van de deelnemer op");
    		}
    		if ( isEmpty( deelnemer.getTelefoonNummer() ) && isEmpty( deelnemer.getMobielNummer() ) ) {
    			throw new RuntimeException("Geef een telefoonnummer of GSM-nummer van de deelnemer op");
    		}
    		
    		d.setVoorNaam( deelnemer.getVoorNaam() );
    		d.setFamilieNaam( deelnemer.getFamilieNaam() );
    		d.setGeboorteDatum( deelnemer.getGeboorteDatum() );
    		d.setEmail( deelnemer.getEmail() );
    		d.setTelefoonNummer( deelnemer.getTelefoonNummer() );
    		d.setMobielNummer( deelnemer.getMobielNummer() );
    		d = this.deelnemerRepository.saveAndFlush( d );
    	}
    	
    	return d;
    	
    }
    
    @Transactional(readOnly=true)
    public List<Vakantie> actueleVakanties( ) {
    	
    	List<Vakantie> all
    		= this.vakantieRepository.findAll();
    	
    	List<Vakantie> vakanties
    		= new ArrayList<Vakantie>( all.size() );
    	
    	for ( Vakantie v : all ) {
    		if ( v.getEindInschrijving().after( new Date() ) ) {
    			vakanties.add( v );
    		}
    	}
    	
    	return vakanties;
    	
    }
    
    protected InschrijvingX addDeelnemer( InschrijvingX inschrijving, Deelnemer deelnemer ) {
    	
    	// InschrijvingX inschrijving
    	// 	= this.inschrijving( inschrijvingID );
    	
    	long inschrijvingID
    		= inschrijving.getId();
    	
    	if ( inschrijving == null ) {
    		throw new RuntimeException( "no inschrijving with id ]" + inschrijvingID + "]");
    	}
    	
    	// ^GAE this.persoonRepository.saveAndFlush( deelnemer );
    	// this.persoonRepository.saveAndFlush( deelnemer );
    	
    	inschrijving.getDeelnemers().add( deelnemer );
    	
    	this.inschrijvingXRepository.saveAndFlush( inschrijving );
    	
    	deelnemer.setId( deelnemer.getKey().getId() );
    	
    	this.inschrijvingXRepository.saveAndFlush( inschrijving );
    	
    	return inschrijving;
    	
    }
    
    protected boolean isEmpty( String x ) {
    	
    	return ( x == null ) || ( x.isEmpty() );
    	
    }
 
}
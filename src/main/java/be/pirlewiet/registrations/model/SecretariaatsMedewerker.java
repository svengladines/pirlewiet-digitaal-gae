package be.pirlewiet.registrations.model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import javax.annotation.Resource;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Transient;

import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import be.pirlewiet.registrations.repositories.DeelnemerRepository;
import be.pirlewiet.registrations.repositories.InschrijvingXRepository;
import be.pirlewiet.registrations.repositories.PersoonRepository;
import be.pirlewiet.registrations.repositories.VakantieRepository;
import be.pirlewiet.registrations.repositories.VraagRepository;

@Entity
@DiscriminatorValue(value = "S")
public class SecretariaatsMedewerker extends Persoon {
	
	@OneToOne
    @JoinColumn(unique=true)
    private Credentials credentials;
	
	@Transient
	@Resource
	protected InschrijvingXRepository inschrijvingXRepository;
	
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
	
    public SecretariaatsMedewerker() {
    }

    public SecretariaatsMedewerker(String voornaam, String familienaam) {
        super(voornaam, familienaam);
    }
    
    public Credentials getCredentials() {
        return credentials;
    }

    public void setCredentials(Credentials credentials) {
        this.credentials = credentials;
    }

    @Transactional(readOnly=false)
    public InschrijvingX ontvangInschrijving( InschrijvingX inschrijving ) {
    	
    	if ( inschrijving.getId() != 0 ) {
    		return null;
    	}
    	
    	inschrijving.setStatus( Status.NIEUW );
    	
    	InschrijvingX saved
    		= this.inschrijvingXRepository.saveAndFlush( inschrijving );
    	
    	Deelnemer deelnemer
			= new Deelnemer();
    	
    	saved = this.addDeelnemer( saved.getId() , deelnemer );
    	
    	for ( String key : vragen.getVragen().keySet() ) {
    		
    		List<Vraag> list
    			= vragen.getVragen().get( key );
    		
    		for ( Vraag vraag : list ) {
    			Vraag savedVraag = this.vraagRepository.save( vraag );
    			saved.getVragen().add( savedVraag );
    		}
    		
    	}
    	
    	return saved;
    }
    
    @Transactional(readOnly=true)
    public List<InschrijvingX> actueleInschrijvingen( Organisatie organisatie ) {
    	
    	List<InschrijvingX> inschrijvingen
    		= new ArrayList<InschrijvingX>( );
    	
    	List<InschrijvingX> all
    		= this.inschrijvingXRepository.findByOrganisatie( organisatie );
    	
    	Iterator<InschrijvingX> it
    		= all.iterator();
    	
    	while ( it.hasNext() ) {
    		
    		InschrijvingX inschrijving
    			= it.next();
    		
    		// needed to lazily load the collections (need to be initialized to transfer to json)
    		inschrijving.getDeelnemers().size();
    		inschrijving.getVragen().size();
    		
    		// enkel toevoegen als de vakantie nog niet voorbij is
    		if ( inschrijving.getVakantie().getEindDatum().after( new Date() ) ) {
    			inschrijvingen.add( inschrijving );
    		} 
    		
    	}
    	
    	return inschrijvingen;
    	
    }
    
    @Transactional(readOnly=true)
    public InschrijvingX inschrijving( Long id ) {
    	
    	InschrijvingX inschrijving
    		= this.inschrijvingXRepository.findById( id );
    	
    	if ( inschrijving != null ) {
    	
	    	// needed to lazily load the lists (can't load them eagerly, unfortunately)
			inschrijving.getDeelnemers().size();
			inschrijving.getVragen().size();
			
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
    public InschrijvingX addDeelnemer( long inschrijvingID, Deelnemer deelnemer ) {
    	
    	InschrijvingX inschrijving
    		= this.inschrijving( inschrijvingID );
    	
    	deelnemer.setId( this.persoonRepository.saveAndFlush( deelnemer ).getId() );
    	
    	inschrijving.getDeelnemers().add( deelnemer );
    	
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
    public InschrijvingX setVakantie( long inschrijvingID, Vakantie vakantie ) {
    	
    	InschrijvingX inschrijving
    		= this.inschrijving( inschrijvingID );
    	
    	Vakantie v
    		= this.vakantieRepository.findById( vakantie.getId() );
    	
		if ( v == null ) {
			throw new RuntimeException("vakantie not found");
		}
    	
	    inschrijving.setVakantie( v );
	    this.inschrijvingXRepository.saveAndFlush( inschrijving );
    	
    	return inschrijving;
    	
    }
    
    @Transactional(readOnly=false)
    public InschrijvingX setAlternatief( long inschrijvingID, Vakantie alternatief ) {
    	
    	InschrijvingX inschrijving
    		= this.inschrijving( inschrijvingID );
    	
    	Vakantie alt
    		= null;
    	
    	if ( alternatief.getId() != 0 ) {
    		alt	= this.vakantieRepository.findById( alternatief.getId() );
    		if ( alt == null ) {
    			throw new RuntimeException("vakantie niet gevonden");
    		}
    		
    		if ( inschrijving.getVakantie() != null ){
	    		if ( alt.getId() == inschrijving.getVakantie().getId() ) {
	    			throw new RuntimeException("De alternatieve vakantie mag niet dezelfde zijn als de verkozen vakantie");
	    		}
    		}
    	}
    	else {
    		logger.info("reset alternatief");
    	}
    	
	    inschrijving.setAlternatief( alt );
	    this.inschrijvingXRepository.saveAndFlush( inschrijving );
    	
    	return inschrijving;
    	
    }
    
    public  Deelnemer pasAan( Deelnemer deelnemer ) {
    	
    	Deelnemer updated 
    		= this.deelnemerRepository.saveAndFlush( deelnemer );
    	
    	return updated;
    	
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
    
    @Transactional(readOnly=true)
    public List<Vakantie> alternatieven( Vakantie vakantie ) {
    	
    	if ( vakantie == null ) {
    		return Arrays.asList();
    	}
    	
    	List<Vakantie> equals
    		= this.vakantieRepository.findByJaarAndPeriodeAndType(vakantie.getJaar(), vakantie.getPeriode(), vakantie.getType() );
    	
    	List<Vakantie> alternatieven
    		= new ArrayList<Vakantie>( equals.size() );
    	
    	for ( Vakantie eq : equals ) {
    		if ( eq.getId() != vakantie.getId() ) {
    			alternatieven.add( eq );
    		}
    	}
    	
    	return alternatieven;
    	
    }
 
}
package be.pirlewiet.registrations.domain;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;

import javax.annotation.Resource;
import javax.mail.internet.MimeMessage;
import javax.persistence.Transient;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.transaction.annotation.Transactional;

import freemarker.template.Configuration;
import freemarker.template.Template;

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
import be.pirlewiet.registrations.repositories.OrganisatieRepository;
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
	
	@Resource
	protected VraagRepository vraagRepository;
	
	@Resource
	protected OrganisatieRepository organisatieRepository;
	
	@Transient
	@Resource
	protected Vragen vragen;
	
	@Resource
	BuitenWipper buitenWipper;
	
	@Resource
	PostBode postBode;
	
	@Resource
	JavaMailSender javaMailSender;
	
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
    	
    	inschrijving.setStatus( new Status( Status.Value.DRAFT ) );
    	
    	Organisatie organisatie 
    		= this.buitenWipper.whoHasID( inschrijving.getOrganisatie().getId() );
    	// to detach ...
    	organisatie.getAdres();
    	
    	inschrijving.setOrganisatie( organisatie );
    	
    	InschrijvingX saved
    		= this.inschrijvingXRepository.saveAndFlush( inschrijving );

    	// stupid GAE ... set id manually
    	saved.setId( saved.getKey().getId() );
    	saved = this.inschrijvingXRepository.saveAndFlush( saved );
    	
    	Deelnemer deelnemer
			= new Deelnemer();
    	
    	saved = this.addDeelnemer( saved , deelnemer );
    	
    	for ( String key : vragen.getVragen().keySet() ) {
    		
    		List<Vraag> list
    			= vragen.getVragen().get( key );
    		
    		for ( Vraag vraag : list ) {
    			this.addVraag( inschrijving, vraag );
    		}
    		
    	}
    	
    	
    	
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
    	
    	logger.info( "total number of enrollments: [{}]", all.size() );
    	
    	Iterator<InschrijvingX> it
    		= all.iterator();
    	
    	boolean isPirlewiet
    		= PirlewietUtil.isPirlewiet( organisatie );
    	
    	logger.info( "find enrollments for organisation: [{}]", organisatie.getId() );
    	
    	while ( it.hasNext() ) {
    		
    		InschrijvingX inschrijving
    			= it.next();
    		
    		this.detach( inschrijving );
    		
    		if ( (!isPirlewiet) && ( ! inschrijving.getOrganisatie().getId().equals( organisatie.getId() ) ) ) {
    			logger.info( "x.[{}] versus o.[{}], no match", inschrijving.getOrganisatie().getId(), organisatie.getId() );
    			continue;
    		}
    		
    		logger.info( "x.[{}] versus o.[{}], match", inschrijving.getOrganisatie().getId(), organisatie.getId() );
    		
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
    public InschrijvingX findInschrijving( Long id ) {
    	
    	InschrijvingX inschrijving
    		= this.inschrijvingXRepository.findOneById( id );
    	
    	this.detach( inschrijving );
    	
    	return inschrijving;
    	
    	
    }
    
    protected void detach( InschrijvingX inschrijving ) {
    	
    	if ( inschrijving != null ) {
        	
	    	// needed to lazily load the lists (can't load them eagerly, unfortunately)
			inschrijving.getDeelnemers().size();
			inschrijving.getVragen().size();
			logger.info( "[{}]; number of holidays [{}]", inschrijving.getId(), inschrijving.getVakanties().size() );
			// same for GAE for embedded
			inschrijving.getContactGegevens().hashCode();
			inschrijving.getVragen().hashCode();
			inschrijving.getAdres().hashCode();
			inschrijving.getStatus().hashCode();
			
			StringTokenizer tok
				= new StringTokenizer( inschrijving.getVks().trim(), ",", false );
			
			while( tok.hasMoreTokens() ) {
				
				String t
					= tok.nextToken().trim();
				
				if ( t.length() == 0 ) {
					continue;
				}
				
				Vakantie v 
					= this.vakantieRepository.findById( Long.valueOf( t.trim() ) ); 
				
				if ( v != null ) {
					inschrijving.getVakanties().add ( v );
				}
				else {
					throw new RuntimeException( "no vakantie with id [" + t.trim() + "]" );
				}
				
			}
			
    	}
    	
    }
    
    public Deelnemer deelnemer( Long id ) {
    	
    	Deelnemer deelnemer
			= this.deelnemerRepository.findById( id );
    	
    	return deelnemer;    	
    	
    }
    
    @Transactional(readOnly=false)
    public InschrijvingX updateVragenLijst( long inschrijvingID, List<Vraag> vragen ) {
    	
    	InschrijvingX inschrijving
    		= this.findInschrijving( inschrijvingID );
    	
    	for ( Vraag vraag : vragen ) {
    	
	    	for ( Vraag v : inschrijving.getVragen() ) {
	    		
	    		if ( v.getId() == ( vraag.getId() ) ) {
	    			
	    			v.setAntwoord( vraag.getAntwoord() );
	    			break;
	    			
	    		}
	    		
	    	}
    	}
    	
    	inschrijving = this.inschrijvingXRepository.saveAndFlush( inschrijving );
    	
    	boolean complete
    		= true;
    	
    	for ( Vraag vraag : inschrijving.getVragen() ) {
			
			if ( ! Vraag.Type.Label.equals( vraag.getType() ) ) {
				if ( ( vraag.getAntwoord() == null ) || ( vraag.getAntwoord().isEmpty() ) ) {
					complete = false;
					break;
				}
			}
			
		}
    	
    	if ( ! complete ) {
    		throw new RuntimeException("Beantwoord alle vragen in de vragenlijst. Vul eventueel 'Niet van toepassing' (NVT) in." );
    	}
    	
    	return inschrijving;
    	
    }
    
    @Transactional(readOnly=false)
    public InschrijvingX updateVakanties( long inschrijvingID, String vakanties ) {
    	
    	InschrijvingX inschrijving
    		= this.findInschrijving( inschrijvingID );
    	
    	inschrijving.setVks( vakanties.replaceAll("\"", "" ) );
    	
		inschrijving = this.inschrijvingXRepository.saveAndFlush( inschrijving );
    	
    	return inschrijving;
    	
    }
    
    @Transactional(readOnly=false)
    public InschrijvingX updateContact( long inschrijvingID, ContactGegevens contactGegevens ) {
    	
    	InschrijvingX inschrijving
    		= this.findInschrijving( inschrijvingID );
    	
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
    public InschrijvingX updateInschrijvingsAdres( long inschrijvingID, Adres adres ) {
    	
    	InschrijvingX inschrijving
    		= this.findInschrijving( inschrijvingID );
    	
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
    public Organisatie updateOrganisatieAdres( long id, Adres adres ) {
    	
    	Organisatie organisatie
    		= this.organisatie( id );
    	
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
    
    @Transactional(readOnly=false)
    public  Deelnemer updateDeelnemer( Long id, Deelnemer deelnemer ) {
    	
    	InschrijvingX inschrijving
    		= this.findInschrijving( id );
    	
    	Deelnemer d
    		= inschrijving.getDeelnemers().get( 0 );
    	
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
    		d.setGeslacht( deelnemer.getGeslacht() );
    		d.setGeboorteDatum( deelnemer.getGeboorteDatum() );
    		d.setEmail( deelnemer.getEmail() );
    		d.setTelefoonNummer( deelnemer.getTelefoonNummer() );
    		d.setMobielNummer( deelnemer.getMobielNummer() );
    		
    		this.inschrijvingXRepository.saveAndFlush( inschrijving );
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
    	// 	= this.findInschrijving( inschrijvingID );
    	
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
    
    protected InschrijvingX addVraag( InschrijvingX inschrijving, Vraag vraag ) {
    	
    	// ^GAE this.persoonRepository.saveAndFlush( deelnemer );
    	// this.persoonRepository.saveAndFlush( deelnemer );
    	
    	inschrijving.getVragen().add( vraag );
    	this.inschrijvingXRepository.saveAndFlush( inschrijving );
    	
    	vraag.setId( vraag.getKey().getId() );
    	
    	this.inschrijvingXRepository.saveAndFlush( inschrijving );
    	
    	return inschrijving;
    	
    }
    
    @Transactional( readOnly=false )
    public Organisatie addOrganisatie( Organisatie organisatie ) {
    	
    	Organisatie saved 
    		= this.organisatieRepository.saveAndFlush( organisatie );
    	
    	saved.setId( saved.getKey().getId() );
    	
    	saved 
			= this.organisatieRepository.saveAndFlush( organisatie );
    	
    	logger.info( "created organisation with id [{}]", saved.getId() );
    	
    	return saved;
    	
    }
    
    @Transactional(readOnly=false)
	public void updateStatus( long inschrijvingID, Status status ) {
	
		InschrijvingX loaded
			= this.findInschrijving( inschrijvingID );
		
		Status.Value oldStatus
			= loaded.getStatus().getValue();
		
		loaded.getStatus().setValue( status.getValue() );
		loaded.getStatus().setComment( status.getComment() );
		this.inschrijvingXRepository.saveAndFlush( loaded );

		if ( Boolean.TRUE.equals( status.getEmailMe() ) ) {
			
			MimeMessage message
				= formatUpdateMessage( loaded, oldStatus );

			if ( message != null ) {
				
				postBode.deliver( message );
				logger.info( "email sent" );
				
			}
			
		}
			
	}
    
    protected boolean isEmpty( String x ) {
    	
    	return ( x == null ) || ( x.isEmpty() );
    	
    }
    
    @Transactional(readOnly=true)
    public Organisatie organisatie( Long id ) {
    	
    	Organisatie organsiatie
    		= this.organisatieRepository.findOneById( id );
    	
    	if ( organsiatie != null ) {
    		logger.info( "found organsiatie with id []", id );
    	}
    	
    	return organsiatie;
    	
    	
    }
    
    protected MimeMessage formatUpdateMessage( InschrijvingX inschrijving, Status.Value oldStatus ) {
		
		MimeMessage message
			= null;
		
		Configuration cfg 
			= new Configuration();
	
		try {
			
			InputStream tis
				= this.getClass().getResourceAsStream( "/templates/pirlewiet/update.tmpl" );
			
			Template template 
				= new Template("code", new InputStreamReader( tis ), cfg );
			
			Map<String, Object> model = new HashMap<String, Object>();
					
			model.put( "organisatie", inschrijving.getOrganisatie() );
			model.put( "inschrijving", inschrijving );
			model.put( "vakanties", vakantieDetails( inschrijving ) );
			model.put( "old", oldStatus );
			model.put( "id", Long.toString( inschrijving.getId() ) );
			
			StringWriter bodyWriter 
				= new StringWriter();
			
			template.process( model , bodyWriter );
			
			bodyWriter.flush();
				
			message = this.javaMailSender.createMimeMessage();
			MimeMessageHelper helper = new MimeMessageHelper( message,"utf-8" );
				
			helper.setFrom( "sven.gladines@gmail.com" );
			helper.setTo( "sven.gladines@telenet.be" );
			helper.setReplyTo( inschrijving.getContactGegevens().getEmail() );
			helper.setSubject( "Aanpassing inschrijving" );
				
			String text
				= bodyWriter.toString();
				
			logger.info( "email text is [{}]", text );
				
			helper.setText(text, true);
				
		}
		catch( Exception e ) {
			logger.warn( "could not write e-mail", e );
			throw new RuntimeException( e );
		}
		
		return message;
    	
    }
    
    protected String vakantieDetails( InschrijvingX inschrijving ) {
		
		StringBuilder b
			= new StringBuilder();
		
		for ( Vakantie v : inschrijving.getVakanties() ) {
			
			if ( v != null ) {
				b.append( b.length() == 0 ? v.getNaam() : new StringBuilder(", ").append( v.getNaam() ));
			}
			
		}
		
		return b.toString();
		
	} 
 
}
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

import javax.annotation.Resource;
import javax.mail.internet.MimeMessage;
import javax.persistence.Transient;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.transaction.annotation.Transactional;

import be.pirlewiet.registrations.application.config.PirlewietApplicationConfig;
import be.pirlewiet.registrations.model.Adres;
import be.pirlewiet.registrations.model.ContactGegevens;
import be.pirlewiet.registrations.model.Deelnemer;
import be.pirlewiet.registrations.model.InschrijvingX;
import be.pirlewiet.registrations.model.Organisatie;
import be.pirlewiet.registrations.model.Status;
import be.pirlewiet.registrations.model.Status.Value;
import be.pirlewiet.registrations.model.Tags;
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

import com.google.appengine.api.datastore.KeyFactory;

import freemarker.template.Configuration;
import freemarker.template.Template;

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
	
	@Resource
	BuitenWipper buitenWipper;
	
	@Resource
	Detacher detacher;
	
	@Resource
	PostBode postBode;
	
	@Resource
	JavaMailSender javaMailSender;
	
	@Resource
	DataGuard dataGuard;
	
	protected String emailAddress;
	
    public SecretariaatsMedewerker( String emailAddress ) {
    	this.emailAddress = emailAddress;
    }
    
    public SecretariaatsMedewerker guard() {
    	this.dataGuard.guard();
    	return this;
    }

    @Transactional(readOnly=false)
    public InschrijvingX ontvangInschrijving( InschrijvingX inschrijving ) {
    	
    	if ( inschrijving.getUuid() != null ) {
    		return null;
    	}
    	
    	inschrijving.setStatus( new Status( Status.Value.DRAFT ) );
    	
    	Organisatie organisatie 
    		= this.buitenWipper.whoHasID( inschrijving.getOrganisatie().getUuid() );
    	// to detach ...
    	organisatie.getAdres();
    	
    	inschrijving.setOrganisatie( organisatie );
    	
    	InschrijvingX saved
    		= this.inschrijvingXRepository.saveAndFlush( inschrijving );

    	// stupid GAE ... set id manually
    	saved.setUuid( KeyFactory.keyToString( saved.getKey() ) );
    	saved = this.inschrijvingXRepository.saveAndFlush( saved );
    	
    	Deelnemer deelnemer
			= new Deelnemer();
    	
    	saved = this.addDeelnemer( saved , deelnemer );
    	
    	Vragen vragen
    		= new Vragen();
    	
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
    	
    	if ( vakantie.getUuid() != null ) {
    		return null;
    	}
    	
    	Vakantie saved
    		= this.vakantieRepository.saveAndFlush( vakantie );

    	// stupid GAE ... set id manually
    	saved.setUuid( KeyFactory.keyToString( saved.getKey() ) );
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
    	
    	logger.info( "find enrollments for organisation: [{}]", organisatie.getUuid() );
    	
    	while ( it.hasNext() ) {
    		
    		InschrijvingX inschrijving
    			= it.next();
    		
    		inschrijving = this.detach( inschrijving.getUuid() );
    		
    		if ( (!isPirlewiet) && ( ! inschrijving.getOrganisatie().getUuid().equals( organisatie.getUuid() ) ) ) {
    			logger.info( "x.[{}] versus o.[{}], no match", inschrijving.getOrganisatie().getUuid(), organisatie.getUuid() );
    			continue;
    		}
    		
    		if ( isPirlewiet ) {
    			
    			if ( Value.DRAFT.equals( inschrijving.getStatus().getValue() ) ) {
    				logger.info( "enrollment with status DRAFT not sent to pirlewiet", inschrijving.getUuid() );
    				continue;
    			}
    			
    		}
    		
    		logger.info( "x.[{}] versus o.[{}], match", inschrijving.getOrganisatie().getUuid(), organisatie.getUuid() );
    		
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
    public InschrijvingX findInschrijving( String uuid ) {
    	
    	InschrijvingX inschrijving 
    		= this.detach( uuid );
    	
    	return inschrijving;
    	
    	
    }
    
    protected InschrijvingX detach( String uuid ) {
    	
    	return this.detacher.findAndDetach( uuid );
    	
    }
    
    public Deelnemer deelnemer( String id ) {
    	
    	Deelnemer deelnemer
			= this.deelnemerRepository.findByUuid( id );
    	
    	return deelnemer;    	
    	
    }
    
    @Transactional(readOnly=false)
    public InschrijvingX updateVragenLijst( String inschrijvingID, List<Vraag> vragen ) {
    	
    	InschrijvingX inschrijving
    		= this.findInschrijving( inschrijvingID );
    	
    	for ( Vraag vraag : vragen ) {
    	
	    	for ( Vraag v : inschrijving.getVragen() ) {
	    		
	    		if ( v.getUuid().equals( ( vraag.getUuid() ) ) ) {
	    			
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
				if ( ! Tags.TAG_MEDIC.equals( vraag.getTag() ) ) { 
					if ( ( vraag.getAntwoord() == null ) || ( vraag.getAntwoord().isEmpty() ) ) {
						logger.info( "question [{}][{}] was not answered", vraag.getUuid(), vraag.getVraag() );
						complete = false;
						break;
					}
				}
			}
			
		}
    	
    	if ( ! complete ) {
    		throw new RuntimeException("Beantwoord alle vragen in de vragenlijst. Vul eventueel 'Niet van toepassing' (NVT) in." );
    	}
    	
    	return inschrijving;
    	
    }
    
    @Transactional(readOnly=false)
    public InschrijvingX updateVakanties( String inschrijvingID, String vakanties ) {
    	
    	InschrijvingX inschrijving
    		= this.findInschrijving( inschrijvingID );
    	
    	inschrijving.setVks( vakanties.replaceAll("\"", "" ) );
    	
		inschrijving = this.inschrijvingXRepository.saveAndFlush( inschrijving );
    	
    	return inschrijving;
    	
    }
    
    @Transactional(readOnly=false)
    public InschrijvingX updateContact( String inschrijvingID, ContactGegevens contactGegevens ) {
    	
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
    public InschrijvingX updateInschrijvingsAdres( String inschrijvingID, Adres adres ) {
    	
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
    public Organisatie updateOrganisatieAdres( String id, Adres adres ) {
    	
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
    public  Deelnemer updateDeelnemer( String id, Deelnemer deelnemer ) {
    	
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
    	
    	String inschrijvingID
    		= inschrijving.getUuid();
    	
    	if ( inschrijving == null ) {
    		throw new RuntimeException( "no inschrijving with id ]" + inschrijvingID + "]");
    	}
    	
    	// ^GAE this.persoonRepository.saveAndFlush( deelnemer );
    	// this.persoonRepository.saveAndFlush( deelnemer );
    	
    	inschrijving.getDeelnemers().add( deelnemer );
    	
    	this.inschrijvingXRepository.saveAndFlush( inschrijving );
    	
    	deelnemer.setUuid( KeyFactory.keyToString( deelnemer.getKey() ) );
    	
    	this.inschrijvingXRepository.saveAndFlush( inschrijving );
    	
    	return inschrijving;
    	
    }
    
    protected InschrijvingX addVraag( InschrijvingX inschrijving, Vraag vraag ) {
    	
    	// ^GAE this.persoonRepository.saveAndFlush( deelnemer );
    	// this.persoonRepository.saveAndFlush( deelnemer );
    	
    	inschrijving.getVragen().add( vraag );
    	this.inschrijvingXRepository.saveAndFlush( inschrijving );
    	
    	vraag.setUuid( KeyFactory.keyToString( vraag.getKey() ) );
    	
    	this.inschrijvingXRepository.saveAndFlush( inschrijving );
    	
    	return inschrijving;
    	
    }
    
    @Transactional( readOnly=false )
    public Organisatie addOrganisatie( Organisatie organisatie ) {
    	
    	String email
    		= organisatie.getEmail();
    	
    	Organisatie existing
    		= this.organisatieRepository.findOneByEmail( email );
    	
    	if ( existing != null ) {
    		throw new PirlewietException( String.format( "Er bestaat al een organisatie met het e-mailadres [%s]. Geef een ander e-mailadres op om een nieuwe organisatie aan te maken.", email ) );
    	}
    	
    	String code 
    		= this.buitenWipper.guard().uniqueCode();
    	
    	organisatie.setCode( code );
    	
    	Organisatie saved 
    		= this.organisatieRepository.saveAndFlush( organisatie );
    	
    	saved.setUuid( KeyFactory.keyToString( saved.getKey() ) );
    	
    	saved 
			= this.organisatieRepository.saveAndFlush( saved );
    	
    	
    	
    	return saved;
    	
    }
    
    @Transactional(readOnly=false)
	public void updateStatus( String inschrijvingID, Status status ) {
	
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
    
    public void sendInitialCode( Organisatie organisation ) {
    	
    	
    	
    }
    
    protected boolean isEmpty( String x ) {
    	
    	return ( x == null ) || ( x.isEmpty() );
    	
    }
    
    @Transactional(readOnly=true)
    public Organisatie organisatie( String uuid ) {
    	
    	Organisatie organsiatie
    		= this.organisatieRepository.findByUuid( uuid );
    	
    	if ( organsiatie != null ) {
    		logger.info( "found organsiatie with id [{}]", uuid );
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
			model.put( "id", inschrijving.getUuid() );
			
			StringWriter bodyWriter 
				= new StringWriter();
			
			template.process( model , bodyWriter );
			
			bodyWriter.flush();
				
			message = this.javaMailSender.createMimeMessage();
			MimeMessageHelper helper = new MimeMessageHelper( message,"utf-8" );
				
			helper.setFrom( PirlewietApplicationConfig.EMAIL_ADDRESS );
			helper.setTo( inschrijving.getContactGegevens().getEmail() );
			helper.setReplyTo( "info@pirlewiet.be" );
			helper.setSubject( "Uw inschrijving bij Pirlewiet" );
				
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
    
    protected MimeMessage formatReadyToRockMessage( Organisatie organisation ) {
		
  		MimeMessage message
  			= null;
  		
  		Configuration cfg 
  			= new Configuration();
  	
  		try {
  			
  			InputStream tis
  				= this.getClass().getResourceAsStream( "/templates/pirlewiet/ready.tmpl" );
  			
  			Template template 
  				= new Template("code", new InputStreamReader( tis ), cfg );
  			
  			Map<String, Object> model = new HashMap<String, Object>();
  					
  			model.put( "organisation", organisation );
  			model.put( "from", this.emailAddress );
  			
  			StringWriter bodyWriter 
  				= new StringWriter();
  			
  			template.process( model , bodyWriter );
  			
  			bodyWriter.flush();
  				
  			message = this.javaMailSender.createMimeMessage();
  			MimeMessageHelper helper = new MimeMessageHelper( message,"utf-8" );
  				
  			helper.setFrom( this.emailAddress, "Pirlewiet Digitaal" );
  			helper.setTo( organisation.getEmail() );
  			helper.setReplyTo( "info@pirlewiet.be" );
  			helper.setSubject( "Pirlewiet VZW - digitale inschrijvingen 2015" );
  				
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
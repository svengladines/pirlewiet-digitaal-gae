package be.pirlewiet.registrations.domain;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;
import javax.mail.internet.MimeMessage;
import javax.persistence.Transient;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.transaction.annotation.Transactional;

import be.pirlewiet.registrations.application.config.ConfiguredVakantieRepository;
import be.pirlewiet.registrations.application.config.PirlewietApplicationConfig;
import be.pirlewiet.registrations.domain.exception.IncompleteObjectException;
import be.pirlewiet.registrations.domain.exception.PirlewietException;
import be.pirlewiet.registrations.domain.q.QList;
import be.pirlewiet.registrations.model.Adres;
import be.pirlewiet.registrations.model.ContactGegevens;
import be.pirlewiet.registrations.model.Deelnemer;
import be.pirlewiet.registrations.model.InschrijvingX;
import be.pirlewiet.registrations.model.Organisatie;
import be.pirlewiet.registrations.model.Status;
import be.pirlewiet.registrations.model.Status.Value;
import be.pirlewiet.registrations.model.Tags;
import be.pirlewiet.registrations.model.Vakantie;
import be.pirlewiet.registrations.model.VakantieType;
import be.pirlewiet.registrations.model.Vraag;
import be.pirlewiet.registrations.repositories.DeelnemerRepository;
import be.pirlewiet.registrations.repositories.EnrollmentRepository;
import be.pirlewiet.registrations.repositories.OrganisatieRepository;
import be.pirlewiet.registrations.repositories.PersoonRepository;
import be.pirlewiet.registrations.repositories.VraagRepository;
import be.pirlewiet.registrations.web.util.DataGuard;
import be.pirlewiet.registrations.web.util.PirlewietUtil;

import com.google.appengine.api.datastore.KeyFactory;

import freemarker.template.Configuration;
import freemarker.template.Template;

public class SecretariaatsMedewerker {
	
	protected final Logger logger
		= LoggerFactory.getLogger( this.getClass() );
	
	protected final Comparator<Vakantie> vacationNameComparator
		= new Comparator<Vakantie>() {

			@Override
			public int compare(Vakantie o1, Vakantie o2) {
				return o1.getNaam().compareTo( o2.getNaam() );
			}
		
		};
		

	protected final Comparator<InschrijvingX> mostRecent
			= new Comparator<InschrijvingX>() {

		@Override
		public int compare(InschrijvingX o1, InschrijvingX o2) {
			
			Date d1 = o1.getInschrijvingsdatum();
			Date d2 = o2.getInschrijvingsdatum();
			
			if ( d1 == null ) {
				return 1;
			}
			else if ( d2 == null ) {
				return -1;
			}
			else {
				return d1.compareTo( d2 );
			}
			
		}
			
	};
	
	@Resource
	protected EnrollmentRepository inschrijvingXRepository;
	
	@Transient
	@Resource
	protected PersoonRepository persoonRepository;
	
	@Transient
	@Resource
	protected DeelnemerRepository deelnemerRepository;
	
	@Transient
	@Resource
	protected ConfiguredVakantieRepository configuredVakantieRepository;
	
	@Resource
	protected VraagRepository vraagRepository;
	
	@Resource
	protected OrganisatieRepository organisatieRepository;
	
	@Resource
	protected HolidayManager holidayManager;
	
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
	
    public SecretariaatsMedewerker( ) {
    }
    
    public SecretariaatsMedewerker guard() {
    	this.dataGuard.guard();
    	return this;
    }

    @Transactional(readOnly=false)
    public InschrijvingX createEnrollment( InschrijvingX inschrijving ) {
    	
    	if ( inschrijving.getUuid() != null ) {
    		return null;
    	}
    	
    	InschrijvingX saved
    		= null;
    	
    	inschrijving.setStatus( new Status( Status.Value.DRAFT ) );
    	
    	Organisatie organisatie 
			= this.buitenWipper.whoHasID( inschrijving.getOrganisatie().getUuid() );
    	// to detach ...
    	organisatie.getAdres();
    	inschrijving.setOrganisatie( organisatie );
    	
    	// TODO, remove hardcoded year!
    	inschrijving.setYear( 2016 );
    	
    	saved = this.inschrijvingXRepository.saveAndFlush( inschrijving );
    		
 	    // stupid GAE ... set id manually
 	    saved.setUuid( KeyFactory.keyToString( saved.getKey() ) );
 	    inschrijving = this.inschrijvingXRepository.saveAndFlush( saved );
    	
    	if ( inschrijving.getReference() != null ) {
    		
    		InschrijvingX cloneFrom 
    			= this.findInschrijving( inschrijving.getReference() );
    		
    		if ( cloneFrom != null ) {
    			this.cloneEnrollment( cloneFrom, inschrijving );
    			return inschrijving;
    		}
    		else {
    			logger.warn( "could not clone, reference [{}] not found", inschrijving.getReference() );
    			throw new RuntimeException("clone failed"); 
    		}
    		
    	}
    	else {
    		
	    	
	    	inschrijving.getContactGegevens().setEmail( organisatie.getEmail() );
	    	
	    	if ( ! this.isEmpty( organisatie.getTelefoonNummer() ) ) {
	    		inschrijving.getContactGegevens().setPhone( organisatie.getTelefoonNummer() );
	    	}
	    	else if ( ! this.isEmpty( organisatie.getGsmNummer() ) ) {
	    		inschrijving.getContactGegevens().setPhone( organisatie.getGsmNummer() );
	    	}
	    	
    		Deelnemer deelnemer
				= new Deelnemer();
    	
    		this.addDeelnemer( inschrijving , deelnemer );
    	
    		QList vragen
    			= QList.template();
    	
	    	for ( String key : vragen.getVragen().keySet() ) {
	    		
	    		List<Vraag> list
	    			= vragen.getVragen().get( key );
	    		
	    		for ( Vraag vraag : list ) {
	    			this.addVraag( inschrijving, vraag );
	    		}
	    		
	    	}	
    	}
    	
    	return saved;
    }
    
    protected void cloneEnrollment( InschrijvingX cloneFrom, InschrijvingX cloneTo ) {
    	
    	cloneTo.setReference( cloneFrom.getUuid() );
    	
    	cloneTo.setVks( cloneFrom.getVks() );
    	
    	// copy contact
    	cloneTo.getContactGegevens().setName( cloneFrom.getContactGegevens().getName() );
    	cloneTo.getContactGegevens().setEmail( cloneFrom.getContactGegevens().getEmail() );
    	cloneTo.getContactGegevens().setPhone( cloneFrom.getContactGegevens().getPhone() );
    	
    	// copy address
    	Adres fromAddress 
    		= cloneFrom.getAdres();
    	
    	cloneTo.getAdres().setGemeente( fromAddress.getGemeente() );
    	cloneTo.getAdres().setZipCode( fromAddress.getZipCode() );
    	cloneTo.getAdres().setStraat( fromAddress.getStraat() );
    	cloneTo.getAdres().setNummer( fromAddress.getNummer() );
    	
    	InschrijvingX saved
    		= this.inschrijvingXRepository.saveAndFlush( cloneTo );

    	// stupid GAE ... set id manually
    	saved.setUuid( KeyFactory.keyToString( saved.getKey() ) );
    	saved = this.inschrijvingXRepository.saveAndFlush( saved );
    	
    	Deelnemer deelnemer
			= new Deelnemer();
    	
    	saved = this.addDeelnemer( saved , deelnemer );
    	
    	QList vragen
			= QList.template();
    	
    	for ( String key : vragen.getVragen().keySet() ) {
    		
    		List<Vraag> list
    			= vragen.getVragen().get( key );
    		
    		for ( Vraag vraag : list ) {
    			this.addVraag( cloneTo, vraag );
    		}
    		
    	}
    	
    }
    
    @Transactional(readOnly=false)
    public Vakantie maakVakantie( Vakantie vakantie ) {
    	
    	if ( vakantie.getUuid() != null ) {
    		return null;
    	}
    	
    	Vakantie saved
    		= this.configuredVakantieRepository.saveAndFlush( vakantie );

    	// stupid GAE ... set id manually
    	saved.setUuid( KeyFactory.keyToString( saved.getKey() ) );
    	this.configuredVakantieRepository.saveAndFlush( saved );
    	
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
    	
    	// TODO, remove hardcoded year!
    	List<InschrijvingX> all
    		= this.inschrijvingXRepository.findByYear( 2016 );// = PirlewietUtil.isPirlewiet( organisatie ) ? this.inschrijvingXRepository.findAll() : this.inschrijvingXRepository.findByOrganisatie( organisatie );
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
    		
    		try {
	    		
	    		inschrijving = this.detach( inschrijving.getUuid() );
	    		
	    		if ( (!isPirlewiet) && ( ! inschrijving.getOrganisatie().getUuid().equals( organisatie.getUuid() ) ) ) {
	    			// logger.info( "x.[{}] versus o.[{}], no match", inschrijving.getOrganisatie().getUuid(), organisatie.getUuid() );
	    			continue;
	    		}
	    		
	    		if ( inschrijving.getReference() != null ) {
	    			continue;
	    		}
	    		
	    		if ( isPirlewiet ) {
	    			
	    			if ( Value.DRAFT.equals( inschrijving.getStatus().getValue() ) ) {
	    				logger.info( "enrollment with status DRAFT not sent to pirlewiet", inschrijving.getUuid() );
	    				continue;
	    			}
	    			
	    		}
	    		
	    		// logger.info( "x.[{}] versus o.[{}], match", inschrijving.getOrganisatie().getUuid(), organisatie.getUuid() );
	    		
	    		inschrijvingen.add( inschrijving );
	    		
	    		/**
	    		// enkel toevoegen als de vakantie nog niet voorbij is
	    		if ( inschrijving.getVakantie().getEindDatum().after( new Date() ) ) {
	    			inschrijvingen.add( inschrijving );
	    		} 
	    		**/
    		}
    		catch( Exception e ) {
    			logger.warn( String.format( "could not process enrollment [%s]", inschrijving.getUuid() ), e );
    		}
    		
    	}
    	
    	Collections.sort( inschrijvingen, mostRecent );
    	
    	return inschrijvingen;
    	
    }
    
  public List<InschrijvingX> drafts( ) {
    	
    	List<InschrijvingX> inschrijvingen
    		= new ArrayList<InschrijvingX>( );
    	
    	List<InschrijvingX> all
    		= this.inschrijvingXRepository.findAll();// = PirlewietUtil.isPirlewiet( organisatie ) ? this.inschrijvingXRepository.findAll() : this.inschrijvingXRepository.findByOrganisatie( organisatie );
    		// SGL|| findByOrganisatie does not work in GAE ... at least not out-of-the-box
    	
    	logger.info( "total number of enrollments: [{}]", all.size() );
    	
    	Iterator<InschrijvingX> it
    		= all.iterator();
    	
    	while ( it.hasNext() ) {
    		
    		InschrijvingX inschrijving
    			= it.next();
    		
    		inschrijving = this.detach( inschrijving.getUuid() );
    		
    		if ( Value.DRAFT.equals( inschrijving.getStatus().getValue() ) ) {
    			inschrijvingen.add( inschrijving );	
    		}
    		
    	}
    	
    	return inschrijvingen;
    	
    }
    
    // @Transactional(readOnly=true)
    public InschrijvingX findInschrijving( String uuid ) {
    	
    	InschrijvingX inschrijving 
    		= this.detach( uuid );
    	
    	return inschrijving;
    	
    	
    }
    
    @Transactional(readOnly=false)
    public InschrijvingX deleteEnrollment( String uuid ) {
    	
    	InschrijvingX inschrijving 
			= this.findInschrijving( uuid );
    	
    	if ( inschrijving != null ) {
    		
    		if ( Status.Value.DRAFT.equals( inschrijving.getStatus().getValue() ) ) {
    			
    			inschrijving.setDeelnemers( null );
    			this.inschrijvingXRepository.delete( inschrijving );
    			
    		}
    		else {
    			throw new PirlewietException( "Je kan een inschrijving enkel verwijderen als die nog niet ingediend is." );
    		}
    		
    	}
    	else {
    		throw new PirlewietException( "Inschrijving bestaat niet (meer)." );
    	}
    	
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
    	
    	Set<String> tags
    		= new HashSet<String>();
    	
    	for ( Vraag vraag : vragen ) {
    		
    		tags.add( vraag.getTag() );
    	
	    	for ( Vraag v : inschrijving.getVragen() ) {
	    		
	    		if ( v.getUuid().equals( ( vraag.getUuid() ) ) ) {
	    			
	    			v.setAntwoord( vraag.getAntwoord() );
	    			break;
	    			
	    		}
	    		
	    	}
    	}
    	
    	inschrijving = this.inschrijvingXRepository.saveAndFlush( inschrijving );
    	
    	return inschrijving;
    	
    }
    
    @Transactional(readOnly=false)
    public InschrijvingX updateVakanties( String inschrijvingID, String vakanties ) {
    	
    	if ( isEmpty( vakanties ) ) {
    		throw new PirlewietException("Selecteer minstens 1 vakantie");
    	}
    	
    	vakanties = vakanties.replaceAll("\"", "" ).trim();
    	
    	InschrijvingX inschrijving
    		= this.findInschrijving( inschrijvingID );
    	
    	// make sure only the same sort of holidays are selected
    	this.holidayManager.singleType( vakanties );
    	
    	inschrijving.setVks( vakanties );
    	
		inschrijving = this.inschrijvingXRepository.saveAndFlush( inschrijving );
    	
    	return inschrijving;
    	
    }
    
    @Transactional(readOnly=false)
    public InschrijvingX updateContact( String inschrijvingID, ContactGegevens contactGegevens ) {
    	
    	InschrijvingX inschrijving
    		= this.findInschrijving( inschrijvingID );
    	
    	if ( contactGegevens.getName().isEmpty() ) {
    		throw new RuntimeException("Geef de naam van de contactpersoon op");
    	}
    	
    	if ( contactGegevens.getPhone().isEmpty() ) {
    		throw new RuntimeException("Geef de GSM-nummer of een telefoonnummer van de contactpersoon op");
    	}
    	
    	if ( contactGegevens.getEmail().isEmpty() ) {
    		throw new RuntimeException("Geef het e-mailadres van de contactpersoon op");
    	}
    	
    	ContactGegevens contact
    		= inschrijving.getContactGegevens( );
    	
    	contact.setName( contactGegevens.getName() );
    	contact.setPhone( contactGegevens.getPhone() );
    	contact.setEmail( contactGegevens.getEmail() );
		
		inschrijving = this.inschrijvingXRepository.saveAndFlush( inschrijving );
    	
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
    public  Deelnemer updateDeelnemer( String id, Deelnemer deelnemer ) {
    	
    	InschrijvingX inschrijving
    		= this.findInschrijving( id );
    	
    	Deelnemer d
    		= inschrijving.getDeelnemers().get( 0 );
    	
    	if ( d != null ) {
    		
    		assertParticipantCompleteness( deelnemer );
    		
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
    		= this.configuredVakantieRepository.findAll();
    	
    	List<Vakantie> vakanties
    		= new ArrayList<Vakantie>( all.size() );
    	
    	for ( Vakantie v : all ) {
    		if ( v.getEindInschrijving().after( new Date() ) ) {
    			if ( ( v.getBeginInschrijving() != null ) && ( v.getBeginInschrijving().before( new Date() ) ) ) {
    				vakanties.add( v );
    			}
    			else {
    				logger.debug( "vakantie [{}] niet actueel, inschrijvingen nog niet begonnen", v.getUuid() );
    			}
    		}
    		else {
				logger.debug( "vakantie [{}] niet actueel, inschrijvingen reeds beeindigd", v.getUuid() );
			}
    	}
    	
    	Collections.sort( vakanties, this.vacationNameComparator );
    	
    	return vakanties;
    	
    }
    
    protected InschrijvingX addDeelnemer( InschrijvingX inschrijving, Deelnemer deelnemer ) {
    	
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
    	
    	MimeMessage message
			= formatReadyToRockMessage( organisation );

		if ( message != null ) {
			
			postBode.deliver( message );
			logger.info( "email sent" );
			
		}
    	
    }
    
    protected boolean isEmpty( String x ) {
    	
    	return ( x == null ) || ( x.trim().isEmpty() );
    	
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
			Status old = new Status( oldStatus );
			model.put( "old", old );
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
  			model.put( "from", PirlewietApplicationConfig.EMAIL_ADDRESS );
  			
  			StringWriter bodyWriter 
  				= new StringWriter();
  			
  			template.process( model , bodyWriter );
  			
  			bodyWriter.flush();
  				
  			message = this.javaMailSender.createMimeMessage();
  			MimeMessageHelper helper = new MimeMessageHelper( message,"utf-8" );
  				
  			helper.setFrom( PirlewietApplicationConfig.EMAIL_ADDRESS, "Pirlewiet Digitaal" );
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
    
    public boolean areAllMandatoryQuestionsAnswered( InschrijvingX enrollment, String tag ) {
    	
    	boolean complete
			= true;

		for ( Vraag vraag : enrollment.getVragen() ) {
			
			if ( ! Vraag.Type.Label.equals( vraag.getType() ) ) {
				if ( tag.equals( vraag.getTag() ) ) { 
					if ( ( vraag.getAntwoord() == null ) || ( vraag.getAntwoord().isEmpty() ) ) {
						logger.info( "[{}]; mandatory question [{}] was not answered", enrollment.getUuid(), vraag.getVraag() );
						complete = false;
						break;
					}
				}
			}
			
		}
	
		return complete;
    }
    
    public boolean isTheParticipantComplete( InschrijvingX enrollment ) {
    	
    	boolean complete
			= true;
    	
    	try {
	
 			Deelnemer participant 
    			= enrollment.getDeelnemers().get( 0 );
    				
    		this.assertParticipantCompleteness( participant );
    		
    		VakantieType type
    			= this.holidayManager.singleType( enrollment.getVks() );
    	
	    	if ( VakantieType.Kika.equals( type ) || VakantieType.Tika.equals( type ) ) {
	    		// for KIKA and TIKA, medical list must be filled in
	    		complete &= this.areAllMandatoryQuestionsAnswered( enrollment, Tags.TAG_MEDIC );	
	    	}
    		
    	 }
    	 catch( IncompleteObjectException e ) {
    	 	complete = false;
    	 	logger.info( "enrollment participant not complete ", e );
    	 }
	
		return complete;
    }
    
    // dynamic: ready to submit, later: medical file complete ? TODO
    public boolean isTheEnrollmentComplete( InschrijvingX enrollment ) {
    	
    	boolean complete
			= true;
    	
    	// TODO: components should check themselves for completeness ?
    	
    	try {
	
	    	// holidays
	    	complete &= ( ! isEmpty( enrollment.getVks() ) );
	    	
	    	if ( ! complete ) {
	    		//throw new IncompleteObjectException( "no holiday: vks=[" + enrollment.getVks() + "]" );
	    		complete = true; // TODO fix holiday!
	    	}
	    	
	    	// contact
	    	if ( enrollment.getReference() == null ) {
	    		ContactGegevens contact
	    			= enrollment.getContactGegevens();
		    	complete &= ( ! isEmpty( contact.getName() ) );
		    	complete &= ( ! isEmpty( contact.getEmail() ) );
		    	complete &= ( ! isEmpty( contact.getPhone() ) );
		    	
		    	if ( ! complete ) {
		    		throw new IncompleteObjectException( "contact incomplete" );
		    	}
	    	
		    	// q-list, only for root enrollment for now
	    		complete &= this.areAllMandatoryQuestionsAnswered( enrollment, Tags.TAG_APPLICATION );
	    		
	    		if ( ! complete ) {
	    			throw new IncompleteObjectException( "not all mandatory questions answered" );
	    		}
	    		
	    	}
	    	
	    	VakantieType type
	    		= this.holidayManager.singleType( enrollment.getVks() );
	    	
	    	if ( VakantieType.Kika.equals( type ) || VakantieType.Tika.equals( type ) ) {
	    		// for KIKA and TIKA, medical list must be filled in
	    		complete &= this.areAllMandatoryQuestionsAnswered( enrollment, Tags.TAG_MEDIC );	
	    	}
			
			// participants
			Deelnemer participant 
				= enrollment.getDeelnemers().get( 0 );
			
			this.assertParticipantCompleteness( participant );
    	}
    	catch( IncompleteObjectException e ) {
    		complete = false;
    		logger.info( "enrollement not complete ", e );
    	}
		
		return complete;
    }
    
    public List<InschrijvingX> findRelated( InschrijvingX enrollment ){
    	
    	String uuid
    		= enrollment.getUuid();
    	
    	List<InschrijvingX> found
    		= this.inschrijvingXRepository.findByReference( uuid );
    	
    	List<InschrijvingX> related
    		= new ArrayList<InschrijvingX>( Math.max( 0, found.size() - 1 ) );
    	
    	for ( InschrijvingX f : found ) {
    		
    		if ( ! uuid.equals( f.getUuid() ) ) {
    			related.add( f );
    		}
    		
    	}
    	
    	return related;
    	
    }
    
    public Status whatIsTheApplicationStatus( List<InschrijvingX> related ) {
    	
    	Status status
    		= new Status( );
    	
		// an application gets status 'submitted', any of the enrollments is submitted
    	// an application gets status 'complete' when none of the enrollments need handling 

    	boolean isComplete
    		= false;
    	
    	boolean isDraft
    		= false;
    	
    	for ( InschrijvingX enrollment : related ) {

			if ( this.needsHandling( enrollment ) ) {
				
				isComplete = false;
				
			}
			else if ( ! this.hasBeenHandled( enrollment ) ) {
				
				// no need for handling and not handled ? then it must be DRAFT
				
				// DRAFT as long as any of the enrollment is in draft
				isDraft = true;
				
			}
    		
    	}
    	
    	if ( isDraft ) {
    		status.setValue( Value.DRAFT );
    	}
    	else if ( isComplete ) {
    		status.setValue( Value.COMPLETE );
    	}
    	else {
    		status.setValue( Value.SUBMITTED );
    	}
    	
    	return status;
    	
    }
    
    protected void assertParticipantCompleteness( Deelnemer participant ) {
    	
    	if ( isEmpty(  participant.getVoorNaam() ) ) {
			throw new IncompleteObjectException("Geef de voornaam van de deelnemer op");
		}
		if ( isEmpty( participant.getFamilieNaam() ) ) {
			throw new IncompleteObjectException("Geef de familienaam van de deelnemer op");
		}
		
		if ( participant.getGeslacht() == null ) {
			throw new IncompleteObjectException("Geef het geslacht van de deelnemer op");
		}
		if ( participant.getGeboorteDatum() == null ) {
			throw new IncompleteObjectException("Geef de geboortedatum van de deelnemer op");
		}
		if ( isEmpty( participant.getTelefoonNummer() ) && isEmpty( participant.getMobielNummer() ) ) {
			throw new IncompleteObjectException("Geef een telefoonnummer of GSM-nummer van de deelnemer op");
		}
    } 
    
    protected boolean needsHandling( InschrijvingX enrollment ) {
    	
    	Value value
    		= enrollment.getStatus().getValue();
    	
    	return ( Value.SUBMITTED.equals( value ) || Value.WAITINGLIST.equals( value ) );
    	
    }
    
    protected boolean hasBeenHandled( InschrijvingX enrollment ) {
    	
    	Value value
    		= enrollment.getStatus().getValue();
    	
    	return ( Value.ACCEPTED.equals( value ) || Value.REJECTED.equals( value ) );
    	
    }
 
}
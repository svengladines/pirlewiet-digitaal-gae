package be.pirlewiet.registrations.domain;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.mail.internet.MimeMessage;
import javax.persistence.Transient;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.transaction.annotation.Transactional;

import be.occam.utils.ftp.FTPClient;
import be.occam.utils.spring.web.Client;
import be.pirlewiet.registrations.application.config.PirlewietApplicationConfig;
import be.pirlewiet.registrations.model.Adres;
import be.pirlewiet.registrations.model.Organisatie;
import be.pirlewiet.registrations.model.Vragen;
import be.pirlewiet.registrations.repositories.OrganisatieRepository;
import be.pirlewiet.registrations.web.util.DataGuard;
import be.pirlewiet.registrations.web.util.PirlewietUtil;

import com.google.appengine.api.datastore.KeyFactory;

import freemarker.template.Configuration;
import freemarker.template.Template;

public class OrganisationManager {
	
	@Resource
	HeadQuarters headQuarters;
	
	@Resource
	FTPClient ftpClient;
	
	@Resource
	protected Organisatie pDiddy;
	
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
	
	@Resource
	PostBode postBode;
	
    public OrganisationManager() {
    }
    
    public OrganisationManager guard() {
    	this.dataGuard.guard();
    	return this;
    }
    
    @Transactional( readOnly=false )
    public Organisatie create( Organisatie organisation ) {
    	
    	String email
    		= organisation.getEmail();
    	
    	Organisatie existing
    		= this.organisatieRepository.findOneByEmail( email );
    	
    	if ( existing != null ) {
    		throw new PirlewietException( String.format( "Er bestaat al een organisatie met het e-mailadres [%s]. Geef een ander e-mailadres op om een nieuwe organisatie aan te maken.", email ) );
    	}
    	
    	String code 
    		= this.buitenWipper.guard().uniqueCode();
    	
    	organisation.setCode( code );
    	
    	Organisatie saved 
    		= this.organisatieRepository.saveAndFlush( organisation );
    	
    	saved.setUuid( KeyFactory.keyToString( saved.getKey() ) );
    	
    	saved 
			= this.organisatieRepository.saveAndFlush( saved );
    	
    	logger.info( "created organiation with uuid [{}]", saved.getUuid() );
    	
    	return saved;
    	
    }

    @Transactional(readOnly=false)
    public Organisatie updateAdres( String id, Adres adres ) {
    	
    	Organisatie organisatie
    		= this.organisation( id );
    	
    	boolean newOrganisation
    		= ( organisatie.getAdres().getGemeente() == null );
    	
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
		
		organisatie = this.organisatieRepository.saveAndFlush( organisatie );
		
		if ( newOrganisation ) {
			
			boolean published = publishNewList();
			
			if ( published ) {
				this.sendCreatedEmailToOrganisation( organisatie );
				this.sendCreatedEmailToPirlewiet( organisatie );
			}
			else {
				throw new PirlewietException( "Registratie van organisatie is mislukt. Probeer AUB opnieuw. Bij aanhoudende problemen, contacteer ons secretariaat.");
			}
		}
		
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
    
    protected boolean sendCreatedEmailToOrganisation( Organisatie organisation ) {
    	
		MimeMessage message
			= formatCreatedMessage( organisation, "/templates/to-organisation/organisation-created.tmpl", organisation.getEmail() );

		if ( message != null ) {
			
			return postBode.deliver( message );
			
		}
		
		return false;
	
    }
    
 protected boolean sendCreatedEmailToPirlewiet( Organisatie organisation ) {
    	
		MimeMessage message
			= formatCreatedMessage( organisation, "/templates/to-pirlewiet/organisation-created.tmpl",this.headQuarters.getEmail()  );

		if ( message != null ) {
			
			return postBode.deliver( message );
			
		}
		
		return false;
	
    }

    protected MimeMessage formatCreatedMessage( Organisatie organisation, String templateLocation, String to ) {

		MimeMessage message
			= null;

		Configuration cfg 
			= new Configuration();
	
		try {
	
			InputStream tis
				= this.getClass().getResourceAsStream( templateLocation );
	
			Template template 
				= new Template("code", new InputStreamReader( tis ), cfg );
	
			Map<String, Object> model = new HashMap<String, Object>();
			
			model.put( "organisation", organisation );
			
			StringWriter bodyWriter 
				= new StringWriter();
			
			template.process( model , bodyWriter );
			
			bodyWriter.flush();
				
			message = this.postBode.message();
			// SGL| GAE does not support multipart_mode_mixed_related (default, when flag true is set)
			MimeMessageHelper helper = new MimeMessageHelper(message, MimeMessageHelper.MULTIPART_MODE_MIXED, "utf-8");
		
			helper.setFrom( PirlewietApplicationConfig.EMAIL_ADDRESS );
			helper.setTo( to );
			helper.setReplyTo( headQuarters.getEmail() );
			helper.setSubject( "Pirlewiet: registratie als doorverwijzer" );
		
			String text
				= bodyWriter.toString();
			
			logger.info( "email text is [{}]", text );
				
			helper.setText(text, true);
	
		} catch( Exception e ) {
			logger.warn( "could not create e-mail", e );
			throw new RuntimeException( e );
		}

		return message;

    }
    
    protected boolean publishNewList() {
    	
    	try {
			String html 
				= Client.getHTML( "http://pirlewiet-digitaal.appspot.com/rs/organisations.html", PirlewietUtil.as( pDiddy ) ).getBody();
			
			logger.info( "html: {}", html );
			
			logger.info( "sending html to FTP server...", html );
			boolean ok 
				= ftpClient.putTextFile("httpdocs/digitaal", "organisations.html", html );
			logger.info( "FTP put [{}]", ok ? "succeeded" : "failed" );
			
			return ok;
			
		}
		catch ( Exception e ) {
			logger.error( "could publish organisations-list ", e );
			return false;
		}
    	
    }
    
}
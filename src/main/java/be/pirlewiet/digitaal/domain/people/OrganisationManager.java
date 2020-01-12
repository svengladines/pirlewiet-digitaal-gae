package be.pirlewiet.digitaal.domain.people;

import static be.occam.utils.javax.Utils.isEmpty;

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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.mail.javamail.MimeMessageHelper;

import be.pirlewiet.digitaal.application.config.PirlewietApplicationConfig;
import be.pirlewiet.digitaal.domain.HeadQuarters;
import be.pirlewiet.digitaal.domain.exception.ErrorCodes;
import be.pirlewiet.digitaal.domain.exception.PirlewietException;
import be.pirlewiet.digitaal.model.Organisation;
import be.pirlewiet.digitaal.repository.OrganisationRepository;
import be.pirlewiet.digitaal.web.util.PirlewietUtil;
import freemarker.template.Configuration;
import freemarker.template.Template;

public class OrganisationManager {
	
	@Resource
	HeadQuarters headQuarters;
	
	@Resource
	protected Organisation pDiddy;
	
	@Resource
	protected AddressManager addressManager;
	
	protected final Logger logger
		= LoggerFactory.getLogger( this.getClass() );
	
	protected final Comparator<Organisation> lastUpdatedFirst
		= new Comparator<Organisation>() {

			@Override
			public int compare(Organisation o1, Organisation o2) {
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
	protected OrganisationRepository organisationRepository;
	
	@Resource
	DoorMan buitenWipper;
	
	@Resource
	MailMan postBode;
	
    public OrganisationManager() {
    }
    
    public Organisation create( Organisation organisation ) {
    	return this.create( organisation, true );
    }
    
    public Organisation create( Organisation organisation, boolean sendEmail ) {
    	
    	String email
    		= organisation.getEmail();
    	
    	if ( isEmpty( organisation.getName() ) ) {
    		throw new PirlewietException( ErrorCodes.ORGANISATION_NAME_MISSING, "Vul het veld 'naam' in." );
    	}
    	
    	if ( isEmpty( email ) ) {
    		throw new PirlewietException( ErrorCodes.ORGANISATION_EMAIL_MISSING, "Vul het veld 'e-mail' in." );
    	}
    	
    	if ( isEmpty( organisation.getPhone() ) ) {
    		throw new PirlewietException( ErrorCodes.ORGANISATION_PHONE_MISSING, "Vul het veld 'telefoon' in." );
    	}
    	
    	if ( isEmpty( organisation.getCity() ) ) {
    		throw new PirlewietException( ErrorCodes.ORGANISATION_EMAIL_MISSING, "Vul het veld 'gemeente' in." );
    	}
    	
    	Organisation existing
    		= this.organisationRepository.findOneByEmail( email );
    	
    	if ( existing != null ) {
    		throw new PirlewietException( ErrorCodes.ORGANISATION_EMAIL_TAKEN, String.format( "Er bestaat al een organisatie met het e-mailadres [%s]. Geef een ander e-mailadres op om een nieuwe organisatie aan te maken.", email ) );
    	}
    	
    	if ( isEmpty( organisation.getCode() ) ) {
    	
	    	String code 
	    		= this.buitenWipper.guard().uniqueCode();
	    	
	    	organisation.setCode( code );
	    	
    	}
    	
    	Organisation saved 
    		= this.organisationRepository.saveAndFlush( organisation );
    	
    	// TODO set uuid
    	
    	saved 
			= this.organisationRepository.saveAndFlush( saved );
    	
    	logger.info( "created organisation with uuid [{}], name [{}] and code [{}]", new Object[] { saved.getUuid(), saved.getName(), saved.getCode() } );
    	
    	if ( sendEmail ) {
    		this.sendCreatedEmailToOrganisation( saved );
    		this.sendCreatedEmailToPirlewiet( saved );
    	}
    	
    	return saved;
    	
    }

    public Organisation updateAddress( Organisation organisation, String addressUuid ) {
    	
    	    	
    	organisation.setAddressUuid( addressUuid );
    	organisation.setUpdated( new Date() );
		
    	organisation = this.organisationRepository.saveAndFlush( organisation );
		
    	return organisation;
    	
    }
    
    public Organisation update( String uuid, Organisation organisation ) {
    	
    	Organisation loaded 
    		= organisation( uuid );
    	
    	if ( loaded == null ) {
    		return null;
    	}
    	
    	if ( isEmpty( organisation.getName() ) ) {
    		throw new PirlewietException( ErrorCodes.ORGANISATION_NAME_MISSING );
    	}
    	
    	if ( isEmpty( organisation.getEmail() ) ) {
    		throw new PirlewietException( ErrorCodes.ORGANISATION_EMAIL_MISSING );
    	}
    	
    	if ( isEmpty( organisation.getPhone() ) ) {
    		throw new PirlewietException( ErrorCodes.ORGANISATION_PHONE_MISSING );
    	}
    	
    	loaded.setPhone( organisation.getPhone() );
    	loaded.setPhone( organisation.getPhone() );
    	loaded.setName( organisation.getName() );
    	loaded.setEmail( organisation.getEmail() );
    	loaded.setUpdated( new Date() );
    	
    	Organisation saved
    		= this.organisationRepository.saveAndFlush( loaded );
    	
    	logger.info( "updated organisation with uiid [{}]", saved.getUuid() );
    	
    	return saved;
    	
    }
    
  public Organisation delete( String uuid ) {
    	
    	Organisation loaded 
    		= organisation( uuid );
    	
    	if ( loaded == null ) {
    		return null;
    	}
    	
    	this.organisationRepository.delete( loaded );
    	
    	logger.info( "deleted organisation with uiid [{}]", uuid );
    	
    	return loaded;
    	
    }
    
    public boolean isInComplete( Organisation organisation, boolean checkAddress ) {
    	
    	boolean incomplete 
    		= false;
    	
    	incomplete |= isEmpty( organisation.getName() ) ;
    	incomplete |= isEmpty( organisation.getPhone() ) ;
    	incomplete |= isEmpty( organisation.getEmail() ) ;
    	if ( checkAddress ) {
    		
    		incomplete = ( organisation.getAddressUuid() == null) || ( ! this.addressManager.isComplete( organisation.getAddressUuid() ) ); 
	    	
    	}
    	
    	return incomplete;
    	
    }
    
    public Organisation organisation( String id ) {
    	
    	Organisation organisatie
    		= this.organisationRepository.findByUuid( id );
    	
    	if ( organisatie != null ) {
    		logger.debug( "found organisation with id [{}]", id );
    	}
    	
    	return organisatie;
    	
    }
    
    public Organisation findOneByUuid( String uuid ) {
    	return this.organisation( uuid );
    }
    
    public List<Organisation> all( ) {
    	
    	List<Organisation> all
    		= this.organisationRepository.findAll();
    	
    	List<Organisation> filtered
			= new ArrayList<Organisation>();
    	
    	for ( Organisation organisation : all ) {
    		
    		if ( PirlewietUtil.isPirlewiet( organisation ) ) {
    			continue;
    		}
    		
    		if ( PirlewietUtil.isPD( organisation ) ) {
    			continue;
    		}
    		
    		filtered.add( organisation );
    		
    	}
    	
    	
    	Collections.sort( filtered , this.lastUpdatedFirst );
    	
    	return filtered;
    	
    	
    }
    
    public boolean isOrganisationOutDated( Organisation organisation ) {
    	
    	return ( organisation.getUpdated() == null );
    	
    }
    
    protected boolean sendCreatedEmailToOrganisation( Organisation organisation ) {
    	
		MimeMessage message
			= formatCreatedMessage( organisation, "/templates/to-organisation/organisation-created.tmpl", organisation.getEmail() );

		if ( message != null ) {
			
			return postBode.deliver( message );
			
		}
		
		return false;
	
    }
    
 protected boolean sendCreatedEmailToPirlewiet( Organisation organisation ) {
    	
		MimeMessage message
			= formatCreatedMessage( organisation, "/templates/to-pirlewiet/organisation-created.tmpl",this.headQuarters.getEmail()  );

		if ( message != null ) {
			
			return postBode.deliver( message );
			
		}
		
		return false;
	
    }

    protected MimeMessage formatCreatedMessage( Organisation organisation, String templateLocation, String to ) {

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
    
}
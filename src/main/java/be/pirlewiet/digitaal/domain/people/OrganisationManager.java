package be.pirlewiet.digitaal.domain.people;

import static be.occam.utils.javax.Utils.isEmpty;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import be.pirlewiet.digitaal.model.OrganisationType;
import org.springframework.beans.factory.annotation.Autowired;

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
import org.springframework.stereotype.Component;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

@Component
public class OrganisationManager {
	
	@Autowired
	HeadQuarters headQuarters;
	
	@Autowired
	protected Organisation pDiddy;
	
	@Autowired
	protected AddressManager addressManager;

	@Autowired
	protected TemplateEngine emailTemplateEngine;
	
	protected final Logger logger = LoggerFactory.getLogger( this.getClass() );
	
	protected final Comparator<Organisation> lastUpdatedFirst
		= new Comparator<Organisation>() {

			@Override
			public int compare(Organisation o1, Organisation o2) {
				if ( ( o1.getUpdated() == null ) && ( o2.getUpdated() == null ) ) {
					return 0;
				}
				else if ( o1.getUpdated() == null ) {
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
	
	@Autowired
	protected OrganisationRepository organisationRepository;
	
	@Autowired
	DoorMan doorMan;
	
	@Autowired
	MailMan postBode;
	
    public OrganisationManager() {
    }
    
    public Organisation create( Organisation organisation ) {
    	return this.create( organisation, true );
    }
    
    public Organisation create( Organisation organisation, boolean sendEmail ) {
    	
    	String email = organisation.getEmail();
    	
    	if ( isEmpty( organisation.getName() ) ) {
    		throw new PirlewietException( ErrorCodes.ORGANISATION_NAME_MISSING, "Vul het veld 'naam' in." );
    	}
    	
    	if ( isEmpty( email ) ) {
    		throw new PirlewietException( ErrorCodes.ORGANISATION_EMAIL_MISSING, "Vul het veld 'e-mail' in." );
    	}
    	
    	if ( isEmpty( organisation.getPhone() ) ) {
    		throw new PirlewietException( ErrorCodes.ORGANISATION_PHONE_MISSING, "Vul het veld 'telefoon' in." );
    	}
    	
    	if (OrganisationType.NON_PROFIT.equals(organisation.getType())) {
			 if (isEmpty( organisation.getCity())) {
				 throw new PirlewietException(ErrorCodes.ORGANISATION_CITY_MISSING, "Vul het veld 'gemeente' in.");
			 }
			Organisation existing = this.organisationRepository.findOneByEmail( email );
			if ( existing != null ) {
				throw new PirlewietException( ErrorCodes.ORGANISATION_EMAIL_TAKEN, String.format( "Er bestaat al een organisatie met het e-mailadres [%s]. Geef een ander e-mailadres op om een nieuwe organisatie aan te maken.", email ) );
			}
    	}
    	
    	if (isEmpty(organisation.getCode())) {
	    	String code = this.doorMan.guard().uniqueCode();
	    	organisation.setCode( code );
    	}
    	
    	organisation.setUuid( UUID.randomUUID().toString() );
    	
    	Organisation saved = this.organisationRepository.saveAndFlush( organisation );
    	
    	logger.info( "created organisation with uuid [{}], name [{}] and code [{}]", new Object[] { saved.getUuid(), saved.getName(), saved.getCode() } );
    	
    	if ( sendEmail ) {
			if (OrganisationType.NON_PROFIT.equals(organisation.getType())) {
				this.sendCreatedEmailToOrganisation(saved);
				this.sendCreatedEmailToPirlewiet( saved );
			}
			else {
				this.sendTouristCreatedEmailToPirlewiet( saved );
			}
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
    	
    	logger.info( "update organisation with uuid [{}]", uuid );
    	
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
    	
    	logger.info( "updated organisation with uuid [{}]", saved.getUuid() );
    	
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

		/* TODO!
		MimeMessage message = formatCreatedMessage( "Pirlewiet Digitaal: registratie", organisation, "/to-organisation/organisation-created", organisation.getEmail() );

		if ( message != null ) {
			return postBode.deliver( message );
		}
		 */
		
		return false;
	
    }

 protected boolean sendCreatedEmailToPirlewiet( Organisation organisation ) {
		/* TODO
		MimeMessage message
			= formatCreatedMessage( "Nieuwe doorverwijzer geregistreerd", organisation, "/email/to-pirlewiet/organisation-created",this.headQuarters.getEmail()  );

		if ( message != null ) {
			
			return postBode.deliver( message );
			
		}
		 */
		return false;
    }

	protected boolean sendTouristCreatedEmailToPirlewiet( Organisation organisation ) {

		/* TODO
		MimeMessage message = formatCreatedMessage( "Nieuwe vakantieganger geregistreerd", organisation, "/email/to-pirlewiet/tourist-created",this.headQuarters.getEmail()  );
		if ( message != null ) {
			return postBode.deliver( message );
		}
		 */
		return false;

	}

    protected String formatCreatedMessage( String subject, Organisation organisation, String templateLocation, String to ) {
		// TODO
		return "TODO";
    }
    
}
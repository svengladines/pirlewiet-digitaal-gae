package be.pirlewiet.digitaal.domain.people;

import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;

import be.pirlewiet.digitaal.domain.exception.ErrorCodes;
import be.pirlewiet.digitaal.domain.exception.PirlewietException;
import be.pirlewiet.digitaal.model.CodeRequest;
import be.pirlewiet.digitaal.model.Organisation;
import be.pirlewiet.digitaal.repository.OrganisationRepository;
import be.pirlewiet.digitaal.web.util.DataGuard;
import be.pirlewiet.digitaal.web.util.PirlewietUtil;
import org.springframework.stereotype.Component;

import javax.mail.internet.MimeMessage;

@Component
public class DoorMan {
	
	protected final Logger logger
		= LoggerFactory.getLogger( this.getClass() );
	
	protected final OrganisationRepository organisationRepository;
	protected final DataGuard dataGuard;
	protected final MailMan mailMan;
	protected final CodeMan codeMan;
	protected final Organisation pDiddy;
	protected final SpokesPerson spokesPerson;

	@Autowired
    public DoorMan(OrganisationRepository organisationRepository, DataGuard dataGuard, MailMan mailMan, CodeMan codeMan, Organisation pDiddy, SpokesPerson spokesPerson) {
        this.organisationRepository = organisationRepository;
        this.dataGuard = dataGuard;
        this.mailMan = mailMan;
        this.codeMan = codeMan;
        this.pDiddy = pDiddy;
        this.spokesPerson = spokesPerson;
    }

    public DoorMan guard() {
    	this.dataGuard.guard();
    	return this;
    }
	
	public Organisation whoHasCode( String code ) {
		
		code = code.replaceAll("\"", "").toLowerCase();
		
		if ( PirlewietUtil.PDIDDY_CODE.equals( code ) ) {
			return pDiddy;
		}
		
		return this.organisationRepository.findOneByCode( code );
	}
	
	//@Transactional(readOnly=true)
	public Organisation actor( String uuid ) {
		
		if ( PirlewietUtil.PDIDDY_ID.equals( uuid ) ) {
			return pDiddy;
		}
		
		Organisation organisatie
			= this.organisationRepository.findByUuid( uuid );
		
		if ( organisatie != null ) {
			return organisatie;
		}
		else {
			throw new PirlewietException( ErrorCodes.PWT_UNKNOWN_ACTOR );
		}
		
	}
	
	//@Transactional(readOnly=true)
	public Organisation whoHasID( String uuid ) {
		
		if ( PirlewietUtil.PDIDDY_ID.equals( uuid ) ) {
			return pDiddy;
		}
		
		Organisation organisatie
			= this.organisationRepository.findByUuid( uuid );
		if ( organisatie != null ) {
			return organisatie;
		}
		else {
			logger.info( "no organisation with id [{}]", uuid );
		}
		
		return null;
		
	}
	
	//@Transactional(readOnly=false)
	public void processCodeRequest( CodeRequest codeRequest ) {
		
		this.processCodeRequest( codeRequest, false );
		
	}
	
	//@Transactional(readOnly=false)
	public void processCodeRequest( CodeRequest codeRequest, boolean sendEmail ) {
		
		logger.info( "code requeest for email [{}]", codeRequest.getEmail() );
		
		String email
			= codeRequest.getEmail();
		
		Organisation organisatie
			= this.organisationRepository.findOneByEmail( email );
		
		if ( organisatie == null ) {
			logger.warn( "no organisation with email [{}]", email );
			codeRequest.setStatus( CodeRequest.Status.REJECTED );
			return;
		}
		
		String code
			= null;
		
		if ( organisatie.getCode() != null ) {
			code = organisatie.getCode();
			logger.info( "code exists, code is [{}]", code );
			if ( ( organisatie.getUuid() == null ) || ( organisatie.getUuid().isEmpty() ) ) {
				organisatie.setUuid( UUID.randomUUID().toString() );
				organisatie = this.organisationRepository.saveAndFlush( organisatie );
			}
		}
		
		while ( code == null ) {
			code = this.codeMan.generateCode();
			if ( this.organisationRepository.findOneByCode( code ) != null ) {
				// code already taken
				code = null;
				continue;
			}
			logger.info( "generated code for [{}] : [{}]", organisatie.getName(), code );
			organisatie.setCode( code );
			organisatie = this.organisationRepository.saveAndFlush( organisatie );
			logger.info( "saved code [{}] for [{}]", code, organisatie.getName() );
		}
		
		
		codeRequest.setCode( code );
		codeRequest.setStatus( CodeRequest.Status.OK );
		
		if ( sendEmail ) {
			String message = this.spokesPerson.formatCodeRequestMessages(code);
			if ( message != null ) {
				mailMan.deliver( email,"Aanvraag code", message );
				logger.info( "code request email sent to [{}]", email );
			}
			
		}
		
	}
	
	//@Transactional(readOnly=false)
	public String uniqueCode( ) {
		
		String code
			= null;
		
		while ( code == null ) {
			code = this.codeMan.generateCode();
			if ( this.organisationRepository.findOneByCode( code ) != null ) {
				// code already taken
				code = null;
				continue;
			}
			logger.info( "generated unique code [{}]", code );
		}
		
		return code;
		
	}

}

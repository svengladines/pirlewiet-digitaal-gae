package be.pirlewiet.registrations.domain;

import java.util.StringTokenizer;

import javax.annotation.Resource;

import org.springframework.transaction.annotation.Transactional;

import be.pirlewiet.registrations.application.config.ConfiguredVakantieRepository;
import be.pirlewiet.registrations.model.InschrijvingX;
import be.pirlewiet.registrations.model.Vakantie;
import be.pirlewiet.registrations.repositories.InschrijvingXRepository;
import be.pirlewiet.registrations.repositories.VakantieRepository;

public class Detacher {
	
	@Resource
	protected InschrijvingXRepository inschrijvingXRepository;
	
	@Resource
	protected ConfiguredVakantieRepository configuredVakantieRepository;
	
	@Transactional(readOnly=false)
	public InschrijvingX findAndDetach( String uuid ) {
		
		InschrijvingX inschrijving
			= this.inschrijvingXRepository.findByUuid( uuid );
		
		if ( inschrijving != null ) {
        	
	    	// needed to lazily load the lists (can't load them eagerly, unfortunately)
			inschrijving.getDeelnemers().size();
			inschrijving.getVragen().size();
			// same for GAE for embedded
			inschrijving.getContactGegevens().hashCode();
			inschrijving.getVragen().hashCode();
			inschrijving.getAdres().hashCode();
			inschrijving.getStatus().hashCode();
			inschrijving.getOrganisatie().getAdres().hashCode();
			
			if ( inschrijving.getVks() != null ) {
				StringTokenizer tok
					= new StringTokenizer( inschrijving.getVks().trim(), ",", false );
				
				while( tok.hasMoreTokens() ) {
					
					String t
						= tok.nextToken().trim();
					
					if ( t.length() == 0 ) {
						continue;
					}
					
					Vakantie v 
						= this.configuredVakantieRepository.findByUuid( t.trim() ); 
			
					if ( v != null ) {
						inschrijving.getVakanties().add ( v );
					}
					else {
						throw new RuntimeException( "no vakantie with id [" + t.trim() + "]" );
					}
					
				}
			}
			
    	}
		
		return inschrijving;
		
	}

}

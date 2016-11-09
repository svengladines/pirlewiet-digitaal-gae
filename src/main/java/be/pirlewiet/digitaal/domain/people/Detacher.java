package be.pirlewiet.digitaal.domain.people;

import java.util.StringTokenizer;

import javax.annotation.Resource;

import org.springframework.transaction.annotation.Transactional;

import be.pirlewiet.digitaal.application.config.ConfiguredVakantieRepository;
import be.pirlewiet.digitaal.model.Enrollment;
import be.pirlewiet.digitaal.model.Holiday;
import be.pirlewiet.digitaal.repositories.EnrollmentRepository;

public class Detacher {
	
	@Resource
	protected EnrollmentRepository inschrijvingXRepository;
	
	@Resource
	protected ConfiguredVakantieRepository configuredVakantieRepository;
	
	@Transactional(readOnly=false)
	public Enrollment findAndDetach( String uuid ) {
		
		Enrollment inschrijving
			= this.inschrijvingXRepository.findByUuid( uuid );
		
		if ( inschrijving != null ) {
        	
	    	// needed to lazily load the lists (can't load them eagerly, unfortunately)
			inschrijving.getDeelnemers().size();
			inschrijving.getVragen().size();
			// same for GAE for embedded
			String contactName = inschrijving.getContactGegevens().getName();
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
					
					Holiday v 
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
	
	@Transactional(readOnly=false)
	public Enrollment detach( Enrollment enrollment ) {
		
		if ( enrollment != null ) {
        	
	    	// needed to lazily load the lists (can't load them eagerly, unfortunately)
			enrollment.getDeelnemers().size();
			enrollment.getVragen().size();
			// same for GAE for embedded
			String contactName = enrollment.getContactGegevens().getName();
			enrollment.getVragen().hashCode();
			enrollment.getAdres().hashCode();
			enrollment.getStatus().hashCode();
			enrollment.getOrganisatie().getAdres().hashCode();
			
			if ( enrollment.getVks() != null ) {
				StringTokenizer tok
					= new StringTokenizer( enrollment.getVks().trim(), ",", false );
				
				while( tok.hasMoreTokens() ) {
					
					String t
						= tok.nextToken().trim();
					
					if ( t.length() == 0 ) {
						continue;
					}
					
					Holiday v 
						= this.configuredVakantieRepository.findByUuid( t.trim() ); 
			
					if ( v != null ) {
						enrollment.getVakanties().add ( v );
					}
					else {
						throw new RuntimeException( "no vakantie with id [" + t.trim() + "]" );
					}
					
				}
			}
			
    	}
		
		return enrollment;
		
	}

}

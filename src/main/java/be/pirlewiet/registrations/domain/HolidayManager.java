package be.pirlewiet.registrations.domain;

import java.util.Comparator;
import java.util.StringTokenizer;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Transactional;

import be.pirlewiet.registrations.domain.exception.ErrorCode;
import be.pirlewiet.registrations.model.Organisatie;
import be.pirlewiet.registrations.model.Vakantie;
import be.pirlewiet.registrations.model.VakantieType;
import be.pirlewiet.registrations.repositories.VakantieRepository;
import be.pirlewiet.registrations.web.ResultDTO;
import be.pirlewiet.registrations.web.ResultDTO.Value;
import be.pirlewiet.registrations.web.util.DataGuard;

public class HolidayManager {
	
	@Resource
	HeadQuarters headQuarters;
	
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
	protected VakantieRepository configuredVakantieRepository;
	
	@Resource
	BuitenWipper buitenWipper;
	
	@Resource
	DataGuard dataGuard;
	
	@Resource
	PostBode postBode;
	
    public HolidayManager() {
    }
    
    public HolidayManager guard() {
    	this.dataGuard.guard();
    	return this;
    }
    
    @Transactional( readOnly=false )
    public ResultDTO<VakantieType> checkSingleType( String vks ) {
    	
    	ResultDTO<VakantieType> result
    		= new ResultDTO<VakantieType>();
    	result.setValue( Value.OK );
    	
    	VakantieType type
    		= null;
    	
    	if ( vks != null ) {
    		
			StringTokenizer tok
				= new StringTokenizer( vks.trim(), ",", false );
			
			while( tok.hasMoreTokens() ) {
				
				String t
					= tok.nextToken().trim();
				
				if ( t.length() == 0 ) {
					continue;
				}
				
				Vakantie v 
					= this.configuredVakantieRepository.findByUuid( t.trim() ); 
		
				if ( v != null ) {
					
					if ( type == null ) {
						
						type = v.getType();
						
					}
					else {
						
						if ( ! type.equals( v.getType() ) ) {
							
							result.setErrorCode( ErrorCode.APPLICATION_HOLIDAY_MIXED );
							
						}
						
					}
					
				}
				else {
					result.setErrorCode( ErrorCode.APPLICATION_HOLIDAY_NOT_FOUND );
				}
				
			}
		}
    	
    	if ( result.getErrorCode() != null ) {
    		result.setValue( Value.NOK );
    	}
    	else {
    		result.setObject( type );
    	}
    	
    	return result;
    	
    }

}
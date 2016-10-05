package be.pirlewiet.digitaal.domain.people;

import java.util.Comparator;
import java.util.StringTokenizer;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Transactional;

import be.pirlewiet.digitaal.domain.HeadQuarters;
import be.pirlewiet.digitaal.domain.exception.ErrorCode;
import be.pirlewiet.digitaal.model.Holiday;
import be.pirlewiet.digitaal.model.HolidayType;
import be.pirlewiet.digitaal.model.Organisation;
import be.pirlewiet.digitaal.repositories.HolidayRepository;
import be.pirlewiet.digitaal.web.ResultDTO;
import be.pirlewiet.digitaal.web.ResultDTO.Value;
import be.pirlewiet.digitaal.web.util.DataGuard;

public class HolidayManager {
	
	@Resource
	protected HeadQuarters headQuarters;
	
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
	protected HolidayRepository holidayRepository;
	
	@Resource
	DataGuard dataGuard;
	
	@Resource
	MailMan postBode;
	
    public HolidayManager() {
    }
    
    public HolidayManager guard() {
    	this.dataGuard.guard();
    	return this;
    }
    
    @Transactional( readOnly=false )
    public ResultDTO<HolidayType> checkSingleType( String vks ) {
    	
    	ResultDTO<HolidayType> result
    		= new ResultDTO<HolidayType>();
    	result.setValue( Value.OK );
    	
    	HolidayType type
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
				
				Holiday v 
					= this.holidayRepository.findByUuid( t.trim() ); 
		
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
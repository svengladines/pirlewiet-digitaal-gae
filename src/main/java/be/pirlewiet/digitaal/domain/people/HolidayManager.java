package be.pirlewiet.digitaal.domain.people;

import static be.occam.utils.javax.Utils.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.StringTokenizer;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Transactional;

import be.occam.utils.spring.web.Result;
import be.occam.utils.spring.web.Result.Value;
import be.pirlewiet.digitaal.domain.HeadQuarters;
import be.pirlewiet.digitaal.domain.exception.ErrorCodes;
import be.pirlewiet.digitaal.model.Holiday;
import be.pirlewiet.digitaal.model.HolidayType;
import be.pirlewiet.digitaal.repositories.HolidayRepository;
import be.pirlewiet.digitaal.web.util.DataGuard;

public class HolidayManager {
	
	@Resource
	protected HeadQuarters headQuarters;
	
	protected final Logger logger
		= LoggerFactory.getLogger( this.getClass() );
	
	protected final Comparator<Holiday> holidayNameComparator
		= new Comparator<Holiday>() {

		@Override
		public int compare(Holiday o1, Holiday o2) {
			return o1.getName().compareTo( o2.getName() );
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
    public Result<HolidayType> checkSingleType( String holidayUUids ) {
    	
    	Result<HolidayType> result
    		= new Result<HolidayType>();
    	result.setValue( Value.OK );
    	
    	HolidayType type
    		= null;
    	
    	if ( holidayUUids != null ) {
    		
    		Set<Holiday> holidays
    			= this.holidaysFromUUidString( holidayUUids );
    		
    		for ( Holiday v : holidays ) {
		
				if ( v != null ) {
					
					if ( type == null ) {
						
						type = v.getType();
						
					}
					else {
						
						if ( ! type.equals( v.getType() ) ) {
							
							result.setErrorCode( ErrorCodes.APPLICATION_HOLIDAY_MIXED );
							
						}
						
					}
					
				}
				else {
					result.setErrorCode( ErrorCodes.APPLICATION_HOLIDAY_NOT_FOUND );
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

    // TODO, add period filter
    public List<Holiday> findCurrentHolidays() {
    	
    	List<Holiday> all
			= this.holidayRepository.findAll();
	
		List<Holiday> selected
			= new ArrayList<Holiday>( all.size() );
		
		Date now
			= new Date();
	
		for ( Holiday h : all ) {
			
			if ( h.getStart().after( now ) ) {
				logger.debug( "holiday [{}] is current", h.getName() );
				selected.add( h );
			}
			else {
				logger.debug( "holiday [{}] not current", h.getName() );
			}
			
		}
	
		Collections.sort( selected, this.holidayNameComparator );
	
		return selected;
    	
    }
    
    public Set<Holiday> holidaysFromUUidString( String uuidString ) {
    	
    	Set<Holiday> holidays
    		= set();
    	
        StringTokenizer tok
    		= new StringTokenizer( uuidString.trim(), ",", false );
        
	    while( tok.hasMoreTokens() ) {
	    	
	    	String t
	    		= tok.nextToken().trim();
	    	
	    	if ( t.length() == 0 ) {
	    		continue;
	    	}
	    	
	    	Holiday v 
	    		= this.holidayRepository.findByUuid( t ); 
	
	    	if ( v != null ) {
	    		holidays.add ( v );
	    	}
	    	else {
	    		throw new RuntimeException( "no holiday with id [" + t.trim() + "]" );
	    	}
	    	
	    }
	    
	    return holidays;
	
    }
    
    public Holiday findOneByUuid( String uuid ) {
    	
    	return this.holidayRepository.findByUuid( uuid );
	
    }
    
    public boolean hasType( Set<Holiday> holidays, HolidayType type ) {
    	
    	boolean has 
    		= false;
    	
    		for ( Holiday holiday : holidays ) {
    			
    			if ( type.equals( holiday.getType() ) ) {
    				has = true;
    				break;
    			}
    			
    		}
		
				    	
    	return has;
    	
    }


}
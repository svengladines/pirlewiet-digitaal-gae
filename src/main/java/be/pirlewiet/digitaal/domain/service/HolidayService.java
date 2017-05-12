package be.pirlewiet.digitaal.domain.service;

import static be.occam.utils.javax.Utils.list;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import be.occam.utils.spring.web.Result;
import be.occam.utils.spring.web.Result.Value;
import be.pirlewiet.digitaal.domain.exception.ErrorCodes;
import be.pirlewiet.digitaal.domain.people.DoorMan;
import be.pirlewiet.digitaal.domain.people.HolidayManager;
import be.pirlewiet.digitaal.dto.HolidayDTO;
import be.pirlewiet.digitaal.model.Holiday;
import be.pirlewiet.digitaal.model.HolidayType;
import be.pirlewiet.digitaal.model.Organisation;

@Service
public class HolidayService extends be.pirlewiet.digitaal.domain.service.Service<HolidayDTO,Holiday> {
	
	@Resource
	protected DoorMan doorMan;
	
	@Resource
	HolidayManager holidayManager;
	
	protected final Comparator<Holiday> chronological = new Comparator<Holiday>() {

		@Override
		public int compare(Holiday o1, Holiday o2) {
			return o1.getStart().compareTo( o2.getStart() );
			
		}
		
	};
	
	public Holiday retrieve( String id, Organisation actor ) {
		return new Holiday();
	}
	
	public Holiday create( Holiday application, Organisation actor ) {
		return application;
	}
	
	public Holiday update( Holiday application, Organisation actor ) {
		return application;
	}
	
	public Holiday delete( Holiday application, Organisation actor ) {
		return application;
	}

	/**
	 * Finds current holidays.
	 */
	@Override
	public Result<List<Result<HolidayDTO>>> query(Organisation actor) {
		
		List<Holiday> currentHolidays
			= this.holidayManager.findCurrentHolidays();
		
		Collections.sort( currentHolidays, this.chronological );
		
		Result<List<Result<HolidayDTO>>> result
			= new Result<List<Result<HolidayDTO>>>();
		
		List<Result<HolidayDTO>> dtos
			= list();
		
		for ( Holiday holiday : currentHolidays ) {
			
			Result<HolidayDTO> individualResult
				= new Result<HolidayDTO>();
			
			individualResult.setValue( Value.OK );
			individualResult.setObject( HolidayDTO.from( holiday ) );
			
			dtos.add( individualResult );
			
		}
		
		result.setValue( Value.OK );
		result.setObject( dtos );
		
		return result;
		
	}
	
	public Result<List<HolidayDTO>> resolve(String enrollmentHolidayString, String applicationHolidayString, boolean checkNotEmpty, boolean checkSingle, boolean checkSingleType, Organisation actor) {
		
		Result<List<HolidayDTO>> result
			= new Result<List<HolidayDTO>>();
		
		result.setValue( Value.OK );
		
		List<HolidayDTO> dtos
			= list();
		
		List<Holiday> holidays
			= list();
		
		if ( enrollmentHolidayString != null ) {
		
			holidays.addAll( this.holidayManager.holidaysFromUUidString( enrollmentHolidayString ) );
			
			for ( Holiday holiday : holidays ) {
				
				dtos.add( HolidayDTO.from( holiday ) );
				
			}
			
		}
		
		if ( applicationHolidayString != null ) {
			
			holidays.addAll( this.holidayManager.holidaysFromUUidString( applicationHolidayString ) );
			
			for ( Holiday holiday : holidays ) {
				
				dtos.add( HolidayDTO.from( holiday ).setIsApplicationHoliday( true ) );
				
			}
		}
		
		result.setObject( dtos );
		
		if ( checkNotEmpty) {
			
			if ( holidays.size() == 0 ) {
				
				result.setValue( Value.NOK );
				
			}
		}
		
		if ( checkSingle ) {
			
			if ( holidays.size() == 1 ) { 
				// no need to change status
			} else if ( holidays.size() == 0 ) {
			
				result.setValue( Value.NOK );
				result.setErrorCode( ErrorCodes.ENROLLMENT_HOLIDAY_NONE );
				
			} else {
			
				result.setValue( Value.NOK );
				result.setErrorCode( ErrorCodes.ENROLLMENT_HOLIDAY_NONE );
				
			}
		}
		
		if ( checkSingleType ) {
			
			Result<HolidayType> r 
				= this.holidayManager.checkSingleType( enrollmentHolidayString );
			
			if ( ! Value.OK.equals( r.getValue() ) ) {
				logger.info( "selected holidays [{}] not compatible", enrollmentHolidayString );
				result.setValue( r.getValue() );
				result.setErrorCode( r.getErrorCode() );
			}
		}
		
		
		return result;
		
	}
	
}

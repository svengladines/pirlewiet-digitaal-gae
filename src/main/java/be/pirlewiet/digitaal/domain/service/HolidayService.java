package be.pirlewiet.digitaal.domain.service;

import static be.occam.utils.javax.Utils.*;
import java.util.Arrays;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import be.occam.utils.spring.web.Result;
import be.occam.utils.spring.web.Result.Value;
import be.pirlewiet.digitaal.domain.people.DoorMan;
import be.pirlewiet.digitaal.domain.people.HolidayManager;
import be.pirlewiet.digitaal.dto.HolidayDTO;
import be.pirlewiet.digitaal.model.Holiday;
import be.pirlewiet.digitaal.model.Organisation;

@Service
public class HolidayService extends be.pirlewiet.digitaal.domain.service.Service<HolidayDTO,Holiday> {
	
	@Resource
	protected DoorMan doorMan;
	
	@Resource
	HolidayManager holidayManager;
	
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
	
	public Result<List<HolidayDTO>> resolve(String holidayString, Organisation actor) {
		
		List<Holiday> holidays
			= this.holidayManager.holidaysFromUUidString( holidayString );
		
		Result<List<HolidayDTO>> result
			= new Result<List<HolidayDTO>>();
		
		List<HolidayDTO> dtos
			= list();
		
		for ( Holiday holiday : holidays ) {
			
			dtos.add( HolidayDTO.from( holiday ) );
			
		}
		
		result.setValue( Value.OK );
		result.setObject( dtos );
		
		return result;
		
	}
	
	
	
}

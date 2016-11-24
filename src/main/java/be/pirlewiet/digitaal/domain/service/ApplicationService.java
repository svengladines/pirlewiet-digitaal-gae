package be.pirlewiet.digitaal.domain.service;

import static be.occam.utils.javax.Utils.list;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import be.occam.utils.spring.web.Result;
import be.occam.utils.spring.web.Result.Value;
import be.pirlewiet.digitaal.domain.people.ApplicationManager;
import be.pirlewiet.digitaal.domain.people.DoorMan;
import be.pirlewiet.digitaal.domain.people.HolidayManager;
import be.pirlewiet.digitaal.dto.ApplicationDTO;
import be.pirlewiet.digitaal.dto.HolidayDTO;
import be.pirlewiet.digitaal.model.Application;
import be.pirlewiet.digitaal.model.Holiday;
import be.pirlewiet.digitaal.model.Organisation;

@Service
public class ApplicationService extends be.pirlewiet.digitaal.domain.service.Service<ApplicationDTO,Application> {
	
	@Resource
	protected DoorMan doorMan;
	
	@Resource
	ApplicationManager applicationManager;
	
	@Resource
	HolidayManager holidayManager;
	
	@Override
	public ApplicationService guard() {
		super.guard();
		return this;
	}

	@Override
	@Transactional(readOnly=true)
	public Result<List<ApplicationDTO>> query( Organisation actor ) {
		
		Result<List<ApplicationDTO>> result
			= new Result<List<ApplicationDTO>>();
		
		List<Application> applications
			= this.guard().applicationManager.findByOrganisation( actor );
		
		List<ApplicationDTO> dtos
			= list();
		
		for ( Application application : applications ) {
			ApplicationDTO dto
				= ApplicationDTO.from( application );
			this.extend( dto, application );
			dtos.add ( dto );
		}
		
		result.setValue( Value.OK );
		result.setObject( dtos );
		
		return result;
		
	}
	
	@Transactional(readOnly=true)
	public Result<ApplicationDTO> findOne( String uuid, Organisation actor ) {
		
		Result<ApplicationDTO> result
			= new Result<ApplicationDTO>();
		
		Application application
			= this.guard().applicationManager.findOne( uuid );
		
		ApplicationDTO dto
			= ApplicationDTO.from( application );
		
		this.extend( dto, application );
		
		result.setValue( Value.OK );
		result.setObject( dto );
		
		return result;
		
	}
	
	@Transactional(readOnly=false)
	public Result<ApplicationDTO> updateHolidays ( String uuid, List<HolidayDTO> holidays, Organisation actor ) {
		
		logger.info("application.updateHolidays");
		
		Result<ApplicationDTO> result
			= new Result<ApplicationDTO>();
		
		List<Holiday> holidayz
			= list();
		
		for ( HolidayDTO dto : holidays ) {
			
			Holiday holiday 
				=  Holiday.from( dto );
			
			holidayz.add( holiday );
		}
		
		Application updated
			= this.applicationManager.updateHolidays( uuid, holidayz );
		
		result.setValue( Value.OK );
		result.setObject( ApplicationDTO.from( updated ) );
		
		return result;
		
	}
	
	protected void extend( ApplicationDTO dto, Application application ) {
		
		String holidayString
			= application.getHolidayUuids();
		
		List<Holiday> holidays
			= this.holidayManager.holidaysFromUUidString( holidayString );
		
		for ( Holiday holiday : holidays ) {
			
			HolidayDTO holidayDTO
				= HolidayDTO.from( holiday );
			
			dto.getHolidays().add( holidayDTO );
			
		}
		
		
	}
	
	
	
}

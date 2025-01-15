package be.pirlewiet.digitaal.domain.service;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Service;

import be.occam.utils.spring.web.Result;
import be.occam.utils.spring.web.Result.Value;
import be.pirlewiet.digitaal.domain.exception.ErrorCodes;
import be.pirlewiet.digitaal.domain.people.DoorMan;
import be.pirlewiet.digitaal.domain.people.PersonManager;
import be.pirlewiet.digitaal.model.Person;
import be.pirlewiet.digitaal.web.dto.PersonDTO;

@Service
public class PersonService extends be.pirlewiet.digitaal.domain.service.Service<PersonDTO,Person> {
	
	@Autowired
	protected DoorMan doorMan;
	
	@Autowired
	PersonManager personManager;
	
	public Result<PersonDTO> retrieve( String uuid ) {
		
		Result<PersonDTO> result 
			= new Result<PersonDTO>();
		
		Person person	
			= this.personManager.findOneByUuid( uuid );
		
		if ( person == null  ) {
			result.setValue( Value.NOK );
			result.setErrorCode( ErrorCodes.PWT_UNKNOWN_PERSON );
		}
		else {
			
			PersonDTO dto
				= PersonDTO.from( person );
			result.setValue( Value.OK );
			result.setObject( dto );
			
		}
		
		return result;
		
	}
	
	
}

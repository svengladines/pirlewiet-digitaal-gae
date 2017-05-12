package be.pirlewiet.digitaal.web.controller.api;

import static be.occam.utils.spring.web.Controller.response;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import be.occam.utils.spring.web.Result;
import be.pirlewiet.digitaal.domain.people.DoorMan;
import be.pirlewiet.digitaal.domain.service.OrganisationService;
import be.pirlewiet.digitaal.dto.AddressDTO;
import be.pirlewiet.digitaal.dto.OrganisationDTO;
import be.pirlewiet.digitaal.model.Organisation;

@Controller
@RequestMapping( {"/organisations/{organisationUuid}"} )
public class OrganisationController {
	
	protected Logger logger 
		= LoggerFactory.getLogger( this.getClass() );
	
	@Resource
	OrganisationService organisationService;
	
	@Resource
	DoorMan doorMan;
	
	
	@RequestMapping( method = { RequestMethod.DELETE } )
	@ResponseBody
	public ResponseEntity<Result<OrganisationDTO>> delete( @PathVariable String organisationUuid, @CookieValue(required=true, value="pwtid") String pwtid ) {
		
			Organisation actor
				= this.doorMan.guard().whoHasID(  pwtid  );
		
			if ( actor == null ) {
				return response( HttpStatus.NOT_FOUND );
			}
			
			Result<OrganisationDTO> result
				= this.organisationService.guard().delete( organisationUuid , actor );
		
			return response( result, HttpStatus.OK );
			
	}
	
}

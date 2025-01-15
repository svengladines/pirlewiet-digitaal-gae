package be.pirlewiet.digitaal.web.controller.api;

import static be.occam.utils.spring.web.Controller.response;

import be.pirlewiet.digitaal.web.dto.AddressDTO;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import be.occam.utils.spring.web.Result;
import be.pirlewiet.digitaal.domain.people.DoorMan;
import be.pirlewiet.digitaal.domain.service.OrganisationService;
import be.pirlewiet.digitaal.model.Organisation;
import be.pirlewiet.digitaal.web.dto.OrganisationDTO;

@Controller
@RequestMapping( {"/api/organisations/{organisationUuid}"} )
public class OrganisationController {
	
	protected Logger logger 
		= LoggerFactory.getLogger( this.getClass() );
	
	@Autowired
	OrganisationService organisationService;
	
	@Autowired
	DoorMan doorMan;

	@RequestMapping( method = { RequestMethod.PUT } )
	@ResponseBody
	public ResponseEntity<OrganisationDTO> update(@PathVariable String organisationUuid, @RequestBody OrganisationDTO organisation, @CookieValue(required=true, value="pwtid") String pwtid ) {

		Organisation actor = this.doorMan.guard().whoHasID(  pwtid  );

		if ( actor == null ) {
			return response( HttpStatus.NOT_FOUND );
		}

		Result<OrganisationDTO> result = this.organisationService.guard().update(organisation, actor);

		return new ResponseEntity<>( result.getObject(), HttpStatus.OK );

	}
	
	@RequestMapping( method = { RequestMethod.DELETE } )
	@ResponseBody
	public ResponseEntity<OrganisationDTO> delete( @PathVariable String organisationUuid, @CookieValue(required=true, value="pwtid") String pwtid ) {
		
			Organisation actor
				= this.doorMan.guard().whoHasID(  pwtid  );
		
			if ( actor == null ) {
				return response( HttpStatus.NOT_FOUND );
			}
			
			Result<OrganisationDTO> result = this.organisationService.guard().delete( organisationUuid , actor );

			return new ResponseEntity<>( result.getObject(), HttpStatus.OK );
			
	}

	@RequestMapping( value="/address", method = { RequestMethod.PUT } )
	@ResponseBody
	public ResponseEntity<Result<AddressDTO>> adressUpdate(
			@RequestBody AddressDTO address,
			@CookieValue(required=true, value="pwtid") String pwtid,
			HttpServletResponse response ) {

		Organisation actor = this.doorMan.guard().whoHasID(  pwtid  );
		Result<AddressDTO> result = this.organisationService.guard().updateAddress( actor.getUuid(), address, actor );
		return new ResponseEntity<>( result, HttpStatus.OK );

	}
	
}

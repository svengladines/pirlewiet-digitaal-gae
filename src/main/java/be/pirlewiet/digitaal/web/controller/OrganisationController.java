package be.pirlewiet.digitaal.web.controller;

import static be.occam.utils.spring.web.Controller.response;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import be.occam.utils.spring.web.Result;
import be.occam.utils.spring.web.Result.Value;
import be.pirlewiet.digitaal.domain.exception.ErrorCodes;
import be.pirlewiet.digitaal.domain.people.DoorMan;
import be.pirlewiet.digitaal.domain.service.OrganisationService;
import be.pirlewiet.digitaal.dto.AddressDTO;
import be.pirlewiet.digitaal.dto.OrganisationDTO;
import be.pirlewiet.digitaal.model.Organisation;

@Controller
@RequestMapping( {"/organisation"} )
public class OrganisationController {
	
	protected Logger logger 
		= LoggerFactory.getLogger( this.getClass() );
	
	@Resource
	OrganisationService organisationService;
	
	@Resource
	DoorMan doorMan;
	
	
	public ResponseEntity<Result<OrganisationDTO>> retrieveMine( @CookieValue(required=true, value="pwtid") String pwtid  ) {
		
		Organisation actor
			= this.doorMan.guard().whoHasID(  pwtid  );
		
		Result<OrganisationDTO> result 
			= this.organisationService.findOneByUuid( pwtid, actor );

		return response( result, result.getValue().equals( Value.OK ) ? HttpStatus.OK : result.getErrorCode().equals( ErrorCodes.ORGANISATION_NOT_FOUND ) ? HttpStatus.NOT_FOUND : HttpStatus.INTERNAL_SERVER_ERROR );
		
	}
	
	
	@RequestMapping( method = { RequestMethod.PUT } )
	@ResponseBody
	public ResponseEntity<Result<OrganisationDTO>> update( @RequestBody OrganisationDTO organisation, @CookieValue(required=true, value="pwtid") String pwtid ) {
		
			Organisation actor
				= this.doorMan.guard().whoHasID(  pwtid  );
		
			if ( actor == null ) {
				return response( HttpStatus.NOT_FOUND );
			}
			
			Result<OrganisationDTO> result
				= this.organisationService.guard().update( organisation , actor );
		
			return response( result, HttpStatus.OK );
			
	}
	
	@RequestMapping( value="/address", method = { RequestMethod.PUT } )
	@ResponseBody
	public ResponseEntity<Result<AddressDTO>> adressUpdate(
				@RequestBody AddressDTO address,
				@CookieValue(required=true, value="pwtid") String pwtid,
				HttpServletResponse response ) {
		
		Organisation actor
			= this.doorMan.guard().whoHasID(  pwtid  );
	
		Result<AddressDTO> result
			= this.organisationService.guard().updateAddress( actor.getUuid(), address, actor );
		
		return response( result, HttpStatus.OK );
		
	}
	
	
	@RequestMapping( method = { RequestMethod.GET }, produces={ MediaType.TEXT_HTML_VALUE } )
	public ModelAndView view( @CookieValue(required=false, value="pwtid") String pwtid ) {
		
		OrganisationDTO organisation
			= null;
		
		if ( pwtid != null ) {
			
			ResponseEntity<Result<OrganisationDTO>> entity
				= this.retrieveMine( pwtid );
			
			organisation = entity.getBody().getObject();
			
		}
		
		if ( organisation == null ) {
			organisation = new OrganisationDTO();
		}
		
		Map<String,Object> model
			= new HashMap<String,Object>();
		
		model.put( "organisation", organisation );
		model.put( "incomplete", organisation.getInComplete() );

		String view 
			= be.pirlewiet.digitaal.web.util.PirlewietUtil.isPirlewiet( organisation ) ? "pirlewiet" : "organisation";
		
		return new ModelAndView( view, model );	
		
	}
	
	/*
	@RequestMapping( value="/{uuid}", method = { RequestMethod.GET }, produces={ MediaType.TEXT_HTML_VALUE } )
	public ModelAndView viewAsPirlewiet(
			@CookieValue(required=true, value="pwtid") String pwtid,
			@PathVariable(value="uuid" ) String uuid ) {
		

		Map<String,Object> model
			= new HashMap<String,Object>();
		
		if ( pwtid != null ) {
			
			OrganisationDTO loaded
				= this.doorMan.guard().whoHasID(  pwtid  );
			
			if ( ! PirlewietUtil.isPirlewiet( loaded ) ) {
				loaded = this.doorMan.guard().whoHasID( uuid  );
			}
			
			model.put( "organisation", loaded );
			model.put( "incomplete", this.organisationService.isInComplete( loaded, true ) );
			model.put( "isPirlewiet", true );
			
		}
		String view 
			= "organisation";
		
		return new ModelAndView( view, model );	
		
	}
	*/
	
}

package be.pirlewiet.digitaal.web.controller.api;

import static be.occam.utils.spring.web.Controller.response;

import java.util.List;
import java.util.Locale;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import be.occam.utils.spring.web.Result;
import be.occam.utils.spring.web.Result.Value;
import be.pirlewiet.digitaal.domain.people.DoorMan;
import be.pirlewiet.digitaal.domain.service.OrganisationService;
import be.pirlewiet.digitaal.model.Organisation;
import be.pirlewiet.digitaal.web.dto.OrganisationDTO;
import be.pirlewiet.digitaal.web.util.ExcelImporter;

@Controller
@RequestMapping(value="/organisations")
public class OrganisationsController {
	
	private final Logger logger = LoggerFactory.getLogger( OrganisationsController.class );
	
	protected final OrganisationService organisationService;
	protected final DoorMan doorMan;

	@Autowired
    public OrganisationsController(OrganisationService organisationService, DoorMan doorMan) {
        this.organisationService = organisationService;
        this.doorMan = doorMan;
    }


    @RequestMapping( method = { RequestMethod.GET} )
	@ResponseBody
	public ResponseEntity<Result<List<Result<OrganisationDTO>>>> query( @CookieValue(required=true, value="pwtid") String pwtid, HttpServletResponse response ) {
		
		Organisation actor 
			= this.doorMan.whoHasID( pwtid );
		
		Result<List<Result<OrganisationDTO>>> created
			= this.organisationService.guard().query( actor );
		
		return response( created, HttpStatus.OK );
			
	}
	
	@RequestMapping( method = { RequestMethod.POST }, consumes = { MediaType.APPLICATION_JSON_VALUE }, produces = { MediaType.APPLICATION_JSON_VALUE } )
	@ResponseBody
	public ResponseEntity<Result<OrganisationDTO>> post( @RequestBody OrganisationDTO organisation, HttpServletResponse response ) {
		
		Result<OrganisationDTO> createdResult
			= this.organisationService.guard().create( organisation, null );
		
		if ( Value.OK.equals( createdResult.getValue() ) ) {
		
			Cookie cookie
				= new Cookie( "pwtid", createdResult.getObject().getUuid() );
	
			cookie.setMaxAge( 3600 * 24 * 30 * 12 );
		
			cookie.setPath( "/" );
		
			response.addCookie( cookie );
			
			return response( createdResult, HttpStatus.OK );
		
		}
		
		return response( HttpStatus.INTERNAL_SERVER_ERROR );
			
	}
	
	@RequestMapping( method = { RequestMethod.POST }, consumes={"multipart/form-data"} )
	public ResponseEntity<Result<List<Result<OrganisationDTO>>>> post( @RequestPart MultipartFile file, Locale locale,  @CookieValue(required=true, value="pwtid") String pwtid) { 
		
		logger.info("post, received file [{}]", file.getOriginalFilename() );
		
		Organisation actor 
			= this.doorMan.guard().whoHasID( pwtid );
	
		return response( this.organisationService.consume( file, actor ), HttpStatus.OK ); 
	
	}
			
}

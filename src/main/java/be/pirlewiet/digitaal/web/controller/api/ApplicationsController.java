package be.pirlewiet.digitaal.web.controller.api;

import java.lang.ref.Reference;
import java.util.List;

import be.pirlewiet.digitaal.domain.service.OrganisationService;
import be.pirlewiet.digitaal.web.dto.EnrollmentDTO;
import be.pirlewiet.digitaal.web.dto.OrganisationDTO;
import be.pirlewiet.digitaal.web.dto.ReferencedApplicationDTO;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import be.occam.utils.spring.web.Result;
import be.pirlewiet.digitaal.domain.people.DoorMan;
import be.pirlewiet.digitaal.domain.service.ApplicationService;
import be.pirlewiet.digitaal.model.Organisation;
import be.pirlewiet.digitaal.web.dto.ApplicationDTO;

import static be.pirlewiet.digitaal.web.controller.Controller.response;

@Controller
@RequestMapping( {"/api/applications"} )
public class ApplicationsController {
	
	protected Logger logger = LoggerFactory.getLogger( this.getClass() );
	@Autowired
	DoorMan doorMan;
	@Autowired
	ApplicationService applicationService;
	@Autowired
	OrganisationService organisationService;
	
	@RequestMapping( method = { RequestMethod.POST }, consumes = { MediaType.APPLICATION_JSON_VALUE }, produces = { MediaType.APPLICATION_JSON_VALUE } )
	@ResponseBody
	public ResponseEntity<Result<ApplicationDTO>> post( 
			@RequestBody ApplicationDTO application, 
			@CookieValue(required=true, value="pwtid") String pwtid  ) {
		
		Organisation actor = this.doorMan.guard().whoHasID(  pwtid  );
		Result<ApplicationDTO> createdResult = this.applicationService.guard().create( application, actor );
		return response( createdResult, HttpStatus.OK );
		
	}
	
	@RequestMapping( method = { RequestMethod.GET }, produces = { MediaType.APPLICATION_JSON_VALUE } )
	@ResponseBody
	public ResponseEntity<List<ApplicationDTO>> get( @CookieValue(required=true, value="pwtid") String pwtid  ) {
		
		List<ApplicationDTO> applications = this.applicationService.guard().findAll();
		
		return response( applications, HttpStatus.OK );
		
	}

	/**
	 * Creates an application based on a reference from another application (like ivv).
	 * @param application
	 * @return
	 */
	@RequestMapping( method = { RequestMethod.POST }, consumes = { MediaType.APPLICATION_FORM_URLENCODED_VALUE })
	public String postForm(
			@ModelAttribute ReferencedApplicationDTO application,
			HttpServletResponse response,
			Model model){

		// first create an invididual organisation
		Result<OrganisationDTO> rCreatedOrganisation = this.organisationService.guard().createFromPerson(application.getApplicant());
		if (Result.Value.OK.equals(rCreatedOrganisation.getValue())) {
			Cookie cookie = new Cookie( "pwtid", "" + rCreatedOrganisation.getObject().getUuid());
			cookie.setMaxAge( 3600 * 24 * 30 * 12 );
			cookie.setPath( "/" );
			response.addCookie( cookie );
			// then create a new application
			Result<ApplicationDTO> rCreatedApplication = this.applicationService.createReferenced(application,rCreatedOrganisation.getObject().getUuid());
			if (Result.Value.OK.equals(rCreatedApplication.getValue())) {
				return "redirect:/referenced/application-%s.html".formatted(rCreatedApplication.getObject().getUuid());
			}
			else {
				logger.error("failed to create referenced application with reference [{}], e-mail [{}], error code = [{}]", application.getReference(), application.getApplicant().getEmail(), rCreatedApplication.getErrorCode().getCode());
			}
		}
		else {
			logger.error("failed to create individual organisation with reference [{}] and e-mail [{}], error code- [{}]", application.getReference(), application.getApplicant().getEmail(), rCreatedOrganisation.getErrorCode().getCode());
		}

		// TODO proper error handling
		return "referenced/error";

	}
	
}

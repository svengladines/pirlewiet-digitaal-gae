package be.pirlewiet.digitaal.web.controller.page.pirlewiet;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import be.occam.utils.spring.web.Result;
import be.pirlewiet.digitaal.domain.people.DoorMan;
import be.pirlewiet.digitaal.domain.service.OrganisationService;
import be.pirlewiet.digitaal.model.Organisation;
import be.pirlewiet.digitaal.web.dto.AddressDTO;
import be.pirlewiet.digitaal.web.dto.OrganisationDTO;
import be.pirlewiet.digitaal.web.util.PirlewietUtil;

@Controller
@RequestMapping(value="/organisation-pirlewiet-{uuid}.html")
public class PirlewietOrganisationPageController {
	
	private final static Logger logger = LoggerFactory.getLogger( PirlewietOrganisationPageController.class );
	
	@Autowired
	OrganisationService organisationService;
	
	@Autowired
	protected DoorMan doorMan;
	
	@RequestMapping( method = { RequestMethod.GET }, produces={ MediaType.TEXT_HTML_VALUE } ) 
	public ModelAndView view( 
			@CookieValue( required = true, value="pwtid" ) String pwtID,
			@PathVariable String uuid)  {
		
		OrganisationDTO organisation
			= null;
		
		Organisation actor
			= this.doorMan.guard().whoHasID(  pwtID  );
		
		
		Result<OrganisationDTO> result 
			= this.organisationService.findOneByUuid( uuid, actor );
			
		organisation = result.getObject();
			
		if ( organisation == null ) {
			organisation = new OrganisationDTO();
		}
		
		AddressDTO address
			= null;
		
		if ( organisation.getAddressUuid() == null ) {
			address = new AddressDTO();
		}
		else {
			Result<AddressDTO> addressResult
				= this.organisationService.getAddress( organisation.getAddressUuid() , actor); 
			address  = addressResult.getObject();
		}
		
		Map<String,Object> model
			= new HashMap<String,Object>();
		
		model.put( "organisation", organisation );
		model.put( "incomplete", organisation.getInComplete() );
		model.put( "address", address );
		
		model.put( "isPirlewiet", PirlewietUtil.isPirlewiet( actor ) );

		String view 
			= "organisation";
		
		return new ModelAndView( view, model );	
			
	}
	
}

package be.pirlewiet.digitaal.web.controller.page;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

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
import be.pirlewiet.digitaal.dto.AddressDTO;
import be.pirlewiet.digitaal.dto.OrganisationDTO;
import be.pirlewiet.digitaal.model.Organisation;

@Controller
@RequestMapping(value="/organisation.html")
public class OrganisationPageController {
	
	private final static Logger logger = LoggerFactory.getLogger( OrganisationPageController.class );
	
	@Resource
	OrganisationService organisationService;
	
	@Resource
	protected DoorMan doorMan;
	
	@RequestMapping( method = { RequestMethod.GET }, produces={ MediaType.TEXT_HTML_VALUE } ) 
	public ModelAndView view( @CookieValue( required = true, value="pwtid" ) String pwtID )  {
		
		OrganisationDTO organisation
			= null;
		
		Organisation actor
			= this.doorMan.guard().whoHasID(  pwtID  );
		
		
		Result<OrganisationDTO> result 
			= this.organisationService.findOneByUuid( pwtID, actor );
			
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

		String view 
			= be.pirlewiet.digitaal.web.util.PirlewietUtil.isPirlewiet( organisation ) ? "pirlewiet" : "organisation";
		
		return new ModelAndView( view, model );	
			
	}
	
}

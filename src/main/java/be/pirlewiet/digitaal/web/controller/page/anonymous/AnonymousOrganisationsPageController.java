package be.pirlewiet.digitaal.web.controller.page.anonymous;

import be.occam.utils.spring.web.Result;
import be.pirlewiet.digitaal.domain.service.OrganisationService;
import be.pirlewiet.digitaal.web.dto.OrganisationDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

@Controller
@RequestMapping( {"/anonymous/organisations.html"} )
public class AnonymousOrganisationsPageController {
	
	protected Logger logger = LoggerFactory.getLogger( this.getClass() );
	
	@Autowired
	OrganisationService organisationService;
	
	protected final Comparator<Result<OrganisationDTO>> byCity 
		= new Comparator<Result<OrganisationDTO>>() {

			@Override
			public int compare(Result<OrganisationDTO> o1, Result<OrganisationDTO> o2) {
				
				return o1.getObject().getCity().compareTo( o2.getObject().getCity() );

			}
		
	};
	
	protected final Comparator<Result<OrganisationDTO>> byName 
	= new Comparator<Result<OrganisationDTO>>() {

		@Override
		public int compare(Result<OrganisationDTO> o1, Result<OrganisationDTO> o2) {
			
			return o1.getObject().getName().compareTo( o2.getObject().getName() );

		}
	
};
	
	@RequestMapping( method = { RequestMethod.GET }, produces={ MediaType.TEXT_HTML_VALUE } )
	public String view( @RequestParam(required=false) String order, Model model  ) {
		
		Result<List<Result<OrganisationDTO>>> organisationsResult = this.organisationService.guard().query( null, false );
		
		this.order(organisationsResult.getObject(), order );
			
		model.addAttribute( "organisationsResult", organisationsResult );
	
		return "anonymous/organisations";

	}
	
	protected void order( List<Result<OrganisationDTO>> list, String order ) {
		
		if ( "city".equals( order ) ) {
			Collections.sort( list, this.byCity );
		}
		else {
			Collections.sort( list, this.byName );
		}
		
	}
	

}

package be.pirlewiet.digitaal.web.controller.page;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import be.occam.utils.spring.web.Result;
import be.pirlewiet.digitaal.domain.people.ApplicationManager;
import be.pirlewiet.digitaal.domain.people.DoorMan;
import be.pirlewiet.digitaal.domain.service.ApplicationService;
import be.pirlewiet.digitaal.domain.service.HolidayService;
import be.pirlewiet.digitaal.dto.ApplicationDTO;
import be.pirlewiet.digitaal.dto.HolidayDTO;
import be.pirlewiet.digitaal.model.Organisation;

@Controller
@RequestMapping( {"application.html"} )
public class ApplicationPageController {
	
	protected Logger logger 
		= LoggerFactory.getLogger( this.getClass() );
	
	@Resource
	ApplicationService applicationService;
	
	@Resource
	HolidayService holidayService;
	
	@Resource
	DoorMan doorMan;
	
	@Resource
	ApplicationManager applicationManager;
	
	@RequestMapping( method = { RequestMethod.GET }, produces={ MediaType.TEXT_HTML_VALUE } )
	public ModelAndView view( @RequestParam String uuid, @CookieValue(required=true, value="pwtid") String pwtid ) {
		
		Organisation actor
			= this.doorMan.guard().whoHasID(  pwtid  );
		
		Result<ApplicationDTO> result
			= this.applicationService.findOne( uuid, actor );
		
		Map<String,Object> model
			= new HashMap<String,Object>();
		
		model.put( "applicationResult", result );
		
		/*
		List<Holiday> vakanties
			= this.secretariaatsMedewerker.guard().actueleVakanties( );
		
		model.put( "vakanties", vakanties );
		model.put( "applicationHolidaysResult", this.secretariaatsMedewerker.guard().checkApplicationHolidaysStatus( application ) );
		model.put( "applicationContactResult", this.secretariaatsMedewerker.guard().checkApplicationContactStatus( application ) );
		model.put( "applicationQuestionListResult", this.secretariaatsMedewerker.guard().checkApplicationQuestionList( application ) );
		model.put( "enrollmentsStatus", this.secretariaatsMedewerker.guard().checkEnrollmentsStatus( application ) );
		model.put( "related", this.secretariaatsMedewerker.guard().findRelated( application, true) );
		
		EnrollmentStatus applicationStatus
			= this.secretariaatsMedewerker.guard().whatIsTheApplicationStatus( application );
		
		model.put("applicationStatus", applicationStatus );
		*/
		
		String view
			= "application";

		return new ModelAndView( view, model );
		
	}
	
}

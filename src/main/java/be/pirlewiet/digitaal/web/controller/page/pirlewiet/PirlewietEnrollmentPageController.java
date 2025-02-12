package be.pirlewiet.digitaal.web.controller.page.pirlewiet;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import be.occam.utils.spring.web.Result;
import be.pirlewiet.digitaal.domain.people.DoorMan;
import be.pirlewiet.digitaal.domain.service.ApplicationService;
import be.pirlewiet.digitaal.domain.service.EnrollmentService;
import be.pirlewiet.digitaal.domain.service.HolidayService;
import be.pirlewiet.digitaal.model.Organisation;
import be.pirlewiet.digitaal.web.dto.ApplicationDTO;
import be.pirlewiet.digitaal.web.dto.EnrollmentDTO;
import be.pirlewiet.digitaal.web.dto.HolidayDTO;
import be.pirlewiet.digitaal.web.util.PirlewietUtil;

@Controller
@RequestMapping(value="/pirlewiet/enrollment-{uuid}.html")
public class PirlewietEnrollmentPageController {
	
	private final static Logger logger = LoggerFactory.getLogger( PirlewietEnrollmentPageController.class );
	
	@Autowired
	EnrollmentService enrollmentService;
	
	@Autowired
	HolidayService holidayService;
	
	@Autowired
	ApplicationService applicationService;
	
	@Autowired
	protected DoorMan doorMan;
	
	@RequestMapping( method = { RequestMethod.GET }, produces={ MediaType.TEXT_HTML_VALUE } ) 
	public String view(
			@CookieValue( required = true, value="pwtid" ) String pwtID,
			@PathVariable String uuid,
			Model model)  {
		
		Organisation actor = this.doorMan.guard().whoHasID(  pwtID  );
		
		Result<EnrollmentDTO> enrollmentResult = this.enrollmentService.findOneByUuid( uuid );
		
		EnrollmentDTO enrollment = enrollmentResult.getObject();
		
		Result<ApplicationDTO> applicationResult = this.applicationService.findOne( enrollment.getApplicationUuid(), actor);

		ApplicationDTO application = applicationResult.getObject();
		
		Result<List<HolidayDTO>> holidaysResult
			= this.holidayService.resolve( enrollmentResult.getObject().getHolidayUuid(), application.getHolidayUuids(), true, true, false, actor);
	
		model.addAttribute( "holidaysResult", holidaysResult );
			
		model.addAttribute( "enrollmentResult", enrollmentResult );
		model.addAttribute("isPirlewiet", PirlewietUtil.isPirlewiet( actor ) );

		return "pirlewiet/enrollment";

	}
	
}

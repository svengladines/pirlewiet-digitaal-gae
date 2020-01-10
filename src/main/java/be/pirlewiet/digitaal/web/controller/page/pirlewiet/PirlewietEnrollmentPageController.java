package be.pirlewiet.digitaal.web.controller.page.pirlewiet;

import java.util.HashMap;
import java.util.List;
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
import be.pirlewiet.digitaal.domain.service.ApplicationService;
import be.pirlewiet.digitaal.domain.service.EnrollmentService;
import be.pirlewiet.digitaal.domain.service.HolidayService;
import be.pirlewiet.digitaal.model.Organisation;
import be.pirlewiet.digitaal.web.dto.ApplicationDTO;
import be.pirlewiet.digitaal.web.dto.EnrollmentDTO;
import be.pirlewiet.digitaal.web.dto.HolidayDTO;
import be.pirlewiet.digitaal.web.util.PirlewietUtil;

@Controller
@RequestMapping(value="/enrollment-{uuid}-pirlewiet.html")
public class PirlewietEnrollmentPageController {
	
	private final static Logger logger = LoggerFactory.getLogger( PirlewietEnrollmentPageController.class );
	
	@Resource
	EnrollmentService enrollmentService;
	
	@Resource
	HolidayService holidayService;
	
	@Resource
	ApplicationService applicationService;
	
	@Resource
	protected DoorMan doorMan;
	
	@RequestMapping( method = { RequestMethod.GET }, produces={ MediaType.TEXT_HTML_VALUE } ) 
	public ModelAndView view( 
			@CookieValue( required = true, value="pwtid" ) String pwtID,
			@PathVariable String uuid)  {
		
		Organisation actor
			= this.doorMan.guard().whoHasID(  pwtID  );
		
		Map<String,Object> model
			= new HashMap<String,Object>();
		
		Result<EnrollmentDTO> enrollmentResult 
			= this.enrollmentService.findOneByUuid( uuid );
		
		EnrollmentDTO enrollment
			= enrollmentResult.getObject();
		
		Result<ApplicationDTO> applicationResult
			= this.applicationService.findOne( enrollment.getApplicationUuid(), actor);
		
		ApplicationDTO application
			= applicationResult.getObject();
		
		Result<List<HolidayDTO>> holidaysResult
			= this.holidayService.resolve( enrollmentResult.getObject().getHolidayUuid(), application.getHolidayUuids(), true, true, false, actor);
	
		model.put( "holidaysResult", holidaysResult );
			
		model.put( "enrollmentResult", enrollmentResult );
		model.put( "isPirlewiet", PirlewietUtil.isPirlewiet( actor ) );

		String view 
			= "enrollment-pirlewiet";
		
		return new ModelAndView( view, model );	
			
	}
	
}

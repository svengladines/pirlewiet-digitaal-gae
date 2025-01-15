package be.pirlewiet.digitaal.web.controller.page.pirlewiet;

import static be.occam.utils.javax.Utils.*;
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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import org.springframework.beans.factory.annotation.Autowired;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping(value="/application-{uuid}-pirlewiet.html")
public class PirlewietApplicationPageController {
	
	private final static Logger logger = LoggerFactory.getLogger( PirlewietApplicationPageController.class );
	
	@Autowired
	HolidayService holidayService;
	
	@Autowired
	ApplicationService applicationService;

	@Autowired
	EnrollmentService enrollmentService;
	
	@Autowired
	protected DoorMan doorMan;
	
	@RequestMapping( method = { RequestMethod.GET }, produces={ MediaType.TEXT_HTML_VALUE } ) 
	public ModelAndView view( 
			@CookieValue( required = true, value="pwtid" ) String pwtID,
			@PathVariable String uuid)  {
		
		Organisation actor
			= this.doorMan.guard().whoHasID(  pwtID  );
		
		Map<String,Object> model
			= new HashMap<String,Object>();
		
		Result<ApplicationDTO> applicationResult
			= this.applicationService.findOne( uuid, actor );
		
		ApplicationDTO application
			= applicationResult.getObject();
		
		Result<List<HolidayDTO>> holidaysResult
			= this.holidayService.resolve( null, application.getHolidayUuids(), true, true, false, actor);

		List<Result<EnrollmentDTO>> appEnrollments
			= this.enrollmentService.query( uuid, actor ).getObject();

		if ( appEnrollments != null ) {

			for ( Result<EnrollmentDTO> enrollmentResult : appEnrollments ) {
				applicationResult.getObject().getEnrollments().add( enrollmentResult.getObject() );
			}
		}

		model.put( "applicationResult", applicationResult );
		model.put( "holidaysResult", holidaysResult );
		model.put( "isPirlewiet", PirlewietUtil.isPirlewiet( actor ) );

		String view 
			= "application-pirlewiet";
		
		return new ModelAndView( view, model );	
			
	}
	
}

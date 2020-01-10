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
import be.pirlewiet.digitaal.domain.service.EnrollmentService;
import be.pirlewiet.digitaal.domain.service.HolidayService;
import be.pirlewiet.digitaal.domain.service.PersonService;
import be.pirlewiet.digitaal.domain.service.QuestionAndAnswerService;
import be.pirlewiet.digitaal.model.Organisation;
import be.pirlewiet.digitaal.model.Tags;
import be.pirlewiet.digitaal.web.dto.ApplicationDTO;
import be.pirlewiet.digitaal.web.dto.EnrollmentDTO;
import be.pirlewiet.digitaal.web.dto.HolidayDTO;
import be.pirlewiet.digitaal.web.dto.PersonDTO;
import be.pirlewiet.digitaal.web.dto.QuestionAndAnswerDTO;

@Controller
@RequestMapping( {"/application-modals.html"} )
public class ApplicationPageModalsController {
	
	protected Logger logger 
		= LoggerFactory.getLogger( this.getClass() );
	
	@Resource
	ApplicationService applicationService;
	
	@Resource
	HolidayService holidayService;
	
	@Resource
	DoorMan doorMan;
	
	@Resource
	PersonService personService;
	
	@Resource
	ApplicationManager applicationManager;
	
	@Resource
	QuestionAndAnswerService questionAndAnswerService;
	
	@Resource
	EnrollmentService enrollmentService;
	
	@RequestMapping( method = { RequestMethod.GET }, produces={ MediaType.TEXT_HTML_VALUE } )
	public ModelAndView view( @RequestParam String uuid, @RequestParam String q, @RequestParam(required=false) String enrollmentUuid, @CookieValue(required=true, value="pwtid") String pwtid ) {
		
		Organisation actor
			= this.doorMan.guard().whoHasID(  pwtid  );
		
		Map<String,Object> model
			= new HashMap<String,Object>();
		
		Result<ApplicationDTO> applicationResult
			= this.applicationService.findOne( uuid, actor );
	
		model.put( "applicationResult", applicationResult );
		
		if ( "holidays".equals( q ) ) {
			
			Result<List<Result<HolidayDTO>>> holidaysResult 
				= this.holidayService.query( actor );
			
			model.put( "holidaysResult", holidaysResult );
			
		}
		else if ( "contact".equals( q ) ) {
			
			Result<PersonDTO> contactResult 
				= this.personService.retrieve( applicationResult.getObject().getContactPersonUuid() );
			
			model.put( "contactResult", contactResult );
			
		} else if ( "qlist".equals( q ) ) {
			
			Result<List<QuestionAndAnswerDTO>> qnaResult 
				= this.questionAndAnswerService.findByEntityAndTag( applicationResult.getObject().getUuid(), Tags.TAG_APPLICATION );
			
			model.put( "qnaResult", qnaResult );
			
		} else if ( "enrollment".equals( q ) ) {
		
			Result<EnrollmentDTO> enrollmentResult;
			
			if ( enrollmentUuid != null ) {
				enrollmentResult = this.enrollmentService.findOneByUuid( enrollmentUuid );
			} else {
				enrollmentResult = this.enrollmentService.template( );
			}
		
			model.put( "enrollmentResult", enrollmentResult );
			
		} else if ( "medical".equals( q ) ) {
			
			Result<List<QuestionAndAnswerDTO>> medicalResult 
				= this.questionAndAnswerService.findByEntityAndTag( enrollmentUuid, Tags.TAG_MEDIC );
			
			model.put( "enrollmentUuid", enrollmentUuid );
			model.put( "medicalResult", medicalResult );
			
		} else if ( "history".equals( q ) ) {
			
			Result<List<QuestionAndAnswerDTO>> medicalResult 
				= this.questionAndAnswerService.findByEntityAndTag( enrollmentUuid, Tags.TAG_HISTORY );
			
			model.put( "enrollmentUuid", enrollmentUuid );
			model.put( "historyResult", medicalResult );
			
		}
		
		String view
			= new StringBuilder("application-").append( q ).toString();

		return new ModelAndView( view, model );
		
	}
		
}

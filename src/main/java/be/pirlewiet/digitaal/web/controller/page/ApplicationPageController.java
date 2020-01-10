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
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import be.occam.utils.spring.web.Result;
import be.occam.utils.spring.web.Result.Value;
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
import be.pirlewiet.digitaal.web.util.PirlewietUtil;

@Controller
@RequestMapping( {"application-{uuid}.html"} )
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
	PersonService personService;
	
	@Resource
	QuestionAndAnswerService questionAndAnswerService;
	
	@Resource
	EnrollmentService enrollmentService;
	
	@RequestMapping( method = { RequestMethod.GET }, produces={ MediaType.TEXT_HTML_VALUE } )
	public ModelAndView view( @PathVariable String uuid, @CookieValue(required=true, value="pwtid") String pwtid ) {
		
		Organisation actor
			= this.doorMan.guard().whoHasID(  pwtid  );
		
		Result<ApplicationDTO> applicationResult
			= this.applicationService.findOne( uuid, actor );
		
		Map<String,Object> model
			= new HashMap<String,Object>();
		
		model.put( "applicationResult", applicationResult );
		
		if ( Value.OK.equals( applicationResult.getValue() ) ) {
			
			ApplicationDTO application
				= applicationResult.getObject();
			
			logger.info( "application [{}], contact uuid is [{}]", application.getUuid(), application.getContactPersonUuid() );
			
			Result<PersonDTO> contactResult
				= this.personService.retrieve( application.getContactPersonUuid() );
			
			logger.info( "application [{}], contact result is [{}]", application.getUuid(), contactResult.getValue() );
			
			model.put( "contactResult", contactResult );
			
			logger.info( "application [{}], holiday uuids are [{}]", application.getUuid(), application.getHolidayUuids() );
			
			Result<List<HolidayDTO>> holidaysResult
				= this.holidayService.resolve( null , application.getHolidayUuids(), true, false, true, actor);
			
			model.put( "holidaysResult", holidaysResult );
			
			logger.info( "application [{}], holidays result is [{}]", application.getUuid(), holidaysResult.getValue() );
			
			Result<List<QuestionAndAnswerDTO>> qnaResult 
				= this.questionAndAnswerService.findByEntityAndTag( applicationResult.getObject().getUuid(), Tags.TAG_APPLICATION );
			
			model.put( "applicationQuestionListResult", this.applicationService.checkApplicationQuestionList( application, qnaResult.getObject() ) );
			
			Result<List<Result<EnrollmentDTO>>> enrollmentsResult 
				= this.enrollmentService.query( application.getUuid(), actor );
			
			model.put( "enrollmentsResult", enrollmentsResult );
			
			
		}
		
		model.put( "isPirlewiet", PirlewietUtil.isPirlewiet( actor ) );
		
		String view
			= "application";

		return new ModelAndView( view, model );
		
	}
	
}

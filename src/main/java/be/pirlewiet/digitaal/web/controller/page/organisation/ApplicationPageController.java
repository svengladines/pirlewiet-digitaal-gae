package be.pirlewiet.digitaal.web.controller.page.organisation;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

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

import javax.swing.text.html.Option;

@Controller
@RequestMapping( {"/organisation/application-{uuid}.html"} )
public class ApplicationPageController {
	
	protected Logger logger = LoggerFactory.getLogger( this.getClass() );
	
	@Autowired
	ApplicationService applicationService;
	
	@Autowired
	HolidayService holidayService;
	
	@Autowired
	DoorMan doorMan;
	
	@Autowired
	PersonService personService;
	
	@Autowired
	QuestionAndAnswerService questionAndAnswerService;
	
	@Autowired
	EnrollmentService enrollmentService;
	
	@RequestMapping( method = { RequestMethod.GET }, produces={ MediaType.TEXT_HTML_VALUE } )
	public String view(
			@PathVariable String uuid, @CookieValue(required=true,
			value="pwtid") String pwtid,
			Model model) {
		
		Organisation actor = this.doorMan.guard().whoHasID(  pwtid  );
		Result<ApplicationDTO> applicationResult = this.applicationService.findOne( uuid, actor );
		
		model.addAttribute( "applicationResult", applicationResult );
		if ( Value.OK.equals( applicationResult.getValue() ) ) {
			ApplicationDTO application = applicationResult.getObject();
			logger.debug( "application [{}], applicant uuid is [{}]", application.getUuid(), application.getContactPersonUuid() );
			Result<PersonDTO> applicantResult = this.personService.retrieve( application.getContactPersonUuid() );
			logger.info( "application [{}], applicant result is [{}]", application.getUuid(), applicantResult.getValue() );
			model.addAttribute( "applicantResult", applicantResult );

			logger.info( "application [{}], holiday uuids are [{}]", application.getUuid(), application.getHolidayUuids() );
			
			Result<List<HolidayDTO>> holidaysResult = this.holidayService.resolve( null , application.getHolidayUuids(), true, false, true, actor);
			
			model.addAttribute( "holidaysResult", holidaysResult );
			
			logger.info( "application [{}], holidays result is [{}]", application.getUuid(), holidaysResult.getValue() );
			
			Result<List<Result<EnrollmentDTO>>> enrollmentsResult = this.enrollmentService.query( application.getUuid(), actor );
			
			model.addAttribute( "enrollmentsResult", enrollmentsResult );

			Result<List<QuestionAndAnswerDTO>> qnaResult = this.questionAndAnswerService.findByEntityAndTag( applicationResult.getObject().getUuid(), Tags.TAG_APPLICATION );
			// verify if all questions have been answeren correctly
			model.addAttribute( "applicationQuestionListResult", this.applicationService.checkApplicationQuestionList( application, qnaResult.getObject() ) );
		}
		return "organisation/application";
	}
	
}

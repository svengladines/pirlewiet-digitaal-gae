package be.pirlewiet.digitaal.web.controller.page;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
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
	
	@Autowired
	ApplicationService applicationService;
	
	@Autowired
	HolidayService holidayService;
	
	@Autowired
	DoorMan doorMan;
	
	@Autowired
	PersonService personService;
	
	@Autowired
	ApplicationManager applicationManager;
	
	@Autowired
	QuestionAndAnswerService questionAndAnswerService;
	
	@Autowired
	EnrollmentService enrollmentService;
	
	@RequestMapping( method = { RequestMethod.GET }, produces={ MediaType.TEXT_HTML_VALUE } )
	public String view(
			@RequestParam String uuid,
			@RequestParam String q,
			@RequestParam(required=false) String enrollmentUuid,
			@CookieValue(required=true, value="pwtid") String pwtid,
			Model model) {
		
		Organisation actor = this.doorMan.guard().whoHasID(  pwtid  );
		
		Result<ApplicationDTO> applicationResult = this.applicationService.findOne( uuid, actor );
	
		model.addAttribute( "applicationResult", applicationResult );
		
		if ( "holidays".equals( q ) ) {
			Result<List<Result<HolidayDTO>>> holidaysResult = this.holidayService.query( actor );
			Set<String> resolved = this.holidayService.resolve(null,applicationResult.getObject().getHolidayUuids(),false,false, false, actor)
					.getObject().stream().map(h -> h.getUuid()).collect(Collectors.toSet());
			holidaysResult.getObject().stream().forEach(r -> {
				r.setValue(resolved.contains(r.getObject().getUuid()) ? Result.Value.OK : Result.Value.NOK);
			});
			model.addAttribute( "holidaysResult", holidaysResult );
		}
		else if ( "contact".equals( q ) ) {
			
			Result<PersonDTO> contactResult = this.personService.retrieve( applicationResult.getObject().getContactPersonUuid() );
			model.addAttribute( "contactResult", contactResult );
			
		} else if ( "qlist".equals( q ) ) {
			
			Result<List<QuestionAndAnswerDTO>> qnaResult = this.questionAndAnswerService.findByEntityAndTag( applicationResult.getObject().getUuid(), Tags.TAG_APPLICATION );
			model.addAttribute( "qnaResult", qnaResult );
			
		} else if ( "enrollment".equals( q ) ) {
		
			Result<EnrollmentDTO> enrollmentResult;
			
			if ((enrollmentUuid != null) && (!enrollmentUuid.equals("undefined"))) {
				enrollmentResult = this.enrollmentService.findOneByUuid( enrollmentUuid );
			} else {
				enrollmentResult = this.enrollmentService.template( );
			}
		
			model.addAttribute( "enrollmentResult", enrollmentResult );
			
		} else if ( "participant".equals( q ) ) {
			
			Result<List<QuestionAndAnswerDTO>> participantResult 
				= this.questionAndAnswerService.findByEntityAndTag( enrollmentUuid, Tags.TAG_PARTICIPANT );
			
			model.addAttribute( "enrollmentUuid", enrollmentUuid );
			model.addAttribute( "participantResult", participantResult );
			
		}
		
		return "application-%s".formatted( q );

	}
		
}

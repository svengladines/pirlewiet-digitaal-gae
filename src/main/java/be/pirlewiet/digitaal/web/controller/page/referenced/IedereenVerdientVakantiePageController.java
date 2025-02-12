package be.pirlewiet.digitaal.web.controller.page.referenced;

import be.occam.utils.spring.web.Result;
import be.occam.utils.spring.web.Result.Value;
import be.pirlewiet.digitaal.domain.exception.ErrorCodes;
import be.pirlewiet.digitaal.domain.exception.NoReferenceException;
import be.pirlewiet.digitaal.domain.people.DoorMan;
import be.pirlewiet.digitaal.domain.service.*;
import be.pirlewiet.digitaal.model.Organisation;
import be.pirlewiet.digitaal.model.Tags;
import be.pirlewiet.digitaal.web.dto.*;
import be.pirlewiet.digitaal.web.util.PirlewietUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping( {"iedereen-verdient-vakantie.html"} )
public class IedereenVerdientVakantiePageController {
	
	protected Logger logger = LoggerFactory.getLogger( this.getClass() );
	
	@RequestMapping( method = { RequestMethod.GET }, produces={ MediaType.TEXT_HTML_VALUE } )
	public String view(
			@RequestParam(value = "ivv-tc", required = false) String reference,
			Model model) {

		Result<String> result = new Result<>();

		if (reference == null) {
			result.setErrorCode(ErrorCodes.REFERENCED_NO_CODE);
			result.setValue(Value.NOK);
		}
		else {
			result.setValue(Value.OK);
			result.setObject(reference);
		}
		model.addAttribute("referenceResult", result);
		return "iedereen-verdient-vakantie";

	}
	
}

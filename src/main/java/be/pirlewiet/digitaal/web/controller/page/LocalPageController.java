package be.pirlewiet.digitaal.web.controller.page;

import be.pirlewiet.digitaal.domain.HeadQuarters;
import be.pirlewiet.digitaal.domain.people.DoorMan;
import be.pirlewiet.digitaal.domain.people.OrganisationManager;
import be.pirlewiet.digitaal.model.Organisation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequestMapping(value="/local.html")
public class LocalPageController {
	
	private final static Logger logger = LoggerFactory.getLogger( LocalPageController.class );
	
	@Autowired
	HeadQuarters headQuarters;
	
	@RequestMapping(method=RequestMethod.GET)
	public String view()  {
		this.headQuarters.initialize();
		return "redirect:login.html";
	}
}

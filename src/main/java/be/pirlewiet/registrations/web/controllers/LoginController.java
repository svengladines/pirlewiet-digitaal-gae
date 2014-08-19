package be.pirlewiet.registrations.web.controllers;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class LoginController {

	Logger logger = Logger.getLogger(this.getClass());
	
	@RequestMapping(value="/login")
	public String returnCustomLoginpage(){
		return "common/login";
	}
	
	@RequestMapping(value="/loginfailed", method = RequestMethod.GET)
	public String loginerror(ModelMap model) {
		model.addAttribute("error", "true");
		return "common/login";
	}
}

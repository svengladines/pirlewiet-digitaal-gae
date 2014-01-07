package be.pirlewiet.registrations.view.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping(value="error")
public class ErrorController {

	@RequestMapping(value="/accessdenied")
	public String accesdenied() {
		return "error/accessdenied";
	}
	
	@RequestMapping(value="/resourcenotfound")
	public String resourcenotfound() {
		
		return "error/resourcenotfound";
	}
}

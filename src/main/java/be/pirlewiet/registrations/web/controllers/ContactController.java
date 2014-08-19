package be.pirlewiet.registrations.web.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class ContactController {

	@RequestMapping("/contact")
	public String navigate() {
		return "dienst/contact";
	}
}

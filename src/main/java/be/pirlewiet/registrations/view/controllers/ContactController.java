package be.pirlewiet.registrations.view.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class ContactController {

	@RequestMapping("/contact")
	public String navigate() {
		return "dienst/contact";
	}
}
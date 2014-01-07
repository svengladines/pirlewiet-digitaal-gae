package be.pirlewiet.registrations.view.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class FAQController {

	@RequestMapping("/faq")
	public String navigate() {
		return "dienst/faq";
	}
}

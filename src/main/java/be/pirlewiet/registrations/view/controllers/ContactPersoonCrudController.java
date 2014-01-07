package be.pirlewiet.registrations.view.controllers;

import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import be.pirlewiet.registrations.model.Contactpersoon;
import be.pirlewiet.registrations.services.ContactpersoonService;
import be.pirlewiet.registrations.services.DienstService;

@Controller
public class ContactPersoonCrudController {

	@Autowired
	private ContactpersoonService contactpersoonService;
	@Autowired
	private DienstService dienstService;
	
	Logger logger = Logger.getLogger(this.getClass());

//	@RequestMapping(value = "cp")
//	public String returnContactPersoonForm(ModelMap m) {
//		System.out.println("Return contactpersoon formulier");
//		m.put("contactpersoon", new Contactpersoon());
//		return "forms/Contactpersoonformulier";
//	}

	@RequestMapping(value = "contactpersoon/add",produces="application/json")
	@Transactional
	public @ResponseBody Map<String,Object> createContactPersoon(@ModelAttribute("contactpersoon") Contactpersoon c) {
		Map<String, Object> data = new HashMap<String, Object>();
		c.setDienst(dienstService.getLoggedInDienst());
		contactpersoonService.create(c);
		data.put("resultaat", "De nieuwe contactpersoon werd toegegvoegd!");
		data.put("resultaatId", c.getId());
		logger.info(c);
		return data;
	}
	
	@RequestMapping(value="contactpersoon/get/{contactpersoonid}")
	public @ResponseBody Map<String,Object> getContactpersoonById(@PathVariable long contactpersoonid) {
		logger.info("Controller reacted on the ajax call for Contactpersoon with ID:" + contactpersoonid);
		Contactpersoon geselecteerdeContactpersoon = contactpersoonService.findById(contactpersoonid);
		Map<String,Object> data = new HashMap<String, Object>();
		if (geselecteerdeContactpersoon == null) {
			return data;
		}
		data.put("voornaam", geselecteerdeContactpersoon.getVoornaam());
		data.put("familienaam",geselecteerdeContactpersoon.getFamilienaam());
		data.put("functie",geselecteerdeContactpersoon.getFunctie());
		data.put("email",geselecteerdeContactpersoon.getEmail());
		data.put("telefoonnr",geselecteerdeContactpersoon.getTelefoonnr());
		return data;
	}
}

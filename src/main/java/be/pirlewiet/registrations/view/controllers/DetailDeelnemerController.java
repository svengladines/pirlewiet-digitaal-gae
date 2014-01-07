package be.pirlewiet.registrations.view.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import be.pirlewiet.registrations.services.DeelnemerService;

@Controller
@RequestMapping("/detailDeelnemer")
public class DetailDeelnemerController {
    @Autowired
    private DeelnemerService deelnemerService;

//    @RequestMapping("/detailDeelnemer")
//    public ModelAndView navigate(Principal principal) {
//        Model m = new ExtendedModelMap();
//        if (principal != null) {
//            m.addAttribute("currentUser", principal.getName());
//        } else {
//            m.addAttribute("currentUser", "GUEST");
//        }
//		m.addAttribute("command", new Deelnemer());
//
//        return new ModelAndView("home", m.asMap());
//    }

    @RequestMapping(value = "/{deelnemerid}", method = RequestMethod.GET)
    public ModelAndView addInschrijving(@PathVariable long deelnemerid) {
        // Manual setting the VakantieProject from Inschrijving. Because Spring
        // MVC can't convert a SelectBox's id into a
        // vakantieproject-Object.

//        VakantieProject gekozenVp = vakantieProjectService.findVakantieProjectById(inschrijving.getVakantieproject().getId());
//        inschrijving.setVakantieproject(gekozenVp);
//        inschrijving = inschrijvingService.createInschrijving(inschrijving);

//        return new ModelAndView("inschrijvingBevestiging", "inschrijvingsid",
//                inschrijving.getId());
        return new ModelAndView("detailDeelnemer", "deelnemer", deelnemerService.findDienstById(deelnemerid));
    }
}

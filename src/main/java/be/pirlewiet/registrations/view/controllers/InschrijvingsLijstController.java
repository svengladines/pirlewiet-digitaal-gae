/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package be.pirlewiet.registrations.view.controllers;

import java.util.List;

import org.apache.log4j.Logger;
import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.ExtendedModelMap;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import be.pirlewiet.registrations.model.Inschrijving;
import be.pirlewiet.registrations.services.DienstService;
import be.pirlewiet.registrations.services.InschrijvingService;

/**
 *
 * @author bgri978
 */
@Controller
public class InschrijvingsLijstController {
    @Autowired
    private InschrijvingService inschrijvingService;
    @Autowired
    private DienstService dienstService;

    private Logger logger = Logger.getLogger(this.getClass());
    
    @RequestMapping("/inschrijving/lijst")
    @Transactional
    public ModelAndView getList() {
        Model m = new ExtendedModelMap();
        List<Inschrijving> aanvragen = inschrijvingService.findActueleInschrijvingenByDienst(dienstService.getLoggedInDienst());
        
        for (Inschrijving aanvraagInschrijving : aanvragen) {
        	Hibernate.initialize(aanvraagInschrijving.getDeelnemers());
        }
        
        m.addAttribute("command", aanvragen);
        
        logger.info(aanvragen.size() + " aanvragen gevonden...");
        
        return new ModelAndView("dienst/inschrijvingslijst", m.asMap());
    }
}

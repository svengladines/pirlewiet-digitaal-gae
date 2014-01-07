package be.pirlewiet.registrations.view.controllers;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.SessionAttributes;

import be.pirlewiet.registrations.model.Deelnemer;
import be.pirlewiet.registrations.model.Dienst;
import be.pirlewiet.registrations.services.DeelnemerService;
import be.pirlewiet.registrations.services.DienstService;

@Controller
@SessionAttributes("deelnemersDatatableData")
public class AanvraagInschrijvingsController {

	Logger logger = Logger.getLogger(this.getClass());
	
	@Autowired
	private DienstService dienstService;
	@Autowired
	private DeelnemerService deelnemerService;
	
	/**
	 * This method generates dummy data for a datatable. The only requirement is that the main object in JSON is of type 'aaData'.
	 * The logic in this method must be replaced with a service call to retrieve live data.
	 * @param req
	 * @param map
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "datatabledatagenerated", produces = "application/json")
	@ResponseBody
	public Map<String, Object> dataTableJson(ModelMap model) {
		
		Dienst loggedInDienst = dienstService.getLoggedInDienst();
		List<Deelnemer> lijstDeelnemersPerDienst = deelnemerService.getDeelnemersByDienst(loggedInDienst); //new ArrayList<Deelnemer>();
		logger.info("Dienst logged in: " + loggedInDienst);
		
		List<Object> b = new ArrayList<Object>();
		DateFormat df = new SimpleDateFormat("YYYY-MM-dd");
		for (Deelnemer deelnemer : lijstDeelnemersPerDienst) {
			List<String> a = new ArrayList<String>();
			a.add(deelnemer.getId() +"");
			a.add(deelnemer.getVoornaam());
			a.add(deelnemer.getFamilienaam());
			a.add(df.format(deelnemer.getGeboortedatum()));
			a.add(deelnemer.getGeslacht() + "");
			b.add(a);
		}
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("aaData", b);
		
		Map<String,Object> sessionData = (Map<String,Object>)model.get("deelnemersDatatableData");
		if (sessionData == null) {
			logger.info("deelnemersDatatableData op de sessie is null ");
			model.put("deelnemersDatatableData", map);
		} else {
			logger.info("Er is al een deelnemersDatatableTabel op de sessie geplaatst...");
			return sessionData;
		}
		return map;
	}

}

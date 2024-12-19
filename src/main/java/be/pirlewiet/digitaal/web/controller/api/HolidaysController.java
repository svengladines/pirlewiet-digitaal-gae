package be.pirlewiet.digitaal.web.controller.api;

import static be.occam.utils.spring.web.Controller.response;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.request.WebRequest;

import be.occam.utils.spring.web.Result;
import be.pirlewiet.digitaal.domain.people.DoorMan;
import be.pirlewiet.digitaal.domain.service.HolidayService;
import be.pirlewiet.digitaal.model.Organisation;
import be.pirlewiet.digitaal.web.dto.HolidayDTO;

@Controller
@RequestMapping( {"/api/holidays"} )
public class HolidaysController {
	
	protected Logger logger 
		= LoggerFactory.getLogger( this.getClass() );
	
	@Autowired
	HolidayService holidayService;
	
	@Autowired
	DoorMan doorMan;
	
	@RequestMapping( method = { RequestMethod.GET }, produces={"application/json"} )
	@ResponseBody
	public ResponseEntity<Result<List<Result<HolidayDTO>>>> get( WebRequest request, @CookieValue(required=true, value="pwtid") String pwtid ) {
		Organisation actor = this.doorMan.whoHasID( pwtid );
		Result<List<Result<HolidayDTO>>> holidaysResult = this.holidayService.query( actor );
		return response( holidaysResult, HttpStatus.OK );
	}
	
}

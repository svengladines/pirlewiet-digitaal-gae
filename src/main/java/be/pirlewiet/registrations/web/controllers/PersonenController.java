package be.pirlewiet.registrations.web.controllers;

import static be.occam.utils.spring.web.Controller.*;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import be.pirlewiet.registrations.model.Persoon;

@Controller
@RequestMapping( {"/personen"} )
public class PersonenController {
	
	protected Logger logger 
		= LoggerFactory.getLogger( this.getClass() );
	
	@RequestMapping( method = { RequestMethod.GET }, produces={"application/json","text/xml"} )
	@ResponseBody
	public ResponseEntity<List<Persoon>> get() {
		
		List<Persoon> personen
			= new ArrayList<Persoon>( );
		
		return response( personen, HttpStatus.OK );
		
	}
	
	@RequestMapping( method = { RequestMethod.POST } )
	@ResponseBody
	public ResponseEntity<Persoon> post(
				@RequestBody Persoon persoon ) {
		
		return response( persoon, HttpStatus.OK );
		
	}
	
}

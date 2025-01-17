package be.pirlewiet.digitaal.web.controller.api;

import static be.occam.utils.spring.web.Controller.response;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value="/api/ping")
public class PingController {
	
	@RequestMapping(method=RequestMethod.GET) 
	public ResponseEntity<String> retrieve( ) {
		return new ResponseEntity<>( "Pong", HttpStatus.OK );
	}
	
}

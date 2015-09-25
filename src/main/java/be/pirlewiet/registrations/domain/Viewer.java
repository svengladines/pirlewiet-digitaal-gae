package be.pirlewiet.registrations.domain;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;

import be.occam.utils.spring.web.Client;

public class Viewer {
	
	protected final Logger logger
		= LoggerFactory.getLogger( this.getClass() );
	
	public String view( String url, String as ) {
		
		Map<String, String> headers
			= new HashMap<String,String>();
		
		headers.put("Cookie", new StringBuilder("pwtid= ").append( as ).toString() );
		
		ResponseEntity<String> getResponse 
			= Client.getHTML( url, headers );
		
		return getResponse.getBody();
		
	}
	

}

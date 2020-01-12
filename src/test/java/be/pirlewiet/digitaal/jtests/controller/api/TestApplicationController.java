package be.pirlewiet.digitaal.jtests.controller.api;

import static be.occam.utils.javax.Utils.map;
import static be.occam.utils.spring.web.Client.getJSON;

import java.util.Map;

import org.junit.Test;
import org.springframework.http.ResponseEntity;

import be.occam.test.jtest.JTest;
import be.occam.utils.spring.web.Result;
import be.pirlewiet.digitaal.web.dto.ApplicationDTO;

public class TestApplicationController extends JTest {
	
	public TestApplicationController() {
		super( "/pirlewiet-digitaal" );
	}
	
	@Test
	public void testUpdateHolidays_OK() {
		
		String url = new StringBuilder( this.baseUrl().toString() ).append("/api/applications?reference=One").toString();

		ResponseEntity<Result> get
			=  getJSON( url, Result.class );
		
		ApplicationDTO application
			= (ApplicationDTO) get.getBody().getObject();
		
		Map<String,String> headers
			= map();
		
		headers.put( "Cookie", "pwtid=001");
		
		// postJSON( url, dto, headers );
		
	}

	

}

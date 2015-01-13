package be.pirlewiet.registrations.jtests.controllers;

import static be.occam.test.jtest.JTestUtil.postMultiPart;
import static org.junit.Assert.assertEquals;

import java.io.InputStream;

import org.junit.Test;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import be.occam.test.jtest.JTest;

public class TestOrganisatiesController extends JTest {

	public TestOrganisatiesController() {
		super("/pirlewiet-registraties");
	}
	
	@Test
	public void testPostFile() throws Exception {
		
		String url
			= this.baseResourceUrl().append("/organisaties").toString();
		
		FileSystemResource resource
			= new FileSystemResource( "/src/test/resources/doorverwijzers-2014.xlsx");
		
		MultiValueMap<String, Object> parts
			= new LinkedMultiValueMap<>();
			
		parts.add( "file", resource.getFile() );
		
		ResponseEntity<byte[]> response
			= postMultiPart( url, byte[].class, parts );
		
		assertEquals( "no 200 received", HttpStatus.OK, response.getStatusCode() );
		
	}
	

}

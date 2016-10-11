package be.pirlewiet.digitaal.jtests.controller;

import static be.occam.utils.javax.Utils.map;
import static be.occam.utils.spring.web.Client.*;

import java.util.Map;

import org.junit.Test;

import be.occam.test.jtest.JTest;
import be.pirlewiet.digitaal.dto.OrganisationDTO;

/**
 * end-2-end tests of the atomic operations of the organisationscontroller.
 * 
 * @author sven
 *
 */
public class TestOrganisationsController extends JTest {
	
	public TestOrganisationsController() {
		super( "/pirlewiet-digitaal" );
	}
	
	@Test
	public void doesItSmoke() {
		

		String url = new StringBuilder( this.baseUrl().toString() ).append("/index.htm").toString();
		
		getHTML( url );
		
	}
	
	@Test
	public void testCreate_OK() {
		

		OrganisationDTO dto
			= new OrganisationDTO();
		
		dto.setName( "Monsters, Inc" );
		dto.setEmail( "info@monsters.bizz" );
		
		String url = new StringBuilder( this.baseUrl().toString() ).append("/api/organisations").toString();
		
		Map<String,String> headers
			= map();
		
		headers.put( "Cookie", "pwtid=001");
		
		postJSON( url, dto, headers );
		
	}

	

}

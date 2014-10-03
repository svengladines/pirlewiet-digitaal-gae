package be.pirlewiet.registrations.jtests.controllers;

import static be.occam.test.jtest.JTestUtil.getJSON;
import static be.occam.test.jtest.JTestUtil.postJSON;
import static org.junit.Assert.*;
import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import be.occam.test.jtest.JTest;
import be.pirlewiet.registrations.jtests.TestData.Ids;
import be.pirlewiet.registrations.model.InschrijvingX;
import be.pirlewiet.registrations.model.Vakantie;

public class TestInschrijvingenController extends JTest {

	public TestInschrijvingenController() {
		super("/pirlewiet-registraties");
	}
	
	@Test
	public void testCreate() {
		
		String url
			= this.baseResourceUrl().append("/inschrijvingen").toString();
		
		Vakantie vakantie
			= new Vakantie();
		vakantie.setId( Ids.Z_KIKA_1 );
		
		InschrijvingX inschrijving
			= new InschrijvingX();
		inschrijving.getVakanties().add( vakantie.getId() );
		
		ResponseEntity<InschrijvingX> response
			= postJSON( url, inschrijving );
		
		assertEquals( "no 201 received", HttpStatus.CREATED, response.getStatusCode() );
		
	}
	
	@Test
	public void testRetrieve() {
		
		String url
			= this.baseResourceUrl().append("/inschrijvingen").toString();
		
		ResponseEntity<InschrijvingX[]> response
			= getJSON( url, InschrijvingX[].class );
		
		assertEquals( "no 200 received", HttpStatus.OK, response.getStatusCode() );
		
		assertTrue( "no inschrijvingen received", response.getBody().length > 0 );
		
	}
	

}

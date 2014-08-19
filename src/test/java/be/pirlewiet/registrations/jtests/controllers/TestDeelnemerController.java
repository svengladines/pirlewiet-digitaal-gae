package be.pirlewiet.registrations.jtests.controllers;

import static be.occam.test.jtest.JTestUtil.getJSON;
import static be.occam.test.jtest.JTestUtil.postJSON;
import static be.occam.test.jtest.JTestUtil.putJSON;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.List;

import org.junit.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import be.occam.test.jtest.JTest;
import be.pirlewiet.registrations.model.Adres;
import be.pirlewiet.registrations.model.ContactGegevens;
import be.pirlewiet.registrations.model.Deelnemer;
import be.pirlewiet.registrations.model.InschrijvingX;
import be.pirlewiet.registrations.model.Status;
import be.pirlewiet.registrations.model.Vraag;

public class TestDeelnemerController extends JTest {

	public TestDeelnemerController() {
		super("/pirlewiet-registraties");
	}
	
	
	@Test
	public void testRetrieve() {
		
		ResponseEntity<Deelnemer> response
			= this.retrieve();
		
		assertEquals( "no 200 received", HttpStatus.OK, response.getStatusCode() );
		assertEquals( "ID not correct", 1L, response.getBody().getId() );
		
	}
	
	@Test
	public void testUpdate() {
		
		String url
			= this.baseResourceUrl().append("/inschrijvingen/1/deelnemers/1").toString();
		
		String familieNaam 
			= "Connor";
		
		ResponseEntity<Deelnemer> entity
			= this.retrieve();
		
		Deelnemer sarah
			= entity.getBody();
		
		sarah.setFamilieNaam( familieNaam );
		
		ResponseEntity<Deelnemer> updateResponse
			= putJSON( url, sarah );
		
		assertEquals( "no 200 received", HttpStatus.OK, updateResponse.getStatusCode() );
		
		entity = this.retrieve();
		
		assertEquals( "faimilieNaam not correct", familieNaam, entity.getBody().getFamilieNaam() );
		
	}
	
	protected ResponseEntity<Deelnemer> retrieve() {
		
		String url
			= this.baseResourceUrl().append("/inschrijvingen/1/deelnemers/1").toString();
		
		ResponseEntity<Deelnemer> response
			= getJSON( url, Deelnemer.class );
	
		assertEquals( "no 200 received", HttpStatus.OK, response.getStatusCode() );
		
		return response;
		
	}
	

}

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
import be.pirlewiet.digitaal.model.Address;
import be.pirlewiet.digitaal.model.PersonInfo;
import be.pirlewiet.digitaal.model.Participant;
import be.pirlewiet.digitaal.model.Enrollment;
import be.pirlewiet.digitaal.model.EnrollmentStatus;
import be.pirlewiet.digitaal.model.QnA;

public class TestDeelnemerController extends JTest {

	public TestDeelnemerController() {
		super("/pirlewiet-registraties");
	}
	
	
	@Test
	public void testRetrieve() {
		
		ResponseEntity<Participant> response
			= this.retrieve();
		
		assertEquals( "no 200 received", HttpStatus.OK, response.getStatusCode() );
		// assertEquals( "ID not correct", 1L, response.getBody().getId() );
		
	}
	
	@Test
	public void testUpdate() {
		
		String url
			= this.baseResourceUrl().append("/inschrijvingen/1/deelnemers/1").toString();
		
		String familieNaam 
			= "Connor";
		
		ResponseEntity<Participant> entity
			= this.retrieve();
		
		Participant sarah
			= entity.getBody();
		
		sarah.setFamilieNaam( familieNaam );
		
		ResponseEntity<Participant> updateResponse
			= putJSON( url, sarah );
		
		assertEquals( "no 200 received", HttpStatus.OK, updateResponse.getStatusCode() );
		
		entity = this.retrieve();
		
		assertEquals( "faimilieNaam not correct", familieNaam, entity.getBody().getFamilieNaam() );
		
	}
	
	protected ResponseEntity<Participant> retrieve() {
		
		String url
			= this.baseResourceUrl().append("/inschrijvingen/1/deelnemers/1").toString();
		
		ResponseEntity<Participant> response
			= getJSON( url, Participant.class );
	
		assertEquals( "no 200 received", HttpStatus.OK, response.getStatusCode() );
		
		return response;
		
	}
	

}

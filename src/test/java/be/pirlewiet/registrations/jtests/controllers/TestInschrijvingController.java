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

public class TestInschrijvingController extends JTest {

	public TestInschrijvingController() {
		super("/pirlewiet-registraties");
	}
	
	
	@Test
	public void testRetrieve() {
		
		ResponseEntity<InschrijvingX> response
			= this.retrieve();
		
		assertEquals( "no 200 received", HttpStatus.OK, response.getStatusCode() );
		assertEquals( "ID not correct", 1L, response.getBody().getId() );
		
	}
	
	@Test
	public void testUpdate_Contact() {
		
		String url
			= this.baseResourceUrl().append("/inschrijvingen/1/contact").toString();
		
		ContactGegevens contactGegevens
			= new ContactGegevens();
		
		String naam = "Homer Simpson";
		contactGegevens.setNaam( naam );
		contactGegevens.setEmail( "homer.simpson@springfield.net");
		contactGegevens.setGsmNummer( "0499221100" );
		contactGegevens.setTelefoonNummer( "016322800" );
		
		ResponseEntity<ContactGegevens> response
			= putJSON( url, contactGegevens );
		
		assertEquals( "no 200 received", HttpStatus.OK, response.getStatusCode() );
		
		ResponseEntity<InschrijvingX> retrieveResponse
			= this.retrieve();
		
		ContactGegevens retrieved
			= retrieveResponse.getBody().getContactGegevens();
		assertNotNull( "no contactgegevens", retrieved );
		assertEquals( "naam not correct", naam, retrieved.getNaam() );
		
	}
	
	@Test
	public void testAdd_Deelnemer() {
		
		String url
			= this.baseResourceUrl().append("/inschrijvingen/1/deelnemers").toString();
		
		Deelnemer deelnemer
			= new Deelnemer();
		
		String voorNaam = "Bart";
		String familieNaam = "Simpson";
		
		deelnemer.setVoorNaam( voorNaam );
		deelnemer.setFamilieNaam( familieNaam );
		
		ResponseEntity<Deelnemer> response
			= postJSON( url, deelnemer );
		
		assertEquals( "no 200 received", HttpStatus.OK, response.getStatusCode() );
		
		ResponseEntity<InschrijvingX> retrieveResponse
			= this.retrieve();
		
		List<Deelnemer> retrieved
			= retrieveResponse.getBody().getDeelnemers();
		
		assertTrue( "no deelnemers", ! retrieved.isEmpty() );
		assertTrue( "too few deelnemers", retrieved.size() >= 2 );
	
		
	}
	
	@Test
	public void testUpdate_Adres() {
		
		String url
			= this.baseResourceUrl().append("/inschrijvingen/1/adres").toString();
		
		Adres adres
			= new Adres();
		
		String straat = "Evergreen Terrace";
		String gemeente = "Springfield";
		
		adres.setStraat( straat );
		adres.setGemeente( gemeente );
		
		ResponseEntity<Adres> response
			= putJSON( url, adres );
		
		assertEquals( "no 200 received", HttpStatus.OK, response.getStatusCode() );
		
		ResponseEntity<InschrijvingX> retrieveResponse
			= this.retrieve();
		
		Adres retrieved
			= retrieveResponse.getBody().getAdres();
		
		assertNotNull( "no address", retrieved );
		assertEquals( "straat not corrected", retrieved.getStraat(), straat );
	
		
	}
	
	@Test
	public void testAdd_Vragen() {
		
		String url
			= this.baseResourceUrl().append("/inschrijvingen/1/vragen").toString();
		
		Vraag vraagAntwoord
			= new Vraag();
		
		String vraag = "Watyadoing ?";
		String antwoord = "Just hangin'";
		
		vraagAntwoord.setVraag( vraag );
		vraagAntwoord.setAntwoord( antwoord );
		
		ResponseEntity<Vraag> response
			= postJSON( url, vraagAntwoord );
		
		assertEquals( "no 200 received", HttpStatus.OK, response.getStatusCode() );
		
		ResponseEntity<InschrijvingX> retrieveResponse
			= this.retrieve();
		
		List<Vraag> retrieved
			= retrieveResponse.getBody().getVragen();
		
		assertTrue( "empty collection vragen", ! retrieved.isEmpty() );
		assertEquals( "collection size incorrect for vragen", 1, retrieved.size() );
	
		
	}
	
	@Test
	public void testUpdate_Opmerking() {
		
		String url
			= this.baseResourceUrl().append("/inschrijvingen/1/opmerking").toString();
		
		String opmerking
			= "cowabunga!";
		
		ResponseEntity<String> response
			= putJSON( url, opmerking );
		
		assertEquals( "no 200 received", HttpStatus.OK, response.getStatusCode() );
		
		ResponseEntity<InschrijvingX> retrieveResponse
			= this.retrieve();
		
		String retrieved
			= retrieveResponse.getBody().getOpmerking();
		
		assertEquals( "opmerking not correct", opmerking, retrieved );
	
		
	}
	
	@Test
	public void testUpdate_Status() {
		
		String url
			= this.baseResourceUrl().append("/inschrijvingen/1/status").toString();
		
		Status status
			= Status.HUISBEZOEK;
		
		ResponseEntity<Status> response
			= putJSON( url, status );
		
		assertEquals( "no 200 received", HttpStatus.OK, response.getStatusCode() );
		
		ResponseEntity<InschrijvingX> retrieveResponse
			= this.retrieve();
		
		Status retrieved
			= retrieveResponse.getBody().getStatus();
		
		assertEquals( "opmerking not correct", status, retrieved );
	
		
	}
	
	protected ResponseEntity<InschrijvingX> retrieve() {
		
		String url
			= this.baseResourceUrl().append("/inschrijvingen/1").toString();
		
		ResponseEntity<InschrijvingX> response
			= getJSON( url, InschrijvingX.class );
	
		assertEquals( "no 200 received", HttpStatus.OK, response.getStatusCode() );
		
		return response;
		
	}
	

}

package be.pirlewiet.digitaal.jtests.scenario;

import static be.occam.test.jtest.JTestUtil.getJSON;
import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import be.occam.test.jtest.JTest;
import be.pirlewiet.digitaal.model.Enrollment;

/**
 * In this scenario, a new organisation registers
 * 
 * @author sven
 *
 */
public class TestScenario_NewOrganisationRegisters_OK extends JTest {
	
	public TestScenario_NewOrganisationRegisters_OK() {
		super("/pirlewiet-digitaal");
	}
	
	@Test
	public void doesItSmoke() {
		
		// 
		
		/*
	
		// 0. de dienstmedewerker surft naar pirlewiet.be. Daar klikt hij op 'inschrijvingen'. Hij komt terecht op de homepagina van de digitale inschrijvingen...
		
		// De medewerker krijgt een scherm met de vraag om de code voor de dienst in te geven. Hij geeft deze in en klikt op OK. Hij komt nu terecht op de pagina van zijn dienst
		
		// Uit de lijst van vakanties waarvoor ingeschreven kan worden, kiest hij een kamp en klikt op het 'inschrijf' icoontje. Een inschrijving wordt aangemaakt. 
		// De gebruiker komt nu terecht op een formulier om de inschrijving te vervolledigen.
		String collectionUrl
			= this.baseResourceUrl().append("/inschrijvingen").toString();
	
		Vakantie vakantie
			= new Vakantie();
		vakantie.setUuid( "x");
	
		Enrollment inschrijving
			= new Enrollment();
		inschrijving.getVakanties().add( vakantie );
		
		ResponseEntity<Enrollment> response
			= postJSON( collectionUrl, inschrijving );
		
		assertEquals( "no 201 received", HttpStatus.CREATED, response.getStatusCode() );
		
		String uuid
			= response.getBody().getUuid();
		
		inschrijving = this.retrieve( id ).getBody();
		
		// ajax:: redirect to inschrijvingen/{id}.html
		StringBuilder url
			= this.url( inschrijving );
		
		// de medewerker vult de contactgegevens in
		PersonInfo contactGegevens
			= new PersonInfo();
	
		String naam = "Homer Simpson";
		contactGegevens.setNaam( naam );
		contactGegevens.setEmail( "homer.simpson@springfield.net");
		contactGegevens.setGsmNummer( "0499221100" );
		contactGegevens.setTelefoonNummer( "016322800" );
	
		ResponseEntity<PersonInfo> contactResponse
			= putJSON( url(inschrijving).append("/contact").toString(), contactGegevens );
		
		assertEquals( "no 200 received", HttpStatus.OK, contactResponse.getStatusCode() );
		
		ResponseEntity<Enrollment> retrieveResponse
			= this.retrieve( id );
		
		PersonInfo retrieved
			= retrieveResponse.getBody().getContactGegevens();
		assertNotNull( "no contactgegevens", retrieved );
		assertEquals( "naam not correct", naam, retrieved.getNaam() );
		
		// de medewerker klikt op de knop 'voeg deelnemer toe'
		Participant bart
			= new Participant();
	
		ResponseEntity<Participant> bartResponse
			= postJSON( url(inschrijving).append("/deelnemers").toString(), bart );
	
		assertEquals( "no 200 received", HttpStatus.OK, bartResponse.getStatusCode() );
		
		bart = bartResponse.getBody();
		
		long bartID 
			= bart.getId(); 
		
		// de medewerker vult nu de velden in in klikt op 'OK'
		
		String bartVoorNaam = "Bart";
		String bartFamilieNaam = "Simpson";
	
		bart.setVoorNaam( bartVoorNaam );
		bart.setFamilieNaam( bartFamilieNaam );
		
		bartResponse = putJSON( url(inschrijving).append("/deelnemers/").append( bartID ).toString(), bart );
	
		retrieveResponse = this.retrieve( id );
		List<Participant> deelnemers 
			= retrieveResponse.getBody().getDeelnemers();
	
		assertEquals( "should have 1 deelnemer", 1, deelnemers.size() );

		Participant lisa
			= new Participant();
		
		ResponseEntity<Participant> lisaResponse
			= postJSON( url(inschrijving).append("/deelnemers").toString(), lisa );
		
		lisa = lisaResponse.getBody();
		
		long lisaID 
			= lisa.get
		
		String lisaVoorNaam = "Lisa";
		String lisaFamilieNaam = "Simpson";

		lisa.setVoorNaam( lisaVoorNaam );
		lisa.setFamilieNaam( lisaFamilieNaam );

		lisaResponse = putJSON( url(inschrijving).append("/deelnemers/").append( lisaID ).toString(), lisa );

		assertEquals( "no 200 received", HttpStatus.OK, lisaResponse.getStatusCode() );

		retrieveResponse = this.retrieve( uuid );
		
		deelnemers = retrieveResponse.getBody().getDeelnemers();

		assertEquals( "should have 2 deelnemer", 2, deelnemers.size() );
		
		// de medewerker vult het adres in
		Adres adres
			= new Adres();
	
		String straat = "Evergreen Terrace";
		String gemeente = "Springfield";
	
		adres.setStraat( straat );
		adres.setGemeente( gemeente );
	
		ResponseEntity<Adres> adresResponse
			= putJSON( url(inschrijving).append("/adres").toString(), adres );
	
		assertEquals( "no 200 received", HttpStatus.OK, adresResponse.getStatusCode() );
	
		retrieveResponse = this.retrieve( uuid );
	
		Adres retrievedAdres
			= retrieveResponse.getBody().getAdres();
		
		assertNotNull( "no address", retrievedAdres );
		assertEquals( "straat not corrected", retrievedAdres.getStraat(), straat );
		
		// de medewerker beantwoordt de extra vragen
		Vraag vraagAntwoord
			= new Vraag( Type.Text, Tags.TAG_ACTIVITIES, "Watyadoing ?");
	
		String antwoord = "Just hangin'";
	
		vraagAntwoord.setAntwoord( antwoord );
	
		ResponseEntity<Vraag> vraagResponse
			= postJSON( url(inschrijving).append("/vragen").toString(), vraagAntwoord );
		
		assertEquals( "no 200 received", HttpStatus.OK, vraagResponse.getStatusCode() );
		
		retrieveResponse = this.retrieve( uuid );
		
		List<Vraag> retrievedVragen
			= retrieveResponse.getBody().getVragen();
		
		assertTrue( "empty collection vragen", ! retrievedVragen.isEmpty() );
		assertEquals( "collection size incorrect for vragen", 1, retrievedVragen.size() );
		
		// de medewerker voegt nog een extra commentaartje toe
		String opmerking
			= "cowabunga!";
	
		ResponseEntity<String> opmerkingResponse
			= putJSON( url(inschrijving).append("/opmerking").toString(), opmerking );
	
		assertEquals( "no 200 received", HttpStatus.OK, opmerkingResponse.getStatusCode() );
	
		retrieveResponse = this.retrieve( uuid );
	
		String retrievedOpmerking
			= retrieveResponse.getBody().getOpmerking();
	
		assertEquals( "opmerking not correct", opmerking, retrievedOpmerking );
		
		// de medewerker leest alle gegevens nog eens na en vinkt het formulier af
		Status status
			= new Status ( Status.Value.SUBMITTED );
	
		ResponseEntity<Status> statusResponse
			= putJSON( url(inschrijving).append("/status").toString(), status );
	
		assertEquals( "no 200 received", HttpStatus.OK, statusResponse.getStatusCode() );
	
		retrieveResponse = this.retrieve( uuid );
	
		Status retrievedStatus 
			= retrieveResponse.getBody().getStatus();
	
		assertEquals( "opmerking not correct", status, retrievedStatus );
		
		// en dan het secretariaat...
		 */
	}
	
	protected ResponseEntity<Enrollment> retrieve( String uuid ) {
		
		String url
			= this.baseResourceUrl().append("/inschrijvingen/").append( uuid ).toString();
		
		ResponseEntity<Enrollment> response
			= getJSON( url, Enrollment.class );
	
		assertEquals( "no 200 received", HttpStatus.OK, response.getStatusCode() );
		
		return response;
		
	}
	
	protected StringBuilder url( Enrollment inschrijving ) {
		
		return this.baseResourceUrl().append("/inschrijvingen/").append( inschrijving.getUuid() );
		
	}
	
}

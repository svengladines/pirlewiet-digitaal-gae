package be.pirlewiet.registrations.utils;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.List;

import org.junit.Test;

import be.pirlewiet.registrations.model.Adres;
import be.pirlewiet.registrations.model.Credentials;
import be.pirlewiet.registrations.model.Dienst;
import be.pirlewiet.registrations.model.DienstJsonObject;
import be.pirlewiet.registrations.utils.JSONUtils;

public class JSONUtilsTest {

	private static final String DIENST_AFDELING = "Afdeling";
	private static final String DIENST_TELEFOON = "02/12.34.56";
	private static final String DIENST_STRAAT = "Korenmarkt";
	private static final String DIENST_POSTCODE = "9000";
	private static final String DIENST_STRAAT_NR = "1";
	private static final String DIENST_GEMEENTE = "Gent";
	private static final String DIENST_FAX = "02/000.000";
	private static final String DIENST_EMAIL = "email@pirlewiet.be";
	private static final String DIENST_NAAM = "testNaam";
	private static final long DIENST_ID = 5L;

	private JSONUtils jsonUtils = new JSONUtils();

	@Test
	public void testGenerateDienstJsonList() {
		List<Dienst> diensten = Arrays.asList(createDienst(), createDienst());

		List<DienstJsonObject> dienstJsonList = jsonUtils.generateDienstJsonList(diensten);
		assertEquals(2, dienstJsonList.size());

		for (DienstJsonObject dienstJsonObject : dienstJsonList) {
			assertEquals(DIENST_ID, dienstJsonObject.getId());
			assertEquals(DIENST_NAAM, dienstJsonObject.getNaam());
			assertEquals(DIENST_EMAIL, dienstJsonObject.getEmail());
			assertEquals(DIENST_FAX, dienstJsonObject.getFax());
			assertEquals(DIENST_GEMEENTE, dienstJsonObject.getGemeente());
			assertEquals(DIENST_STRAAT_NR, dienstJsonObject.getNummer());
			assertEquals(DIENST_POSTCODE, dienstJsonObject.getPostcode());
			assertEquals(DIENST_STRAAT, dienstJsonObject.getStraat());
			assertEquals(DIENST_TELEFOON, dienstJsonObject.getTelefoon());
			assertEquals(DIENST_AFDELING, dienstJsonObject.getAfdeling());
			assertTrue(dienstJsonList.get(0).isActief());
		}
	}

	private Adres createAdres() {
		Adres adres = new Adres();
		adres.setGemeente(DIENST_GEMEENTE);
		adres.setNummer(DIENST_STRAAT_NR);
		adres.setPostcode(DIENST_POSTCODE);
		adres.setStraat(DIENST_STRAAT);
		return adres;
	}

	private Dienst createDienst() {
		Dienst dienst = new Dienst();
		dienst.setAdres(createAdres());
		dienst.setAfdeling(DIENST_AFDELING);
		dienst.setEmailadres(DIENST_EMAIL);
		dienst.setFaxnummer(DIENST_FAX);
		dienst.setNaam(DIENST_NAAM);
		dienst.setTelefoonnummer(DIENST_TELEFOON);
		dienst.setCredentials(createEnabledCredentials());
		dienst.setId(5L);

		return dienst;
	}

	private Credentials createEnabledCredentials() {
		Credentials credentials = new Credentials();
		credentials.setEnabled(true);
		return credentials;
	}
}

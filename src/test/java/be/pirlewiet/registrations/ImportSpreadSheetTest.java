package be.pirlewiet.registrations;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.IOException;

import org.junit.Ignore;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import be.pirlewiet.registrations.model.Adres;
import be.pirlewiet.registrations.standalone.ImportSpreadSheet;

public class ImportSpreadSheetTest extends AbstractTransactionalTest {
	private final static Logger LOGGER = LoggerFactory.getLogger(ImportSpreadSheetTest.class);
	@Autowired
	ImportSpreadSheet importSpreadSheet;

	@Test
	@Ignore
	public void tryToOpenFile() throws IOException, UnsupportedOperationException, Exception {
		LOGGER.info("start test import spreadsheet 1e versie");
		importSpreadSheet.ImportSpreadSheet("db.xls");
		LOGGER.info("end test import spreadsheet 1e versie");
	}

	@Test
	@Ignore
	public void testNNNE() {
		assertFalse(importSpreadSheet.notNullnotEmpty(null));
		assertFalse(importSpreadSheet.notNullnotEmpty(""));
		assertTrue(importSpreadSheet.notNullnotEmpty("test"));
	}

	@Test
	@Ignore
	public void testToAdres1() {
		Adres a = importSpreadSheet.toAdres(null, null, null);
		assertFalse(importSpreadSheet.notNullnotEmpty(a.getGemeente()));
		assertFalse(importSpreadSheet.notNullnotEmpty(a.getPostcode()));
		assertFalse(importSpreadSheet.notNullnotEmpty(a.getNummer()));
		assertFalse(importSpreadSheet.notNullnotEmpty(a.getStraat()));
	}

	@Test
	@Ignore
	public void testToAdres2() {
		Adres a = importSpreadSheet.toAdres("1", null, null);
		assertEquals(a.getNummer(), "1");
		assertFalse(importSpreadSheet.notNullnotEmpty(a.getStraat()));
	}

	@Test
	@Ignore
	public void testToAdres3() {
		Adres a = importSpreadSheet.toAdres("straat", null, null);
		assertFalse(importSpreadSheet.notNullnotEmpty(a.getNummer()));
		assertEquals(a.getStraat(), "straat");
	}

	@Test
	@Ignore
	public void testToAdres4() {
		Adres a = importSpreadSheet.toAdres("straat 1", null, null);
		assertEquals(a.getNummer(), "1");
		assertEquals(a.getStraat(), "straat");
	}

	@Test
	@Ignore
	public void testToAdres5() {
		Adres a = importSpreadSheet.toAdres("straat strat 71", null, null);
		assertEquals(a.getNummer(), "71");
		assertEquals(a.getStraat(), "straat strat");
	}

	@Test
	@Ignore
	public void testToAdres6() {
		Adres a = importSpreadSheet.toAdres("straat strat 71 bus 6", null, null);
		assertEquals(a.getNummer(), "71 bus 6");
		assertEquals(a.getStraat(), "straat strat");
	}

	@Test
	@Ignore
	public void testToAdres7() {
		Adres a = importSpreadSheet.toAdres("straat strat 71 yady", null, null);
		assertEquals(a.getNummer(), "71 yady");
		assertEquals(a.getStraat(), "straat strat");
	}

	@Test
	@Ignore
	public void testNaamSplit() {
		String[] s = importSpreadSheet.splitNaamInVoornaamAchternaam("");
		assertFalse(importSpreadSheet.notNullnotEmpty(s[0]));
		assertFalse(importSpreadSheet.notNullnotEmpty(s[1]));
		s = importSpreadSheet.splitNaamInVoornaamAchternaam("aaa");
		assertFalse(importSpreadSheet.notNullnotEmpty(s[0]));
		assertEquals(s[1], "aaa");
		s = importSpreadSheet.splitNaamInVoornaamAchternaam("aaa bbbb");
		assertEquals(s[0], "aaa");
		assertEquals(s[1], "bbbb");
		s = importSpreadSheet.splitNaamInVoornaamAchternaam("aaa  bbbb");
		assertEquals(s[0], "aaa");
		assertEquals(s[1], " bbbb");
		s = importSpreadSheet.splitNaamInVoornaamAchternaam("aaa van bbbb");
		assertEquals(s[0], "aaa");
		assertEquals(s[1], "van bbbb");
	}

}

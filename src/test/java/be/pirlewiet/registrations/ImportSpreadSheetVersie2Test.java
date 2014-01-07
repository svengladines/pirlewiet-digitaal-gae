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
import be.pirlewiet.registrations.standalone.ImportSpreadSheetv2;

public class ImportSpreadSheetVersie2Test extends AbstractTransactionalTest {
	private final static Logger LOGGER = LoggerFactory.getLogger(ImportSpreadSheetVersie2Test.class);
	@Autowired
	ImportSpreadSheetv2 importSpreadSheetv2;

	@Test
	@Ignore
	public void tryToOpenFile2test() throws IOException, UnsupportedOperationException, Exception {
		LOGGER.info("-- > start test import spreadsheetv2");
		importSpreadSheetv2.ImportSpreadSheet("dbnieuweversie.xls");
		LOGGER.info("-- > end test import spreadsheetv2");
	}

	@Test
	@Ignore
	public void test2NNNE() {
		assertFalse(importSpreadSheetv2.notNullnotEmpty(null));
		assertFalse(importSpreadSheetv2.notNullnotEmpty(""));
		assertTrue(importSpreadSheetv2.notNullnotEmpty("test"));
	}

	@Test
	@Ignore
	public void test2ToAdres1() {
		Adres a = importSpreadSheetv2.toAdres(null, null, null);
		assertFalse(importSpreadSheetv2.notNullnotEmpty(a.getGemeente()));
		assertFalse(importSpreadSheetv2.notNullnotEmpty(a.getPostcode()));
		assertFalse(importSpreadSheetv2.notNullnotEmpty(a.getNummer()));
		assertFalse(importSpreadSheetv2.notNullnotEmpty(a.getStraat()));
	}

	@Test
	@Ignore
	public void test2ToAdres2() {
		Adres a = importSpreadSheetv2.toAdres("1", null, null);
		assertEquals(a.getNummer(), "1");
		assertFalse(importSpreadSheetv2.notNullnotEmpty(a.getStraat()));
	}

	@Test
	@Ignore
	public void test2ToAdres3() {
		Adres a = importSpreadSheetv2.toAdres("straat", null, null);
		assertFalse(importSpreadSheetv2.notNullnotEmpty(a.getNummer()));
		assertEquals(a.getStraat(), "straat");
	}

	@Test
	@Ignore
	public void test2ToAdres4() {
		Adres a = importSpreadSheetv2.toAdres("straat 1", null, null);
		assertEquals(a.getNummer(), "1");
		assertEquals(a.getStraat(), "straat");
	}

	@Test
	@Ignore
	public void test2ToAdres5() {
		Adres a = importSpreadSheetv2.toAdres("straat strat 71", null, null);
		assertEquals(a.getNummer(), "71");
		assertEquals(a.getStraat(), "straat strat");
	}

	@Test
	@Ignore
	public void test2ToAdres6() {
		Adres a = importSpreadSheetv2.toAdres("straat strat 71 bus 6", null, null);
		assertEquals(a.getNummer(), "71 bus 6");
		assertEquals(a.getStraat(), "straat strat");
	}

	@Test
	@Ignore
	public void test2ToAdres7() {
		Adres a = importSpreadSheetv2.toAdres("straat strat 71 yady", null, null);
		assertEquals(a.getNummer(), "71 yady");
		assertEquals(a.getStraat(), "straat strat");
	}

	@Test
	@Ignore
	public void test2NaamSplit() {
		String[] s = importSpreadSheetv2.splitNaamInVoornaamAchternaam("");
		assertFalse(importSpreadSheetv2.notNullnotEmpty(s[0]));
		assertFalse(importSpreadSheetv2.notNullnotEmpty(s[1]));
		s = importSpreadSheetv2.splitNaamInVoornaamAchternaam("aaa");
		assertFalse(importSpreadSheetv2.notNullnotEmpty(s[0]));
		assertEquals(s[1], "aaa");
		s = importSpreadSheetv2.splitNaamInVoornaamAchternaam("aaa bbbb");
		assertEquals(s[0], "aaa");
		assertEquals(s[1], "bbbb");
		s = importSpreadSheetv2.splitNaamInVoornaamAchternaam("aaa  bbbb");
		assertEquals(s[0], "aaa");
		assertEquals(s[1], " bbbb");
		s = importSpreadSheetv2.splitNaamInVoornaamAchternaam("aaa van bbbb");
		assertEquals(s[0], "aaa");
		assertEquals(s[1], "van bbbb");
	}
}

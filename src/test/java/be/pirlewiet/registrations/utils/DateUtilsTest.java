package be.pirlewiet.registrations.utils;

import static org.junit.Assert.assertEquals;

import java.util.Calendar;
import java.util.GregorianCalendar;

import org.junit.Test;

import be.pirlewiet.registrations.utils.DateUtils;

public class DateUtilsTest {

	private DateUtils dateUtils = new DateUtils();

	private static final Calendar CAL = new GregorianCalendar(2012, 05, 19);
	private static final Calendar CAL_1900 = new GregorianCalendar(1900, 0, 1);

	@Test
	public void testWithSlashes() {
		assertEquals(CAL.getTime(), dateUtils.toDate("19/06/2012"));
	}

	@Test
	public void testWithStripes() {
		assertEquals(CAL.getTime(), dateUtils.toDate("19-06-2012"));
	}

	@Test
	public void testWithInvalidFormat() {
		assertEquals(CAL_1900.getTime(), dateUtils.toDate("19-june-2012"));
	}

}

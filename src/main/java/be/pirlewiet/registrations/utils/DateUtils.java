package be.pirlewiet.registrations.utils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.springframework.stereotype.Component;

@Component
public class DateUtils {

	/**
	 * Probeert de datestring naar een java.util.Date om te vormen indien het
	 * formaat dd/MM/yy of dd-MM-yy gerespecteerd wordt, indien niet dan wordt
	 * 01/01/1900 gebruikt als datum.
	 */
	public Date toDate(String datestring) {
		DateFormat df = new SimpleDateFormat("dd/MM/yy");
		DateFormat df2 = new SimpleDateFormat("dd-MM-yy");
		java.util.Date d = null;
		try {
			d = df.parse(datestring);
		} catch (ParseException ex) {
			try {
				d = df2.parse(datestring);
			} catch (ParseException ex1) {
				try {
					d = df.parse("01/01/1900");
				} catch (ParseException ex3) {
					throw new RuntimeException();
				}
			}
		}
		return d;
	}

}

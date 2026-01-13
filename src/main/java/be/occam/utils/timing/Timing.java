package be.occam.utils.timing;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

public class Timing {
	
	public static String datePattern 
		= "dd/MM/yyyy";
	
	public static String flatDatePattern 
		= "ddMMyyyy";
	
	public static String timePattern
		= "HH:mm";
	
	public static final TimeZone timeZone;
	public static final SimpleDateFormat momentFormat;
	public static final SimpleDateFormat dateFormat;
	public static final SimpleDateFormat flatDateFormat;
	public static final SimpleDateFormat timeFormat;
		
	
	static {
		
		timeZone = TimeZone.getTimeZone("Europe/Brussels");
		
		momentFormat = new SimpleDateFormat( new StringBuilder( datePattern ).append( " " ).append( timePattern ).toString() );
		dateFormat = new SimpleDateFormat( new StringBuilder( datePattern ).toString() );
		flatDateFormat = new SimpleDateFormat( new StringBuilder( flatDatePattern ).toString() );
		timeFormat = new SimpleDateFormat( new StringBuilder( timePattern ).toString() );
		
		momentFormat.setTimeZone( timeZone );
		dateFormat.setTimeZone( timeZone );
		flatDateFormat.setTimeZone( timeZone );
		timeFormat.setTimeZone( timeZone );
	}
	
	public static Date date( String date ) {
		
		try {
			return dateFormat.parse( date );
		}
		catch( ParseException e ) {
			throw new RuntimeException( e );
		}
		
	}
	
	public static Date date( String date, SimpleDateFormat format ) {
		
		try {
			return format.parse( date );
		}
		catch( ParseException e ) {
			throw new RuntimeException( e );
		}
		
	}
	
	public static Date date( String date, String format ) {
		
		try {
			SimpleDateFormat dateFormat =
				new SimpleDateFormat( format );
			
			TimeZone timeZone = TimeZone.getTimeZone("Europe/Brussels");
			dateFormat.setTimeZone( timeZone );
			
			return dateFormat.parse( date );
		}
		catch( ParseException e ) {
			throw new RuntimeException( e );
		}
		
	}
	
	public static Date moment( String date ) {
		
		try {
			return momentFormat.parse( date );
		}
		catch( ParseException e ) {
			throw new RuntimeException( e );
		}
		
	}
	
	public static Date moment( String date, String time ) {
		
		return moment( new StringBuilder( date ).append( " " ).append( time ).toString() );

	}
	
	public static String date( Date date ) {
		
		return dateFormat.format( date );
		
	}
	
	public static String time( Date date ) {
		
		return timeFormat.format( date );
		
	}
	
	public static Date time( String time ) {
		
		try {
			return timeFormat.parse( time );
		}
		catch( ParseException e ) {
			throw new RuntimeException( e );
		}
		
	}
	
	public static String date( Date date, String format ) {
		
		SimpleDateFormat simple
			= new SimpleDateFormat( format );
		simple.setTimeZone( timeZone );
		
		return date( date, simple );
		
	}
	
	public static String date( Date date, SimpleDateFormat format ) {
		
		return format.format( date );
		
	}
	
	public static String moment( Date date ) {
		
		return momentFormat.format( date );
		
	}

}

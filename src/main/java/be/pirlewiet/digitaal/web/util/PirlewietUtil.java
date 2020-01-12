package be.pirlewiet.digitaal.web.util;

import java.util.HashMap;
import java.util.Map;

import be.pirlewiet.digitaal.model.Organisation;
import be.pirlewiet.digitaal.web.dto.OrganisationDTO;

public class PirlewietUtil {
	
	public static final String PDIDDY_ID 
		= "imtheoneandonlydominator";
	
	public static final String PDIDDY_CODE 
		= "dom123";
	
	public static final String PDIDDY_EMAIL 
		= "pirlewiet.digitaal@gmail.com";
	
	public static final String PIRLEWIET_EMAIL 
		= "info@pirlewiet.be";
	
	public static boolean isPirlewiet( OrganisationDTO organisatie ) {
		if ( organisatie == null ) {
			return false;
		}
		return organisatie.getEmail() == null ? false : organisatie.getEmail().toLowerCase().equals( PIRLEWIET_EMAIL );
	}
	
	
	public static boolean isPirlewiet( Organisation organisatie ) {
		if ( organisatie == null ) {
			return false;
		}
		return organisatie.getEmail() == null ? false : organisatie.getEmail().toLowerCase().equals( PIRLEWIET_EMAIL );
	}
	
	public static boolean isPD( Organisation organisatie ) {
		if ( organisatie == null ) {
			return false;
		}
		return organisatie.getEmail() == null ? false : organisatie.getEmail().toLowerCase().equals( PDIDDY_EMAIL );
	}
	
	public static Map<String,String> as( Organisation organisation ) {
		
		Map<String,String> headers
			= new HashMap<String,String>();
		
		StringBuilder cookie 
			= new StringBuilder();
		
		String current
			= headers.get( "Cookie" );
		
		if ( current != null ) {
			cookie.append( "; " );
		}
		
		cookie.append( "pwtid=" ).append( organisation.getUuid() );
		
		headers.put( "Cookie", cookie.toString() );
		
		return headers;
		
	}

}

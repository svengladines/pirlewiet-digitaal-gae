package be.pirlewiet.registrations.utils;

import be.pirlewiet.registrations.model.Organisatie;

public class PirlewietUtil {
	
	public static boolean isPirlewiet( Organisatie organisatie ) {
		return organisatie.getId().equals( 1 );
	}

}

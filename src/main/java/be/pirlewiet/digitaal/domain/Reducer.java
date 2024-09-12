package be.pirlewiet.digitaal.domain;

import be.pirlewiet.digitaal.web.dto.OrganisationDTO;
import org.springframework.stereotype.Component;

@Component
public class Reducer {
	
	public void reduce( OrganisationDTO organisation ) {
		
		String email = organisation.getEmail();
		
		StringBuilder reducedEmail
			= new StringBuilder();
		
		String beforeAt
			= email.substring(0, email.indexOf("@"));
		
		int len
			= beforeAt.length();
		
		for ( int i=0; i < len ; i++ ) {
			
			reducedEmail.append( ( i < ( len > 6 ? 3 : 1 ) ) || ( i > ( len > 6 ? len - 4 : len - 2 ) ) ? beforeAt.charAt( i ) : "." );
			
		}
		
		reducedEmail.append( email.substring( beforeAt.length() ) );
		
		organisation.setEmail( reducedEmail.toString() );
		
		organisation.setCode( "" );
		
	}

}

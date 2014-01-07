package be.pirlewiet.registrations.services;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.transaction.annotation.Transactional;

public class CustomUserDetailsService implements UserDetailsService {

	@Autowired
	private CredentialsService credentialsService;
	
	private static Logger logger = Logger.getLogger(CustomUserDetailsService.class);
	
	@Override
	@Transactional
	public UserDetails loadUserByUsername(String Username) {
		logger.info("The user with username " + Username + " tried to log on.");
		return credentialsService.findCredentialsByUsername(Username);
	}	
}

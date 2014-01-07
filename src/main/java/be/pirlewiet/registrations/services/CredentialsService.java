package be.pirlewiet.registrations.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import be.pirlewiet.registrations.model.Credentials;
import be.pirlewiet.registrations.repositories.CredentialsRepository;

@Service
@Transactional
public class CredentialsService {
	@Autowired
	private CredentialsRepository credentialsRepository;

	public Credentials findCredentialsByUsername(String username) {
		return credentialsRepository.findCredentialsByUsername(username);
	}

	public String getLoggedInUsername(){
    	Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    	return ((UserDetails)authentication.getPrincipal()).getUsername();
	}

	public Credentials create(Credentials c) {
		return credentialsRepository.create(c);
	}
	public Credentials update(Credentials c) {
		return credentialsRepository.update(c);
	}
//	public List<Credentials> findAllSecretariaatCredentials() {
//		return credentialsRepository.findAllSecretariaatCredentials();
//	}
}

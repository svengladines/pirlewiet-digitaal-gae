package be.pirlewiet.registrations.services;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailSendException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import be.pirlewiet.registrations.model.Credentials;
import be.pirlewiet.registrations.model.Secretariaatsmedewerker;
import be.pirlewiet.registrations.repositories.SecretariaatsmedewerkerRepository;
import be.pirlewiet.registrations.utils.MailUtils;
import be.pirlewiet.registrations.utils.PasswordUtils;

@Service
@Transactional(propagation = Propagation.REQUIRED)
public class SecretariaatsmedewerkerService {
	@Autowired
	private SecretariaatsmedewerkerRepository secretariaatsmedewerkerRepository;
	@Autowired
	private CredentialsService credentialsService;
	@Autowired
	private MailUtils mailUtils;
	@Autowired
	private PasswordUtils passwordUtils;

	public Secretariaatsmedewerker findById(long id) {
		return secretariaatsmedewerkerRepository.find(id);
	}

	public List<Secretariaatsmedewerker> findByName(String naam) {
		return secretariaatsmedewerkerRepository.findByNaam(naam);
	}

	public List<Secretariaatsmedewerker> findDisplayedSecretariaatMedewerker() {
		return secretariaatsmedewerkerRepository.findDisplayedSecretariaatMedewerker();
	}

	public Secretariaatsmedewerker setDisplayedFalse(long id) {
		return secretariaatsmedewerkerRepository.setDisplayedFalse(id);
	}

	public Secretariaatsmedewerker createAndSendPasswordAndAssignRoleSecretariaat(Secretariaatsmedewerker s) throws MailSendException {
		Secretariaatsmedewerker s2 = new Secretariaatsmedewerker(s.getVoornaam(), s.getFamilienaam());
		Credentials c2 = new Credentials();
		s2.setCredentials(c2);
		c2.setEmailadres(s.getCredentials().getEmailadres());
		c2.setEnabled(true);

		ArrayList<String> roles = new ArrayList<String>();
		roles.add("ROLE_SECRETARIAAT");
		c2.setRoles(roles);
		c2.setUsername(s.getCredentials().getUsername());
		c2.setPassword(passwordUtils.generateAndEncodeAndSendPassword(s2.getCredentials().getEmailadres()));
		credentialsService.create(c2);

		// Default: set Displayed to true
		s2.setDisplayed(true);

		Secretariaatsmedewerker result = secretariaatsmedewerkerRepository.create(s2);

		return result;
	}

	public Secretariaatsmedewerker reset(Secretariaatsmedewerker s) throws MailSendException, Exception {
		s.getCredentials().setPassword(passwordUtils.generateAndEncodeAndSendPassword(s.getCredentials().getEmailadres()));
		return s;

	}

	public Secretariaatsmedewerker update(Secretariaatsmedewerker s) {
		return secretariaatsmedewerkerRepository.update(s);
	}

	public List<Secretariaatsmedewerker> getAll() {
		return secretariaatsmedewerkerRepository.findAll();
	}
}

package be.pirlewiet.registrations.utils;

import java.util.Random;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailSendException;
import org.springframework.security.authentication.encoding.PasswordEncoder;
import org.springframework.security.authentication.encoding.ShaPasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class PasswordUtils {

	private final static Logger LOGGER = LoggerFactory.getLogger(MailUtils.class);
	private final static String ALPHAABET = "abcdefghijklmnopqrstuvwxyz0123456789";
	private final static PasswordEncoder PASSWORD_ENCODER = new ShaPasswordEncoder();

	@Autowired
	private MailUtils mailUtils;

	private String generatePassword() {
		String result = "";
		Random randomGenerator = new Random();
		for (int i = 0; i < 4; i++) {
			result += ALPHAABET.charAt(randomGenerator.nextInt(26));
		}
		for (int i = 0; i < 4; i++) {
			result += ALPHAABET.charAt(randomGenerator.nextInt(10) + 26);
		}
		LOGGER.info(result);
		return result;
	}

	private String encodePassword(String password) {
		return PASSWORD_ENCODER.encodePassword(password, "");
	}

	public String generateAndEncodeAndSendPassword(String emailadres) throws MailSendException {
		String password = generatePassword();
		String encodedPass = encodePassword(password);

		mailUtils.sendmail(emailadres, password);

		return encodedPass;
	}

}

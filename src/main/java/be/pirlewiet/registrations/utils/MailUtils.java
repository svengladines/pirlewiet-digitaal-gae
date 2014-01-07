/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package be.pirlewiet.registrations.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailSendException;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.stereotype.Service;

import be.pirlewiet.registrations.services.SecretariaatsmedewerkerService;

/**
 * 
 * @author BGRI978
 */
@Service
public class MailUtils {

	private final static Logger LOGGER = LoggerFactory.getLogger(MailUtils.class);

	@Autowired
	private SecretariaatsmedewerkerService secretariaatsmedewerkerService;
	@Autowired
	private MailSender mailSender;
	@Autowired
	private SimpleMailMessage templateMessage;

	public void sendmail(String emailadres, String passwoord) throws MailSendException {
		if (secretariaatsmedewerkerService == null) {
			LOGGER.info("secretariaatsmedewerkerService NULL");
		}
		if (mailSender == null) {
			LOGGER.info("mailSender NULL");
		}
		if (templateMessage == null) {
			LOGGER.info("templateMessage NULL");
		}
		String newLine = System.getProperty("line.separator");
		SimpleMailMessage msg = new SimpleMailMessage(templateMessage);
		msg.setTo(emailadres);
		msg.setText(("Beste, " + newLine + newLine + "U kan aanloggen op pirlewiet, met passwoord: [" + passwoord + "]" + newLine + newLine + " Met vriendelijke goeten" + newLine + "Pirlewiet"));
		LOGGER.info("Sending mail to: " + emailadres);

		// mailSender.send(msg);

	}
}

package be.pirlewiet.digitaal.domain.people;

import be.pirlewiet.digitaal.domain.HeadQuarters;
import brevo.ApiClient;
import brevo.ApiException;
import brevo.Configuration;
import brevo.auth.ApiKeyAuth;
import brevoApi.TransactionalEmailsApi;
import brevoModel.CreateSmtpEmail;
import brevoModel.SendSmtpEmail;
import brevoModel.SendSmtpEmailTo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMailMessage;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

import javax.mail.internet.MimeMessage;
import java.util.List;

@Component
public class MailMan {
	
	protected final Logger logger = LoggerFactory.getLogger( this.getClass() );

	@Autowired
	protected JavaMailSender mailSender;
	
	@Autowired
	HeadQuarters headQuarters;

	@Value("${brevo.api.key}")
	String brevoApiKey;

	/**
	 * GAE does not support jakarta email stuff (it still expects javax.mail), so use REST API.
	 */
	public boolean deliver(String to, String subject, String text) {
		try {

			ApiClient defaultClient = Configuration.getDefaultApiClient();
			ApiKeyAuth apiKey = (ApiKeyAuth) defaultClient.getAuthentication("api-key");
			apiKey.setApiKey(brevoApiKey);
			// Uncomment the following line to set a prefix for the API key, e.g. "Token" (defaults to null)
			//apiKey.setApiKeyPrefix("Token");

			TransactionalEmailsApi apiInstance = new TransactionalEmailsApi();
			SendSmtpEmail sendSmtpEmail = new SendSmtpEmail();
			sendSmtpEmail.setTo(List.of(new SendSmtpEmailTo().email(to)));
			sendSmtpEmail.setSubject(subject);
			sendSmtpEmail.setHtmlContent(text);// SendSmtpEmail | Values to send a transactional email
			logger.info( "Sent email to [{}] with subject [{}]", to, subject);
			return true;
		}
		catch( Exception e ) {
			logger.error( "Failed to send email to [{}]", to, e );
			return false;
		}
	}

	/*
	public void deliver(String to, String subject, String text) {

		try {
			//ThreadFactory bgThreadFactory = ThreadManager.backgroundThreadFactory();
			//ExecutorService executorService = Executors.newFixedThreadPool(10, bgThreadFactory);
			//bgThreadFactory.newThread(() -> {
			//	try {
					MailService.Message msg = new MailService.Message(PirlewietApplicationConfig.EMAIL_ADDRESS, to, subject, text);
					MailServiceFactory.getMailService().send(msg);
			//	} catch(Exception e) {
			//		logger.error("Exception while sending email message to [{}]", to, e);
			//	}
			//});
		}
		catch(Exception e) {
			logger.error("Exception while sending email message to [{}]", to, e);
		}
	}
	 */

	/*
	public MimeMessage message( ) {
		
		try {
			return this.javaMailSender.createMimeMessage();
		}
		catch( Exception e ) {
			logger.error( "could not create mime message", e );
			throw new RuntimeException( e );
		}
		
	}
	 */
	
}

package be.pirlewiet.digitaal.domain.people;

import java.util.Properties;
import be.pirlewiet.digitaal.application.config.PirlewietApplicationConfig;
import be.pirlewiet.digitaal.domain.HeadQuarters;
import com.google.appengine.api.mail.MailService;
import com.google.appengine.api.mail.MailServiceFactory;

import javax.mail.Message;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import com.sendgrid.Method;
import com.sendgrid.Request;
import com.sendgrid.Response;
import com.sendgrid.SendGrid;
import com.sendgrid.helpers.mail.Mail;
import com.sendgrid.helpers.mail.objects.Content;
import com.sendgrid.helpers.mail.objects.Email;
import org.springframework.beans.factory.annotation.Autowired;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class MailMan {
	
	protected final Logger logger = LoggerFactory.getLogger( this.getClass() );
	
	@Autowired
	HeadQuarters headQuarters;

	@Value("${sendgrid.api.key}")
	String sendGridAPIKey;

	/**
	 * Doesnt seem to work with Java 21 & jakarta.mail...
	 * @param to
	 * @param subject
	 * @param text
	 * @return
	 */
	public boolean deliver(String to, String subject, String text) {
		try {
			Email f = new Email(PirlewietApplicationConfig.EMAIL_ADDRESS);
			Email t = new Email(to);
			Content content = new Content("text/html", text);
			Mail mail = new Mail(f, subject, t, content);

			SendGrid sg = new SendGrid(sendGridAPIKey);
			Request request = new Request();
			request.setMethod(Method.POST);
			request.setEndpoint("mail/send");
			request.setBody(mail.build());
			Response response = sg.api(request);
			logger.info("sg response status [{}]",response.getStatusCode());
			logger.info("sg response body [{}]",response.getBody());
			logger.info("sg response headers [{}]", response.getHeaders());
			return true;
		}
		catch( Exception e ) {
			logger.error( "could not send email", e );
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

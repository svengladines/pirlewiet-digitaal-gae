package be.pirlewiet.digitaal.domain.people;

import java.util.Properties;

import jakarta.mail.Transport;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

@Component
public class MailMan {
	
	protected final Logger logger
		= LoggerFactory.getLogger( this.getClass() );
	
	@Autowired
	JavaMailSender javaMailSender;
	
	public boolean deliver( MimeMessage mimeMessage ) {
		try {
			
			Properties props = new Properties();
			// SGL| doesn't seem to work ... tries SMTP
			// this.javaMailSender.send( mimeMessage ); 
			Transport.send( mimeMessage );
			logger.info( "sent email to {}", (Object) mimeMessage.getRecipients(  MimeMessage.RecipientType.TO ) );
			return true;
			
		}
		catch( Exception e ) {
			logger.error( "could not send email", e );
			return false;
		}
		
	}
	
	public MimeMessage message( ) {
		
		try {
			return this.javaMailSender.createMimeMessage();
		}
		catch( Exception e ) {
			logger.error( "could not create mime message", e );
			throw new RuntimeException( e );
		}
		
	}
	
}

package be.pirlewiet.registrations.domain;

import java.util.Properties;

import javax.annotation.Resource;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMessage.RecipientType;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.mail.javamail.JavaMailSender;

public class PostBode {
	
	protected final Logger logger
		= LoggerFactory.getLogger( this.getClass() );
	
	@Resource
	JavaMailSender javaMailSender;
	
	public boolean deliver( MimeMessage mimeMessage ) {
		try {
			
			Properties props = new Properties();
			// SGL| doesn't seem to work ... tries SMTP
			// this.javaMailSender.send( mimeMessage ); 
			Transport.send( mimeMessage );
			logger.info( "sent email to {}", (Object) mimeMessage.getRecipients(  RecipientType.TO ) );
			return true;
			
		}
		catch( Exception e ) {
			logger.error( "could not send email", e );
			return false;
		}
		
	}
	
}
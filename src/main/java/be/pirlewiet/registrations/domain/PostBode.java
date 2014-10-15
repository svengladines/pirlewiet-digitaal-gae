package be.pirlewiet.registrations.domain;

import javax.annotation.Resource;
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
	
	public void deliver( MimeMessage mimeMessage ) {
		try {
		
			this.javaMailSender.send( mimeMessage );
		
			logger.info( "sent email to {}", (Object) mimeMessage.getRecipients(  RecipientType.TO ) );
			
		}
		catch( Exception e ) {
			logger.error( "could not send email", e );
			throw new RuntimeException( e );
		}
		
	}
	
}

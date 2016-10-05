package be.pirlewiet.digitaal.web.controllers;

import static be.occam.utils.spring.web.Controller.response;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import be.occam.utils.ftp.FTPClient;
import be.occam.utils.spring.web.Client;
import be.pirlewiet.digitaal.domain.people.DoorMan;
import be.pirlewiet.digitaal.domain.people.OrganisationManager;
import be.pirlewiet.digitaal.model.Organisation;
import be.pirlewiet.digitaal.web.util.PirlewietUtil;

@Controller
@RequestMapping(value="/pd/{page}.html")
public class PDController {
	
	private final static Logger logger = LoggerFactory.getLogger( PDController.class );
	
	@Resource
	OrganisationManager organisationManager;
	
	@Resource
	protected DoorMan buitenWipper;
	
	@Resource
	protected Organisation pDiddy;
	
	@Resource
	protected FTPClient ftpClient;
	
	@RequestMapping(method=RequestMethod.GET, produces={ MediaType.TEXT_HTML_VALUE } ) 
	@ResponseBody
	public ResponseEntity<String> view( @PathVariable String page, @CookieValue( required = true, value="pwtid" ) String pwtID )  {
		
		ModelAndView mav
			= new ModelAndView();
		
		Organisation organisatie
			= this.buitenWipper.guard().whoHasID(  pwtID  );
		
		String html
			= "no es bueno";
		
		if ( ( organisatie == null ) || ( ! be.pirlewiet.digitaal.web.util.PirlewietUtil.isPD( organisatie ) ) ){
			mav.setView( new RedirectView("/code.htm") );
			html = "get outta here!";
		}
		else if ( "organisations".equals( page ) ) {
			
			try {
				html = Client.getHTML( "http://pirlewiet-digitaal.appspot.com/rs/organisations.html", PirlewietUtil.as( pDiddy ) ).getBody();
				logger.info( "html: {}", html );
				
				logger.info( "sending html to FTP server...", html );
				boolean ok 
					= ftpClient.putTextFile("httpdocs/digitaal", "organisations.html", html );
				logger.info( "FTP put [{}]", ok ? "succeeded" : "failed" );
				
			}
			catch ( Exception e ) {
				logger.error( "could not get organisations as PD", e );
			}
		
		}
		
		return response( html, HttpStatus.OK );
		
	}
	
}

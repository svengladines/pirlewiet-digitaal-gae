package be.pirlewiet.registrations.domain;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;

import javax.annotation.Resource;
import javax.mail.internet.MimeMessage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.transaction.annotation.Transactional;

import be.occam.utils.ftp.FTPClient;
import be.occam.utils.spring.web.Client;
import be.pirlewiet.registrations.application.config.PirlewietApplicationConfig;
import be.pirlewiet.registrations.domain.exception.PirlewietException;
import be.pirlewiet.registrations.model.Adres;
import be.pirlewiet.registrations.model.InschrijvingX;
import be.pirlewiet.registrations.model.Organisatie;
import be.pirlewiet.registrations.model.Vakantie;
import be.pirlewiet.registrations.model.VakantieType;
import be.pirlewiet.registrations.repositories.OrganisatieRepository;
import be.pirlewiet.registrations.repositories.VakantieRepository;
import be.pirlewiet.registrations.web.util.DataGuard;
import be.pirlewiet.registrations.web.util.PirlewietUtil;

import com.google.appengine.api.datastore.KeyFactory;

import freemarker.template.Configuration;
import freemarker.template.Template;

public class HolidayManager {
	
	@Resource
	HeadQuarters headQuarters;
	
	protected final Logger logger
		= LoggerFactory.getLogger( this.getClass() );
	
	protected final Comparator<Organisatie> lastUpdatedFirst
		= new Comparator<Organisatie>() {

			@Override
			public int compare(Organisatie o1, Organisatie o2) {
				if ( o1.getUpdated() == null ) {
					return 1;
				}
				else if ( o2.getUpdated() == null ) {
					return -1;
				}
				else {
					return o1.getUpdated().after( o2.getUpdated() ) ? -1 : 1;
				}
			}
		
		};
	
	@Resource
	protected VakantieRepository configuredVakantieRepository;
	
	@Resource
	BuitenWipper buitenWipper;
	
	@Resource
	DataGuard dataGuard;
	
	@Resource
	PostBode postBode;
	
    public HolidayManager() {
    }
    
    public HolidayManager guard() {
    	this.dataGuard.guard();
    	return this;
    }
    
    @Transactional( readOnly=false )
    public VakantieType singleType( String vks ) {
    	
    	VakantieType type
    		= null;
    	
    	if ( vks != null ) {
    		
			StringTokenizer tok
				= new StringTokenizer( vks.trim(), ",", false );
			
			while( tok.hasMoreTokens() ) {
				
				String t
					= tok.nextToken().trim();
				
				if ( t.length() == 0 ) {
					continue;
				}
				
				Vakantie v 
					= this.configuredVakantieRepository.findByUuid( t.trim() ); 
		
				if ( v != null ) {
					
					if ( type == null ) {
						
						type = v.getType();
						
					}
					else {
						
						if ( ! type.equals( v.getType() ) ) {
							
							throw new PirlewietException( "Je mag per inschrijving enkel vakanties van hetzelfde type selecteren.");
							
						}
						
					}
					
				}
				else {
					throw new RuntimeException( "no vakantie with id [" + t.trim() + "]" );
				}
				
			}
		}
    	
    	return type;
    	
    }

}
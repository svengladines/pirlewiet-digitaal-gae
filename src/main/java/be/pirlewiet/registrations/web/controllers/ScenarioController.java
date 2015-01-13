package be.pirlewiet.registrations.web.controllers;

import static be.occam.utils.spring.web.Controller.response;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;

import javax.annotation.Resource;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItemIterator;
import org.apache.commons.fileupload.FileItemStream;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.support.DefaultMultipartHttpServletRequest;

import be.pirlewiet.registrations.domain.BuitenWipper;
import be.pirlewiet.registrations.domain.OrganisationManager;
import be.pirlewiet.registrations.domain.PirlewietException;
import be.pirlewiet.registrations.domain.SecretariaatsMedewerker;
import be.pirlewiet.registrations.domain.scenarios.SetOrganisationsUuidScenario;
import be.pirlewiet.registrations.model.Organisatie;
import be.pirlewiet.registrations.web.util.ExcelImporter;

@Controller
@RequestMapping(value="/scenarios/{id}")
public class ScenarioController {
	
	private final Logger logger 
		= LoggerFactory.getLogger( ScenarioController.class );
	
	@Resource
	SetOrganisationsUuidScenario setOrganisationsUuidScenario;
	
	protected final ExcelImporter excelImporter
		= new ExcelImporter();
	
	@RequestMapping( method = { RequestMethod.GET } )
	@ResponseBody
	public ResponseEntity<Boolean> get( @PathVariable("id") String id ) {
		
		try {
		
			if ( "uuid".equals( id ) ) {
				this.setOrganisationsUuidScenario.guard().execute();
				return response( Boolean.TRUE, HttpStatus.OK );
			}
			else {
				return response( Boolean.FALSE, HttpStatus.NOT_FOUND );
			}
			
		}
		catch( Exception e ) {
			logger.warn( "scenario execution failed", e );
			return response( Boolean.FALSE, HttpStatus.INTERNAL_SERVER_ERROR );
		}
			
	}
		
}

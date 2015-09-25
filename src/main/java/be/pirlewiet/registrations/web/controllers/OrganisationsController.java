package be.pirlewiet.registrations.web.controllers;

import static be.occam.utils.spring.web.Controller.response;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;

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
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.support.DefaultMultipartHttpServletRequest;
import org.springframework.web.servlet.ModelAndView;

import be.pirlewiet.registrations.domain.BuitenWipper;
import be.pirlewiet.registrations.domain.OrganisationManager;
import be.pirlewiet.registrations.domain.PirlewietException;
import be.pirlewiet.registrations.domain.Reducer;
import be.pirlewiet.registrations.domain.SecretariaatsMedewerker;
import be.pirlewiet.registrations.model.Organisatie;
import be.pirlewiet.registrations.utils.PirlewietUtil;
import be.pirlewiet.registrations.web.util.ExcelImporter;

@Controller
@RequestMapping(value="/organisations")
public class OrganisationsController {
	
	private final Logger logger 
		= LoggerFactory.getLogger( OrganisationsController.class );
	
	@Resource
	protected SecretariaatsMedewerker secretariaatsMedewerker;
	
	@Resource
	protected OrganisationManager organisationManager;
	
	@Resource
	BuitenWipper buitenWipper;
	
	@Resource
	Reducer reducer;
	
	protected final ExcelImporter excelImporter
		= new ExcelImporter();
	
	@RequestMapping( method = { RequestMethod.POST} )
	@ResponseBody
	public ResponseEntity<Organisatie> post( @RequestBody Organisatie organisation,  HttpServletResponse response ) {
		
		if ( this.organisationManager.guard().isInComplete( organisation, false ) ) {
			
			return response( HttpStatus.UNPROCESSABLE_ENTITY );
			
		}
		else { 
		
			Organisatie created
				= this.secretariaatsMedewerker.guard().addOrganisatie( organisation );
			
			Cookie cookie
				= new Cookie( "pwtid", created.getUuid() );
		
			cookie.setMaxAge( 3600 * 24 * 30 * 12 );
		
			cookie.setPath( "/" );
		
			response.addCookie( cookie );
			
			return response( created, HttpStatus.OK );
			
		}
			
	}
	
	//@RequestMapping( method = { RequestMethod.POST }, consumes={"multipart/form-data"} )
	public ResponseEntity<Organisatie[]> post( @RequestPart MultipartFile file, Locale locale ) { 
		
		logger.info("postfile, received file [{}]", file.getOriginalFilename() );
		
		try {
			
			List<String[]> rows 
				= this.excelImporter.getExcelData( file, 1, 1,2,3,4,5,6,7,8,9,10 );
		
			if (rows.size() == 0 || rows.isEmpty()) {
				
				this.logger.warn( "no rows in file" );
				
			}
			
			List<Organisatie> organisations
				= new ArrayList<Organisatie>( rows.size() );
			
			for ( String[] row : rows ) {
				
				try {
				
					Organisatie organisatie 
						= this.mapTo( row ); 
	
					if ( ( organisatie != null ) && ( ! isEmpty( organisatie.getEmail() ) ) ) {
					
						logger.info( "mapped organisation [{}], email [{}]", organisatie.getNaam(), organisatie.getEmail() );
					
						organisations.add( this.secretariaatsMedewerker.guard().addOrganisatie( organisatie ) );
					}
				}
				catch( PirlewietException e ) {
					logger.warn( "could not add organisation: [{}]", e.getMessage() );
				}
				catch( Exception e ) {
					logger.warn( "could not add organisation: [{}]", e );
				}
					
			}
				
			return response( organisations.toArray( new Organisatie[] {} ), HttpStatus.OK );
			
		}
		catch( Exception e ) {
			
			logger.warn("error", e );
			
			throw new RuntimeException( e );
			
		}
	}
	
	@RequestMapping( method = { RequestMethod.POST }, consumes={"multipart/form-data"} )
	public ResponseEntity<Organisatie[]> post( HttpServletRequest request, Locale locale ) { 
		
		logger.info("postfile");
		
		try {
			
			byte[] bytes
				= this.bytesFromRequest( request );
			
			ByteArrayInputStream bis
				= new ByteArrayInputStream( bytes );
			
			List<String[]> rows 
				= this.excelImporter.getExcelData( bis, 1, 1,2,3,4,5,6,7,8,9,10 );
		
			if (rows.size() == 0 || rows.isEmpty()) {
				
				this.logger.warn( "no rows in file" );
				
			}
			
			List<Organisatie> organisations
				= new ArrayList<Organisatie>( rows.size() );
			
			for ( String[] row : rows ) {
				
				try {
				
					Organisatie organisatie 
						= this.mapTo( row ); 
	
					if ( ( organisatie != null ) && ( ! isEmpty( organisatie.getEmail() ) ) ) {
					
						logger.info( "mapped organisation [{}], email [{}]", organisatie.getNaam(), organisatie.getEmail() );
					
						organisations.add( this.secretariaatsMedewerker.guard().addOrganisatie( organisatie ) );
					}
				}
				catch( PirlewietException e ) {
					logger.warn( "could not add organisation: [{}]", e.getMessage() );
				}
				catch( Exception e ) {
					logger.warn( "could not add organisation: [{}]", e );
				}
					
			}
				
			return response( organisations.toArray( new Organisatie[] {} ), HttpStatus.OK );
			
		}
		catch( Exception e ) {
			
			logger.warn("error", e );
			
			throw new RuntimeException( e );
			
		}
	}
	
	@RequestMapping( method = { RequestMethod.GET }, produces={ MediaType.TEXT_HTML_VALUE } )
	public ModelAndView view( @CookieValue(required=true, value="pwtid") String pwtid, @RequestParam(required=false) String order ) {
		
		Organisatie actor
			= this.buitenWipper.guard().whoHasID( pwtid  );
		
		Map<String,Object> model
			= new HashMap<String,Object>();
	
		List<Organisatie> organisations 
			= this.organisationManager.all( );
		
		String view
			= null;
		
		if ( PirlewietUtil.isPirlewiet( actor) ) {
			
			view = "organisations_pirlewiet";
			
		}
		else {
			
			for ( Organisatie organisation : organisations ) {
				this.reducer.reduce( organisation );
			}
			
			view = "organisations";
			
		}
		
		Collections.sort( organisations, this.comparator( order != null ? order : "name" ) );
		
		model.put( "organisations", organisations );

		
		
		return new ModelAndView( view, model );
		
	}
		
	@ExceptionHandler( PirlewietException.class)
	@ResponseBody
	public ResponseEntity<String> handleFailure( PirlewietException e ){
		
		logger.warn( "failure while handling request", e );
		return response( e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR );
		
	}
		
	@ExceptionHandler(Exception.class)
	@ResponseBody
	public ResponseEntity<String> handleError( Exception e ){
		
		logger.warn( "error while handling request", e );
		return response( "Er trad een fout op. Probeer AUB opnieuw. Indien het probleem blijft optreden, contacteer dan het secretariaat van Pirlewiet.", HttpStatus.INTERNAL_SERVER_ERROR );
		
	}
	
	protected Organisatie mapTo( String[] columns ) {
		
		Organisatie organisatie
			= new Organisatie();
		
		if ( ! isEmpty( columns[ 0 ] ) ) {
			
			organisatie.setNaam( columns[ 0 ].trim() );
			
		}
		else {
			
			organisatie.setNaam( columns[ 1 ].trim() );
			
		}
		
		String straat
			= columns[ 3 ];
		
		if ( ! isEmpty( straat ) ) {
			
			int space =	straat.lastIndexOf(" ");
			
			if ( space != -1 ) {
				organisatie.getAdres().setStraat( straat.substring( 0, space ) );
				logger.info( "[straat={}]", straat.substring( 0, space ) );
				String nummer
					= straat.substring( space + 1 ).trim();
				organisatie.getAdres().setNummer( nummer );
				logger.info( "[nummer={}]", straat.substring( space + 1 ) );
			}
			else {
//				logger.info( "[straat={}]", straat.trim() );
				organisatie.getAdres().setStraat( straat.trim() );
			}
			
		}
		
		String zip
			= columns[ 4 ];
		
		if ( ! isEmpty( zip ) ) {
			zip = "" + Double.valueOf( zip.trim() ).intValue();
			logger.info( "[zip={}]", zip );
			organisatie.getAdres().setZipCode( zip );	
		}
		
		String gemeente
			= columns[ 5 ];
		
		logger.info( "[gemeente={}]", gemeente );
		organisatie.getAdres().setGemeente( gemeente );
		
		String telefoon
			= columns[ 6 ];
		
		if ( ! isEmpty( telefoon ) ) {
			
			telefoon = telefoon.trim().replace(" ", "");
			
			if ( telefoon.startsWith( "04" ) ) {
				
				logger.info( "[telefoon is gsm = {}]", telefoon );
				organisatie.setGsmNummer( telefoon );
			}
			else {
				logger.info( "[telefoon={}]", telefoon );
				organisatie.setTelefoonNummer( telefoon );
			}
			
		}
		
		String gsm
			 = columns[ 7 ];
		
		if ( ! isEmpty( gsm ) ) {
			
			gsm = gsm.trim().replace(" ", "");
			
			logger.info( "[gsm={}]", gsm );
			organisatie.setGsmNummer( gsm.trim() );	
		}
		
		String email
			= columns[ 9 ];
		
		if ( ! isEmpty( email ) ) {
			logger.info( "[email={}]", email );
			organisatie.setEmail( email.trim() );	
		}
		
		return organisatie;
		
	}
	
	protected boolean isEmpty( String s ) {
		return (s == null)  || s.isEmpty();
	}
	
	protected byte[] bytesFromRequest( HttpServletRequest request ) {
		
		ByteArrayOutputStream bos
			= new ByteArrayOutputStream();
		
		if ( false /* request instanceof DefaultMultipartHttpServletRequest */ ) {
			
			try {
			
				DefaultMultipartHttpServletRequest d
					= (DefaultMultipartHttpServletRequest) request;
				
				Iterator<String> names = d.getFileNames();
				
				while ( names.hasNext() ) {
					
					String name 
						= names.next();
					
					MultipartFile file 
						= d.getFile( name );
				
			        // FileItemStream item = iterator.next();
			        InputStream stream = file.getInputStream();
			       
			          int len;
			          byte[] buffer 
			          	= new byte[8192];
			          
			          while ((len = stream.read(buffer, 0, buffer.length)) != -1) {
			        	  bos.write( buffer );
			          }
			     }
		    } catch ( Exception e ) {
		    	throw new RuntimeException( e );
		    }
		
		}
		else {
			
			try {
			 
				ServletFileUpload upload 
			 		= new ServletFileUpload();
			 
				/*
				List list
					= upload.parseRequest( request );
				
				list.size();
				*/ 
				FileItemIterator iterator 
					= upload.getItemIterator( request );
		      while (iterator.hasNext()) {
		    	  
		    	  FileItemStream fi
		    	  	= iterator.next();
		    	  
		    	  

		          if ( fi.isFormField() ) {
		            logger.info("Got a form field: " + fi.getFieldName());
		          } else {
		        	  
		        	  InputStream stream 
			    	  	= fi.openStream();
		        	  
		        	  int len;
		              byte[] buffer = new byte[8192];
		              while ((len = stream.read(buffer, 0, buffer.length)) != -1) {
		            	  bos.write(buffer, 0, len);
		              }
		        	
		          }
		    	  
		      }
				
		     }
		     catch( Exception e ) {
		    	 logger.warn( "could not parse HTTP request", e );
		     }

			
		}
		
		return bos.toByteArray();
	}
	
	protected final Comparator<Organisatie> comparator( final String param ) {
		
		switch( param ) {
		
		case "city": 
				return new Comparator<Organisatie>() {
					@Override
					public int compare(Organisatie o1, Organisatie o2) {
						return o1.getAdres().getGemeente().compareTo( o2.getAdres().getGemeente() );
					} };
		case "street": 
			return new Comparator<Organisatie>() {
				@Override
				public int compare(Organisatie o1, Organisatie o2) {
					return o1.getAdres().getStraat().compareTo( o2.getAdres().getStraat() );
				} };
		default:
			return new Comparator<Organisatie>() {
				@Override
				public int compare(Organisatie o1, Organisatie o2) {
					return o1.getNaam().compareTo( o2.getNaam() );
				} };	
		}
		
	}
		
}

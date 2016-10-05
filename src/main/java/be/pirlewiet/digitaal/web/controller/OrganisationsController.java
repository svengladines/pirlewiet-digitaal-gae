package be.pirlewiet.digitaal.web.controller;

import static be.occam.utils.spring.web.Controller.response;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.Comparator;
import java.util.Iterator;

import javax.annotation.Resource;
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
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.support.DefaultMultipartHttpServletRequest;

import be.pirlewiet.digitaal.domain.Reducer;
import be.pirlewiet.digitaal.domain.exception.PirlewietException;
import be.pirlewiet.digitaal.domain.people.DoorMan;
import be.pirlewiet.digitaal.domain.service.OrganisationService;
import be.pirlewiet.digitaal.dto.OrganisationDTO;
import be.pirlewiet.digitaal.model.Address;
import be.pirlewiet.digitaal.model.Organisation;
import be.pirlewiet.digitaal.web.util.ExcelImporter;

@Controller
@RequestMapping(value="/organisations")
public class OrganisationsController {
	
	private final Logger logger 
		= LoggerFactory.getLogger( OrganisationsController.class );
	
	@Resource
	protected OrganisationService organisationService;
	
	@Resource
	DoorMan doorMan;
	
	@Resource
	Reducer reducer;
	
	protected final ExcelImporter excelImporter
		= new ExcelImporter();
	
	@RequestMapping( method = { RequestMethod.POST} )
	@ResponseBody
	public ResponseEntity<OrganisationDTO> post( @RequestBody OrganisationDTO organisation, @CookieValue(required=true, value="pwtid") String pwtid, HttpServletResponse response ) {
		
		Organisation actor 
			= this.doorMan.whoHasID( pwtid );
		
		OrganisationDTO created
			= this.organisationService.guard().create( organisation, actor );
		
		/*
		Cookie cookie
			= new Cookie( "pwtid", created.getUuid() );
	
		cookie.setMaxAge( 3600 * 24 * 30 * 12 );
	
		cookie.setPath( "/" );
	
		response.addCookie( cookie );
		
		*/
		
		return response( created, HttpStatus.OK );
			
	}
	
	//@RequestMapping( method = { RequestMethod.POST }, consumes={"multipart/form-data"} )
	/*
	public ResponseEntity<Organisation[]> post( @RequestPart MultipartFile file, Locale locale ) { 
		
		logger.info("postfile, received file [{}]", file.getOriginalFilename() );
		
		try {
			
			List<String[]> rows 
				= this.excelImporter.getExcelData( file, 1, 1,2,3,4,5,6,7,8,9,10 );
		
			if (rows.size() == 0 || rows.isEmpty()) {
				
				this.logger.warn( "no rows in file" );
				
			}
			
			List<Organisation> organisations
				= new ArrayList<Organisation>( rows.size() );
			
			for ( String[] row : rows ) {
				
				try {
				
					Organisation organisatie 
						= this.mapTo( row ); 
	
					if ( ( organisatie != null ) && ( ! isEmpty( organisatie.getEmail() ) ) ) {
					
						logger.info( "mapped organisation [{}], email [{}]", organisatie.getName(), organisatie.getEmail() );
					
						organisations.add( this.organisationManager.guard().create( organisatie ) );
					}
				}
				catch( PirlewietException e ) {
					logger.warn( "could not add organisation: [{}]", e.getMessage() );
				}
				catch( Exception e ) {
					logger.warn( "could not add organisation: [{}]", e );
				}
					
			}
				
			return response( organisations.toArray( new Organisation[] {} ), HttpStatus.OK );
			
		}
		catch( Exception e ) {
			
			logger.warn("error", e );
			
			throw new RuntimeException( e );
			
		}
	}
	
	@RequestMapping( method = { RequestMethod.POST }, consumes={"multipart/form-data"} )
	public ResponseEntity<Organisation[]> post( HttpServletRequest request, Locale locale ) { 
		
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
			
			List<Organisation> organisations
				= new ArrayList<Organisation>( rows.size() );
			
			for ( String[] row : rows ) {
				
				try {
				
					Organisation organisatie 
						= this.mapTo( row ); 
	
					if ( ( organisatie != null ) && ( ! isEmpty( organisatie.getEmail() ) ) ) {
					
						logger.info( "mapped organisation [{}], email [{}]", organisatie.getName(), organisatie.getEmail() );
					
						organisations.add( this.organisationManager.guard().create( organisatie ) );
					}
				}
				catch( PirlewietException e ) {
					logger.warn( "could not add organisation: [{}]", e.getMessage() );
				}
				catch( Exception e ) {
					logger.warn( "could not add organisation: [{}]", e );
				}
					
			}
				
			return response( organisations.toArray( new Organisation[] {} ), HttpStatus.OK );
			
		}
		catch( Exception e ) {
			
			logger.warn("error", e );
			
			throw new RuntimeException( e );
			
		}
	}
	
	@RequestMapping( method = { RequestMethod.GET }, produces={ MediaType.TEXT_HTML_VALUE } )
	public ModelAndView view( @CookieValue(required=true, value="pwtid") String pwtid, @RequestParam(required=false) String order ) {
		
		Organisation actor
			= this.doorMan.guard().whoHasID( pwtid  );
		
		Map<String,Object> model
			= new HashMap<String,Object>();
	
		List<Organisation> organisations 
			= this.organisationManager.all( );
		
		String view
			= null;
		
		if ( PirlewietUtil.isPirlewiet( actor) ) {
			
			view = "organisations_pirlewiet";
			
		}
		else if ( PirlewietUtil.isPD( actor ) ) {
			
			for ( Organisation organisation : organisations ) {
				this.reducer.reduce( organisation );
			}
			
			view = "organisations_public";
			
		}
		
		Collections.sort( organisations, this.comparator( order != null ? order : "name" ) );
		
		model.put( "organisations", organisations );

		return new ModelAndView( view, model );
		
	}
	*/
		
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
	
	protected Organisation mapTo( String[] columns ) {
		
		Organisation organisatie
			= new Organisation();
		
		if ( ! isEmpty( columns[ 0 ] ) ) {
			
			organisatie.setName( columns[ 0 ].trim() );
			
		}
		else {
			
			organisatie.setName( columns[ 1 ].trim() );
			
		}
		
		String straat
			= columns[ 3 ];
		
		if ( ! isEmpty( straat ) ) {
			
			int space =	straat.lastIndexOf(" ");
			
			if ( space != -1 ) {
				organisatie.getAddress().setStraat( straat.substring( 0, space ) );
				logger.info( "[straat={}]", straat.substring( 0, space ) );
				String nummer
					= straat.substring( space + 1 ).trim();
				organisatie.getAddress().setNummer( nummer );
				logger.info( "[nummer={}]", straat.substring( space + 1 ) );
			}
			else {
//				logger.info( "[straat={}]", straat.trim() );
				organisatie.getAddress().setStraat( straat.trim() );
			}
			
		}
		
		String zip
			= columns[ 4 ];
		
		if ( ! isEmpty( zip ) ) {
			zip = "" + Double.valueOf( zip.trim() ).intValue();
			logger.info( "[zip={}]", zip );
			organisatie.getAddress().setZipCode( zip );	
		}
		
		String gemeente
			= columns[ 5 ];
		
		logger.info( "[gemeente={}]", gemeente );
		organisatie.getAddress().setGemeente( gemeente );
		
		String telefoon
			= columns[ 6 ];
		
		if ( ! isEmpty( telefoon ) ) {
			
			telefoon = telefoon.trim().replace(" ", "");
			
			if ( telefoon.startsWith( "04" ) ) {
				
				logger.info( "[telefoon is gsm = {}]", telefoon );
				organisatie.setPhone( telefoon );
			}
			else {
				logger.info( "[telefoon={}]", telefoon );
				organisatie.setPhone( telefoon );
			}
			
		}
		
		String gsm
			 = columns[ 7 ];
		
		if ( ! isEmpty( gsm ) ) {
			
			gsm = gsm.trim().replace(" ", "");
			
			logger.info( "[gsm={}]", gsm );
			organisatie.setPhone( gsm.trim() );	
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
	
	protected final Comparator<Organisation> comparator( final String param ) {
		
		switch( param ) {
		
		case "city": 
				return new Comparator<Organisation>() {
					@Override
					public int compare(Organisation o1, Organisation o2) {
						Address a1 = o1.getAddress();
						Address a2 = o2.getAddress();
						if ( ( a1 == null ) || ( a1.getGemeente() == null ) ) {
							return -1;
						}
						
						if ( ( a2 == null ) || ( a2.getGemeente() == null ) ) {
							return 1;
						}
						
						return a1.getGemeente().compareTo( a2.getGemeente() );
					} };
		case "street": 
			return new Comparator<Organisation>() {
				@Override
				public int compare(Organisation o1, Organisation o2) {
					Address a1 = o1.getAddress();
					Address a2 = o2.getAddress();
					if ( ( a1 == null ) || ( a1.getStraat() == null ) ) {
						return -1;
					}
					
					if ( ( a2 == null ) || ( a2.getStraat() == null ) ) {
						return 1;
					}
					return a1.getStraat().compareTo( a2.getStraat() );
				} };
		default:
			return new Comparator<Organisation>() {
				@Override
				public int compare(Organisation o1, Organisation o2) {
					
					if ( ( o1.getName() == null ) ) {
						return -1;
					}
					
					if ( ( o2.getName() == null ) ) {
						return 1;
					}
					
					return o1.getName().compareTo( o2.getName() );
				} };	
		}
		
	}
		
}

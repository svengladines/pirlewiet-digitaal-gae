package be.pirlewiet.registrations.web.controllers;

import static be.occam.utils.spring.web.Controller.response;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
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
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.ModelAndView;

import be.occam.utils.timing.Timing;
import be.pirlewiet.registrations.domain.BuitenWipper;
import be.pirlewiet.registrations.domain.SecretariaatsMedewerker;
import be.pirlewiet.registrations.model.Deelnemer;
import be.pirlewiet.registrations.model.InschrijvingX;
import be.pirlewiet.registrations.model.Organisatie;
import be.pirlewiet.registrations.model.Status;
import be.pirlewiet.registrations.utils.PirlewietUtil;

@Controller
@RequestMapping( {"/inschrijvingen"} )
public class InschrijvingenController {
	
	protected Logger logger 
		= LoggerFactory.getLogger( this.getClass() );
	
	@Resource
	SecretariaatsMedewerker secretariaatsMedewerker;
	
	@Resource
	BuitenWipper buitenWipper;
	
	@RequestMapping( method = { RequestMethod.GET }, produces={"application/json","text/xml"} )
	@ResponseBody
	public ResponseEntity<List<InschrijvingX>> get( WebRequest request, @CookieValue(required=true, value="pwtid") String pwtid ) {
		
		List<InschrijvingX> inschrijvingen
			= new ArrayList<InschrijvingX>( );
		
		Organisatie organisatie
			= this.organisatie( request, pwtid );
		
		inschrijvingen.addAll( this.secretariaatsMedewerker.guard().actueleInschrijvingen( organisatie ) );
		
		return response( inschrijvingen, HttpStatus.OK );
		
	}
	
	@RequestMapping( method = { RequestMethod.POST } )
	@ResponseBody
	public ResponseEntity<InschrijvingX> post(
				@RequestBody InschrijvingX inschrijving, WebRequest request, @CookieValue(required=true, value="pwtid") String pwtid ) {
		
		Organisatie organisatie
			= this.organisatie( request, pwtid );
		
		inschrijving.setOrganisatie( organisatie );
		
		InschrijvingX aangemaakt
			= this.secretariaatsMedewerker.guard().ontvangInschrijving( inschrijving );
		
		if ( aangemaakt == null ) {
			throw new RuntimeException("create failed");
		}
		
		return response( aangemaakt, HttpStatus.CREATED );
			
	}
	
	@RequestMapping( value="/download", method = { RequestMethod.GET }, produces={ "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet" } )
	public ResponseEntity<byte[]> download( @CookieValue(required=true, value="pwtid") String pwtid ) {
		

		Organisatie organisatie
			= this.buitenWipper.guard().whoHasID( Long.valueOf( pwtid ) );
		
		// TODO: check organisatie != null
		
		List<InschrijvingX> inschrijvingen 
			= this.secretariaatsMedewerker.guard().actueleInschrijvingen( organisatie );
		
		List<String[]> mapped
			= mapTo( inschrijvingen );

		byte[] result 
			= asBytes( mapped );
	
		String disp
			= new StringBuilder("attachment; filename=_").append( "pirliwiet-digitaal" ).append( Timing.date(new Date(), Timing.dateFormat ) ).append( ".xlsx" ).toString();
	
		Map<String,String> headers
			= new HashMap<String,String>();
		
		headers.put( "Content-Disposition", disp.toString() );
	
		return response( result, HttpStatus.OK, headers );
		
	}
	
	@RequestMapping( method = { RequestMethod.GET }, produces={ MediaType.TEXT_HTML_VALUE } )
	public ModelAndView view( @CookieValue(required=true, value="pwtid") String pwtid ) {
		
		Organisatie organisatie
			= this.buitenWipper.guard().whoHasID( Long.valueOf( pwtid ) );

		Map<String,Object> model
			= new HashMap<String,Object>();
	
		model.put( "organisatie", organisatie );
	
		if ( PirlewietUtil.isPirlewiet( organisatie ) ) {
			
			List<InschrijvingX> inschrijvingen 
				= this.secretariaatsMedewerker.guard().actueleInschrijvingen( organisatie );
			
			List<InschrijvingX> submitted
				= new ArrayList<InschrijvingX>();
		
			List<InschrijvingX> transit
				= new ArrayList<InschrijvingX>();
			
			List<InschrijvingX> accepted
				= new ArrayList<InschrijvingX>();
			
			logger.info( "number of enrollments: [{}]", inschrijvingen.size() );
			
			for ( InschrijvingX inschrijving : inschrijvingen ) {
				
				switch( inschrijving.getStatus().getValue() ) {
				case SUBMITTED: 
					submitted.add( inschrijving );
					logger.info( "added a submitted inschrijving: [{}]", inschrijving.getId() );
					break;
				case TRANSIT:
					transit.add( inschrijving );
					break;
				default:
					logger.info( "inschrijving with unsupported status: [{}]", inschrijving.getStatus() );
					break;
						
				}
				
			}
		
			model.put( "submitted", submitted );
			model.put( "transit", transit );
		}
		else {
			List<InschrijvingX> inschrijvingen 
				= this.secretariaatsMedewerker.guard().actueleInschrijvingen( organisatie );
			model.put( "inschrijvingen", inschrijvingen );	
		}
		
	
		String view
			= PirlewietUtil.isPirlewiet( organisatie ) ? "inschrijvingen_pirlewiet" : "inschrijvingen";
		
		return new ModelAndView( view, model );
		
	}
	
	
	
	@ExceptionHandler(Exception.class)
	public ResponseEntity<String> handleError( Exception e ){
		
		logger.warn( "failure while handling request", e );
		return response( e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR );
		
	}
	
	protected Organisatie organisatie( WebRequest request, String pwtid ) {
		
		Organisatie organisatie
			= new Organisatie();
		organisatie.setId( Long.valueOf( pwtid ) );
		
		return organisatie;
		
	}
	
	protected List<String[]> mapTo( Collection<InschrijvingX> inschrijvingen ) {
		
		try {
			
			List<String[]> mapped
				= new ArrayList<String[]>( inschrijvingen.size() );
			
			for ( InschrijvingX inschrijving : inschrijvingen ) {
				
				try {
				
					String[] columns
						= new String[ 6 ];
					
					Deelnemer deelnemer
						= inschrijving.getDeelnemers().get( 0 );
					
					columns[ 0 ] = Timing.date( inschrijving.getInschrijvingsdatum(), Timing.dateFormat );
					columns[ 1 ] = deelnemer.getVoorNaam();
					columns[ 2 ] = deelnemer.getFamilieNaam();
					columns[ 3 ] = Timing.date( deelnemer.getGeboorteDatum(), Timing.dateFormat );
					columns[ 4 ] = deelnemer.getEmail();
					// columns[ 5 ] = inschrijving.getVakantieDetails();
					/*
					Adres adres
						= inschrijving.getAdres();
					columns[ 6 ] = adres.getStraat();
					columns[ 7 ] = adres.getNummer();
					columns[ 8 ] = adres.getGemeente();
					*/
					mapped.add( columns );
				}
				catch( Exception e ) {
					logger.warn( "failed to map inschrijving", e );
				}
				
			}

			return mapped;
			
		}
		catch( Exception e ) {
			
			logger.warn("Er is een probleem; could not map inschrijving", e );
			return null;
		}
		
	}
	
	public byte[] asBytes(List<String[]> data, String... headers) {

		try {

			Workbook book = new XSSFWorkbook();

			Sheet sheet = book.createSheet();

			int offset = 0;

			if (headers.length > 0) {

				Row row = sheet.createRow(offset++);

				for (int i = 0; i < headers.length; i++) {

					Cell c = row.createCell(i);

					c.setCellValue(headers[i]);

				}

			}

			for (int r = 0; r < data.size(); r++) {

				String[] cols = data.get(r);

				Row row = sheet.createRow(offset + r);

				for (int c = 0; c < cols.length; c++) {

					Cell cell = row.createCell(c);

					cell.setCellValue(cols[c]);

				}

			}

			// write to buffer
			ByteArrayOutputStream bos = new ByteArrayOutputStream();
			book.write(bos);

			// returned buffered byte array
			return bos.toByteArray();

		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	
}

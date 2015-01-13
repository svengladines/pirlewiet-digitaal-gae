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
import be.pirlewiet.registrations.model.Geslacht;
import be.pirlewiet.registrations.model.InschrijvingX;
import be.pirlewiet.registrations.model.Organisatie;
import be.pirlewiet.registrations.model.Vakantie;
import be.pirlewiet.registrations.model.Vraag;
import be.pirlewiet.registrations.model.Vraag.Type;
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
			= this.buitenWipper.guard().whoHasID( pwtid  );
		
		// TODO: check organisatie != null
		
		List<InschrijvingX> inschrijvingen 
			= this.secretariaatsMedewerker.guard().actueleInschrijvingen( organisatie );
		
		List<String[]> mapped
			= mapTo( inschrijvingen );

		byte[] result 
			= asBytes( mapped );
	
		String disp
			= new StringBuilder("attachment; filename=_").append( "pirlewiet-digitaal" ).append( Timing.date(new Date(), Timing.dateFormat ) ).append( ".xlsx" ).toString();
	
		Map<String,String> headers
			= new HashMap<String,String>();
		
		headers.put( "Content-Disposition", disp.toString() );
	
		return response( result, HttpStatus.OK, headers );
		
	}
	
	@RequestMapping( method = { RequestMethod.GET }, produces={ MediaType.TEXT_HTML_VALUE } )
	public ModelAndView view( @CookieValue(required=true, value="pwtid") String pwtid ) {
		
		Organisatie organisatie
			= this.buitenWipper.guard().whoHasID( pwtid  );

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
			
			List<InschrijvingX> waiting
				= new ArrayList<InschrijvingX>();
			
			List<InschrijvingX> rejected
				= new ArrayList<InschrijvingX>();
			
			List<InschrijvingX> cancelled
				= new ArrayList<InschrijvingX>();
			
			List<InschrijvingX> accepted
				= new ArrayList<InschrijvingX>();
			
			logger.info( "number of enrollments: [{}]", inschrijvingen.size() );
			
			for ( InschrijvingX inschrijving : inschrijvingen ) {
				
				switch( inschrijving.getStatus().getValue() ) {
				case SUBMITTED: 
					submitted.add( inschrijving );
					logger.info( "added a submitted inschrijving: [{}]", inschrijving.getUuid() );
					break;
				case TRANSIT:
					transit.add( inschrijving );
					break;
				case WAITINGLIST:
					waiting.add( inschrijving );
					break;
				case REJECTED:
					rejected.add( inschrijving );
					break;
				case CANCELLED:
					cancelled.add( inschrijving );
					break;
				case ACCEPTED:
					accepted.add( inschrijving );
					break;
				default:
					logger.info( "inschrijving with unsupported status: [{}]", inschrijving.getStatus() );
					break;
						
				}
				
			}
		
			model.put( "submitted", submitted );
			model.put( "transit", transit );
			model.put( "waiting", waiting );
			model.put( "rejected", rejected );
			model.put( "cancelled", cancelled );
			model.put( "accepted", accepted );
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
		organisatie.setUuid( pwtid );
		
		return organisatie;
		
	}
	
	protected List<String[]> mapTo( Collection<InschrijvingX> inschrijvingen ) {
		
		List<String[]> mapped
			= new ArrayList<String[]>( inschrijvingen.size() );
		
		try {
			
			for ( InschrijvingX inschrijving : inschrijvingen ) {
				
				try {
					
					//INSCHRIJVINGSDATUM	VOORNAAM	NAAM	M/V	GEBOORTEDATUM	ADRES	POSTCODE	GEMEENTE	TEL/GSM	E-MAIL	
					// NAAM DIENST	CONTACTPERSOON DIENST	ADRES DIENST TEL/GSM DIENST	E-MAIL DIENST
				
					List<String> columns
						= new ArrayList<String>( 16 );
					
					Deelnemer deelnemer
						= inschrijving.getDeelnemers().get( 0 );
					
					Organisatie organisation
						= inschrijving.getOrganisatie();
					
					columns.add( Timing.date( inschrijving.getInschrijvingsdatum(), Timing.dateFormat ) );
					columns.add( deelnemer.getVoorNaam() );
					columns.add( deelnemer.getFamilieNaam() );
					columns.add( deelnemer.getGeslacht().equals( Geslacht.M ) ? "M" : "V" );
					columns.add( Timing.date( deelnemer.getGeboorteDatum(), Timing.dateFormat ) );
					columns.add( new StringBuilder().append( inschrijving.getAdres().getStraat() ).append( " " ).append( inschrijving.getAdres().getNummer() ).toString() );
					columns.add( inschrijving.getAdres().getZipCode() );
					columns.add( inschrijving.getAdres().getGemeente() );
					columns.add( isEmpty( deelnemer.getMobielNummer() ) ? deelnemer.getTelefoonNummer() : deelnemer.getMobielNummer()); 
					columns.add( deelnemer.getEmail());
					
					columns.add( organisation.getNaam());
					columns.add( inschrijving.getContactGegevens().getNaam());
					columns.add( new StringBuilder().append( organisation.getAdres().getStraat() ).append( " " ).append( organisation.getAdres().getNummer() ).append( ",").append( organisation.getAdres().getZipCode() ).append( " ").append( organisation.getAdres().getGemeente() ).toString());
					columns.add( isEmpty( organisation.getGsmNummer() ) ? organisation.getTelefoonNummer() : organisation.getGsmNummer());
					columns.add( organisation.getEmail());
					
					
					
					// CONTACT VIA	FACTUUR	
					
					// HUISARTS	TEL HUISARTS
					
					// SPORT	SPEL	WANDELEN	FIETSEN	ZWEMMEN	ROKEN	AANDACHTSPUNTEN	GENEESMIDDELEN	FOTO'S	NAAM GEZIN	KEUZE VAKANTIE
					
					List<Vraag> vragen
						= inschrijving.getVragen();
					
					columns.add( antwoord( vragen.get( 1 ) ) );
					columns.add( antwoord( vragen.get( 0 ) ) );
					
					columns.add( antwoord( vragen.get( 4 ) ) );
					columns.add( antwoord( vragen.get( 5 ) ) );
					
					columns.add( antwoord( vragen.get( 7 ) ) );
					columns.add( antwoord( vragen.get( 8 ) ) );
					columns.add( antwoord( vragen.get( 9 ) ) );
					columns.add( antwoord( vragen.get( 10 ) ) );
					columns.add( antwoord( vragen.get( 11 ) ) );
					columns.add( antwoord( vragen.get( 12 ) ) );
					columns.add( antwoord( vragen.get( 13 ) ) );
					columns.add( antwoord( vragen.get( 14 ) ) );
					columns.add( antwoord( vragen.get( 15 ) ) );
					
					columns.add( antwoord( vragen.get( 3 ) ) );
					
					columns.add( antwoord( vragen.get( 2 ) ) );
					
					StringBuilder b
						= new StringBuilder("");
					
					for ( Vakantie v : inschrijving.getVakanties() ) {
						
						if ( b.length() > 0 ) {
							b.append( ",");
						}
						
						b.append( v.getNaam() );
						
					}
					
					columns.add( b.toString() );
					
					mapped.add( columns.toArray( new String[] {} ) );
				}
				catch( Exception e ) {
					logger.warn( "failed to map inschrijving", e );
				}
				
			}

		}
		catch( Exception e ) {
			
			logger.warn("Er is een probleem; could not map inschrijving", e );
			
		}
		
		return mapped;
		
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

	protected boolean isEmpty( String x ) {
	    	
		return ( x == null ) || ( x.isEmpty() );
	    	
	}
	
	protected String antwoord( Vraag vraag ) {
		
		String antwoord 
			= "";
		
		if ( Type.Text.equals( vraag.getType() ) ) {
			
			antwoord = isEmpty( vraag.getAntwoord() ) ? "?T" : vraag.getAntwoord().trim();
			
		}
		else if ( Type.YesNo.equals( vraag.getType() ) ) {
			
			if ( vraag.getAntwoord().equals( "Y") ) {
				antwoord = "Ja";
			}
			else if ( vraag.getAntwoord().equals( "N") ) {
				antwoord = "Nee";
			} 
			else {
				antwoord = "?YN";
			}
			
		}
		else if ( Type.Area.equals( vraag.getType() ) ) {
			
			antwoord = isEmpty( vraag.getAntwoord() ) ? "?A" : vraag.getAntwoord().trim();
			
		}
		else {
			antwoord = vraag.getVraag();
		}
		
		return antwoord;
		// return vraag.getVraag();
		
	}
	
}

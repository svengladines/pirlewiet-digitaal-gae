package be.pirlewiet.digitaal.domain.people;

import static be.occam.utils.javax.Utils.isEmpty;
import static be.occam.utils.javax.Utils.list;
import static be.occam.utils.javax.Utils.trim;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.multipart.MultipartFile;

import be.pirlewiet.digitaal.domain.exception.ErrorCodes;
import be.pirlewiet.digitaal.domain.exception.PirlewietException;
import be.pirlewiet.digitaal.model.Address;
import be.pirlewiet.digitaal.model.Organisation;
import be.pirlewiet.digitaal.web.util.ExcelImporter;
import be.pirlewiet.digitaal.web.util.Tuple;

public class Excelsior {
	
	protected final Logger logger
		= LoggerFactory.getLogger( this.getClass() );
	
	@Resource 
	protected final ExcelImporter excelImporter
		= new ExcelImporter();
	
	public List<String[]> toRows( MultipartFile file ) {
		
		List<String[]> rows = new ArrayList<String[]>();

		try {
			
			rows = this.excelImporter.getExcelData( file,1,1,2,3,4,5,6,7,8,9,10 );
			
		} catch ( Exception e ) {
			
			logger.info( "bugger", e );
			
			throw new PirlewietException( ErrorCodes.INTERNAL );
			
		}
		
		return rows;
		
		
	}
	
	public List<Tuple<Organisation,Address>> toOrganisations( List<String[]> rows ) {
		
		List<Tuple<Organisation,Address>> tuples
			= list();
		
		for ( String[] row : rows ) {
			
			Tuple<Organisation,Address> t
				= this.mapTo( row );
			
			tuples.add( t );
			
		}
		
		return tuples;
		
		
	}
	
	protected Tuple<Organisation,Address> mapTo( String[] row ) {
		
		// code	email	gemeente	gsmNummer	naam	nummer	straat	telefoonNummer	uuid	zipCode
		
		try {
			
			Organisation organisation
				= new Organisation();
			
			Address address
				= new Address();
			
			if  (row.length > 0 ) {
				organisation.setCode( trim( row[0] ) );
			}
			
			if  (row.length > 1 ) {
				organisation.setEmail( trim( row[1] ) );
			}
			
			if  (row.length > 2 ) {
				organisation.setCity( trim( row[2] ) );
				address.setCity( trim( row[2] ) );
			}
			
			if  (row.length > 4 ) {
				organisation.setName( trim( row[4] ) );
			}
			
			if  (row.length > 5 ) {
				address.setNumber( trim( row[5] ) );
			}

			if  (row.length > 6 ) {
				address.setStreet( trim( row[6] ) );
			}
			
			if  (row.length > 7 ) {
				String mobile = trim( row[3] );
				String fixed = trim( row[7] );
				
				if ( isEmpty( fixed ) ) {
					
					if ( isEmpty( mobile ) ) {
						organisation.setPhone( "?" );
					}
					else {
						organisation.setPhone( mobile );
					}
				}
				else {
					organisation.setPhone( fixed );
				}
			}
			
			if  (row.length > 8 ) {
				// don't set uuid
				
			}
			
			if  (row.length > 9 ) {
				address.setZipCode( trim( row[9] ) );
			}
			
			Tuple<Organisation,Address> tuple
				= new Tuple<Organisation,Address>(organisation,address); 

			return tuple;
			
		}
		catch( Exception e ) {
			throw new PirlewietException( ErrorCodes.INTERNAL, e.getMessage() );
		}
		
	}
	
	/*
	
	public List<String[]> asStrings( Enrollment application, Collection<Enrollment> related, EnrollmentStatus.Value status ) {
		
		List<String[]> mapped
			= new ArrayList<String[]>( related.size() + 1 );
			
		List<Enrollment> all
			= new ArrayList<Enrollment>( related.size() + 1 );
		
		all.add( application );
		all.addAll( related );
		
		try {
			
			if ( status != null ) {
				if ( ! status.equals( application.getStatus().getValue() ) ) {
					return mapped;
				}
			}
			
			for ( Enrollment enrollment : all ) {
				
				try {
					
					//INSCHRIJVINGSDATUM, VOORNAAM, NAAM, M/V, GEBOORTEDATUM, ADRES	POSTCODE	GEMEENTE	TEL/GSM	E-MAIL	
					// NAAM DIENST	CONTACTPERSOON DIENST	ADRES DIENST TEL/GSM DIENST	E-MAIL DIENST
				
					List<String> columns
						= new ArrayList<String>( 16 );
					
					Participant deelnemer
						= enrollment.getDeelnemers().get( 0 );
					
					Organisation organisation
						= application.getOrganisatie();
					
					if ( enrollment.getInschrijvingsdatum() != null ) {
						columns.add( Timing.date( enrollment.getInschrijvingsdatum(), Timing.dateFormat ) );
					}
					else {
						columns.add( "?" );
					}
					
					if ( deelnemer != null ) {
						columns.add( deelnemer.getVoorNaam() );
						columns.add( deelnemer.getFamilieNaam() );
						columns.add( deelnemer.getGeslacht() != null ? deelnemer.getGeslacht().name() : "?" );
						columns.add( deelnemer.getGeboorteDatum() != null ? Timing.date( deelnemer.getGeboorteDatum(), Timing.dateFormat ) : "?" );
					}
					
					Address adres
						= enrollment.getAdres();
					
					if ( adres != null ) {
						
						columns.add( new StringBuilder().append( adres.getStraat() ).append( " " ).append( adres.getNummer() ).toString() );
						columns.add( adres.getZipCode() );
						columns.add( adres.getGemeente() );
					}
					
					if ( deelnemer != null ) {
						columns.add( isEmpty( deelnemer.getMobielNummer() ) ? deelnemer.getTelefoonNummer() : deelnemer.getMobielNummer()); 
						columns.add( deelnemer.getEmail());
					}
					
					columns.add( organisation.getNaam());
					PersonInfo contact
						= application.getContactGegevens();
					
					if ( contact != null ) {
						columns.add( application.getContactGegevens().getName() );
						columns.add( new StringBuilder().append( organisation.getAdres().getStraat() ).append( " " ).append( organisation.getAdres().getNummer() ).append( ",").append( organisation.getAdres().getZipCode() ).append( " ").append( organisation.getAdres().getGemeente() ).toString());
						columns.add( contact.getPhone() );
						columns.add( contact.getEmail());
					}
					
					// CONTACT VIA	FACTUUR	
					
					// HUISARTS	TEL HUISARTS
					
					// SPORT	SPEL	WANDELEN	FIETSEN	ZWEMMEN	ROKEN	AANDACHTSPUNTEN	GENEESMIDDELEN	FOTO'S	NAAM GEZIN	KEUZE VAKANTIE
					
					List<QuestionAndAnswer> applicationQuestions
						= application.getVragen();
					
					List<QuestionAndAnswer> enrollmentQuestions
						= application.getVragen();
					
					QuestionSheet appQList
						= new QuestionSheet( applicationQuestions );
					
					QuestionSheet eQList
						= new QuestionSheet( enrollmentQuestions );
					
					// Q = contact
					columns.add( antwoord( appQList.getVraag( QIDs.QID_SHARED_CONTACT ) ) );
					// R = bill
					columns.add( antwoord( appQList.getVraag( QIDs.QID_SHARED_BILL ) ) );
					// S = medic
					columns.add( antwoord( eQList.getVraag( QIDs.QID_MEDIC_MEDIC ) ) );
					// T = medic tel
					columns.add( antwoord( eQList.getVraag( QIDs.QID_MEDIC_MEDIC_TEL ) ) );
					// U = sports
					columns.add( antwoord( eQList.getVraag( QIDs.QID_MEDIC_SPORTS) ) );
					// V = game
					columns.add( antwoord( eQList.getVraag( QIDs.QID_MEDIC_GAME ) ) );
					// W = wandelen
					columns.add( antwoord( eQList.getVraag( QIDs.QID_MEDIC_HIKE ) ) );
					// X = fietsen 
					columns.add( antwoord( eQList.getVraag( QIDs.QID_MEDIC_BIKE ) ) );
					// Y = zwemmen
					columns.add( antwoord( eQList.getVraag( QIDs.QID_MEDIC_SWIM ) ) );
					// Z = roken
					columns.add( antwoord( eQList.getVraag( QIDs.QID_MEDIC_SMOKE ) ) );
					// AA = aandachtspunten = 10
					columns.add( antwoord( eQList.getVraag( QIDs.QID_MEDIC_REMARKS ) ) );
					// AB = medicijnen = 11
					columns.add( antwoord( eQList.getVraag( QIDs.QID_MEDIC_MEDICINS ) ) );
					// AC = foto's = 0
					columns.add( antwoord( appQList.getVraag( QIDs.QID_SHARED_PHOTO ) ) );
					// AD = naam gezin = ?
					columns.add( "/" );
					// AE = eerder meegeweest ?
					columns.add( antwoord( eQList.getVraag( QIDs.QID_HISTORY ) ) );
					
					StringBuilder b
						= new StringBuilder("");
					
					for ( Holiday v : application.getVakanties() ) {
						
						if ( b.length() > 0 ) {
							b.append( ",");
						}
						
						b.append( v.getNaam() );
						
					}
					
					columns.add( b.toString() );
					
					columns.add( enrollment.getStatus().getComment() );
					
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
	
	public List<String[]> asStrings( Collection<Organisation> organisations, boolean reduced ) {
		
		List<String[]> mapped
			= new ArrayList<String[]>( organisations.size() );
		
		try {
			
			for ( Organisation organisation : organisations ) {
				
				try {
					
					// NAAM DIENST ADRES TEL GSM E-MAIL
				
					List<String> columns
						= new ArrayList<String>( 8 );
					
					columns.add( organisation.getNaam() );
					
					Address adres
						= organisation.getAdres();
					
					if ( adres != null ) {
						
						columns.add( new StringBuilder().append( adres.getStraat() ).append( " " ).append( adres.getNummer() ).toString() );
						columns.add( adres.getZipCode() );
						columns.add( adres.getGemeente() );
					}
					
					
					columns.add( organisation.getTelefoonNummer() );
					columns.add( organisation.getGsmNummer() );
					
					columns.add( organisation.getEmail() );
					columns.add( organisation.getCode() );	
						
					mapped.add( columns.toArray( new String[] {} ) );
				}
				catch( Exception e ) {
					logger.warn( "failed to map organisation", e );
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
	
	protected String antwoord( QuestionAndAnswer vraag ) {
		
		String antwoord 
			= "?";
		
		if ( vraag == null ) {
			return antwoord;
		}
		
		if ( Type.Text.equals( vraag.getType() ) ) {
			
			antwoord = isEmpty( vraag.getAntwoord() ) ? "?" : vraag.getAntwoord().trim();
			
		}
		else if ( Type.YesNo.equals( vraag.getType() ) ) {
			
			if ( isEmpty( vraag.getAntwoord() ) ) {
				antwoord = "?";
			}
			else if ( vraag.getAntwoord().equals( "Y") ) {
				antwoord = "Ja";
			}
			else if ( vraag.getAntwoord().equals( "N") ) {
				antwoord = "Nee";
			} 
			else {
				antwoord = "?";
			}
			
		}
		else if ( Type.Area.equals( vraag.getType() ) ) {
			
			antwoord = isEmpty( vraag.getAntwoord() ) ? "?" : vraag.getAntwoord().trim();
			
		}
		else {
			antwoord = vraag.getVraag();
		}
		
		return antwoord;
		// return vraag.getVraag();
		
	}
	
	*/
	
}

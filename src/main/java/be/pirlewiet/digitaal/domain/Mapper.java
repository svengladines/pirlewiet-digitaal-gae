package be.pirlewiet.digitaal.domain;

import static be.pirlewiet.digitaal.web.util.Utils.isEmpty;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import be.occam.utils.timing.Timing;
import be.pirlewiet.digitaal.domain.q.QIDs;
import be.pirlewiet.digitaal.domain.q.QList;
import be.pirlewiet.digitaal.model.Address;
import be.pirlewiet.digitaal.model.Enrollment;
import be.pirlewiet.digitaal.model.EnrollmentStatus;
import be.pirlewiet.digitaal.model.Holiday;
import be.pirlewiet.digitaal.model.Organisation;
import be.pirlewiet.digitaal.model.Participant;
import be.pirlewiet.digitaal.model.PersonInfo;
import be.pirlewiet.digitaal.model.QnA;
import be.pirlewiet.digitaal.model.QnA.Type;

public class Mapper {
	
	protected final Logger logger
		= LoggerFactory.getLogger( this.getClass() );
	
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
					
					List<QnA> applicationQuestions
						= application.getVragen();
					
					List<QnA> enrollmentQuestions
						= application.getVragen();
					
					QList appQList
						= new QList( applicationQuestions );
					
					QList eQList
						= new QList( enrollmentQuestions );
					
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
	
	protected String antwoord( QnA vraag ) {
		
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
	
}

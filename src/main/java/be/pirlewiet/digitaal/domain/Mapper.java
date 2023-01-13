package be.pirlewiet.digitaal.domain;

import static be.occam.utils.javax.Utils.isEmpty;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.Collection;
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

import be.occam.utils.timing.Timing;
import be.pirlewiet.digitaal.domain.people.AddressManager;
import be.pirlewiet.digitaal.domain.people.DoorMan;
import be.pirlewiet.digitaal.domain.people.OrganisationManager;
import be.pirlewiet.digitaal.domain.people.PersonManager;
import be.pirlewiet.digitaal.domain.people.QuestionAndAnswerManager;
import be.pirlewiet.digitaal.domain.q.QIDs;
import be.pirlewiet.digitaal.domain.q.QuestionSheet;
import be.pirlewiet.digitaal.domain.service.EnrollmentService;
import be.pirlewiet.digitaal.domain.service.HolidayService;
import be.pirlewiet.digitaal.model.Address;
import be.pirlewiet.digitaal.model.Application;
import be.pirlewiet.digitaal.model.Enrollment;
import be.pirlewiet.digitaal.model.EnrollmentStatus;
import be.pirlewiet.digitaal.model.Gender;
import be.pirlewiet.digitaal.model.Organisation;
import be.pirlewiet.digitaal.model.Person;
import be.pirlewiet.digitaal.model.QuestionAndAnswer;
import be.pirlewiet.digitaal.model.QuestionType;
import be.pirlewiet.digitaal.model.Tags;


public class Mapper {
	
	protected final Logger logger
		= LoggerFactory.getLogger( this.getClass() );
	
	@Resource
	PersonManager personManager;
	
	@Resource
	AddressManager addressManager;
	
	@Resource
	OrganisationManager organisationManager;
	
	@Resource
	QuestionAndAnswerManager questionAndAnswerManager;
	
	@Resource
	HolidayService holidayService;
	
	@Resource
	EnrollmentService enrollmentService;
	
	@Resource
	DoorMan doorMan;
	
	public List<String[]> asStrings( 
			Application application, 
			Collection<Enrollment> enrollments,
			EnrollmentStatus.Value status,
			Map<String,Address> addressMap,
			Map<String,Person> personMap,
			Map<String,Organisation> organisationMap,
			Map<String,Map<String,List<QuestionAndAnswer>>> qnaMap
			) {
		
		List<String[]> mapped
			= new ArrayList<String[]>( enrollments.size() );
			
		List<Enrollment> all
			= new ArrayList<Enrollment>( enrollments.size() );
		
		all.addAll( enrollments );
		
		try {
			
			if ( status != null ) {
				if ( ! status.equals( application.getStatus().getValue() ) ) {
					return mapped;
				}
			}
			
			/*
			Map<String,QuestionAndAnswer> questionMap
				= Utils.map();
			*/
			
			Map<String,List<QuestionAndAnswer>> applicationQuestionsMap
				= qnaMap.get( application.getUuid() );
			
			List<QuestionAndAnswer> applicationQuestions
				= applicationQuestionsMap.get( Tags.TAG_APPLICATION );
				
				
			//= this.questionAndAnswerManager.findByEntityAndTag( , Tags.TAG_APPLICATION );
		
			// List<QuestionAndAnswer> enrollmentQuestions
			//	= this.questionAndAnswerManager.findByEntityAndTag( application.getUuid(), Tags.TAG_VARIOUS );
			
			//Organisation organisation
				// = this.organisationManager.findOneByUuid( application.getOrganisationUuid() );
			Organisation organisation
				= organisationMap.get( application.getOrganisationUuid() );
			
			Person contact
				// = this.personManager.findOneByUuid( application.getContactPersonUuid() );
				= personMap.get( application.getContactPersonUuid() );
			
			Organisation pirlewiet
				= this.doorMan.whoHasCode( "dig151" );
			
			for ( Enrollment enrollment : all ) {
				
				try {
					
					/*
					 * Cfr mail secretariaat 18/01/2017
					 * DATUM ÌNSCHRIJVING - VOORNAAM - NAAM - M/V - GEBOORTEDATUM - ADRES - POSTCODE - GEMEENTE - TEL/GSM - E-MAIL- NAAM DIENST - CONTACTPERSOON DIENST - ADRES DIENST - TEL/GSM DIENST - E-MAIL DIENST - CONTACT VIA DOORVERWIJZER? - FACTUUR - HUISARTS - TEL HUISARTS - SPORT - SPEL - WANDELEN - FIETSEN - ZWEMMEN - ROKEN - AANDACHTSPUNTEN - GENEESMIDDELEN - FOTO'S	- OPMERKINGEN
					 */

					List<String> columns
						= new ArrayList<String>( 21 );
					
					/**
					 *  Really bad!!!! TODO, fix this, but it's urgent!
					 */
					
					if ( isEmpty( enrollment.getHolidayName() ) ) {
						
						logger.warn( "enrollment [{}] does not have a holiday name", enrollment.getUuid() );
						/*
						Result<List<HolidayDTO>> holidaysResult
							= this.holidayService.resolve( enrollment.getHolidayUuid(), application.getHolidayUuids(), false, false, false, pirlewiet );
						
						List<HolidayDTO> holidays
							= holidaysResult.getObject();
						
						Result<EnrollmentDTO> updatedEnrollment 
							= this.enrollmentService.updateHolidays( enrollment.getUuid(), holidays, pirlewiet );
						
						enrollment.setHolidayName( updatedEnrollment.getObject().getHolidayName() );
						*/
					}
					
					columns.add( enrollment.getHolidayName() );
					
					Person participant
						= personMap.get( enrollment.getParticipantUuid() );
					
					if ( application.getSubmitted() != null ) {
						//  DATUM ÌNSCHRIJVING 
						columns.add( Timing.date( application.getSubmitted(), Timing.dateFormat ) );
					}
					else {
						columns.add( "?" );
					}
					
					if ( participant != null ) {
						// VOORNAAM
						columns.add( participant.getGivenName() );
						// NAAM
						columns.add( participant.getFamilyName() );
						// M/V
						columns.add( participant.getGender() != null ? map( participant.getGender() ) : "?" );
						// GEBOORTEDATUM
						columns.add( participant.getBirthDay()!= null ? Timing.date( participant.getBirthDay(), Timing.dateFormat ) : "?" );
					}
					
					Address address
						= addressMap.get( enrollment.getAddressUuid() );
					
					if ( address != null ) {
						
						// ADRES
						columns.add( new StringBuilder().append( address.getStreet() ).append( " " ).append( address.getNumber() ).toString() );
						// POSTCODE
						columns.add( address.getZipCode() );
						// GEMEENTE
						columns.add( address.getCity() );
					}
					
					// doorverwijzer/contactpersooon
					
					// NAAM DIENST - CONTACTPERSOON DIENST - ADRES DIENST - TEL/GSM DIENST - E-MAIL DIENST 
					
					if ( participant != null ) {
						// TEL/GSM
						columns.add( isEmpty( participant.getPhone() ) ? "?"  : participant.getPhone() );
						// E-MAIL
						columns.add( participant.getEmail());
						columns.add( isEmpty( participant.getStateNumber() ) ? "?"  : participant.getStateNumber() );
					}
					
					// NAAM DIENST
					columns.add( organisation.getName());
					
					if ( contact != null ) {
						// CONTACTPERSOON DIENST
						columns.add( String.format( "%s %s", contact.getGivenName(), contact.getFamilyName() ) );
						// ??? columns.add( new StringBuilder().append( organisation.getaddress().getStraat() ).append( " " ).append( organisation.getaddress().getNummer() ).append( ",").append( organisation.getaddress().getZipCode() ).append( " ").append( organisation.getaddress().getGemeente() ).toString());
					}
					
					Address organisationAddress
						= addressMap.get( organisation.getAddressUuid() );
				
					StringBuilder b 
						= new StringBuilder()
						.append( organisationAddress.getStreet() )
						.append( " " ).append( organisationAddress.getNumber() )
						.append( " " ).append( organisationAddress.getZipCode() )
						.append( " " ).append( organisationAddress.getCity() );
					columns.add( b.toString() );
					
					columns.add( contact.getPhone() );
					columns.add( contact.getEmail());

					// CONTACT VIA DOORVERWIJZER? - FACTUUR - HUISARTS - TEL HUISARTS - SPORT - SPEL - WANDELEN - FIETSEN - ZWEMMEN - ROKEN - AANDACHTSPUNTEN - GENEESMIDDELEN - FOTO'S	- OPMERKINGEN
					
					// CONTACT VIA	FACTUUR	
					
					// HUISARTS	TEL HUISARTS
					
					// SPORT	SPEL	WANDELEN	FIETSEN	ZWEMMEN	ROKEN	AANDACHTSPUNTEN	GENEESMIDDELEN	FOTO'S	NAAM GEZIN	KEUZE VAKANTIE
					
					Map<String,List<QuestionAndAnswer>> enrollmentQuestionsMap
						= qnaMap.get( enrollment.getUuid() );
					
					if ( enrollmentQuestionsMap == null ) {
						logger.warn( "no enrollment qna found for enrollment [{}], stopped mapping", enrollment.getUuid() );
						continue;
					}
				
					List<QuestionAndAnswer> medicQuestions
						// = this.questionAndAnswerManager.findByEntityAndTag( enrollment.getUuid(), Tags.TAG_MEDIC );
						= enrollmentQuestionsMap.get( Tags.TAG_MEDIC );
					
					List<QuestionAndAnswer> enrollmentVariousQuestions
					// = this.questionAndAnswerManager.findByEntityAndTag( enrollment.getUuid(), Tags.TAG_MEDIC );
						= enrollmentQuestionsMap.get( Tags.TAG_PARTICIPANT );
					
					QuestionSheet appQList
						= new QuestionSheet( applicationQuestions );
					
					QuestionSheet eQList
						 = new QuestionSheet( enrollmentVariousQuestions );
					
					QuestionSheet mQList
						= new QuestionSheet( medicQuestions );
					
					// Q = contact
					columns.add( answer( appQList.getQuestion( QIDs.QID_SHARED_CONTACT ) ) );
					// R = bill
					columns.add( answer( appQList.getQuestion( QIDs.QID_SHARED_BILL ) ) );
					
					// S = medic
					//columns.add( answer( mQList.getQuestion( QIDs.QID_MEDIC_MEDIC ) ) );
					// T = medic tel
					//columns.add( answer( mQList.getQuestion( QIDs.QID_MEDIC_MEDIC_TEL ) ) );
					// U = sports
					//columns.add( answer( mQList.getQuestion( QIDs.QID_MEDIC_SPORTS) ) );
					// V = game
					//columns.add( answer( mQList.getQuestion( QIDs.QID_MEDIC_GAME ) ) );
					// W = wandelen
					//columns.add( answer( mQList.getQuestion( QIDs.QID_MEDIC_HIKE ) ) );
					// X = fietsen 
					//columns.add( answer( mQList.getQuestion( QIDs.QID_MEDIC_BIKE ) ) );
					// Y = zwemmen
					//columns.add( answer( mQList.getQuestion( QIDs.QID_MEDIC_SWIM ) ) );
					// Z = roken
					//columns.add( answer( mQList.getQuestion( QIDs.QID_MEDIC_SMOKE ) ) );
					// AA = aandachtspunten = 10
					columns.add( answer( mQList.getQuestion( QIDs.QID_MEDIC_REMARKS ) ) );
					// AB = medicijnen = 11
					columns.add( answer( mQList.getQuestion( QIDs.QID_MEDIC_MEDICINS ) ) );
					// AC = foto's = 0
					columns.add( answer( appQList.getQuestion( QIDs.QID_SHARED_PHOTO ) ) );
					// AD = eerder meegeweest ?
					columns.add( answer( eQList.getQuestion( QIDs.QID_HISTORY ) ) );
					// AE = VOV Partner
					columns.add( answer( eQList.getQuestion( QIDs.QID_ADULTERY_WITH ) ) );
					// AF = VOV Partner
					columns.add( answer( eQList.getQuestion( QIDs.QID_ADULTERY_WITH_WHO ) ) );
					// AG = mobility - family car
					columns.add( answer( appQList.getQuestion( QIDs.QID_FAMILY_CAR ) ) );
					// AH = comment					
					columns.add( enrollment.getStatus().getComment() );
					
					mapped.add( columns.toArray( new String[] {} ) );
				}
				catch( Exception e ) {
					logger.warn( "failed to map enrollment", e );
				}
				
			}

		}
		catch( Exception e ) {
			
			logger.warn("Er is een probleem; could not map inschrijving", e );
			
		}
		
		return mapped;
		
	}
	

	/*
	public List<String[]> asStrings( Collection<Organisation> organisations, boolean reduced ) {
		
		List<String[]> mapped
			= new ArrayList<String[]>( organisations.size() );
			
		
		try {
			
			for ( Organisation organisation : organisations ) {
				
				try {
					
					// NAAM DIENST address TEL GSM E-MAIL
				
					List<String> columns
						= new ArrayList<String>( 8 );
					
					columns.add( organisation.getName() );
					
					Address organisastionAddress
						= this.adderssManager.findOneByUuid( organisation.getAddressUuid() );
					
					if ( address != null ) {
						
						columns.add( new StringBuilder().append( address.getStraat() ).append( " " ).append( address.getNummer() ).toString() );
						columns.add( address.getZipCode() );
						columns.add( address.getGemeente() );
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
	
	*/
	
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
	
	protected String answer( QuestionAndAnswer qna ) {
		
		String antwoord 
			= "?";
		
		
		if ( qna == null ) {
			return antwoord;
		}
		
		if ( QuestionType.Text.equals( qna.getType() ) ) {
			
			antwoord = isEmpty( qna.getAnswer() ) ? "?" : qna.getAnswer().trim();
			
		}
		else if ( QuestionType.YesNo.equals( qna.getType() ) ) {
			
			if ( isEmpty( qna.getAnswer() ) ) {
				antwoord = "?";
			}
			else if ( qna.getAnswer().equals( "Y") ) {
				antwoord = "Ja";
			}
			else if ( qna.getAnswer().equals( "N") ) {
				antwoord = "Nee";
			} 
			else {
				antwoord = "?";
			}
			
		}
		else if ( QuestionType.Area.equals( qna.getType() ) ) {
			
			antwoord = isEmpty( qna.getAnswer() ) ? "?" : qna.getAnswer().trim();
			
		}
		else if ( QuestionType.MC.equals( qna.getType() ) ) {
			
			if ( isEmpty( qna.getAnswer() ) ) {
				antwoord = "/";
			}
			else {
				antwoord = qna.getAnswer().trim();
			}
			
		}
		else {
			antwoord = qna.getQuestion();
		}
		
		return antwoord;
		// return qna.getQuestion();
		
	}
	
	protected String map( final Gender gender ) {
		
		String value
			= "";
		
		switch( gender ) {
		
		case M: value = "m";
				break;
		case F: value = "v";
				break;
		default: value = "?";
				break;
		}
		
		return value;
		
	} 
	
	
}

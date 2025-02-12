package be.pirlewiet.digitaal.domain;

import static be.occam.utils.javax.Utils.isEmpty;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import be.occam.utils.javax.Utils;
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
import org.springframework.stereotype.Component;

@Component
public class Mapper {
	
	protected final Logger logger
		= LoggerFactory.getLogger( this.getClass() );
	
	@Autowired
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
			
			
			Map<String,List<QuestionAndAnswer>> applicationQuestionsMap = qnaMap.get( application.getUuid() );
			
			List<QuestionAndAnswer> applicationQuestions = applicationQuestionsMap.get( Tags.TAG_APPLICATION );
				
			Organisation organisation = organisationMap.get( application.getOrganisationUuid() );
			
			Person applicant = personMap.get( application.getContactPersonUuid() );
			
			Organisation pirlewiet = this.doorMan.whoHasCode( "dig151" );
			
			for ( Enrollment enrollment : all ) {
				
				try {
					
					List<String> columns = new ArrayList<String>( 30 );

					Person participant = personMap.get( enrollment.getParticipantUuid() );

					// A|
					columns.add(isEmpty(enrollment.getHolidayName()) ? "?" : enrollment.getHolidayName());
					// B|
					columns.add( ( application.getSubmitted() != null ) ? Timing.date( application.getSubmitted(), Timing.dateFormat ) : "?" );

					if ( participant != null ) {
						// C|
						columns.add( participant.getGivenName() );
						// D|
						columns.add( participant.getFamilyName() );
						// E|
						columns.add( participant.getGender() != null ? map( participant.getGender() ) : "?" );
						// F|
						columns.add( participant.getBirthDay()!= null ? Timing.date( participant.getBirthDay(), Timing.dateFormat ) : "?" );
					}
					
					Address participantAddress = addressMap.get( enrollment.getAddressUuid() );
					
					if ( participantAddress != null ) {
						// G| participant street
						columns.add( new StringBuilder().append( participantAddress.getStreet() ).append( " " ).append( participantAddress.getNumber() ).toString() );
						// H| participant zip
						columns.add( participantAddress.getZipCode() );
						// I| participant city
						columns.add( participantAddress.getCity() );
					}
					
					// J| participant phone
					columns.add( isEmpty( participant.getPhone() ) ? "?"  : participant.getPhone() );
					// K| participant email
					columns.add( isEmpty( participant.getEmail() ) ? "?"  : participant.getEmail());

					// L| organisation name
					columns.add( organisation.getName());
					Address organisationAddress = addressMap.get( organisation.getAddressUuid() );
					StringBuilder b = new StringBuilder()
							.append( organisationAddress.getStreet() )
							.append( " " ).append( organisationAddress.getNumber() )
							.append( " " ).append( organisationAddress.getZipCode() )
							.append( " " ).append( organisationAddress.getCity() );

					// M| organisation address
					columns.add( b.toString() );

					if ( applicant != null ) {
						// N| applicant name
						columns.add( String.format( "%s %s", applicant.getGivenName(), applicant.getFamilyName() ) );
					}
					// O| applicant phone
					columns.add( applicant.getPhone() );
					// P| applicant email
					columns.add( applicant.getEmail());

					Map<String,List<QuestionAndAnswer>> enrollmentQuestionsMap = qnaMap.get( enrollment.getUuid() );
					if ( enrollmentQuestionsMap == null ) {
						logger.warn( "no enrollment qna found for enrollment [{}], stopped mapping", enrollment.getUuid() );
						continue;
					}

					List<QuestionAndAnswer> participantQuestions = enrollmentQuestionsMap.get( Tags.TAG_PARTICIPANT );
					// = this.questionAndAnswerManager.findByEntityAndTag( enrollment.getUuid(), Tags.TAG_MEDIC );
					QuestionSheet appQList = new QuestionSheet( applicationQuestions );
					// Q = bill
					columns.add( answer( appQList.getQuestion( QIDs.QID_SHARED_BILL ) ) );
					// R = bill-detail
					columns.add( answer( appQList.getQuestion( QIDs.QID_SHARED_BILL_DETAIL ) ) );
					// S = contact naam
					columns.add( answer( appQList.getQuestion( QIDs.QID_SHARED_CONTACT ) ) );
					// T = contact telefoon
					columns.add( answer( appQList.getQuestion( QIDs.QID_SHARED_CONTACT_PHONE ) ) );
					// U| photo
					columns.add( answer( appQList.getQuestion( QIDs.QID_SHARED_PHOTO ) ) );
					/////// vragenlijst deelnemer
					QuestionSheet enrQList = new QuestionSheet( participantQuestions );
					// V| = eerder meegeweest ?
					columns.add( answer( enrQList.getQuestion( QIDs.QID_HISTORY ) ) );
					// W| = spreekt nederlands
					columns.add( answer( enrQList.getQuestion( QIDs.QID_MEDIC_DUTCH ) ) );
					// X| = mobility - family car
					columns.add( answer( enrQList.getQuestion( QIDs.QID_FAMILY_CAR ) ) );
					// Y| = aandachtspunten = 10
					columns.add( answer( enrQList.getQuestion( QIDs.QID_MEDIC_REMARKS ) ) );
					
					// Z = VOV Partner
					columns.add( answer( enrQList.getQuestion( QIDs.QID_ADULTERY_WITH ) ) );
					// AA = VOV Partner
					columns.add( answer( enrQList.getQuestion( QIDs.QID_ADULTERY_WITH_WHO ) ) );
					// AB = VOV zelvoorzienend
					columns.add( answer( appQList.getQuestion( QIDs.QID_ADULTERY_SELF_RELIANT) ) );
					// AC = comment
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
	
	public String[] headers() {
		
		List<String> headers = Utils.list();
		// A
		headers.add("Vakantie(s)");
		// B
		headers.add("Datum inschrijving");
		// deelnemer
		// C
		headers.add("Voornaam");
		// D
		headers.add("Naam");
		// E
		headers.add("Geslacht");
		// F
		headers.add("Geboortedatum");
		// G
		headers.add("Straat");
		// H
		headers.add("Postcode");
		// I
		headers.add("Gemeente");
		// J
		headers.add("Telefoon");
		// K
		headers.add("E-mail");
		// doorverwijzer
		// L
		headers.add("Doorverwijzer naam");
		// M
		headers.add("Doorverwijzer adres");
		// N|
		headers.add("Aanvrager naam");
		// O|
		headers.add("Aanvrager telefoon");
		// P|
		headers.add("Aanvrager e-mail");
		//////// vragenlijst dossier
		// Q|
		headers.add("Wie factuur?");
		// R|
		headers.add("Factuur naam?");
		// S|
		headers.add("Contactpersoon naam");
		// T|
		headers.add("Contactpersoon telefoon");
		// U|
		headers.add("Mogen foto's?");
		// vragenlijst deelnemer
		// V|
		headers.add("Meegeweest ?");
		// W|
		headers.add("Nederlands?");
		// X|
		headers.add("Vervoer");
		// Y|
		headers.add("Aandachtspunten");
		// Z|
		headers.add("(Vov) Met vriend(in) ?");
		// AA|
		headers.add("(Vov) Naam vriend(in)");
		// AB|
		headers.add("(Vov) Zelfvoorzienend?");
		// AC|
		headers.add("Commentaar");
		return headers.toArray( new String[] {} );
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

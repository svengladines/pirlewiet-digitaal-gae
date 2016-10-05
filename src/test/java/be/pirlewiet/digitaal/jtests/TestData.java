package be.pirlewiet.digitaal.jtests;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;

import be.occam.utils.timing.Timing;
import be.pirlewiet.digitaal.domain.people.Secretary;
import be.pirlewiet.digitaal.model.Participant;
import be.pirlewiet.digitaal.model.Gender;
import be.pirlewiet.digitaal.model.Enrollment;
import be.pirlewiet.digitaal.model.Organisation;
import be.pirlewiet.digitaal.model.Period;
import be.pirlewiet.digitaal.model.EnrollmentStatus;
import be.pirlewiet.digitaal.model.Holiday;
import be.pirlewiet.digitaal.model.HolidayType;
import be.pirlewiet.digitaal.repositories.EnrollmentRepository;
import be.pirlewiet.digitaal.repositories.OrganisatieRepository;
import be.pirlewiet.digitaal.repositories.PersoonRepository;
import be.pirlewiet.digitaal.repositories.VakantieRepository;

import com.google.appengine.api.datastore.KeyFactory;

public class TestData {
	
	public static class Ids {
		
		public static Long Z_KIKA_1 = 1L;
		public static Long Z_KIKA_2 = 2L;
		public static Long Z_TIKA_1 = 3L;
		
		public static Long lisa= 1L;
		
		public static Long IN_lisa_KIKA_1;
		
	}

	@Resource
	VakantieRepository vakantieRepository;
	
	@Resource
	PersoonRepository persoonRepository;
	
	@Resource
	Secretary secretariaatsMedewerker;
	
	@Resource
	OrganisatieRepository organsiatieRepository;
	
	@PostConstruct
	public void injectData() {
		
		Holiday zomerKikaEen
			= new Holiday();
		
		zomerKikaEen.setBeginDatum( Timing.date("05/09/2014") );
		zomerKikaEen.setEindDatum( Timing.date("10/09/2014") );
		zomerKikaEen.setEindInschrijving( Timing.date("30/08/2014") );
		zomerKikaEen.setType( HolidayType.Kika );
		zomerKikaEen.setJaar( 2014 );
		zomerKikaEen.setNaam( "KIKA 1");
		zomerKikaEen.setPeriode( Period.Summer );
		
		zomerKikaEen = this.vakantieRepository.saveAndFlush( zomerKikaEen );
		zomerKikaEen.setUuid( KeyFactory.keyToString( zomerKikaEen.getKey()  ) );
		zomerKikaEen = this.vakantieRepository.saveAndFlush( zomerKikaEen );
		
		Holiday zomerKikaTwee
			= new Holiday();
	
		// zomerKikaTwee.setId( Ids.Z_KIKA_2 );
		zomerKikaTwee.setBeginDatum( Timing.date("12/09/2014") );
		zomerKikaTwee.setEindDatum( Timing.date("18/09/2014") );
		zomerKikaTwee.setEindInschrijving( Timing.date("30/08/2014") );
		zomerKikaTwee.setType( HolidayType.Kika );
		zomerKikaTwee.setJaar( 2014 );
		zomerKikaTwee.setNaam( "KIKA 2");
		zomerKikaTwee.setPeriode( Period.Summer );
	
		this.vakantieRepository.saveAndFlush( zomerKikaTwee );
		zomerKikaTwee.setUuid( KeyFactory.keyToString( zomerKikaTwee.getKey()  ) );
		zomerKikaTwee = this.vakantieRepository.saveAndFlush( zomerKikaTwee );
		
		Participant lisa
			= new Participant();
		
		// lisa.setId( Ids.lisa );
		lisa.setVoorNaam( "Lisa");
		lisa.setFamilieNaam( "Simpson" );
		lisa.setEmail("lisa.simpson@springfield.net");
		lisa.setGeslacht( Gender.F );
		
		this.persoonRepository.saveAndFlush( lisa );
		
		Organisation pirlewiet
			= new Organisation();

		pirlewiet.setNaam("Pirlewiet");
		pirlewiet.setCode( "pwt001" );
		pirlewiet.setUuid( "pwt-uuid");
		pirlewiet = this.organsiatieRepository.saveAndFlush( pirlewiet );
		
		Organisation ocmw
			= new Organisation();
	
		ocmw.setNaam("OCMW Leuven");
		ocmw.setCode( "abc123" );
	
		ocmw = this.organsiatieRepository.saveAndFlush( ocmw );
		
		Enrollment lisaKika1
			= new Enrollment();
		
		lisaKika1.setVks( "1" );
		lisaKika1.getVakanties().add( zomerKikaEen );
		lisaKika1.getDeelnemers().add( lisa );
		lisaKika1.setOrganisatie( ocmw );
		lisaKika1.setStatus( new EnrollmentStatus ( EnrollmentStatus.Value.DRAFT ) );
		
		this.secretariaatsMedewerker.createEnrollment( lisaKika1 );
		
	}
	
}

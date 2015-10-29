package be.pirlewiet.registrations.jtests;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;

import be.occam.utils.timing.Timing;
import be.pirlewiet.registrations.model.Deelnemer;
import be.pirlewiet.registrations.model.Gender;
import be.pirlewiet.registrations.model.InschrijvingX;
import be.pirlewiet.registrations.model.Organisatie;
import be.pirlewiet.registrations.model.Period;
import be.pirlewiet.registrations.model.Status;
import be.pirlewiet.registrations.model.Vakantie;
import be.pirlewiet.registrations.model.VakantieType;
import be.pirlewiet.registrations.repositories.EnrollmentRepository;
import be.pirlewiet.registrations.repositories.OrganisatieRepository;
import be.pirlewiet.registrations.repositories.PersoonRepository;
import be.pirlewiet.registrations.repositories.VakantieRepository;

import com.google.appengine.api.datastore.KeyFactory;

public class TestData {
	
	public static class Ids {
		
		public static Long Z_KIKA_1 = 1L;
		public static Long Z_KIKA_2 = 2L;
		public static Long Z_TIKA_1 = 3L;
		
		public static Long SARAH= 1L;
		
		public static Long IN_SARAH_KIKA_1;
		
	}

	@Resource
	VakantieRepository vakantieRepository;
	
	@Resource
	PersoonRepository persoonRepository;
	
	@Resource
	EnrollmentRepository inschrijvingXRepository;
	
	@Resource
	OrganisatieRepository organsiatieRepository;
	
	@PostConstruct
	public void injectData() {
		
		Vakantie zomerKikaEen
			= new Vakantie();
		
		zomerKikaEen.setBeginDatum( Timing.date("05/09/2014") );
		zomerKikaEen.setEindDatum( Timing.date("10/09/2014") );
		zomerKikaEen.setEindInschrijving( Timing.date("30/08/2014") );
		zomerKikaEen.setType( VakantieType.Kika );
		zomerKikaEen.setJaar( 2014 );
		zomerKikaEen.setNaam( "KIKA 1");
		zomerKikaEen.setPeriode( Period.Summer );
		
		zomerKikaEen = this.vakantieRepository.saveAndFlush( zomerKikaEen );
		zomerKikaEen.setUuid( KeyFactory.keyToString( zomerKikaEen.getKey()  ) );
		zomerKikaEen = this.vakantieRepository.saveAndFlush( zomerKikaEen );
		
		Vakantie zomerKikaTwee
			= new Vakantie();
	
		// zomerKikaTwee.setId( Ids.Z_KIKA_2 );
		zomerKikaTwee.setBeginDatum( Timing.date("12/09/2014") );
		zomerKikaTwee.setEindDatum( Timing.date("18/09/2014") );
		zomerKikaTwee.setEindInschrijving( Timing.date("30/08/2014") );
		zomerKikaTwee.setType( VakantieType.Kika );
		zomerKikaTwee.setJaar( 2014 );
		zomerKikaTwee.setNaam( "KIKA 2");
		zomerKikaTwee.setPeriode( Period.Summer );
	
		this.vakantieRepository.saveAndFlush( zomerKikaTwee );
		zomerKikaTwee.setUuid( KeyFactory.keyToString( zomerKikaTwee.getKey()  ) );
		zomerKikaTwee = this.vakantieRepository.saveAndFlush( zomerKikaTwee );
		
		Deelnemer sarah
			= new Deelnemer();
		
		// sarah.setId( Ids.SARAH );
		sarah.setVoorNaam( "Lisa");
		sarah.setFamilieNaam( "Simpson" );
		sarah.setEmail("lisa.simpson@springfield.net");
		sarah.setGeslacht( Gender.F );
		
		this.persoonRepository.saveAndFlush( sarah );
		
		Organisatie pirlewiet
			= new Organisatie();

		pirlewiet.setNaam("Pirlewiet");
		pirlewiet.setCode( "pwt001" );
		pirlewiet.setUuid( "pwt-uuid");
		pirlewiet = this.organsiatieRepository.saveAndFlush( pirlewiet );
		
		Organisatie ocmw
			= new Organisatie();
	
		ocmw.setNaam("OCMW Leuven");
		ocmw.setCode( "abc123" );
	
		ocmw = this.organsiatieRepository.saveAndFlush( ocmw );
		
		InschrijvingX sarahKika1
			= new InschrijvingX();
		sarahKika1.setVks( "1" );
		sarahKika1.getVakanties().add( zomerKikaEen );
		sarahKika1.getDeelnemers().add( sarah );
		sarahKika1.setOrganisatie( ocmw );
		sarahKika1.setStatus( new Status ( Status.Value.SUBMITTED ) );
		
		this.inschrijvingXRepository.saveAndFlush( sarahKika1 );
		
		
		
	}
	
}

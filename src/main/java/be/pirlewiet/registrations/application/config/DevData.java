package be.pirlewiet.registrations.application.config;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;

import be.occam.utils.timing.Timing;
import be.pirlewiet.registrations.model.Deelnemer;
import be.pirlewiet.registrations.model.Geslacht;
import be.pirlewiet.registrations.model.Organisatie;
import be.pirlewiet.registrations.model.Periode;
import be.pirlewiet.registrations.model.Vakantie;
import be.pirlewiet.registrations.model.VakantieType;
import be.pirlewiet.registrations.repositories.InschrijvingXRepository;
import be.pirlewiet.registrations.repositories.OrganisatieRepository;
import be.pirlewiet.registrations.repositories.PersoonRepository;
import be.pirlewiet.registrations.repositories.VakantieRepository;

import com.google.appengine.tools.development.testing.LocalServiceTestHelper;

public class DevData {
	
	public static class Ids {
		
		public static Long Z_KIKA_1 = 1L;
		public static Long Z_KIKA_2 = 2L;
		public static Long Z_TIKA_1 = 3L;
		
		public static Long SARAH= 1L;
		
		public static Long IN_SARAH_KIKA_1 = 1L;
		
		public static Long PIRLEWIET= 1L;
		public static Long OCMW= 2L;
		
	}

	@Resource
	VakantieRepository vakantieRepository;
	
	@Resource
	PersoonRepository persoonRepository;
	
	@Resource
	InschrijvingXRepository inschrijvingXRepository;
	
	@Resource
	OrganisatieRepository organsiatieRepository;
	
	@PostConstruct
	public void injectData() {
		
		Vakantie zomerKikaEen
			= new Vakantie();
		
		// zomerKikaEen.setId( Ids.Z_KIKA_1 );
		zomerKikaEen.setBeginDatum( Timing.date("05/12/2014") );
		zomerKikaEen.setEindDatum( Timing.date("10/12/2015") );
		zomerKikaEen.setEindInschrijving( Timing.date("30/11/2014") );
		zomerKikaEen.setType( VakantieType.Kika );
		zomerKikaEen.setJaar( 2014 );
		zomerKikaEen.setNaam( "KIKA 1");
		zomerKikaEen.setPeriode( Periode.Zomer );
		
		zomerKikaEen = this.vakantieRepository.saveAndFlush( zomerKikaEen );
		zomerKikaEen.setId( zomerKikaEen.getKey().getId() );
		this.vakantieRepository.saveAndFlush( zomerKikaEen );
		
		Vakantie zomerKikaTwee
			= new Vakantie();
	
		zomerKikaTwee.setId( Ids.Z_KIKA_2 );
		zomerKikaTwee.setBeginDatum( Timing.date("12/12/2014") );
		zomerKikaTwee.setEindDatum( Timing.date("18/12/2014") );
		zomerKikaTwee.setEindInschrijving( Timing.date("30/12/2014") );
		zomerKikaTwee.setType( VakantieType.Kika );
		zomerKikaTwee.setJaar( 2014 );
		zomerKikaTwee.setNaam( "KIKA 2");
		zomerKikaTwee.setPeriode( Periode.Zomer );
	
		zomerKikaTwee = this.vakantieRepository.saveAndFlush( zomerKikaTwee );
		
		Deelnemer sarah
			= new Deelnemer();
		
		// sarah.setId( Ids.SARAH );
		sarah.setVoorNaam( "Lisa");
		sarah.setFamilieNaam( "Simpson" );
		sarah.setEmail("lisa.simpson@springfield.net");
		sarah.setGeslacht( Geslacht.V );
		
		// this.persoonRepository.saveAndFlush( sarah );
		
		Organisatie pirlewiet
			= new Organisatie();

		pirlewiet.setId( Ids.PIRLEWIET );
		pirlewiet.setNaam("Pirlewiet");
		pirlewiet.setCode( "pwt001" );
		pirlewiet = this.organsiatieRepository.saveAndFlush( pirlewiet );
		
		Organisatie ocmw
			= new Organisatie();
	
		ocmw.setId( Ids.OCMW );
		ocmw.setNaam("OCMW Leuven");
		ocmw.setCode( "abc123" );
	
		ocmw = this.organsiatieRepository.saveAndFlush( ocmw );
		
		/*
		InschrijvingX sarahKika1
			= new InschrijvingX();
		sarahKika1.setId( Ids.IN_SARAH_KIKA_1 );
		sarahKika1.setVakantie( zomerKikaEen );
		sarahKika1.getDeelnemers().add( sarah );
		sarahKika1.setOrganisatie( ocmw );
		sarahKika1.setStatus( Status.INGEDIEND );
		
		this.inschrijvingXRepository.saveAndFlush( sarahKika1 );
		*/
		
		
		
	}
	
}

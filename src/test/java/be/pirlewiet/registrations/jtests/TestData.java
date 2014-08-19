package be.pirlewiet.registrations.jtests;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;

import be.occam.utils.timing.Timing;
import be.pirlewiet.registrations.model.Deelnemer;
import be.pirlewiet.registrations.model.Geslacht;
import be.pirlewiet.registrations.model.InschrijvingX;
import be.pirlewiet.registrations.model.Organisatie;
import be.pirlewiet.registrations.model.Vakantie;
import be.pirlewiet.registrations.model.VakantieType;
import be.pirlewiet.registrations.repositories.InschrijvingXRepository;
import be.pirlewiet.registrations.repositories.OrganisatieRepository;
import be.pirlewiet.registrations.repositories.PersoonRepository;
import be.pirlewiet.registrations.repositories.VakantieRepository;

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
	InschrijvingXRepository inschrijvingXRepository;
	
	@Resource
	OrganisatieRepository organsiatieRepository;
	
	@PostConstruct
	public void injectData() {
		
		Vakantie zomerKikaEen
			= new Vakantie();
		
		zomerKikaEen.setId( Ids.Z_KIKA_1 );
		zomerKikaEen.setBeginDatum( Timing.date("05/07/2014") );
		zomerKikaEen.setEindDatum( Timing.date("10/07/2014") );
		zomerKikaEen.setEindInschrijving( Timing.date("30/08/2014") );
		zomerKikaEen.setType( VakantieType.Kika );
		zomerKikaEen.setJaar( 2014 );
		zomerKikaEen.setNaam( "KIKA 1");
		
		this.vakantieRepository.saveAndFlush( zomerKikaEen );
		
		Vakantie zomerKikaTwee
			= new Vakantie();
	
		zomerKikaTwee.setId( Ids.Z_KIKA_2 );
		zomerKikaTwee.setBeginDatum( Timing.date("12/07/2014") );
		zomerKikaTwee.setEindDatum( Timing.date("18/07/2014") );
		zomerKikaTwee.setEindInschrijving( Timing.date("30/08/2014") );
		zomerKikaTwee.setType( VakantieType.Kika );
		zomerKikaTwee.setJaar( 2014 );
		zomerKikaTwee.setNaam( "KIKA 2");
	
		this.vakantieRepository.saveAndFlush( zomerKikaTwee );
		
		Deelnemer sarah
			= new Deelnemer();
		
		sarah.setId( Ids.SARAH );
		sarah.setVoorNaam( "Sarah");
		sarah.setFamilieNaam( "Palin" );
		sarah.setEmail("sara.palin@teaparty.us");
		sarah.setGeslacht( Geslacht.V );
		
		this.persoonRepository.saveAndFlush( sarah );
		
		Organisatie pirlewiet
			= new Organisatie();

		pirlewiet.setNaam("Pirlewiet");
		pirlewiet = this.organsiatieRepository.saveAndFlush( pirlewiet );
		
		Organisatie ocmw
			= new Organisatie();
	
		ocmw.setNaam("OCMW Leuven");
	
		ocmw = this.organsiatieRepository.saveAndFlush( ocmw );
		
		InschrijvingX sarahKika1
			= new InschrijvingX();
		sarahKika1.setVakantie( zomerKikaEen );
		sarahKika1.getDeelnemers().add( sarah );
		sarahKika1.setOrganisatie( ocmw );
		
		this.inschrijvingXRepository.saveAndFlush( sarahKika1 );
		
		
		
	}
	
}

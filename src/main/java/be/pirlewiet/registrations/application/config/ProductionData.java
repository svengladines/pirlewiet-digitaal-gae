package be.pirlewiet.registrations.application.config;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;

import be.occam.utils.timing.Timing;
import be.pirlewiet.registrations.model.Periode;
import be.pirlewiet.registrations.model.Vakantie;
import be.pirlewiet.registrations.model.VakantieType;
import be.pirlewiet.registrations.repositories.VakantieRepository;

public class ProductionData {

	@Resource
	VakantieRepository vakantieRepository;
	
	@PostConstruct
	public void injectData() {
		
		Vakantie kikaEen
			= new Vakantie();
		
		kikaEen.setBeginDatum( Timing.date("04/04/2015") );
		kikaEen.setEindDatum( Timing.date("11/04/2015") );
		kikaEen.setEindInschrijving( Timing.date("01/04/2015") );
		kikaEen.setType( VakantieType.Kika );
		kikaEen.setJaar( 2015 );
		kikaEen.setNaam( "KIKA 1 Pasen");
		kikaEen.setPeriode( Periode.Pasen );
		
		this.vakantieRepository.saveAndFlush( kikaEen );
		
		Vakantie kikaTwee
			= new Vakantie();
	
		kikaTwee.setBeginDatum( Timing.date("11/04/2015") );
		kikaTwee.setEindDatum( Timing.date("18/04/2015") );
		kikaTwee.setEindInschrijving( Timing.date("01/04/2015") );
		kikaTwee.setType( VakantieType.Kika );
		kikaTwee.setJaar( 2015 );
		kikaTwee.setNaam( "KIKA 2 Pasen");
		kikaTwee.setPeriode( Periode.Pasen );
	
		this.vakantieRepository.saveAndFlush( kikaTwee );
		

		Vakantie gezins
			= new Vakantie();
	
		gezins.setBeginDatum( Timing.date("13/04/2015") );
		gezins.setEindDatum( Timing.date("18/04/2015") );
		gezins.setEindInschrijving( Timing.date("01/04/2015") );
		gezins.setType( VakantieType.Gezin );
		gezins.setJaar( 2015 );
		gezins.setNaam( "Gezinsvakantie Pasen");
		gezins.setPeriode( Periode.Pasen );
	
		this.vakantieRepository.saveAndFlush( gezins );
		
	}
	
}

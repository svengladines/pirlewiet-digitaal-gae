package be.pirlewiet.registrations.application.config;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;

import com.google.appengine.api.datastore.KeyFactory;

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
		
		kikaEen.setBeginDatum( Timing.date("06/04/2015") );
		kikaEen.setEindDatum( Timing.date("10/04/2015") );
		kikaEen.setEindInschrijving( Timing.date("01/04/2015") );
		kikaEen.setType( VakantieType.Kika );
		kikaEen.setJaar( 2015 );
		kikaEen.setNaam( "KIKA 1 Pasen");
		kikaEen.setPeriode( Periode.Pasen );
		
		kikaEen = this.vakantieRepository.saveAndFlush( kikaEen );
		kikaEen.setUuid( KeyFactory.keyToString( kikaEen.getKey() ) );
		kikaEen = this.vakantieRepository.saveAndFlush( kikaEen );
		
		Vakantie kikaTwee
			= new Vakantie();
	
		kikaTwee.setBeginDatum( Timing.date("13/04/2015") );
		kikaTwee.setEindDatum( Timing.date("17/04/2015") );
		kikaTwee.setEindInschrijving( Timing.date("01/04/2015") );
		kikaTwee.setType( VakantieType.Kika );
		kikaTwee.setJaar( 2015 );
		kikaTwee.setNaam( "KIKA 2 Pasen");
		kikaTwee.setPeriode( Periode.Pasen );
	
		this.vakantieRepository.saveAndFlush( kikaTwee );
		kikaTwee.setUuid( KeyFactory.keyToString( kikaTwee.getKey() ) );
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
		gezins.setUuid( KeyFactory.keyToString( gezins.getKey() ) );
		this.vakantieRepository.saveAndFlush( gezins );
		
		Vakantie vov
		= new Vakantie();

		vov.setBeginDatum( Timing.date("18/05/2015") );
		vov.setEindDatum( Timing.date("22/05/2015") );
		vov.setEindInschrijving( Timing.date("01/04/2015") );
		vov.setType( VakantieType.DrieDaagse );
		vov.setJaar( 2015 );
		vov.setNaam( "Vakantie Onder Volwassenen");
		vov.setPeriode( Periode.Pasen );

		this.vakantieRepository.saveAndFlush( vov );
		
		this.vakantieRepository.saveAndFlush( vov );
		vov.setUuid( KeyFactory.keyToString( vov.getKey() ) );
		this.vakantieRepository.saveAndFlush( vov );
		
	}
	
}

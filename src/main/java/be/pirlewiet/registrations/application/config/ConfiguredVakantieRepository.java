package be.pirlewiet.registrations.application.config;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import be.occam.utils.timing.Timing;
import be.pirlewiet.registrations.model.Period;
import be.pirlewiet.registrations.model.Vakantie;
import be.pirlewiet.registrations.model.VakantieType;
import be.pirlewiet.registrations.repositories.VakantieRepository;

public class ConfiguredVakantieRepository implements VakantieRepository {
	
	final Map<String,Vakantie> bootcamps
		= new HashMap<String,Vakantie>();
	
	public ConfiguredVakantieRepository() {
		
		Vakantie kikaEenPasen
			= new Vakantie();

		kikaEenPasen.setBeginDatum( Timing.date("29/03/2016") );
		kikaEenPasen.setEindDatum( Timing.date("02/04/2016") );
		kikaEenPasen.setBeginInschrijving( Timing.date("12/12/2015") );
		kikaEenPasen.setEindInschrijving( Timing.date("15/04/2016") );
		kikaEenPasen.setType( VakantieType.Kika );
		kikaEenPasen.setJaar( 2016 );
		kikaEenPasen.setNaam( "KIKA 1 Pasen");
		kikaEenPasen.setPeriode( Period.Spring );
		
		kikaEenPasen.setUuid( "ahRzfnBpcmxld2lldC1kaWdpdGFhbHIVCxIIVmFrYW50aWUYgICAgNuYmwoM" );
		this.bootcamps.put( kikaEenPasen.getUuid(), kikaEenPasen );
		
		Vakantie kikaTweePasen
			= new Vakantie();

		kikaTweePasen.setBeginDatum( Timing.date("04/04/2016") );
		kikaTweePasen.setEindDatum( Timing.date("08/04/2016") );
		kikaTweePasen.setBeginInschrijving( Timing.date("12/12/2015") );
		kikaTweePasen.setEindInschrijving( Timing.date("15/04/2016") );
		kikaTweePasen.setType( VakantieType.Kika );
		kikaTweePasen.setJaar( 2016 );
		kikaTweePasen.setNaam( "KIKA 2 Pasen");
		kikaTweePasen.setPeriode( Period.Spring );
	
		kikaTweePasen.setUuid( "ahRzfnBpcmxld2lldC1kaWdpdGFhbHIVCxIIVmFrYW50aWUYgICAgKD9iQoM" );
		this.bootcamps.put( kikaTweePasen.getUuid(), kikaTweePasen );
		
		Vakantie gezinsVoorjaar
			= new Vakantie();

		gezinsVoorjaar.setBeginDatum( Timing.date("29/03/2016") );
		gezinsVoorjaar.setEindDatum( Timing.date("02/04/2016") );
		gezinsVoorjaar.setBeginInschrijving( Timing.date("12/12/2015") );
		gezinsVoorjaar.setEindInschrijving( Timing.date("15/04/2016") );
		gezinsVoorjaar.setType( VakantieType.Gezin );
		gezinsVoorjaar.setJaar( 2016 );
		gezinsVoorjaar.setNaam( "Gezinsvakantie Pasen");
		gezinsVoorjaar.setPeriode( Period.Spring );
		gezinsVoorjaar.setUuid( "ahRzfnBpcmxld2lldC1kaWdpdGFhbHIVCxIIVmFrYW50aWUYgICAgPn-kwoM" );
	
		this.bootcamps.put( gezinsVoorjaar.getUuid(), gezinsVoorjaar );

		Vakantie vovVoorjaar
			= new Vakantie();
	
		vovVoorjaar.setBeginDatum( Timing.date("23/05/2016") );
		vovVoorjaar.setEindDatum( Timing.date("27/05/2016") );
		vovVoorjaar.setBeginInschrijving( Timing.date("12/12/2015") );
		vovVoorjaar.setEindInschrijving( Timing.date("01/05/2016") );
		vovVoorjaar.setType( VakantieType.DrieDaagse );
		vovVoorjaar.setJaar( 2016 );
		vovVoorjaar.setNaam( "Vakantie Onder Volwassenen");
		vovVoorjaar.setPeriode( Period.Spring );
	
		vovVoorjaar.setUuid( "ahRzfnBpcmxld2lldC1kaWdpdGFhbHIVCxIIVmFrYW50aWUYgICAgP21mAoM" );
		this.bootcamps.put( vovVoorjaar.getUuid(), vovVoorjaar );
		
		/**
		 * Zomer
		 */
		
		{
			// VOV2
			Vakantie vakantie
				= new Vakantie();
		
			vakantie.setBeginDatum( Timing.date("08/07/2016") );
			vakantie.setEindDatum( Timing.date("15/07/2016") );
			vakantie.setBeginInschrijving( Timing.date("01/03/2016") );
			vakantie.setEindInschrijving( Timing.date("01/07/2016") );
			vakantie.setType( VakantieType.Vov );
			vakantie.setJaar( 2016 );
			vakantie.setNaam( "Zomer VOV 2");
			vakantie.setPeriode( Period.Summer );
		
			vakantie.setUuid( "VOV2-SUMMER-2016" );
			
			this.bootcamps.put( vakantie.getUuid(), vakantie );
		}
		
		{
			// Driedaagse
			Vakantie vakantie
				= new Vakantie();

			vakantie.setBeginDatum( Timing.date("08/07/2016") );
			vakantie.setEindDatum( Timing.date("11/07/2016") );
			vakantie.setBeginInschrijving( Timing.date("01/03/2016") );
			vakantie.setEindInschrijving( Timing.date("01/07/2016") );
			vakantie.setType( VakantieType.DrieDaagse );
			vakantie.setJaar( 2016 );
			vakantie.setNaam( "Zomer Driedaagse");
			vakantie.setPeriode( Period.Summer );
		
			vakantie.setUuid( "DRIEDAAGSE-SUMMER-2016" );
			this.bootcamps.put( vakantie.getUuid(), vakantie );
			
		}
		
		{
		
			Vakantie vakantie
				= new Vakantie();
	
			vakantie.setBeginDatum( Timing.date("11/07/2016") );
			vakantie.setEindDatum( Timing.date("15/07/2016") );
			vakantie.setBeginInschrijving( Timing.date("01/03/2016") );
			vakantie.setEindInschrijving( Timing.date("01/07/2016") );
			vakantie.setType( VakantieType.Cava );
			vakantie.setJaar( 2016 );
			vakantie.setNaam( "Zomer Cava");
			vakantie.setPeriode( Period.Summer );
		
			vakantie.setUuid( "CAVA-SUMMER-2016" );
			
			this.bootcamps.put( vakantie.getUuid(), vakantie );
		
		}
		
		{
			// KIKA 1
			Vakantie vakantie
				= new Vakantie();

			vakantie.setBeginDatum( Timing.date("11/07/2016") );
			vakantie.setEindDatum( Timing.date("21/07/2016") );
			vakantie.setBeginInschrijving( Timing.date("01/03/2016") );
			vakantie.setEindInschrijving( Timing.date("01/07/2016") );
			vakantie.setType( VakantieType.Kika );
			vakantie.setJaar( 2016 );
			vakantie.setNaam( "Zomer KIKA 1");
			vakantie.setPeriode( Period.Summer );
		
			vakantie.setUuid( "KIKA1-SUMMER-2016" );
			this.bootcamps.put( vakantie.getUuid(), vakantie );
			
		}

		{
			// TIKA
			Vakantie vakantie
				= new Vakantie();

			vakantie.setBeginDatum( Timing.date("16/07/2016") );
			vakantie.setEindDatum( Timing.date("23/07/2016") );
			vakantie.setBeginInschrijving( Timing.date("01/03/2016") );
			vakantie.setEindInschrijving( Timing.date("01/07/2016") );
			vakantie.setType( VakantieType.Tika );
			vakantie.setJaar( 2016 );
			vakantie.setNaam( "Zomer Tika");
			vakantie.setPeriode( Period.Summer );
		
			vakantie.setUuid( "TIKA-SUMMER-2016" );
			this.bootcamps.put( vakantie.getUuid(), vakantie );
			
		}
		
		{
			// KIKA 2
			Vakantie vakantie
				= new Vakantie();

			vakantie.setBeginDatum( Timing.date("20/07/2016") );
			vakantie.setEindDatum( Timing.date("29/07/2016") );
			vakantie.setBeginInschrijving( Timing.date("01/03/2016") );
			vakantie.setEindInschrijving( Timing.date("01/07/2016") );
			vakantie.setType( VakantieType.Kika );
			vakantie.setJaar( 2016 );
			vakantie.setNaam( "Zomer Kika 2");
			vakantie.setPeriode( Period.Summer );
		
			vakantie.setUuid( "KIKA2-SUMMER-2016" );
			this.bootcamps.put( vakantie.getUuid(), vakantie );
			
		}
		
		{
			// GEZINS 1
			Vakantie vakantie
				= new Vakantie();

			vakantie.setBeginDatum( Timing.date("30/07/2016") );
			vakantie.setEindDatum( Timing.date("07/08/2016") );
			vakantie.setBeginInschrijving( Timing.date("01/03/2016") );
			vakantie.setEindInschrijving( Timing.date("15/07/2016") );
			vakantie.setType( VakantieType.Gezin );
			vakantie.setJaar( 2016 );
			vakantie.setNaam( "Zomer Gezins 1");
			vakantie.setPeriode( Period.Summer );
		
			vakantie.setUuid( "GEZIN1-SUMMER-2016" );
			this.bootcamps.put( vakantie.getUuid(), vakantie );
			
		}
		
		{
			// GEZINS 2
			Vakantie vakantie
				= new Vakantie();

			vakantie.setBeginDatum( Timing.date("07/08/2016") );
			vakantie.setEindDatum( Timing.date("15/08/2016") );
			vakantie.setBeginInschrijving( Timing.date("01/03/2016") );
			vakantie.setEindInschrijving( Timing.date("15/07/2016") );
			vakantie.setType( VakantieType.Gezin );
			vakantie.setJaar( 2016 );
			vakantie.setNaam( "Zomer Gezins 2");
			vakantie.setPeriode( Period.Summer );
		
			vakantie.setUuid( "GEZIN2-SUMMER-2016" );
			this.bootcamps.put( vakantie.getUuid(), vakantie );
			
		}
	
				
	}

	@Override
	public Page<Vakantie> findAll(Pageable arg0) {

		return null;
		
	}

	@Override
	public long count() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void delete(Long arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void delete(Vakantie arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void delete(Iterable<? extends Vakantie> arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void deleteAll() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean exists(Long arg0) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public Iterable<Vakantie> findAll(Iterable<Long> arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Vakantie findOne(Long arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public <S extends Vakantie> S save(S arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Vakantie> findAll() {
	
		List<Vakantie> camps
			= new ArrayList<Vakantie>( this.bootcamps.size() );
		
		camps.addAll( this.bootcamps.values() );
		
		return camps;
		
	}

	@Override
	public List<Vakantie> findAll(Sort paramSort) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public <S extends Vakantie> List<S> save(Iterable<S> paramIterable) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void flush() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Vakantie saveAndFlush(Vakantie paramT) {
		return paramT;
	}

	@Override
	public void deleteInBatch(Iterable<Vakantie> paramIterable) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void deleteAllInBatch() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Vakantie findByUuid(String paramString) {
		
		return this.bootcamps.get( paramString );

	}

	@Override
	public List<Vakantie> findByJaarAndPeriodeAndType(int jaar, Period periode,
			VakantieType type) {
		// TODO Auto-generated method stub
		return null;
	}


}

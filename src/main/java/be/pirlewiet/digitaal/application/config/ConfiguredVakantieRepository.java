package be.pirlewiet.digitaal.application.config;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import be.occam.utils.timing.Timing;
import be.pirlewiet.digitaal.model.Period;
import be.pirlewiet.digitaal.model.Holiday;
import be.pirlewiet.digitaal.model.HolidayType;
import be.pirlewiet.digitaal.repositories.VakantieRepository;

public class ConfiguredVakantieRepository implements VakantieRepository {
	
	final Map<String,Holiday> bootcamps
		= new HashMap<String,Holiday>();
	
	public ConfiguredVakantieRepository() {
		
		Holiday kikaEenPasen
			= new Holiday();

		kikaEenPasen.setBeginDatum( Timing.date("29/03/2016") );
		kikaEenPasen.setEindDatum( Timing.date("02/04/2016") );
		kikaEenPasen.setBeginInschrijving( Timing.date("12/12/2015") );
		kikaEenPasen.setEindInschrijving( Timing.date("15/04/2016") );
		kikaEenPasen.setType( HolidayType.Kika );
		kikaEenPasen.setJaar( 2016 );
		kikaEenPasen.setNaam( "KIKA 1 Pasen");
		kikaEenPasen.setPeriode( Period.Spring );
		
		kikaEenPasen.setUuid( "ahRzfnBpcmxld2lldC1kaWdpdGFhbHIVCxIIVmFrYW50aWUYgICAgNuYmwoM" );
		this.bootcamps.put( kikaEenPasen.getUuid(), kikaEenPasen );
		
		Holiday kikaTweePasen
			= new Holiday();

		kikaTweePasen.setBeginDatum( Timing.date("04/04/2016") );
		kikaTweePasen.setEindDatum( Timing.date("08/04/2016") );
		kikaTweePasen.setBeginInschrijving( Timing.date("12/12/2015") );
		kikaTweePasen.setEindInschrijving( Timing.date("15/04/2016") );
		kikaTweePasen.setType( HolidayType.Kika );
		kikaTweePasen.setJaar( 2016 );
		kikaTweePasen.setNaam( "KIKA 2 Pasen");
		kikaTweePasen.setPeriode( Period.Spring );
	
		kikaTweePasen.setUuid( "ahRzfnBpcmxld2lldC1kaWdpdGFhbHIVCxIIVmFrYW50aWUYgICAgKD9iQoM" );
		this.bootcamps.put( kikaTweePasen.getUuid(), kikaTweePasen );
		
		Holiday gezinsVoorjaar
			= new Holiday();

		gezinsVoorjaar.setBeginDatum( Timing.date("29/03/2016") );
		gezinsVoorjaar.setEindDatum( Timing.date("02/04/2016") );
		gezinsVoorjaar.setBeginInschrijving( Timing.date("12/12/2015") );
		gezinsVoorjaar.setEindInschrijving( Timing.date("15/04/2016") );
		gezinsVoorjaar.setType( HolidayType.Gezin );
		gezinsVoorjaar.setJaar( 2016 );
		gezinsVoorjaar.setNaam( "Gezinsvakantie Pasen");
		gezinsVoorjaar.setPeriode( Period.Spring );
		gezinsVoorjaar.setUuid( "ahRzfnBpcmxld2lldC1kaWdpdGFhbHIVCxIIVmFrYW50aWUYgICAgPn-kwoM" );
	
		this.bootcamps.put( gezinsVoorjaar.getUuid(), gezinsVoorjaar );

		Holiday vovVoorjaar
			= new Holiday();
	
		vovVoorjaar.setBeginDatum( Timing.date("23/05/2016") );
		vovVoorjaar.setEindDatum( Timing.date("27/05/2016") );
		vovVoorjaar.setBeginInschrijving( Timing.date("12/12/2015") );
		vovVoorjaar.setEindInschrijving( Timing.date("27/05/2016") );
		vovVoorjaar.setType( HolidayType.DrieDaagse );
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
			Holiday vakantie
				= new Holiday();
		
			vakantie.setBeginDatum( Timing.date("09/07/2016") );
			vakantie.setEindDatum( Timing.date("15/07/2016") );
			vakantie.setBeginInschrijving( Timing.date("01/03/2016") );
			vakantie.setEindInschrijving( Timing.date("15/07/2016") );
			vakantie.setType( HolidayType.Vov );
			vakantie.setJaar( 2016 );
			vakantie.setNaam( "Zomer VOV 2");
			vakantie.setPeriode( Period.Summer );
		
			vakantie.setUuid( "VOV2-SUMMER-2016" );
			
			this.bootcamps.put( vakantie.getUuid(), vakantie );
		}
		
		{
			// Driedaagse
			Holiday vakantie
				= new Holiday();

			vakantie.setBeginDatum( Timing.date("09/07/2016") );
			vakantie.setEindDatum( Timing.date("11/07/2016") );
			vakantie.setBeginInschrijving( Timing.date("01/03/2016") );
			vakantie.setEindInschrijving( Timing.date("11/07/2016") );
			vakantie.setType( HolidayType.DrieDaagse );
			vakantie.setJaar( 2016 );
			vakantie.setNaam( "Zomer Driedaagse");
			vakantie.setPeriode( Period.Summer );
		
			vakantie.setUuid( "DRIEDAAGSE-SUMMER-2016" );
			this.bootcamps.put( vakantie.getUuid(), vakantie );
			
		}
		
		{
		
			Holiday vakantie
				= new Holiday();
	
			vakantie.setBeginDatum( Timing.date("11/07/2016") );
			vakantie.setEindDatum( Timing.date("15/07/2016") );
			vakantie.setBeginInschrijving( Timing.date("01/03/2016") );
			vakantie.setEindInschrijving( Timing.date("15/07/2016") );
			vakantie.setType( HolidayType.Cava );
			vakantie.setJaar( 2016 );
			vakantie.setNaam( "Zomer Cava");
			vakantie.setPeriode( Period.Summer );
		
			vakantie.setUuid( "CAVA-SUMMER-2016" );
			
			this.bootcamps.put( vakantie.getUuid(), vakantie );
		
		}
		
		{
			// KIKA 1
			Holiday vakantie
				= new Holiday();

			vakantie.setBeginDatum( Timing.date("13/07/2016") );
			vakantie.setEindDatum( Timing.date("21/07/2016") );
			vakantie.setBeginInschrijving( Timing.date("01/03/2016") );
			vakantie.setEindInschrijving( Timing.date("21/07/2016") );
			vakantie.setType( HolidayType.Kika );
			vakantie.setJaar( 2016 );
			vakantie.setNaam( "Zomer KIKA 1");
			vakantie.setPeriode( Period.Summer );
		
			vakantie.setUuid( "KIKA1-SUMMER-2016" );
			this.bootcamps.put( vakantie.getUuid(), vakantie );
			
		}

		{
			// TIKA
			Holiday vakantie
				= new Holiday();

			vakantie.setBeginDatum( Timing.date("17/07/2016") );
			vakantie.setEindDatum( Timing.date("23/07/2016") );
			vakantie.setBeginInschrijving( Timing.date("01/03/2016") );
			vakantie.setEindInschrijving( Timing.date("23/07/2016") );
			vakantie.setType( HolidayType.Tika );
			vakantie.setJaar( 2016 );
			vakantie.setNaam( "Zomer Tika");
			vakantie.setPeriode( Period.Summer );
		
			vakantie.setUuid( "TIKA-SUMMER-2016" );
			this.bootcamps.put( vakantie.getUuid(), vakantie );
			
		}
		
		{
			// KIKA 2
			Holiday vakantie
				= new Holiday();

			vakantie.setBeginDatum( Timing.date("22/07/2016") );
			vakantie.setEindDatum( Timing.date("28/07/2016") );
			vakantie.setBeginInschrijving( Timing.date("01/03/2016") );
			vakantie.setEindInschrijving( Timing.date("28/07/2016") );
			vakantie.setType( HolidayType.Kika );
			vakantie.setJaar( 2016 );
			vakantie.setNaam( "Zomer Kika 2");
			vakantie.setPeriode( Period.Summer );
		
			vakantie.setUuid( "KIKA2-SUMMER-2016" );
			this.bootcamps.put( vakantie.getUuid(), vakantie );
			
		}
		
		{
			// GEZINS 1
			Holiday vakantie
				= new Holiday();

			vakantie.setBeginDatum( Timing.date("31/07/2016") );
			vakantie.setEindDatum( Timing.date("07/08/2016") );
			vakantie.setBeginInschrijving( Timing.date("01/03/2016") );
			vakantie.setEindInschrijving( Timing.date("07/08/2016") );
			vakantie.setType( HolidayType.Gezin );
			vakantie.setJaar( 2016 );
			vakantie.setNaam( "Zomer Gezins 1");
			vakantie.setPeriode( Period.Summer );
		
			vakantie.setUuid( "GEZIN1-SUMMER-2016" );
			this.bootcamps.put( vakantie.getUuid(), vakantie );
			
		}
		
		{
			// GEZINS 2
			Holiday vakantie
				= new Holiday();

			vakantie.setBeginDatum( Timing.date("08/08/2016") );
			vakantie.setEindDatum( Timing.date("15/08/2016") );
			vakantie.setBeginInschrijving( Timing.date("01/03/2016") );
			vakantie.setEindInschrijving( Timing.date("15/08/2016") );
			vakantie.setType( HolidayType.Gezin );
			vakantie.setJaar( 2016 );
			vakantie.setNaam( "Zomer Gezins 2");
			vakantie.setPeriode( Period.Summer );
		
			vakantie.setUuid( "GEZIN2-SUMMER-2016" );
			this.bootcamps.put( vakantie.getUuid(), vakantie );
			
		}
	
				
	}

	@Override
	public Page<Holiday> findAll(Pageable arg0) {

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
	public void delete(Holiday arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void delete(Iterable<? extends Holiday> arg0) {
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
	public Iterable<Holiday> findAll(Iterable<Long> arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Holiday findOne(Long arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public <S extends Holiday> S save(S arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Holiday> findAll() {
	
		List<Holiday> camps
			= new ArrayList<Holiday>( this.bootcamps.size() );
		
		camps.addAll( this.bootcamps.values() );
		
		return camps;
		
	}

	@Override
	public List<Holiday> findAll(Sort paramSort) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public <S extends Holiday> List<S> save(Iterable<S> paramIterable) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void flush() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Holiday saveAndFlush(Holiday paramT) {
		return paramT;
	}

	@Override
	public void deleteInBatch(Iterable<Holiday> paramIterable) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void deleteAllInBatch() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Holiday findByUuid(String paramString) {
		
		return this.bootcamps.get( paramString );

	}

	@Override
	public List<Holiday> findByJaarAndPeriodeAndType(int jaar, Period periode,
			HolidayType type) {
		// TODO Auto-generated method stub
		return null;
	}


}

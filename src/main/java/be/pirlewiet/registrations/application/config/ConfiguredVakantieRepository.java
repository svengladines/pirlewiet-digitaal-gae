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

		kikaEenPasen.setBeginDatum( Timing.date("06/04/2016") );
		kikaEenPasen.setEindDatum( Timing.date("10/04/2016") );
		kikaEenPasen.setBeginInschrijving( Timing.date("15/01/2016") );
		kikaEenPasen.setEindInschrijving( Timing.date("01/04/2016") );
		kikaEenPasen.setType( VakantieType.Kika );
		kikaEenPasen.setJaar( 2016 );
		kikaEenPasen.setNaam( "Pasen KIKA 1");
		kikaEenPasen.setPeriode( Period.Spring );
		
		kikaEenPasen.setUuid( "ahRzfnBpcmxld2lldC1kaWdpdGFhbHIVCxIIVmFrYW50aWUYgICAgL6CgwkM" );
		this.bootcamps.put( kikaEenPasen.getUuid(), kikaEenPasen );
		
		Vakantie kikaTweePasen
			= new Vakantie();

		kikaTweePasen.setBeginDatum( Timing.date("13/04/2016") );
		kikaTweePasen.setEindDatum( Timing.date("17/04/2016") );
		kikaTweePasen.setBeginInschrijving( Timing.date("15/01/2016") );
		kikaTweePasen.setEindInschrijving( Timing.date("01/04/2016") );
		kikaTweePasen.setType( VakantieType.Kika );
		kikaTweePasen.setJaar( 2016 );
		kikaTweePasen.setNaam( "Pasen KIKA 2");
		kikaTweePasen.setPeriode( Period.Spring );
	
		kikaTweePasen.setUuid( "ahRzfnBpcmxld2lldC1kaWdpdGFhbHIVCxIIVmFrYW50aWUYgICAgIKvgwoM" );
		this.bootcamps.put( kikaTweePasen.getUuid(), kikaTweePasen );
		
		
		
		Vakantie gezinsPasen
			= new Vakantie();

		gezinsPasen.setBeginDatum( Timing.date("14/04/2016") );
		gezinsPasen.setEindDatum( Timing.date("18/04/2016") );
		gezinsPasen.setBeginInschrijving( Timing.date("15/01/2016") );
		gezinsPasen.setEindInschrijving( Timing.date("01/04/2016") );
		gezinsPasen.setType( VakantieType.Gezin );
		gezinsPasen.setJaar( 2016 );
		gezinsPasen.setNaam( "Pasen Gezins");
		gezinsPasen.setPeriode( Period.Spring );
		gezinsPasen.setUuid( "ahRzfnBpcmxld2lldC1kaWdpdGFhbHIVCxIIVmFrYW50aWUYgICAgMK4kgoM" );
	
		this.bootcamps.put( gezinsPasen.getUuid(), gezinsPasen );

		Vakantie vov1
			= new Vakantie();
	
		vov1.setBeginDatum( Timing.date("18/05/2016") );
		vov1.setEindDatum( Timing.date("22/05/2016") );
		vov1.setBeginInschrijving( Timing.date("15/01/2016") );
		vov1.setEindInschrijving( Timing.date("01/05/2016") );
		vov1.setType( VakantieType.Vov );
		vov1.setJaar( 2016 );
		vov1.setNaam( "Voorjaar VOV 1");
		vov1.setPeriode( Period.Spring );
	
		vov1.setUuid( "ahRzfnBpcmxld2lldC1kaWdpdGFhbHIVCxIIVmFrYW50aWUYgICAgJ23kQkM" );
		this.bootcamps.put( vov1.getUuid(), vov1 );
		
		Vakantie kikaEen
			= new Vakantie();

		kikaEen.setBeginDatum( Timing.date("14/07/2016") );
		kikaEen.setEindDatum( Timing.date("21/07/2016") );
		kikaEen.setBeginInschrijving( Timing.date("01/10/2015") );
		kikaEen.setEindInschrijving( Timing.date("01/07/2016") );
		kikaEen.setType( VakantieType.Kika );
		kikaEen.setJaar( 2016 );
		kikaEen.setNaam( "Zomer KIKA 1");
		kikaEen.setPeriode( Period.Summer );
	
		kikaEen.setUuid( "ahRzfnBpcmxld2lldC1kaWdpdGFhbHIVCxIIVmFrYW50aWUYgICAgLnikQoM" );
		this.bootcamps.put( kikaEen.getUuid(), kikaEen );

		Vakantie kikaTwee
			= new Vakantie();

		kikaTwee.setBeginDatum( Timing.date("23/07/2016") );
		kikaTwee.setEindDatum( Timing.date("30/07/2016") );
		kikaTwee.setBeginInschrijving( Timing.date("01/10/2015") );
		kikaTwee.setEindInschrijving( Timing.date("15/07/2016") );
		kikaTwee.setType( VakantieType.Kika );
		kikaTwee.setJaar( 2016 );
		kikaTwee.setNaam( "Zomer KIKA 2");
		kikaTwee.setPeriode( Period.Summer );
	
		kikaTwee.setUuid( "ahRzfnBpcmxld2lldC1kaWdpdGFhbHIVCxIIVmFrYW50aWUYgICAgMSmiQoM" );
		this.bootcamps.put( kikaTwee.getUuid(), kikaTwee );

		Vakantie vov2
			= new Vakantie();
	
		vov2.setBeginDatum( Timing.date("05/07/2016") );
		vov2.setEindDatum( Timing.date("10/07/2016") );
		vov2.setBeginInschrijving( Timing.date("01/10/2015") );
		vov2.setEindInschrijving( Timing.date("01/07/2016") );
		vov2.setType( VakantieType.Vov );
		vov2.setJaar( 2016 );
		vov2.setNaam( "Zomer VOV 2");
		vov2.setPeriode( Period.Summer );
	
		vov2.setUuid( "ahRzfnBpcmxld2lldC1kaWdpdGFhbHIVCxIIVmFrYW50aWUYgICAgJu-nQoM" );
		
		this.bootcamps.put( vov2.getUuid(), vov2 );
	
		Vakantie drie
		= new Vakantie();
	
		drie.setBeginDatum( Timing.date("11/07/2016") );
		drie.setEindDatum( Timing.date("13/07/2016") );
		drie.setBeginInschrijving( Timing.date("01/10/2015") );
		drie.setEindInschrijving( Timing.date("01/07/2016") );
		drie.setType( VakantieType.DrieDaagse );
		drie.setJaar( 2016 );
		drie.setNaam( "Zomer Driedaagse");
		drie.setPeriode( Period.Summer );
		drie.setUuid( "ahRzfnBpcmxld2lldC1kaWdpdGFhbHIVCxIIVmFrYW50aWUYgICAgMDhigoM" );
		
		this.bootcamps.put( drie.getUuid(), drie );
	
		Vakantie cava
			= new Vakantie();
	
		cava.setBeginDatum( Timing.date("13/07/2016") );
		cava.setEindDatum( Timing.date("17/07/2016") );
		cava.setBeginInschrijving( Timing.date("01/10/2015") );
		cava.setEindInschrijving( Timing.date("01/07/2016") );
		cava.setType( VakantieType.Cava );
		cava.setJaar( 2016 );
		cava.setNaam( "Zomer Cava");
		cava.setPeriode( Period.Summer );
	
		cava.setUuid( "ahRzfnBpcmxld2lldC1kaWdpdGFhbHIVCxIIVmFrYW50aWUYgICAgO_9mwoM" );
		
		this.bootcamps.put( cava.getUuid(), cava );
	
		Vakantie tika
			= new Vakantie();
	
		tika.setBeginDatum( Timing.date("18/07/2016") );
		tika.setEindDatum( Timing.date("24/07/2016") );
		tika.setBeginInschrijving( Timing.date("01/10/2015") );
		tika.setEindInschrijving( Timing.date("01/07/2016") );
		tika.setType( VakantieType.Tika );
		tika.setJaar( 2016 );
		tika.setNaam( "Zomer TIKA");
		tika.setPeriode( Period.Summer );
	
		tika.setUuid( "ahRzfnBpcmxld2lldC1kaWdpdGFhbHIVCxIIVmFrYW50aWUYgICAgLn2iAoM" );
		
		this.bootcamps.put( tika.getUuid(), tika );
	
		Vakantie gezins2
			= new Vakantie();
	
		gezins2.setBeginDatum( Timing.date("09/08/2016") );
		gezins2.setEindDatum( Timing.date("16/08/2016") );
		gezins2.setBeginInschrijving( Timing.date("01/10/2015") );
		gezins2.setEindInschrijving( Timing.date("01/08/2016") );
		gezins2.setType( VakantieType.Gezin );
		gezins2.setJaar( 2016 );
		gezins2.setNaam( "Zomer Gezins 2");
		gezins2.setPeriode( Period.Summer );
	
		gezins2.setUuid( "ahRzfnBpcmxld2lldC1kaWdpdGFhbHIVCxIIVmFrYW50aWUYgICAgML7jwoM" );
		
		this.bootcamps.put( gezins2.getUuid(), gezins2 );
	
		Vakantie gezins1
			= new Vakantie();
	
		gezins1.setBeginDatum( Timing.date("01/08/2016") );
		gezins1.setEindDatum( Timing.date("08/08/2016") );
		gezins1.setBeginInschrijving( Timing.date("01/10/2015") );
		gezins1.setEindInschrijving( Timing.date("15/07/2016") );
		gezins1.setType( VakantieType.Gezin );
		gezins1.setJaar( 2016 );
		gezins1.setNaam( "Zomer Gezins 1");
		gezins1.setPeriode( Period.Summer );
	
		gezins1.setUuid( "ahRzfnBpcmxld2lldC1kaWdpdGFhbHIVCxIIVmFrYW50aWUYgICAgO_nhgoM" );
		
		this.bootcamps.put( gezins1.getUuid(), gezins1  );
		
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

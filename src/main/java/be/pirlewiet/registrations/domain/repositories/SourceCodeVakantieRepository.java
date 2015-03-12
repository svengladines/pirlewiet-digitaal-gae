package be.pirlewiet.registrations.domain.repositories;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;

import be.pirlewiet.registrations.model.Periode;
import be.pirlewiet.registrations.model.Vakantie;
import be.pirlewiet.registrations.model.VakantieType;

public class SourceCodeVakantieRepository implements JpaRepository<Vakantie, Long>{

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
		// TODO Auto-generated method stub
		return null;
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
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void deleteInBatch(Iterable<Vakantie> paramIterable) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void deleteAllInBatch() {
		// TODO Auto-generated method stub
		
	}

	


}

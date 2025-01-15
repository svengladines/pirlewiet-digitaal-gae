package be.pirlewiet.digitaal.domain.service;

import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import be.occam.utils.spring.web.Result;
import be.pirlewiet.digitaal.model.Organisation;
import be.pirlewiet.digitaal.web.util.DataGuard;

public abstract class Service<D,O> {
	
	protected final Logger logger
		= LoggerFactory.getLogger( this.getClass() );
	
	@Autowired
	DataGuard dataGuard;
	
	public Service<D,O> guard() {
	    this.dataGuard.guard();
	    return this;
	}
	
	public Result<D> create( D dto, Organisation actor ) {
		return new Result<D>( dto );
	}
	
	public Result<D> update( D dto , Organisation actor ) {
		return new Result<D>( dto );
	}
	
	public Result<D> delete( String id, Organisation actor ) {
		return new Result<D>( );
	}
	
	public Result<List<Result<D>>> query( Organisation actor ) {
		List<Result<D>> list
			= Arrays.asList();
		return new Result<List<Result<D>>>( list );
	}

}

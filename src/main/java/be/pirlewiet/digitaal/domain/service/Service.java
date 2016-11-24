package be.pirlewiet.digitaal.domain.service;

import java.util.Arrays;
import java.util.List;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import be.occam.utils.spring.web.Result;
import be.pirlewiet.digitaal.model.Organisation;
import be.pirlewiet.digitaal.web.util.DataGuard;

public abstract class Service<D,O> {
	
	protected final Logger logger
		= LoggerFactory.getLogger( this.getClass() );
	
	@Resource
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
	
	public Result<D> delete( D dto, Organisation actor ) {
		return new Result<D>( dto );
	}
	
	public Result<List<D>> query( Organisation actor ) {
		List<D> list
			= Arrays.asList();
		return new Result<List<D>>( list );
	}

}

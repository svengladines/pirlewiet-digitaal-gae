package be.pirlewiet.digitaal.domain.service;

import java.util.Arrays;
import java.util.List;

import javax.annotation.Resource;

import be.pirlewiet.digitaal.model.D;
import be.pirlewiet.digitaal.web.util.DataGuard;

public abstract class Service<D,O> {
	
	@Resource
	DataGuard dataGuard;
	
	public Service<D,O> guard() {
	    this.dataGuard.guard();
	    return this;
	}
	
	public D create( D dto, D actor ) {
		return dto;
	}
	
	public D update( D organisation, D actor ) {
		return organisation;
	}
	
	public D delete( D organisation, D actor ) {
		return organisation;
	}
	
	public List<D> query( D actor ) {
		return Arrays.asList( );
	}

}

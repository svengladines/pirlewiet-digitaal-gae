package be.pirlewiet.digitaal.domain.service;

import static be.occam.utils.javax.Utils.list;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import be.occam.utils.spring.web.Result;
import be.occam.utils.spring.web.Result.Value;
import be.pirlewiet.digitaal.domain.Reducer;
import be.pirlewiet.digitaal.domain.exception.ErrorCodes;
import be.pirlewiet.digitaal.domain.exception.PirlewietException;
import be.pirlewiet.digitaal.domain.people.AddressManager;
import be.pirlewiet.digitaal.domain.people.DoorMan;
import be.pirlewiet.digitaal.domain.people.Excelsior;
import be.pirlewiet.digitaal.domain.people.OrganisationManager;
import be.pirlewiet.digitaal.model.Address;
import be.pirlewiet.digitaal.model.Organisation;
import be.pirlewiet.digitaal.web.dto.AddressDTO;
import be.pirlewiet.digitaal.web.dto.OrganisationDTO;
import be.pirlewiet.digitaal.web.util.PirlewietUtil;
import be.pirlewiet.digitaal.web.util.Tuple;

@Service
public class OrganisationService extends be.pirlewiet.digitaal.domain.service.Service<OrganisationDTO,Organisation> {
	
	@Resource
	protected DoorMan doorMan;
	
	@Resource
	OrganisationManager organisationManager;
	
	@Resource
	Excelsior excelsior;
	
	@Resource
	Reducer reducer;
	
	@Resource
	AddressManager addressManager;
	
	@Override
	public OrganisationService guard() {
		super.guard();
		return this;
	}
	
	@Override
	public Result<List<Result<OrganisationDTO>>> query(Organisation actor) {
		
		List<Organisation> organisations
			= this.organisationManager.all();
		
		
		List<OrganisationDTO> dtos
			= list();
		
		for ( Organisation organisation : organisations ) {
			
			OrganisationDTO dto
				= OrganisationDTO.from( organisation );
			
			if ( ! PirlewietUtil.isPirlewiet( actor ) ) {
				this.reducer.reduce( dto );	
			}
			
			dtos.add( dto );
			
		}
		
		Result<List<Result<OrganisationDTO>>> result
			= new Result<List<Result<OrganisationDTO>>>();
		
		List<Result<OrganisationDTO>> individualResults
			= list();
		
		for ( OrganisationDTO dto : dtos ) {
			
			Result<OrganisationDTO> individualResult
				= new Result<OrganisationDTO>();
			individualResult.setValue( Value.OK );
			individualResult.setObject( dto );
			
			individualResults.add( individualResult );
			
			
		}
		
		result.setValue( Value.OK );
		result.setObject( individualResults );
		
		return result;
			
		
	}



	@Override
	@Transactional(readOnly=false)
	public Result<OrganisationDTO> create(OrganisationDTO dto, Organisation actor) {
		
		Result<OrganisationDTO> result
			= new Result<OrganisationDTO>();
		
		Organisation organisation
			= Organisation.from( dto );
		
		Organisation created
			= this.organisationManager.create(organisation);
		
		result.setValue( Value.OK );
		result.setObject( OrganisationDTO.from( created ) );
		
		return result;
		
	}
	
	@Override
	@Transactional(readOnly=false)
	public Result<OrganisationDTO> update(OrganisationDTO dto, Organisation actor) {
		
		Result<OrganisationDTO> result
			= new Result<OrganisationDTO>();
		
		Organisation organisation
			= Organisation.from( dto );
		
		Organisation updated
			= this.organisationManager.update( dto.getUuid(), organisation );
		
		result.setValue( Value.OK );
		result.setObject( OrganisationDTO.from( updated ) );
		
		return result;
		
	}
	
	@Override
	@Transactional(readOnly=false)
	public Result<OrganisationDTO> delete(String organisationUuid, Organisation actor) {
		
		Result<OrganisationDTO> result
			= new Result<OrganisationDTO>();
		
		result.setValue( Value.OK );
		
		if ( PirlewietUtil.isPirlewiet( actor ) ) {
			
			Organisation deleted
				= this.organisationManager.delete( organisationUuid );
			
			if ( deleted != null ) {
			
				result.setObject( OrganisationDTO.from( deleted ) );
				
			}
			
		}
		
		return result;
		
	}
	
	@Transactional(readOnly=true)
	public Result<OrganisationDTO> findOneByUuid( String uuid, Organisation actor ) {
		
		Result<OrganisationDTO> result
			= new Result<OrganisationDTO>();
		
		Organisation found 
			= this.organisationManager.organisation( uuid );
		
		if ( found == null ) {
			result.setValue( Value.NOK );
			result.setErrorCode( ErrorCodes.ORGANISATION_NOT_FOUND );
		}
		else {
			result.setValue( Value.OK );
			
			boolean inComplete 
				= this.organisationManager.isInComplete( found, true );
			
			if  (found.getAddressUuid() == null ) {
				
				// address is required
				inComplete = true;
				
			}
			else {
				
				inComplete |= ( ! this.addressManager.isComplete( found.getAddressUuid() ) );
				
			}
			
			OrganisationDTO dto
				= OrganisationDTO.from( found );
			
			dto.setInComplete( inComplete );
			
			result.setObject( dto );
		}
		
		return result;
		
	}
	
	//@Transactional(readOnly=false)
	public Result<List<Result<OrganisationDTO>>> consume( MultipartFile file, Organisation actor) {
		
		Result<List<Result<OrganisationDTO>>>  result
			= new Result<List<Result<OrganisationDTO>>>();
		
		List<String[]> rows
			= this.excelsior.toRows( file );
		
		List<Tuple<Organisation, Address>> tuples
			= this.excelsior.toOrganisations( rows );
		
		List<Result<OrganisationDTO>> individualResults
			= list();
	
		for ( Tuple<Organisation,Address> tuple : tuples ) {
			
			try {
		
				Result<OrganisationDTO> individualResult
					= new Result<OrganisationDTO>();
				
				Organisation organisation
					= tuple.getX();
				
				String code
					= organisation.getCode();
				
				Organisation loaded
					= this.doorMan.whoHasCode( code );
				
				if ( loaded == null ) {
				
					Address address
						= tuple.getY();
					
					Address createdAddress
						= this.addressManager.create( address );
					
					organisation.setAddressUuid( createdAddress.getUuid() );
					
					Organisation created
						= this.organisationManager.create( organisation, false );
					
					individualResult.setValue( Value.OK );
					individualResult.setObject( OrganisationDTO.from( created ) );
					
					individualResults.add( individualResult );
					
					logger.warn("organisation [{}] successfully consumed and created", created.getName() );
				}
			}
			catch( PirlewietException e ){
				logger.warn("failed to consume organisation-tuple for organisation [{}], error code is [{}]", tuple.getX().getName(), e.getErrorCode().getCode() );
				Result<OrganisationDTO> individualResult
					= new Result<OrganisationDTO>();
				individualResult.setValue( Value.NOK );
				individualResult.setObject( OrganisationDTO.from( tuple.getX() ) );
				individualResult.setErrorCode( e.getErrorCode() );
				individualResults.add( individualResult );
			}
		
		}
		
		result.setValue( Value.OK );
		result.setObject( individualResults );
		
		return result;
		
	}
	
	@Transactional(readOnly=false)
	public Result<AddressDTO> getAddress( String addressUuid, Organisation actor ) {
		
		Result<AddressDTO> result
			= new Result<AddressDTO>();
		
		Address address 
			= this.addressManager.findOneByUuid( addressUuid );
		
		if ( address == null ) {
			result.setValue( Value.NOK );
			result.setErrorCode( ErrorCodes.INTERNAL );
		}
		else {
			
		}
		
		result.setValue( Value.OK );
		result.setObject( AddressDTO.from( address ) );
		
		return result;
		
	}
	
	@Transactional(readOnly=false)
	public Result<AddressDTO> updateAddress( String uuid, AddressDTO address, Organisation actor ) {
		
		Result<AddressDTO> result
			= new Result<AddressDTO>();
		
		Organisation found 
			= this.organisationManager.organisation( uuid );
	
		if ( found == null ) {
			logger.info( "no organisation found with uuid [{}]", uuid );
			result.setValue( Value.NOK );
			result.setErrorCode( ErrorCodes.ORGANISATION_NOT_FOUND );
		}
		
		Address toReturn
			= null;
		
		if ( found.getAddressUuid() == null ) {
			logger.info( "[{}]; no address yet for organisation [{}], create new one", uuid );
			toReturn = this.addressManager.create( Address.from( address ) );
			this.organisationManager.updateAddress( found, toReturn.getUuid() );
		}
		else {
			logger.info( "[{}]; organisation has address with uuid [{}]", uuid, found.getAddressUuid() );
			Address toUpdate 
				= this.addressManager.findOneByUuid( found.getAddressUuid() );
			logger.info( "[{}]; found address with uuid [{}]", uuid, found.getAddressUuid() );
			toReturn = this.addressManager.update( toUpdate, Address.from( address ) ) ;	
		}
		
		result.setValue( Value.OK );
		result.setObject( AddressDTO.from( toReturn ) );
		
		return result;
		
	}
	
}

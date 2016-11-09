package be.pirlewiet.digitaal.domain.service;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import be.occam.utils.spring.web.Result;
import be.occam.utils.spring.web.Result.Value;
import be.pirlewiet.digitaal.domain.exception.ErrorCodes;
import be.pirlewiet.digitaal.domain.people.AddressManager;
import be.pirlewiet.digitaal.domain.people.DoorMan;
import be.pirlewiet.digitaal.domain.people.OrganisationManager;
import be.pirlewiet.digitaal.dto.AddressDTO;
import be.pirlewiet.digitaal.dto.OrganisationDTO;
import be.pirlewiet.digitaal.model.Address;
import be.pirlewiet.digitaal.model.Organisation;
import be.pirlewiet.digitaal.repositories.OrganisationRepository;

@Service
public class OrganisationService extends be.pirlewiet.digitaal.domain.service.Service<OrganisationDTO,Organisation> {
	
	@Resource
	protected DoorMan doorMan;
	
	@Resource
	OrganisationManager organisationManager;
	
	@Resource
	OrganisationRepository organisationRepository;
	
	@Resource
	AddressManager addressManager;
	
	@Override
	public OrganisationService guard() {
		super.guard();
		return this;
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
				= this.organisationManager.isInComplete( found, false );
			
			if  (found.getAddressUuid() == null ) {
				
				// address is required
				inComplete = true;
				
			}
			else {
				
				inComplete |= ( ! this.addressManager.isComplete( uuid ) );
				
			}
			
			OrganisationDTO dto
				= OrganisationDTO.from( found );
			
			dto.setInComplete( inComplete );
			
			result.setObject( dto );
		}
		
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
			result.setValue( Value.NOK );
			result.setErrorCode( ErrorCodes.ORGANISATION_NOT_FOUND );
		}
		
		Address updated
			= this.addressManager.createOrUpdate( Address.from( address ) );
		
		this.organisationManager.updateAddress( found, updated.getUuid() );
		
		result.setValue( Value.OK );
		result.setObject( AddressDTO.from( updated ) );
		
		return result;
		
	}
	
}

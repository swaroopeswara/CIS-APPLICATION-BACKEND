package com.dw.ngms.cis.uam.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dw.ngms.cis.uam.entity.OrganisationType;
import com.dw.ngms.cis.uam.repository.OrganisationTypeRepository;

@Service
public class OraganisationTypeService {
	
	@Autowired
	private OrganisationTypeRepository organisationTypeRepository;
	
	public List<OrganisationType> getAllOrganisatioTypes() {
		return organisationTypeRepository.findAll();
	}//getAllOrganisatioTypes

}

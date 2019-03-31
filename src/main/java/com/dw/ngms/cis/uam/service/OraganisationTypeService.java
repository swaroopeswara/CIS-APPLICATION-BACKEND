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
	
	public List<OrganisationType> getAllOrganisationTypes() {
		return organisationTypeRepository.findAll();
	}//getAllOrganisatioTypes

	public OrganisationType addOrganisationType(OrganisationType organisationType) {
		if(organisationType == null|| organisationType.getId() != null) return null;
		organisationType.setOrganisationCode(getOrganisationTypeCode());
		return organisationTypeRepository.save(organisationType);
	}//getAllOrganisatioTypes

	private String getOrganisationTypeCode() {
		String code = null;
		// TODO need to complete this impl
		code = "ORGTY0010";
		return code;
	}//getOrganisationTypeCode
}

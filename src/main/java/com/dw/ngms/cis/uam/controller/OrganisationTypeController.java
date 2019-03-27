package com.dw.ngms.cis.uam.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.dw.ngms.cis.uam.entity.OrganisationType;
import com.dw.ngms.cis.uam.service.OraganisationTypeService;

@RestController
@RequestMapping("/organisationType")
public class OrganisationTypeController extends MessageController {

	@Autowired
	private OraganisationTypeService oraganisationTypeService;
	
	@GetMapping("/getOrgTypes")
	public ResponseEntity<?> getAllOrgTypes(HttpServletRequest request) {
		try{
			List<OrganisationType> organisationTypeList = this.oraganisationTypeService.getAllOrganisatioTypes();
			return (CollectionUtils.isEmpty(organisationTypeList)) ? generateEmptyResponse(request, "OrgTypes not found")
					: ResponseEntity.status(HttpStatus.OK).body(organisationTypeList);
		} catch (Exception exception) {
			return generateFailureResponse(request, exception);
		}
	}//getAllOrganisationTypes
	
	
	
	
}
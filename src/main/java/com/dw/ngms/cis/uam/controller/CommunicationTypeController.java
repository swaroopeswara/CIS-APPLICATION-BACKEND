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

import com.dw.ngms.cis.uam.entity.CommunicationType;
import com.dw.ngms.cis.uam.service.CommunicationTypeService;

@RestController
@RequestMapping("/communicationType")
public class CommunicationTypeController extends MessageController {
	
	@Autowired
	private CommunicationTypeService communicationTypeService;
	
	@GetMapping("/getCommunicationTypes")
	public ResponseEntity<?> getAllOrgTypes(HttpServletRequest request) {
		try{
			List<CommunicationType> communicationTypeList = this.communicationTypeService.getAllCommunicationTypes();
			return (CollectionUtils.isEmpty(communicationTypeList)) ? generateEmptyResponse(request, "CommTypes not found")
					: ResponseEntity.status(HttpStatus.OK).body(communicationTypeList);
		} catch (Exception exception) {
			return generateFailureResponse(request, exception);
		}
	}//getAllCommunicationTypes

}

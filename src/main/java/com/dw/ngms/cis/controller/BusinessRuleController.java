package com.dw.ngms.cis.controller;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.dw.ngms.cis.entity.BusinessRuleHistory;
import com.dw.ngms.cis.service.BusinessRuleService;
import com.dw.ngms.cis.uam.controller.MessageController;

@RestController
@RequestMapping("/cisorigin.cis/api/v1")
@CrossOrigin(origins = "*")
public class BusinessRuleController extends MessageController {
	
    @Autowired
    private BusinessRuleService businessRuleService;

	@PostMapping("/createBusinessRuleHistory")
    public ResponseEntity<?> createBusinessRuleHistory(HttpServletRequest request, @RequestBody @Valid BusinessRuleHistory businessRuleHistory) {
        try{
        	businessRuleHistory = businessRuleService.addBusinessRuleHistory(businessRuleHistory);
        	return (businessRuleHistory == null) ? generateEmptyResponse(request, "Failed to add business rule history") :
				ResponseEntity.status(HttpStatus.OK).body("Successful");
		} catch (Exception exception) {
			return generateFailureResponse(request, exception);
		}
    }//createBusinessRuleHistory
}

package com.dw.ngms.cis.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dw.ngms.cis.entity.BusinessRuleHistory;
import com.dw.ngms.cis.repository.BusinessRuleHistoryRepository;

@Service
public class BusinessRuleService {

	@Autowired
    private BusinessRuleHistoryRepository businessRuleHistoryRepository;

    public BusinessRuleHistory addBusinessRuleHistory(BusinessRuleHistory businessRuleHistory) {
    	if(businessRuleHistory == null) return null;
        return this.businessRuleHistoryRepository.save(businessRuleHistory);
    }//addBusinessRuleHistory
}

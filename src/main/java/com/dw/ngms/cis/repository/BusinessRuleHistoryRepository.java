package com.dw.ngms.cis.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.dw.ngms.cis.entity.BusinessRuleHistory;

@Repository
public interface BusinessRuleHistoryRepository extends JpaRepository<BusinessRuleHistory, Long> {

}

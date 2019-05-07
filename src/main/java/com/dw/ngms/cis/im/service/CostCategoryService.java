package com.dw.ngms.cis.im.service;

import com.dw.ngms.cis.im.entity.ApplicationProperties;
import com.dw.ngms.cis.im.entity.CostCategories;
import com.dw.ngms.cis.im.repository.CostCategoryRepository;
import com.dw.ngms.cis.uam.dto.ExternalUserAssistantDTO;
import com.dw.ngms.cis.uam.entity.ExternalUserAssistant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by swaroop on 2019/04/16.
 */

@Service
public class CostCategoryService {


    @Autowired
    private CostCategoryRepository costCategoryRepository;

    public List<CostCategories> getAllCostCategories() {
        return this.costCategoryRepository.findActiveCategories();
    }//getAllCostCategories

    public CostCategories saveCostCategory(CostCategories costCategories) {
        return this.costCategoryRepository.save(costCategories);
    } //saveCostCategory

    public Long getCategoryId() {
        return this.costCategoryRepository.getCategoryId();
    } //getCategoryId

    public CostCategories findByCategoryCode(String categoryCode) {
        return this.costCategoryRepository.findByCategoryCode(categoryCode);
    }




}

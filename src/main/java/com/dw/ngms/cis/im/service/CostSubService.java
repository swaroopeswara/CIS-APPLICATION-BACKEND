package com.dw.ngms.cis.im.service;

import com.dw.ngms.cis.im.entity.CostSubCategories;
import com.dw.ngms.cis.im.repository.CostSubRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;

/**
 * Created by swaroop on 2019/04/16.
 */
@Service
public class CostSubService {


    @Autowired
    private CostSubRepository costSubRepository;

    public Long getCostSubCategoryId() {
        return this.costSubRepository.getCostSubCategoryId();
    } //getCostSubCategoryId

    public CostSubCategories saveCostSubCategories(CostSubCategories costSubCategories) {
        return this.costSubRepository.save(costSubCategories);
    } //saveCostSubCategories


    public CostSubCategories findBycostSubCategoryCode(String costSubCategoryCode) {
        return this.costSubRepository.findBycostSubCategoryCode(costSubCategoryCode);
    }


    public CostSubCategories updateCostSubCategory(CostSubCategories costSubCategories) {
        CostSubCategories costSubCategoriesItem = this.costSubRepository.findBycostSubCategoryCode((costSubCategories.getCostSubCategoryCode()));
        if (costSubCategoriesItem == null)
            throw new RuntimeException("Cost Category is not registered with code " + costSubCategories.getCostCategoryCode());

        return this.costSubRepository.save(populateCostSubCategory(costSubCategories, costSubCategoriesItem));
    }//updateCostSubCategory


    private CostSubCategories populateCostSubCategory(CostSubCategories costSubCategories, CostSubCategories costSubCategoriesItem) {
        if(costSubCategories.getCostCategoryName() != null)  costSubCategoriesItem.setCostCategoryName(costSubCategories.getCostCategoryName());
        if(costSubCategories.getCostSubCategoryName() != null)costSubCategoriesItem.setCostSubCategoryName(costSubCategories.getCostSubCategoryName());
        if(costSubCategories.getDescription() != null) costSubCategoriesItem.setDescription(costSubCategories.getDescription());
        if(costSubCategories.getFixedRate() != null)costSubCategoriesItem.setFixedRate(costSubCategories.getFixedRate());
        if(costSubCategories.getHourRate() != null) costSubCategoriesItem.setHourRate(costSubCategories.getHourRate());
        if(costSubCategories.getHalfHourRate() != null) costSubCategoriesItem.setHalfHourRate(costSubCategories.getHalfHourRate());
        if(costSubCategories.getIsActive() != null)costSubCategoriesItem.setIsActive(costSubCategories.getIsActive());
        if(costSubCategories.getIsDeleted() != null)costSubCategoriesItem.setIsDeleted(costSubCategories.getIsDeleted());
        return costSubCategoriesItem;
    }//populateCostSubCategory

    public List<CostSubCategories> getSubCostCategoriesByCostCategoryCode(String costCategoryCode) {
        return this.costSubRepository.getSubCostCategoriesByCostCategoryCode(costCategoryCode);


    }

}

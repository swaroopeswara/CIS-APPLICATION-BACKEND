package com.dw.ngms.cis.im.service;

import com.dw.ngms.cis.im.entity.CostSubCategories;
import com.dw.ngms.cis.im.repository.CostSubRepository;
import com.dw.ngms.cis.uam.dto.UserUpdateDTO;
import com.dw.ngms.cis.uam.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.Date;
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



    public CostSubCategories updateSUbCategories(CostSubCategories updateCostCategories) {
        if(updateCostCategories == null || updateCostCategories.getCostSubCategoryCode() == null) return null;
        CostSubCategories subCategoriesValues = this.costSubRepository.findBycostSubCategoryCode(updateCostCategories.getCostSubCategoryCode());
        System.out.println("Test update internal Update" +subCategoriesValues.getCostSubCategoryCode());
        if(subCategoriesValues == null) return null;
        return this.costSubRepository.save(getPopulatedCostSubCategoriesWithUpdate(updateCostCategories,subCategoriesValues));
    }//updateSUbCategories


    private CostSubCategories getPopulatedCostSubCategoriesWithUpdate(CostSubCategories updateCostCategories, CostSubCategories costSubCategories) {
        if(updateCostCategories.getCostCategoryId() != null && updateCostCategories.getCostCategoryId() != 0) costSubCategories.setCostCategoryId(updateCostCategories.getCostCategoryId());
        if(updateCostCategories.getSubCategoryId() != null && updateCostCategories.getSubCategoryId() != 0) costSubCategories.setSubCategoryId(updateCostCategories.getSubCategoryId());

        if(updateCostCategories.getCostSubCategoryCode() != null && updateCostCategories.getCostSubCategoryCode() != "") costSubCategories.setCostSubCategoryCode(updateCostCategories.getCostSubCategoryCode());
        if(updateCostCategories.getCostSubCategoryName() != null && updateCostCategories.getCostSubCategoryName() != "") costSubCategories.setCostSubCategoryName(updateCostCategories.getCostSubCategoryName());
        if(updateCostCategories.getCostCategoryCode() != null && updateCostCategories.getCostCategoryCode() != "") costSubCategories.setCostCategoryCode(updateCostCategories.getCostCategoryCode());
        if(updateCostCategories.getCostCategoryName() != null && updateCostCategories.getCostCategoryName() != "") costSubCategories.setCostCategoryName(updateCostCategories.getCostCategoryName());



        if(updateCostCategories.getDescription() != null && updateCostCategories.getDescription() != "") costSubCategories.setDescription(updateCostCategories.getDescription());
        if(updateCostCategories.getFixedRate() != null && updateCostCategories.getFixedRate() != "") costSubCategories.setFixedRate(updateCostCategories.getFixedRate());
        if(updateCostCategories.getHourRate() != null && updateCostCategories.getHourRate() != "") costSubCategories.setHourRate(updateCostCategories.getHourRate());

        if(updateCostCategories.getHalfHourRate() != null && updateCostCategories.getHalfHourRate() != "") costSubCategories.setHalfHourRate(updateCostCategories.getHalfHourRate());
        if(updateCostCategories.getIsActive() != null && updateCostCategories.getIsActive() != "") costSubCategories.setIsActive(updateCostCategories.getIsActive());
        costSubCategories.setModifiedDate(new Date());
        if(updateCostCategories.getIsDeleted() != null && updateCostCategories.getIsDeleted() != "") costSubCategories.setIsDeleted(updateCostCategories.getIsDeleted());



        return costSubCategories;
    }//getPopulatedUserWithModifiedDetails




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

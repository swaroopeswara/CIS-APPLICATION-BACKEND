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



    public CostSubCategories updateSUbCategories(CostSubCategories costSubCategories) {
        if(costSubCategories == null || costSubCategories.getCostSubCategoryCode() == null) return null;
        CostSubCategories subCategories = this.costSubRepository.findBycostSubCategoryCode(costSubCategories.getCostSubCategoryCode());
        System.out.println("Test update internal Update" +subCategories.getCostSubCategoryCode());
        if(subCategories == null) return null;
        return this.costSubRepository.save(getPopulatedCostSubCategoriesWithUpdate(subCategories, costSubCategories));
    }//updateSUbCategories


    private CostSubCategories getPopulatedCostSubCategoriesWithUpdate(CostSubCategories subCategories, CostSubCategories costSubCategories) {
        if(subCategories.getCostCategoryId() != null && subCategories.getCostCategoryId() != 0) costSubCategories.setCostCategoryId(subCategories.getCostCategoryId());
        if(subCategories.getSubCategoryId() != null && subCategories.getSubCategoryId() != 0) costSubCategories.setSubCategoryId(subCategories.getSubCategoryId());

        if(subCategories.getCostSubCategoryCode() != null && subCategories.getCostSubCategoryCode() != "") costSubCategories.setCostCategoryCode(subCategories.getCostSubCategoryCode());
        if(subCategories.getCostSubCategoryName() != null && subCategories.getCostSubCategoryName() != "") costSubCategories.setCostCategoryName(subCategories.getCostSubCategoryName());
        if(subCategories.getCostCategoryCode() != null && subCategories.getCostCategoryCode() != "") costSubCategories.setCostCategoryCode(subCategories.getCostCategoryCode());
        if(subCategories.getCostCategoryName() != null && subCategories.getCostCategoryName() != "") costSubCategories.setCostCategoryName(subCategories.getCostCategoryName());



        if(subCategories.getDescription() != null && subCategories.getDescription() != "") costSubCategories.setDescription(subCategories.getDescription());
        if(subCategories.getFixedRate() != null && subCategories.getFixedRate() != "") costSubCategories.setFixedRate(subCategories.getFixedRate());
        if(subCategories.getHourRate() != null && subCategories.getHourRate() != "") costSubCategories.setHourRate(subCategories.getHourRate());

        if(subCategories.getHalfHourRate() != null && subCategories.getHalfHourRate() != "") costSubCategories.setHalfHourRate(subCategories.getHalfHourRate());
        if(subCategories.getIsActive() != null && subCategories.getIsActive() != "") costSubCategories.setIsActive(subCategories.getIsActive());
        costSubCategories.setModifiedDate(new Date());
        if(subCategories.getIsDeleted() != null && subCategories.getIsDeleted() != "") costSubCategories.setIsDeleted(subCategories.getIsDeleted());



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

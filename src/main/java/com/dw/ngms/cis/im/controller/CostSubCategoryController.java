package com.dw.ngms.cis.im.controller;

import com.dw.ngms.cis.controller.MessageController;
import com.dw.ngms.cis.im.entity.CostCategories;
import com.dw.ngms.cis.im.entity.CostSubCategories;
import com.dw.ngms.cis.im.entity.RequestItems;
import com.dw.ngms.cis.im.service.CostSubService;
import com.dw.ngms.cis.uam.dto.UserUpdateDTO;
import com.dw.ngms.cis.uam.entity.PlsUser;
import com.dw.ngms.cis.uam.entity.User;
import com.dw.ngms.cis.uam.jsonresponse.UserControllerResponse;
import com.google.gson.Gson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.io.IOException;
import java.util.List;

import static org.springframework.util.StringUtils.isEmpty;

/**
 * Created by swaroop on 2019/04/16.
 */
@RestController
@RequestMapping("/cisorigin.im/api/v1")
@CrossOrigin(origins = "*")
public class CostSubCategoryController extends MessageController {

    @Autowired
    private CostSubService costSubService;

    @GetMapping("/getSubCostCategoriesByCostCategoryCode")
    public ResponseEntity<?> getSubCostCategoriesByCostCategoryCode(HttpServletRequest request,
                                                                    @RequestParam String costCategoryCode) {
        try {
            List<CostSubCategories> costCategoriesList = costSubService.getSubCostCategoriesByCostCategoryCode(costCategoryCode);
            return (CollectionUtils.isEmpty(costCategoriesList)) ? ResponseEntity.status(HttpStatus.OK).body(costCategoriesList)
                    : ResponseEntity.status(HttpStatus.OK).body(costCategoriesList);
        } catch (Exception exception) {
            return generateFailureResponse(request, exception);
        }
    }//getSubCostCategoriesByCostCategoryCode


    @PostMapping("/createSubCategory")
    public ResponseEntity<?> createSubCategory(HttpServletRequest request, @RequestBody @Valid CostSubCategories costSubCategories) {
        try {
            Long subCategoryId = this.costSubService.getCostSubCategoryId();
            System.out.println("sub categoryId is "+subCategoryId);
            costSubCategories.setCostSubCategoryCode("SUBCOST" + Long.toString(subCategoryId));
            CostSubCategories costSubCategoriesSave = this.costSubService.saveCostSubCategories(costSubCategories);
            return ResponseEntity.status(HttpStatus.OK).body(costSubCategoriesSave);
        } catch (Exception exception) {
            return generateFailureResponse(request, exception);
        }
    }//createCategory

    @PostMapping("/updateCostSubCategory")
    public ResponseEntity<?> updateCostSubCategory(HttpServletRequest request, @RequestBody CostSubCategories updateCostCategories) {
        try {
            CostSubCategories subCategories = this.costSubService.updateSUbCategories(updateCostCategories);
                return (subCategories == null) ? generateEmptyResponse(request, "Failed to update internal user") :
                        ResponseEntity.status(HttpStatus.OK).body("Update Successful");

        } catch (Exception exception) {
            return generateFailureResponse(request, exception);
        }
    }//updateInternalUser


    @GetMapping("/getCostOfCategory")
    public ResponseEntity<?> getCostOfCategory(HttpServletRequest request,
                                                                    @RequestParam String costSubCategoryName) {
        try {
            List<CostSubCategories> costOfCategoriesList = costSubService.getCostOfCategory(costSubCategoryName);
            return (CollectionUtils.isEmpty(costOfCategoriesList)) ? generateEmptyResponse(request, "Cost Of Category(s) not found")
                    : ResponseEntity.status(HttpStatus.OK).body(costOfCategoriesList);
        } catch (Exception exception) {
            return generateFailureResponse(request, exception);
        }
    }//getCostOfCategory




    @RequestMapping(value = "/deactivateSubCategory", method = RequestMethod.POST)
    public ResponseEntity<?> deactivateSubCategory(HttpServletRequest request, @RequestBody @Valid CostSubCategories costSubCategories) throws IOException {
        try {
            Gson gson = new Gson();
            UserControllerResponse userControllerResponse = new UserControllerResponse();
            String json = null;
            CostSubCategories costSubCategoriesItem = this.costSubService.findBycostSubCategoryCode(costSubCategories.getCostSubCategoryCode());
            if (isEmpty(costSubCategoriesItem)){
                userControllerResponse.setMessage("Cost Sub Categories not found");
                json = gson.toJson(userControllerResponse);
                return ResponseEntity.status(HttpStatus.OK).body(json);
            }
            if (!isEmpty(costSubCategoriesItem)) {
                costSubCategoriesItem.setIsActive("N");
                this.costSubService.saveCostSubCategories(costSubCategoriesItem);
            }
            userControllerResponse.setMessage("Cost Sub Category de-activated Successfully");
            json = gson.toJson(userControllerResponse);
            return ResponseEntity.status(HttpStatus.OK).body(json);
        } catch (Exception exception) {
            return generateFailureResponse(request, exception);
        }
    }//deactivateCategory


    @PostMapping("/updateSubCategory")
    public ResponseEntity<?> updateSubCategory(HttpServletRequest request, @RequestBody @Valid CostSubCategories costSubCategories) {
        try{
            costSubCategories = costSubService.updateCostSubCategory(costSubCategories);
            return (costSubCategories == null) ? generateEmptyResponse(request, "Failed to update pls user") :
                    ResponseEntity.status(HttpStatus.OK).body("Successful");
        } catch (Exception exception) {
            return generateFailureResponse(request, exception);
        }
    }//registerPlsUser

}

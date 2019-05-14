package com.dw.ngms.cis.im.controller;

import com.dw.ngms.cis.im.entity.*;
import com.dw.ngms.cis.im.service.CostCategoryService;
import com.dw.ngms.cis.im.service.CostSubService;
import com.dw.ngms.cis.uam.controller.MessageController;
import com.dw.ngms.cis.uam.dto.ExternalUserAssistantDTO;
import com.dw.ngms.cis.uam.entity.ExternalUserAssistant;
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
import java.util.ArrayList;
import java.util.List;

import static org.springframework.util.StringUtils.isEmpty;

/**
 * Created by swaroop on 2019/04/16.
 */
@RestController
@RequestMapping("/cisorigin.im/api/v1")
@CrossOrigin(origins = "*")
public class CostCategoryController extends MessageController {


    @Autowired
    private CostCategoryService costCategoryService;


    @Autowired
    private CostSubService costSubService;


 @GetMapping("/getCostCategories")
    public ResponseEntity<?> getCostCategories(HttpServletRequest request) {
        try {
            List<CostCategories> costCategoriesList = costCategoryService.getAllCostCategories();
            return (CollectionUtils.isEmpty(costCategoriesList)) ? generateEmptyResponse(request, "CostCategories(s) not found")
                    : ResponseEntity.status(HttpStatus.OK).body(costCategoriesList);
        } catch (Exception exception) {
            return generateFailureResponse(request, exception);
        }
    }//getCostCategories



    @GetMapping("/getCategories")
    public ResponseEntity<?> getCategories(HttpServletRequest request) {
        try {
            List<CostCategories> categoriesList = costCategoryService.getAllCostCategories();
            return (CollectionUtils.isEmpty(categoriesList)) ? generateEmptyResponse(request, "Categories(s) not found")
                    : ResponseEntity.status(HttpStatus.OK).body(categoriesList);
        } catch (Exception exception) {
            return generateFailureResponse(request, exception);
        }
    }//getFormatTypes


    @PostMapping("/createCategory")
    public ResponseEntity<?> setPropertyValueByName(HttpServletRequest request, @RequestBody @Valid CostCategories costCategories) {
        try {
            Long categoryId = this.costCategoryService.getCategoryId();
            System.out.println("categoryId before is "+categoryId);
            costCategories.setCategoryCode("COST" + Long.toString(categoryId));

            List<CostSubCategories> req = new ArrayList<>();
            if (costCategories.getSubCategoriesItems() != null) {
                for (CostSubCategories costSubCategoriesItems : costCategories.getSubCategoriesItems()) {
                    Long subCategoryCode = this.costSubService.getCostSubCategoryId();
                    costSubCategoriesItems.setCostSubCategoryCode("SUBCOST" + Long.toString(subCategoryCode));
                    System.out.println("categoryId after is "+categoryId);
                    costSubCategoriesItems.setCostCategoryId(categoryId);
                    costSubCategoriesItems.setCostCategoryCode(costCategories.getCategoryCode());
                    costSubCategoriesItems.setCostCategoryName(costCategories.getCategoryName());
                    req.add(costSubCategoriesItems);
                }
            }

            costCategories.setSubCategoriesItems(req);
            CostCategories costCategoriesSave = this.costCategoryService.saveCostCategory(costCategories);
            return ResponseEntity.status(HttpStatus.OK).body(costCategoriesSave);
        } catch (Exception exception) {
            System.out.println("exception is "+exception.getMessage());
            return generateFailureResponse(request, exception);
        }
    }//createCategory






    @RequestMapping(value = "/deactivateCategory", method = RequestMethod.POST)
    public ResponseEntity<?> deactivateCategory(HttpServletRequest request, @RequestBody @Valid CostCategories costCategories) throws IOException {
        try {
            Gson gson = new Gson();
            UserControllerResponse userControllerResponse = new UserControllerResponse();
            String json = null;
            CostCategories costCategoriesItem = this.costCategoryService.findByCategoryCode(costCategories.getCategoryCode());
            if (isEmpty(costCategoriesItem)){
                userControllerResponse.setMessage("Cost Categories not found");
                json = gson.toJson(userControllerResponse);
                return ResponseEntity.status(HttpStatus.OK).body(json);
            }
            if (!isEmpty(costCategoriesItem)) {
                costCategoriesItem.setIsActive("N");
                this.costCategoryService.saveCostCategory(costCategoriesItem);
            }
            userControllerResponse.setMessage("Category de-activated Successfully");
            json = gson.toJson(userControllerResponse);
            return ResponseEntity.status(HttpStatus.OK).body(json);
        } catch (Exception exception) {
            return generateFailureResponse(request, exception);
        }
    }//deactivateCategory


}

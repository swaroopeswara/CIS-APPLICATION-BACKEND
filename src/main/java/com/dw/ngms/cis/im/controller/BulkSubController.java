package com.dw.ngms.cis.im.controller;

import com.dw.ngms.cis.im.entity.BulkSubTypes;
import com.dw.ngms.cis.im.entity.CostSubCategories;
import com.dw.ngms.cis.im.service.BulkSubService;
import com.dw.ngms.cis.im.service.CostSubService;
import com.dw.ngms.cis.uam.controller.MessageController;
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
public class BulkSubController extends MessageController {

    @Autowired
    private BulkSubService bulkSubService;

    @GetMapping("/getBulkRequestSubTypesByTypeCode")
    public ResponseEntity<?> getSubCostCategoriesByCostCategoryCode(HttpServletRequest request,
                                                                    @RequestParam String bulkTypeCode) {
        try {
            List<BulkSubTypes> bulkSubTypesList = bulkSubService.getBulkRequestSubTypesByTypeCode(bulkTypeCode);
            return (CollectionUtils.isEmpty(bulkSubTypesList)) ? ResponseEntity.status(HttpStatus.OK).body(bulkSubTypesList)
                    : ResponseEntity.status(HttpStatus.OK).body(bulkSubTypesList);
        } catch (Exception exception) {
            return generateFailureResponse(request, exception);
        }
    }//getSubCostCategoriesByCostCategoryCode


    @PostMapping("/createBulkSubType")
    public ResponseEntity<?> createBulkSubType(HttpServletRequest request, @RequestBody @Valid BulkSubTypes bulkSubTypes) {
        try {
            Long subBulkId = this.bulkSubService.getSubBulkID();
            System.out.println("subBulkId is "+subBulkId);
            bulkSubTypes.setSubBulkCode("SUBBULK" + Long.toString(subBulkId));
            BulkSubTypes saveBulkTypes = this.bulkSubService.saveSubBulk(bulkSubTypes);
            return ResponseEntity.status(HttpStatus.OK).body(saveBulkTypes);
        } catch (Exception exception) {
            return generateFailureResponse(request, exception);
        }
    }//createCategory


}

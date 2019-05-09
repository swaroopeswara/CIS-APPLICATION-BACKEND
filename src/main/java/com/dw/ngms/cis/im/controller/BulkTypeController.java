package com.dw.ngms.cis.im.controller;

import com.dw.ngms.cis.im.entity.BulkSubTypes;
import com.dw.ngms.cis.im.entity.BulkTypes;
import com.dw.ngms.cis.im.service.BulkSubService;
import com.dw.ngms.cis.im.service.BulkTypeService;
import com.dw.ngms.cis.im.service.CostSubService;
import com.dw.ngms.cis.uam.controller.MessageController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by swaroop on 2019/04/16.
 */
@RestController
@RequestMapping("/cisorigin.im/api/v1")
@CrossOrigin(origins = "*")
public class BulkTypeController extends MessageController {


    @Autowired
    private BulkTypeService bulkTypeService;


    @Autowired
    private BulkSubService bulkSubService;


 @GetMapping("/getActiveBulkTypes")
    public ResponseEntity<?> getAllBulkTypes(HttpServletRequest request) {
        try {
            List<BulkTypes> bulkTypesList = bulkTypeService.getActiveBulkTypes();
            return (CollectionUtils.isEmpty(bulkTypesList)) ? generateEmptyResponse(request, "Bulk Type(s) not found")
                    : ResponseEntity.status(HttpStatus.OK).body(bulkTypesList);
        } catch (Exception exception) {
            return generateFailureResponse(request, exception);
        }
    }//getCostCategories




    @PostMapping("/createBulkRequestType")
    public ResponseEntity<?> createBulkRequestType(HttpServletRequest request, @RequestBody @Valid BulkTypes bulkTypes) {
        try {
            Long bulkTypeId = this.bulkTypeService.getBulkTypeId();
            System.out.println("Bulk Type Id "+bulkTypeId);
            bulkTypes.setBulkTypeCode("BULK" + Long.toString(bulkTypeId));

            List<BulkSubTypes> req = new ArrayList<>();
            if (!bulkTypes.getSubBulkItems().isEmpty()) {
                for (BulkSubTypes bulkSubItems : bulkTypes.getSubBulkItems()) {
                    Long subBulkId = this.bulkSubService.getSubBulkID();
                    bulkSubItems.setSubBulkCode("SUBBULK" + Long.toString(subBulkId));
                    System.out.println("Sub Bulk ID  is "+subBulkId);
                    bulkSubItems.setBulkTypeId(bulkTypeId);
                    bulkSubItems.setBulkTypeCode(bulkTypes.getBulkTypeCode());
                    bulkSubItems.setBulkTypeName(bulkTypes.getBulkTypeName());
                    req.add(bulkSubItems);
                }
            }

            bulkTypes.setSubBulkItems(req);
            BulkTypes saveBulk = this.bulkTypeService.saveBulk(bulkTypes);
            return ResponseEntity.status(HttpStatus.OK).body(saveBulk);
        } catch (Exception exception) {
            return generateFailureResponse(request, exception);
        }
    }//createCategory







}

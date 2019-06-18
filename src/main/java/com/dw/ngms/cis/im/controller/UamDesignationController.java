package com.dw.ngms.cis.im.controller;

import com.dw.ngms.cis.controller.MessageController;
import com.dw.ngms.cis.im.entity.UamDesignations;
import com.dw.ngms.cis.im.service.UamDesignationTypeService;
import com.dw.ngms.cis.uam.entity.InternalRole;
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
 * Created by swaroop on 2019/04/19.
 */
@RestController
@RequestMapping("/cisorigin.im/api/v1")
@CrossOrigin(origins = "*")
public class UamDesignationController extends MessageController {

    @Autowired
    private UamDesignationTypeService uamDesignationTypeService;


    @PostMapping("/createUamDesignation")
    public ResponseEntity<?> createUamDesignation(HttpServletRequest request, @RequestBody @Valid UamDesignations uamDesignations) {
        try {
            Long uamDesignId = this.uamDesignationTypeService.getUamDesignation();
            System.out.println("uamDesignId is "+uamDesignId);
            uamDesignations.setDesignationCode("DN" + Long.toString(uamDesignId));
            UamDesignations uamDesignationsSave = this.uamDesignationTypeService.saveUamDesignation(uamDesignations);
            return ResponseEntity.status(HttpStatus.OK).body(uamDesignationsSave);
        } catch (Exception exception) {
            return generateFailureResponse(request, exception);
        }
    }//createUamDesignation


    @GetMapping("/getUamDesignations")
    public ResponseEntity<?> getUamDesignations(HttpServletRequest request) {
        try {
            List<UamDesignations> formatTypesList = uamDesignationTypeService.getAllUamDesignations();
            return (CollectionUtils.isEmpty(formatTypesList)) ? generateEmptyResponse(request, "Uam Designations(s) not found")
                    : ResponseEntity.status(HttpStatus.OK).body(formatTypesList);
        } catch (Exception exception) {
            return generateFailureResponse(request, exception);
        }
    }//getUamDesignations


    @GetMapping(value = "/deleteUamDesignation")
    public ResponseEntity<?> deleteInternalRole(HttpServletRequest request, @RequestParam String designationCode) throws IOException {
        try {
            if(designationCode!= null && !isEmpty(designationCode)) {
                UamDesignations uamDesignations = this.uamDesignationTypeService.findByUamDesignationCode(designationCode);
                System.out.println("Intenal user code is " +uamDesignations.getDesignationCode());
                if(!isEmpty(uamDesignations)){
                    this.uamDesignationTypeService.deleteUamDesignation(uamDesignations);
                    return ResponseEntity.status(HttpStatus.OK).body("Uam Designation deleted successfully");
                }
            }
            return ResponseEntity.status(HttpStatus.OK).body("Uam Designation not found for given Designation Code "+designationCode);

        } catch (Exception exception) {
            return generateFailureResponse(request, exception);
        }
    }





}

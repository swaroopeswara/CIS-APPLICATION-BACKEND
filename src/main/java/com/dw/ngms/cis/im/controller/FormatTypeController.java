package com.dw.ngms.cis.im.controller;

import com.dw.ngms.cis.controller.MessageController;
import com.dw.ngms.cis.im.entity.DeliveryMethods;
import com.dw.ngms.cis.im.entity.FormatTypes;
import com.dw.ngms.cis.im.entity.MediaTypes;
import com.dw.ngms.cis.im.entity.Requests;
import com.dw.ngms.cis.im.service.FormatTypeService;

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
public class FormatTypeController extends MessageController {

    @Autowired
    private FormatTypeService formatTypeService;


    @PostMapping("/createFormatType")
    public ResponseEntity<?> createFormatType(HttpServletRequest request, @RequestBody @Valid FormatTypes formatTypes) {
        try {
            Long formatTypeId = this.formatTypeService.getFormatType();
            System.out.println("formatTypeId is "+formatTypeId);
            formatTypes.setFormatTypeCode("IMF" + Long.toString(formatTypeId));
            formatTypes.setIsDeleted("N");
            FormatTypes formatTypesSave = this.formatTypeService.saveFormatType(formatTypes);
            return ResponseEntity.status(HttpStatus.OK).body(formatTypesSave);
        } catch (Exception exception) {
            return generateFailureResponse(request, exception);
        }
    }//createCategory


    @GetMapping("/getFormatTypes")
    public ResponseEntity<?> getFormatTypes(HttpServletRequest request) {
        try {
            List<FormatTypes> formatTypesList = formatTypeService.getAllFormatTypes();
            return (CollectionUtils.isEmpty(formatTypesList)) ? generateEmptyResponse(request, "FormatType(s) not found")
                    : ResponseEntity.status(HttpStatus.OK).body(formatTypesList);
        } catch (Exception exception) {
            return generateFailureResponse(request, exception);
        }
    }//getFormatTypes


    @RequestMapping(value = "/deleteFormatType", method = RequestMethod.POST)
    public ResponseEntity<?> deleteRole(HttpServletRequest request, @RequestBody @Valid FormatTypes formatTypes) throws IOException {
        try {
            FormatTypes deliveryMethods = this.formatTypeService.getFormatByCode(formatTypes.getFormatTypeCode());
            if (isEmpty(deliveryMethods)) {
                return generateEmptyResponse(request, "Format Types are  not found");
            }
            if (!isEmpty(deliveryMethods)) {
                deliveryMethods.setIsDeleted(formatTypes.getIsDeleted());
                FormatTypes formatTypesSave = this.formatTypeService.saveFormatType(deliveryMethods);
                return ResponseEntity.status(HttpStatus.OK).body("Format Type Deleted Successfully");
            }
            return ResponseEntity.status(HttpStatus.OK).body("Format Types are  not found");
        } catch (Exception exception) {
            return generateFailureResponse(request, exception);
        }
    }



}

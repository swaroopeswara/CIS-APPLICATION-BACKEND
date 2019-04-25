package com.dw.ngms.cis.im.controller;

import com.dw.ngms.cis.im.entity.DeliveryMethods;
import com.dw.ngms.cis.im.entity.FormatTypes;
import com.dw.ngms.cis.im.service.DeliveryMethodService;
import com.dw.ngms.cis.uam.controller.MessageController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.List;

/**
 * Created by swaroop on 2019/04/19.
 */
@RestController
@RequestMapping("/cisorigin.im/api/v1")
@CrossOrigin(origins = "*")
public class DeliveryMethodController extends MessageController {

    @Autowired
    private DeliveryMethodService deliveryMethodService;


    @PostMapping("/createDeliveryMethod")
    public ResponseEntity<?> createDeliveryMethod(HttpServletRequest request, @RequestBody @Valid DeliveryMethods deliveryMethods) {
        try {
            Long deleviryMethodId = this.deliveryMethodService.getDeleviryMethodId();
            System.out.println("deleviryMethodId is "+deleviryMethodId);
            deliveryMethods.setDeliveryMethodCode("DLM" + Long.toString(deleviryMethodId));
            DeliveryMethods deliveryMethodSave = this.deliveryMethodService.saveDeliveryMethod(deliveryMethods);
            return ResponseEntity.status(HttpStatus.OK).body(deliveryMethodSave);
        } catch (Exception exception) {
            return generateFailureResponse(request, exception);
        }
    }//createCategory


    @GetMapping("/getDeliveryMethods")
    public ResponseEntity<?> getDeliveryMethods(HttpServletRequest request) {
        try {
            List<DeliveryMethods> deliveryMethodsList = deliveryMethodService.getAllDeliveryMethos();
            return (CollectionUtils.isEmpty(deliveryMethodsList)) ? generateEmptyResponse(request, "DeliveryMethod(s) not found")
                    : ResponseEntity.status(HttpStatus.OK).body(deliveryMethodsList);
        } catch (Exception exception) {
            return generateFailureResponse(request, exception);
        }
    }//getFormatTypes



}

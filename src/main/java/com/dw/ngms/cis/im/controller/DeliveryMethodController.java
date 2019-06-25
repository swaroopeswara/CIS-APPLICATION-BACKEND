package com.dw.ngms.cis.im.controller;

import com.dw.ngms.cis.controller.MessageController;
import com.dw.ngms.cis.im.entity.DeliveryMethods;
import com.dw.ngms.cis.im.entity.FormatTypes;
import com.dw.ngms.cis.im.entity.Requests;
import com.dw.ngms.cis.im.repository.RequestRepository;
import com.dw.ngms.cis.im.service.DeliveryMethodService;

import com.dw.ngms.cis.im.service.RequestService;
import com.dw.ngms.cis.uam.entity.ExternalUserRoles;
import com.dw.ngms.cis.uam.entity.InternalUserRoles;
import com.dw.ngms.cis.uam.entity.Roles;
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
public class DeliveryMethodController extends MessageController {

    @Autowired
    private DeliveryMethodService deliveryMethodService;


    @Autowired
    private RequestService requestService;


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



    @RequestMapping(value = "/deleteDeliveryMethod", method = RequestMethod.POST)
    public ResponseEntity<?> deleteRole(HttpServletRequest request, @RequestBody @Valid DeliveryMethods deliveryMethod) throws IOException {
        try {
            DeliveryMethods deliveryMethods = this.deliveryMethodService.getDeliveryMethodByCode(deliveryMethod.getDeliveryMethodCode());
            if (isEmpty(deliveryMethods)) {
                return generateEmptyResponse(request, "Delivery Methods are  not found");
            }
            if (!isEmpty(deliveryMethods)) {
                    List<Requests> requestsList = this.requestService.getRequestByDeliveryname(deliveryMethod.getDeliveryMethodName());
                    if(requestsList.size() >0 && !requestsList.isEmpty() && requestsList.get(0).getDeliveryMethod()!= null){
                        return generateEmptyWithOKResponse(request, "already in use");
                    }else{
                        System.out.println("Delete this Delivery Method Name");
                        this.deliveryMethodService.deleteDeliveyMethod(deliveryMethods);
                        return generateEmptyWithOKResponse(request, "Delivery Method Deleted successfully");
                    }


            }
            return ResponseEntity.status(HttpStatus.OK).body("User Role Deleted Successfully");
        } catch (Exception exception) {
            return generateFailureResponse(request, exception);
        }
    }





}

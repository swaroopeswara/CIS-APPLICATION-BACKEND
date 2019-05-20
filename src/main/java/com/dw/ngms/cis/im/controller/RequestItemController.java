package com.dw.ngms.cis.im.controller;

import com.dw.ngms.cis.controller.MessageController;
import com.dw.ngms.cis.im.dto.RequestItemsDTO;
import com.dw.ngms.cis.im.entity.RequestItems;
import com.dw.ngms.cis.im.entity.RequestKinds;
import com.dw.ngms.cis.im.entity.RequestTypes;
import com.dw.ngms.cis.im.entity.Requests;
import com.dw.ngms.cis.im.service.RequestItemService;
import com.dw.ngms.cis.im.service.RequestService;
import com.dw.ngms.cis.uam.entity.ExternalUser;
import com.google.gson.Gson;
import jdk.nashorn.internal.parser.JSONParser;
import org.apache.commons.collections.CollectionUtils;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.springframework.util.StringUtils.isEmpty;

/**
 * Created by swaroop on 2019/04/19.
 */
@RestController
@RequestMapping("/cisorigin.im/api/v1")
@CrossOrigin(origins = "*")
public class RequestItemController extends MessageController {

    @Autowired
    private RequestItemService requestItemService;

    @Autowired
    private RequestService requestsService;


   /*@GetMapping("/getRequestsOfUser")
    public ResponseEntity<?> getRequestsOfUser(HttpServletRequest request,
                                               @RequestParam(required=false) String provinceCode,
                                               @RequestParam(required=false) String userCode) {
        try {
            List<Requests> requestList = new ArrayList<>();
            if(StringUtils.isEmpty(provinceCode) || "all".equalsIgnoreCase(provinceCode.trim())){
                requestList = requestService.getAllRequests();
            }else if(!StringUtils.isEmpty(userCode) && !StringUtils.isEmpty(provinceCode.trim()) ){
                requestList = requestService.getRequestByUserCodeProvinceCode(userCode,provinceCode);
            }
            return (CollectionUtils.isEmpty(requestList)) ? generateEmptyResponse(request, "Request(s) not found")
                    : ResponseEntity.status(HttpStatus.OK).body(requestList);
        } catch (Exception exception) {
            return generateFailureResponse(request, exception);
        }
    }//getRequestsOfUser
*/

    @PostMapping("/createRequestItem")
    public ResponseEntity<?> createRequestType(HttpServletRequest request, @RequestBody @Valid RequestItems requestItems) {
        Gson gson = new Gson();
        try {
            Long requestItemCode = this.requestItemService.getRequestItemId();
            System.out.println("requestItemCode is " + requestItemCode);
            requestItems.setRequestItemCode("REQITEM" + Long.toString(requestItemCode));
            RequestItemsDTO requestItemsDTO = new RequestItemsDTO();
            requestItemsDTO.setGazetteType1(requestItems.getGazetteType1());
            requestItemsDTO.setGazetteType2(requestItems.getGazetteType2());
            requestItemsDTO.setRequestCost(requestItems.getRequestCost());
            requestItemsDTO.setRequestHours(requestItems.getRequestHours());
            String requestJson = gson.toJson(requestItemsDTO);
            requestItems.setResultJson(requestJson);
            RequestItems requestItemsSave = this.requestItemService.saveRequestItem(requestItems);

            List<RequestItems> getAllRequestItems = this.requestItemService.getRequestsByRequestItemCode(requestItemsSave.getRequestCode());

            RequestItems[] itemsArray = new RequestItems[getAllRequestItems.size()];
            itemsArray = getAllRequestItems.toArray(itemsArray);

            Double totalSum = 0.00;
            for (int i = 0; i < itemsArray.length; i++) {
                totalSum = totalSum + Double.parseDouble(itemsArray[i].getRequestCost());
            }
            System.out.println("Total is" + totalSum);

            Requests requests = this.requestsService.getRequestsByRequestCode(requestItemsSave.getRequestCode());
            requests.setTotalAmount(String.valueOf(totalSum));
            this.requestsService.saveRequest(requests);

            return ResponseEntity.status(HttpStatus.OK).body(requestItemsSave);
        } catch (Exception exception) {
            return generateFailureResponse(request, exception);
        }
    }//createRequestItem


    @GetMapping("/getRequestItemsOfRequest")
    public ResponseEntity<?> getRequestItemsOfRequest(HttpServletRequest request,
                                                      @RequestParam String requestCode) {
        try {
            List<RequestItems> requestItemsList = requestItemService.getRequestsByRequestCode(requestCode);
            return (org.springframework.util.CollectionUtils.isEmpty(requestItemsList)) ? generateEmptyResponse(request, "RequestKind(s) not found")
                    : ResponseEntity.status(HttpStatus.OK).body(requestItemsList);
        } catch (Exception exception) {
            return generateFailureResponse(request, exception);
        }
    }//getRequestItemsOfRequest


    @RequestMapping(value = "/deleteRequestItem", method = RequestMethod.POST)
    public ResponseEntity<?> deleteRequestItem(HttpServletRequest request,
                                               @RequestBody @Valid RequestItems items) throws IOException {
        try {
            RequestItems requestItems = this.requestItemService.getRequestsByRequestCodeAndItemCode(items.getRequestCode(), items.getRequestItemCode());
            if (isEmpty(requestItems)) {
                return generateEmptyResponse(request, "RequestItems are  not found");
            }
            if (!isEmpty(requestItems)) {
                this.requestItemService.deleteRequestItem(requestItems);
                List<RequestItems> getAllRequestItems = this.requestItemService.getRequestsByRequestItemCode(requestItems.getRequestCode());

                RequestItems[] itemsArray = new RequestItems[getAllRequestItems.size()];
                itemsArray = getAllRequestItems.toArray(itemsArray);

                Double totalSum = 0.00;
                for (int i = 0; i < itemsArray.length; i++) {
                    totalSum = totalSum + Double.parseDouble(itemsArray[i].getRequestCost());
                }
                System.out.println("Total sum after delete is" + totalSum);


                Requests requests = this.requestsService.getRequestsByRequestCode(requestItems.getRequestCode());
                requests.setTotalAmount(String.valueOf(totalSum));
                this.requestsService.saveRequest(requests);

                return ResponseEntity.status(HttpStatus.OK).body("Request Item Deleted Successfully");
            }

            return generateEmptyResponse(request, "RequestItems are  not found");
        } catch (Exception exception) {
            return generateFailureResponse(request, exception);
        }
    }


}

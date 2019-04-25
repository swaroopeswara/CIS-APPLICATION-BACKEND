package com.dw.ngms.cis.im.controller;

import com.dw.ngms.cis.im.entity.RequestItems;
import com.dw.ngms.cis.im.entity.RequestTypes;
import com.dw.ngms.cis.im.entity.Requests;
import com.dw.ngms.cis.im.service.RequestItemService;
import com.dw.ngms.cis.im.service.RequestService;
import com.dw.ngms.cis.im.service.RequestTypeService;
import com.dw.ngms.cis.uam.controller.MessageController;
import com.dw.ngms.cis.uam.dto.UserDTO;
import com.dw.ngms.cis.uam.entity.ExternalUser;
import com.dw.ngms.cis.uam.entity.ExternalUserRoles;
import com.dw.ngms.cis.uam.entity.InternalUserRoles;
import com.dw.ngms.cis.uam.entity.User;
import com.dw.ngms.cis.uam.storage.StorageService;
import com.dw.ngms.cis.uam.utilities.Constants;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import static org.springframework.util.StringUtils.isEmpty;

/**
 * Created by swaroop on 2019/04/19.
 */
@RestController
@RequestMapping("/cisorigin.im/api/v1")
@CrossOrigin(origins = "*")
public class RequestController extends MessageController {

    @Autowired
    private RequestService requestService;

    @Autowired
    private RequestItemService requestItemService;

    @Autowired
    private StorageService testService;


   @GetMapping("/getRequestsOfUser")
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




    @PostMapping("/createRequest")
    public ResponseEntity<?> createRequest(HttpServletRequest request, @RequestBody @Valid Requests requests) {
        try {
            Long requestId = this.requestService.getRequestId();
            System.out.println("requestTypeId is "+requestId);
            requests.setRequestId(requestId);
            requests.setRequestCode("REQ" + Long.toString(requestId));

            List<RequestItems> req = new ArrayList<>();
            if (!requests.getRequestItems().isEmpty()) {
                for (RequestItems requestItems : requests.getRequestItems()) {
                    Long requestItemCode = this.requestItemService.getRequestItemId();
                    requestItems.setRequestItemId(requestItemCode);
                    requestItems.setRequestId(requestId);
                    requestItems.setRequestItemCode("REQITEM" + Long.toString(requestItemCode));
                    requestItems.setRequestCode(requests.getRequestCode());
                    req.add(requestItems);
                }
            }
            requests.setRequestItems(req);
            Requests requestToSave = this.requestService.saveRequest(requests);



            return ResponseEntity.status(HttpStatus.OK).body(requestToSave);
        } catch (Exception exception) {
            return generateFailureResponse(request, exception);
        }
    }//createRequest




    @PostMapping("/uploadPaymentConfirmation")
    public ResponseEntity<?> handleFileUpload(HttpServletRequest request, @RequestParam("file") MultipartFile file,
                                              @RequestParam("requestCode") String requestCode

    ) {
        String message = "";
        List<String> files = new ArrayList<String>();
        try {
            if (file.isEmpty()) {
                return generateEmptyResponse(request, "File Not Found to upload, Please upload a file");
            } else if(requestCode!= null && !isEmpty(requestCode) ) {
                    Requests requests = this.requestService.getRequestsByRequestCode(requestCode);
                     if (requests != null && requests != null) {
                        String fileName = testService.store(file);
                        files.add(file.getOriginalFilename());
                        requests.setInvoiceFilePath(Constants.uploadDirectoryPath + fileName);
                        message = "You successfully uploaded " + requests.getInvoiceFilePath() + "!";
                        requestService.saveRequest(requests);
                        return ResponseEntity.status(HttpStatus.OK).body(message);
                    } else {
                        return generateEmptyResponse(request, "No Internal User Roles  found");
                    }
                }
            return generateEmptyResponse(request, "No Internal User Roles  found");
        } catch (Exception exception) {
            message = "FAIL to upload " + file.getOriginalFilename() + "!";
            return generateFailureResponse(request, exception);
        }
    }//uploadPaymentConfirmation
}

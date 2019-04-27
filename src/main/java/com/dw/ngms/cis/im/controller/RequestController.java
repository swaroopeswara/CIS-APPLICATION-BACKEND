package com.dw.ngms.cis.im.controller;

import static org.springframework.util.StringUtils.isEmpty;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.dw.ngms.cis.im.entity.RequestItems;
import com.dw.ngms.cis.im.entity.Requests;
import com.dw.ngms.cis.im.service.RequestItemService;
import com.dw.ngms.cis.im.service.RequestService;
import com.dw.ngms.cis.uam.controller.MessageController;
import com.dw.ngms.cis.uam.service.TaskService;
import com.dw.ngms.cis.uam.storage.StorageService;
import com.dw.ngms.cis.uam.utilities.Constants;
import com.dw.ngms.cis.workflow.api.ProcessAdditionalInfo;
import com.dw.ngms.cis.workflow.model.Target;

import lombok.extern.slf4j.Slf4j;

/**
 * Created by swaroop on 2019/04/19.
 */
@Slf4j
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
	@Autowired
    private TaskService taskService;

	@GetMapping("/getTaskTargetFlows")
	public ResponseEntity<?> getTaskTargetFlows(HttpServletRequest request, @RequestParam Long taskid){
		if (StringUtils.isEmpty(taskid)) {
            return generateFailureResponse(request, new Exception("Invalid task details passed"));
        }
        try {
        	List<Target> targets = taskService.getTaskTargetFlows(taskid);
        	return (CollectionUtils.isEmpty(targets)) ? generateEmptyResponse(request, "Target(s) not found") :
        		ResponseEntity.status(HttpStatus.OK).body(targets);
        } catch (Exception exception) {
            return generateFailureResponse(request, exception);
        }
	}//getTaskTargetFlows
	
	@PostMapping("/processUserState")
	public ResponseEntity<?> processUserState(HttpServletRequest request, @RequestBody @Valid ProcessAdditionalInfo additionalInfo){
		if (additionalInfo == null || StringUtils.isEmpty(additionalInfo.getRequestCode())  || StringUtils.isEmpty(additionalInfo.getTaskId()) || 
				StringUtils.isEmpty(additionalInfo.getTargetSequenceId())) {
            return generateFailureResponse(request, new Exception("Invalid request/task details passed"));
        }
        try {        	
        	this.updateRequestProvinceAndSectionCodes(additionalInfo);
        	Target target = taskService.processUserState(additionalInfo);        	
        	return (target ==null) ? generateEmptyResponse(request, "Target not found") :
        		ResponseEntity.status(HttpStatus.OK).body(target.getDescription() + " succesfully completed");
        } catch (Exception exception) {
            return generateFailureResponse(request, exception);
        }
	}//processUserState
	
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
            log.info("requestTypeId is "+requestId);
            requests.setRequestId(requestId);
            requests.setRequestCode("REQ" + Long.toString(requestId));

//            List<RequestItems> req = new ArrayList<>();
//            if (!requests.getRequestItems().isEmpty()) {
//                for (RequestItems requestItems : requests.getRequestItems()) {
//                    Long requestItemCode = this.requestItemService.getRequestItemId();
//                    requestItems.setRequestItemId(requestItemCode);
//                    requestItems.setRequestId(requestId);
//                    requestItems.setRequestItemCode("REQITEM" + Long.toString(requestItemCode));
//                    requestItems.setRequestCode(requests.getRequestCode());
//                    req.add(requestItems);
//                }
//            }
//            requests.setRequestItems(req);
            Requests requestToSave = requests;//this.requestService.saveRequest(requests);

            //FIXME need to get the 'processId'
            taskService.startProcess("infoRequest", requests);

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
    
    private void updateRequestProvinceAndSectionCodes(ProcessAdditionalInfo additionalInfo) {
    	if(additionalInfo != null && additionalInfo.getRequestCode() != null) {
	    	Requests request = requestService.getRequestsByRequestCode(additionalInfo.getRequestCode());
	    	if(request != null) {		
				if(!StringUtils.isEmpty(additionalInfo.getProvinceCode()))
					request.setProvinceCode(additionalInfo.getProvinceCode());
//				if(!StringUtils.isEmpty(additionalInfo.getSectionCode()))
//					request.setSectionCode(additionalInfo.getSectionCode());//FIXME
	    	}
		}
	}//updateRequestProvinceAndSectionCodes
}

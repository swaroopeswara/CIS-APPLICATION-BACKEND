package com.dw.ngms.cis.im.controller;

import static org.springframework.util.StringUtils.isEmpty;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLConnection;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.FileCopyUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.dw.ngms.cis.im.dto.InvoiceDTO;
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
import com.itextpdf.text.pdf.AcroFields;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.PdfStamper;

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
    public ResponseEntity<?> getTaskTargetFlows(HttpServletRequest request, @RequestParam Long taskid) {
        if (taskid == null) {
            return generateFailureResponse(request, new Exception("Invalid task details passed"));
        }
        try {
            List<Target> targets = taskService.getTaskTargetFlows(taskid);//FIXME confirm param taskid / request code?
            return (CollectionUtils.isEmpty(targets)) ? generateEmptyResponse(request, "Target(s) not found") :
                    ResponseEntity.status(HttpStatus.OK).body(targets);
        } catch (Exception exception) {
            return generateFailureResponse(request, exception);
        }
    }//getTaskTargetFlows

    @GetMapping("/getRequestStatus")
    public ResponseEntity<?> getTaskCurrentStatus(HttpServletRequest request, @RequestParam String requestcode) {
        if (StringUtils.isEmpty(requestcode)) {
            return generateFailureResponse(request, new Exception("Invalid request code passed"));
        }
        try {
            String status = taskService.getTaskCurrentStatus(requestcode);
            return (StringUtils.isEmpty(status)) ? generateEmptyResponse(request, "Request status not found") :
                    ResponseEntity.status(HttpStatus.OK).body(status);
        } catch (Exception exception) {
            return generateFailureResponse(request, exception);
        }
    }//getTaskCurrentStatus

    @PostMapping("/processUserState")
    public ResponseEntity<?> processUserState(HttpServletRequest request, @RequestBody @Valid ProcessAdditionalInfo additionalInfo) {
        if (additionalInfo == null || StringUtils.isEmpty(additionalInfo.getRequestCode()) || StringUtils.isEmpty(additionalInfo.getTaskId()) ||
                StringUtils.isEmpty(additionalInfo.getTargetSequenceId())) {
            return generateFailureResponse(request, new Exception("Invalid request/task details passed"));
        }
        try {
            this.updateRequestProvinceAndSectionCodes(additionalInfo);
            Target target = taskService.processUserState(additionalInfo);
            return (target == null) ? generateEmptyResponse(request, "Target not found") :
                    ResponseEntity.status(HttpStatus.OK).body(target.getDescription() + " succesfully completed");
        } catch (Exception exception) {
            return generateFailureResponse(request, exception);
        }
    }//processUserState

    @GetMapping("/getRequestsOfUser")
    public ResponseEntity<?> getRequestsOfUser(HttpServletRequest request,
                                               @RequestParam(required = false) String provinceCode,
                                               @RequestParam(required = false) String userCode) {
        try {
            List<Requests> requestList = new ArrayList<>();
            if (StringUtils.isEmpty(provinceCode) || "all".equalsIgnoreCase(provinceCode.trim())) {
                requestList = requestService.getAllRequests();
            } else if (!StringUtils.isEmpty(userCode) && !StringUtils.isEmpty(provinceCode.trim())) {
                requestList = requestService.getRequestByUserCodeProvinceCode(userCode, provinceCode);
            }
            return (CollectionUtils.isEmpty(requestList)) ? ResponseEntity.status(HttpStatus.OK).body(requestList)
                    : ResponseEntity.status(HttpStatus.OK).body(requestList);
        } catch (Exception exception) {
            return generateFailureResponse(request, exception);
        }
    }//getRequestsOfUser


    @GetMapping("/getRequestByRequestCode")
    public ResponseEntity<?> getRequestByRequestCode(HttpServletRequest request,
                                                     @RequestParam String requestCode) {
        try {

            Requests requests = this.requestService.getRequestsByRequestCode(requestCode);
            return (requests != null) ? ResponseEntity.status(HttpStatus.OK).body(requests)
                    : generateEmptyResponse(request, "Request(s) not found");
        } catch (Exception exception) {
            return generateFailureResponse(request, exception);
        }
    }//RequestController


    @GetMapping("/updateRequestOnLapse")
    public ResponseEntity<?> updateRequestOnLapse(HttpServletRequest request,
                                                  @RequestParam @Valid String reuestcode, @RequestParam @Valid Integer lapsetime,
                                                  @RequestParam @Valid boolean islapsed) {
    	log.info("Start processing updateRequestOnLapse");
        if (StringUtils.isEmpty(reuestcode)) {
            return generateFailureResponse(request, new Exception("Invalid request code"));
        }
        try {
            boolean isProcessed = requestService.updateRequestOnLapse(reuestcode, lapsetime, islapsed);
            return (!isProcessed) ? generateFailureResponse(request, new Exception("Failed to update request")) :
                    ResponseEntity.status(HttpStatus.OK).body("Request succesfully processed");
        } catch (Exception exception) {
            return generateFailureResponse(request, exception);
        }
    }//updateRequestOnLapse


    @PostMapping("/uploadPaymentConfirmation")
    public ResponseEntity<?> handleFileUpload(HttpServletRequest request, @RequestParam("file") MultipartFile file,
                                              @RequestParam("taskId") Long taskId,
                                              @RequestParam("requestCode") String requestCode,
                                              @RequestParam("provinceCode") String provinceCode,
                                              @RequestParam("sectionCode") String sectionCode,
                                              @RequestParam("targetSequenceId") String targetSequenceId,
                                              @RequestParam("userCode") String userCode,
                                              @RequestParam("userName") String userName,
                                              @RequestParam("url") String url
    ) {
        String message = "";
        List<String> files = new ArrayList<String>();
        try {
            if (file.isEmpty()) {
                return generateEmptyResponse(request, "File Not Found to upload, Please upload a file");
            } else if (requestCode != null && !isEmpty(requestCode)) {
                Requests requests = this.requestService.getRequestsByRequestCode(requestCode);
                if (requests != null && requests != null) {
                    String fileName = testService.store(file);
                    files.add(file.getOriginalFilename());
                    requests.setInvoiceFilePath(Constants.uploadDirectoryPath + fileName);
                    message = "You successfully uploaded " + requests.getInvoiceFilePath() + "!";
                    requestService.saveRequest(requests);

                    ProcessAdditionalInfo processAdditionalInfo = new ProcessAdditionalInfo();
                    processAdditionalInfo.setTaskId(taskId);
                    processAdditionalInfo.setRequestCode(requestCode);
                    processAdditionalInfo.setProvinceCode(provinceCode);
                    processAdditionalInfo.setSectionCode(sectionCode);
                    processAdditionalInfo.setTargetSequenceId(targetSequenceId);
                    processAdditionalInfo.setUserCode(userCode);
                    processAdditionalInfo.setUserName(userName);
                    processAdditionalInfo.setUrl(url);


                    processUserState(request, processAdditionalInfo);

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


    @PostMapping("/createRequest")
    public ResponseEntity<?> createRequest(HttpServletRequest request, @RequestBody @Valid Requests requests) {
        try {
            Long requestId = this.requestService.getRequestId();
            log.info("requestTypeId is " + requestId);
            requests.setRequestId(requestId);
            requests.setRequestCode("REQ" + Long.toString(requestId));
            requests.setReferenceNumber("REQ" + Long.toString(requestId));
            requests.setPaymentStatus("PENDING");
            //String processId = requests.getProcessId();
            String processId = "infoRequest";
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

            taskService.startProcess(processId, requests);

            return ResponseEntity.status(HttpStatus.OK).body(requestToSave);
        } catch (Exception exception) {
            return generateFailureResponse(request, exception);
        }
    }//createRequest




   /* @PostMapping("/uploadPaymentConfirmation")
     public ResponseEntity<?> uploadPaymentConfirmation(HttpServletRequest request, @RequestParam("file") MultipartFile file
                                                       ,@RequestBody @Valid ProcessAdditionalInfo info

     ) {
         String message = "";
         List<String> files = new ArrayList<String>();
         try {
             if (file.isEmpty()) {
                 return generateEmptyResponse(request, "File Not Found to upload, Please upload a file");
             } else if (info.getUserCode() != null && !isEmpty(info.getRequestCode())) {
                 Requests requests = this.requestService.getRequestsByRequestCode(info.getRequestCode());
                 if (requests != null && requests != null) {
                     String fileName = testService.store(file);
                     files.add(file.getOriginalFilename());
                     requests.setPopFilePath(Constants.uploadDirectoryPath + fileName);
                     message = "You successfully uploaded " + requests.getInvoiceFilePath() + "!";
                     requestService.saveRequest(requests);

                     ProcessAdditionalInfo processAdditionalInfo = new ProcessAdditionalInfo();
                     processAdditionalInfo.setTaskId(info.getTaskId());
                     processAdditionalInfo.setRequestCode(info.getRequestCode());
                     processAdditionalInfo.setProvinceCode(info.getProvinceCode());
                     processAdditionalInfo.setSectionCode(info.getSectionCode());
                     processAdditionalInfo.setTargetSequenceId(info.getTargetSequenceId());
                     processAdditionalInfo.setUserCode(info.getUserCode());
                     processAdditionalInfo.setUserName(info.getUserName());
                     processUserState(request,processAdditionalInfo);

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
     }//uploadPaymentConfirmation*/

    private void updateRequestProvinceAndSectionCodes(ProcessAdditionalInfo additionalInfo) {
        if (additionalInfo != null && additionalInfo.getRequestCode() != null) {
            Requests request = requestService.getRequestsByRequestCode(additionalInfo.getRequestCode());
            if (request != null) {
                if (!StringUtils.isEmpty(additionalInfo.getProvinceCode()))
                    request.setProvinceCode(additionalInfo.getProvinceCode());
                if (!StringUtils.isEmpty(additionalInfo.getSectionCode()))
                    request.setSectionCode(additionalInfo.getSectionCode());
            }
        }
    }//updateRequestProvinceAndSectionCodes

    @SuppressWarnings("unused")
    @PostMapping("/generateInvoice")
    public ResponseEntity<?> saveAndSendInvoiceMail(HttpServletRequest request, HttpServletResponse response,
                                                    @RequestParam("requestCode") String requestCode,
                                                    @RequestBody @Valid InvoiceDTO invoiceDTO) throws IOException {
        try {
            Requests req = requestService.getRequestsByRequestCode(requestCode);
            if (req != null) {
                ClassPathResource template = new ClassPathResource(Constants.pdfTemplate);
                String timeStamp = new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss").format(new Date());
                String filename = Constants.fileNamePDF;
                filename = timeStamp + "_" + filename;
                File file = new File(Constants.invoiceDirectory + filename);
                if (template.exists()) {
                    try {
                        PdfReader reader = new PdfReader(template.getFilename());
                        PdfStamper stamper = new PdfStamper(reader, new FileOutputStream(file));
                        AcroFields form = stamper.getAcroFields();
                        System.out.println("");
                        form.setField("FULL_NAME", invoiceDTO.getFullName());
                        SimpleDateFormat fmt = new SimpleDateFormat("dd MMMM yyyy");
                        form.setField("DATE", fmt.format(new Date()));
                        form.setField("ORGANIZATION", invoiceDTO.getOrgnization());
                        form.setField("REQUEST_NUMBER", invoiceDTO.getRequestNumber());
                        form.setField("REQUEST_TYPE", invoiceDTO.getRequestType());
                        form.setField("TELEPHONE", invoiceDTO.getTelephone());
                        form.setField("POSTAL_ADDRESS", invoiceDTO.getPostalAddress());
                        form.setField("EMAIL", invoiceDTO.getEmail());
                        form.setField("MOBILE", invoiceDTO.getMoible());
                        form.setField("DEPOSIT", invoiceDTO.getAmount());
                        stamper.setFormFlattening(true);
                        stamper.close();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    String mimeType = URLConnection.guessContentTypeFromName(file.getName());
                    if (mimeType == null) {
                        mimeType = "application/octet-stream";
                    }
                    response.setContentType(mimeType);
                    response.setHeader("Content-Disposition", String.format("inline; filename=\"" + file.getName() + "\""));
                    response.setContentLength((int) file.length());
                    InputStream inputStream = new BufferedInputStream(new FileInputStream(file));
                    FileCopyUtils.copy(inputStream, response.getOutputStream());
                    req.setInvoiceFilePath(Constants.invoiceDirectory + "/" + filename);
                    Requests updatedRequest = requestService.saveRequest(req);
                    ProcessAdditionalInfo processAdditionalInfo = getProcessAdditionalInfo(invoiceDTO);
                    processUserState(request, processAdditionalInfo);
                    return ResponseEntity.status(HttpStatus.OK).body("Generated Invoice Successfully");
                }
            } else {
                return generateEmptyResponse(request, "No Request found with requestCode " + requestCode);
            }

        } catch (Exception exception) {
            return generateFailureResponse(request, exception);
        }
        return generateEmptyResponse(request, "No Request found with requestCode " + requestCode);
    }

    private ProcessAdditionalInfo getProcessAdditionalInfo(@RequestBody @Valid InvoiceDTO invoiceDTO) {
        ProcessAdditionalInfo processAdditionalInfo = new ProcessAdditionalInfo();
        if (invoiceDTO.getProcessAdditionalInfo() != null) {
            processAdditionalInfo.setTaskId(invoiceDTO.getProcessAdditionalInfo().getTaskId());
            processAdditionalInfo.setRequestCode(invoiceDTO.getProcessAdditionalInfo().getRequestCode());
            processAdditionalInfo.setProvinceCode(invoiceDTO.getProcessAdditionalInfo().getProvinceCode());
            processAdditionalInfo.setSectionCode(invoiceDTO.getProcessAdditionalInfo().getSectionCode());
            processAdditionalInfo.setTargetSequenceId(invoiceDTO.getProcessAdditionalInfo().getTargetSequenceId());
            processAdditionalInfo.setUserCode(invoiceDTO.getProcessAdditionalInfo().getUserCode());
            processAdditionalInfo.setUserName(invoiceDTO.getProcessAdditionalInfo().getUserName());
            invoiceDTO.setProcessAdditionalInfo(processAdditionalInfo);
        }
        return processAdditionalInfo;
    }
}

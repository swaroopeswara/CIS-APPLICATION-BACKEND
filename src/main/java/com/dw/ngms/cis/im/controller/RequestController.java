package com.dw.ngms.cis.im.controller;


import com.dw.ngms.cis.controller.MessageController;
import com.dw.ngms.cis.im.dto.InvoiceDTO;
import com.dw.ngms.cis.im.entity.RequestItems;
import com.dw.ngms.cis.im.entity.Requests;
import com.dw.ngms.cis.im.service.RequestItemService;
import com.dw.ngms.cis.im.service.RequestService;
import com.dw.ngms.cis.uam.dto.FilePathsDTO;
import com.dw.ngms.cis.uam.dto.MailDTO;
import com.dw.ngms.cis.uam.entity.TaskLifeCycle;
import com.dw.ngms.cis.uam.entity.User;
import com.dw.ngms.cis.uam.jsonresponse.UserControllerResponse;
import com.dw.ngms.cis.uam.service.ProvinceService;
import com.dw.ngms.cis.uam.service.TaskService;
import com.dw.ngms.cis.uam.storage.StorageService;
import com.dw.ngms.cis.uam.utilities.Constants;
import com.dw.ngms.cis.workflow.api.ProcessAdditionalInfo;
import com.dw.ngms.cis.workflow.model.Target;
import com.google.gson.Gson;
import com.itextpdf.text.pdf.AcroFields;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.PdfStamper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPReply;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.util.FileCopyUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.*;
import java.net.URLConnection;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import static org.springframework.util.StringUtils.isEmpty;

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

    @Autowired
    private ProvinceService provinceService;


    @Autowired
    private JavaMailSender mailSender;

    @GetMapping("/getTaskTargetFlows")
    public ResponseEntity<?> getTaskTargetFlows(HttpServletRequest request,
                                                @RequestParam Long taskid, String internalrolecode) {
        if (taskid == null) {
            return generateFailureResponse(request, new Exception("Invalid task details passed"));
        }
        try {
            List<Target> targets = (StringUtils.isEmpty(internalrolecode)) ? taskService.getTaskTargetFlows(taskid) :
                    taskService.getTaskTargetFlows(taskid, internalrolecode);
            return (CollectionUtils.isEmpty(targets)) ? generateEmptyResponse(request, "Target(s) not found") :
                    ResponseEntity.status(HttpStatus.OK).body(targets);
        } catch (Exception exception) {
            return generateFailureResponse(request, exception);
        }
    }//getTaskTargetFlows

    @GetMapping("/getTaskHistory")
    public ResponseEntity<?> getTaskHistory(HttpServletRequest request, @RequestParam @Valid String requestcode) {
        if (StringUtils.isEmpty(requestcode)) {
            return generateFailureResponse(request, new Exception("Invalid request code passed"));
        }
        try {
            List<TaskLifeCycle> taskLifeCycle = taskService.getTasksLifeCycleByTaskReferenceCode(requestcode);
            return (CollectionUtils.isEmpty(taskLifeCycle)) ? generateEmptyResponse(request, "Task history not found") :
                    ResponseEntity.status(HttpStatus.OK).body(taskLifeCycle);
        } catch (Exception exception) {
            return generateFailureResponse(request, exception);
        }
    }//getTaskHistory

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
            if (StringUtils.isEmpty(provinceCode) || "all".equalsIgnoreCase(provinceCode.trim()) && !StringUtils.isEmpty(userCode)) {
                requestList = requestService.getRequestByUserCode(userCode);
            } else if (!StringUtils.isEmpty(userCode) && !StringUtils.isEmpty(provinceCode.trim())) {
                requestList = requestService.getRequestByUserCodeProvinceCode(userCode, provinceCode);
            }
            return (CollectionUtils.isEmpty(requestList)) ? ResponseEntity.status(HttpStatus.OK).body(requestList)
                    : ResponseEntity.status(HttpStatus.OK).body(requestList);
        } catch (Exception exception) {
            return generateFailureResponse(request, exception);
        }
    }//getRequestsOfUser


    @GetMapping("/getRequestsPaidInfoByProvince")
    public ResponseEntity<?> getRequestsPaidInfoByProvince(HttpServletRequest request,
                                                           @RequestParam(required = false) String provinceCode,
                                                           @RequestParam(required = false) String period) {
        try {
            Double totalSum = 0.00;
            List<Requests> requestList = new ArrayList<>();
            if (StringUtils.isEmpty(provinceCode) || "all".equalsIgnoreCase(provinceCode.trim()) && period != null) {
                if (period.equalsIgnoreCase("Week")) {
                    requestList = requestService.getAllRequestsPaidInfoByProvinceWeek();
                    Requests[] itemsArray = new Requests[requestList.size()];
                    itemsArray = requestList.toArray(itemsArray);
                    for (int i = 0; i < itemsArray.length; i++) {
                        totalSum = totalSum + Double.parseDouble(itemsArray[i].getTotalAmount());
                    }
                    System.out.println("Total Week is" + totalSum);
                } else if (period.equalsIgnoreCase("Month")) {
                    requestList = requestService.getAllRequestsPaidInfoByProvinceMonth();
                    Requests[] itemsArray = new Requests[requestList.size()];
                    itemsArray = requestList.toArray(itemsArray);
                    for (int i = 0; i < itemsArray.length; i++) {
                        totalSum = totalSum + Double.parseDouble(itemsArray[i].getTotalAmount());
                    }
                    System.out.println("Total Month is" + totalSum);
                }

            } else if (StringUtils.isEmpty(provinceCode) || "all".equalsIgnoreCase(provinceCode.trim())) {
                requestList = requestService.getAllRequestsPaidInfoByProvince();
                Requests[] itemsArray = new Requests[requestList.size()];
                itemsArray = requestList.toArray(itemsArray);
                for (int i = 0; i < itemsArray.length; i++) {
                    totalSum = totalSum + Double.parseDouble(itemsArray[i].getTotalAmount());
                }
                System.out.println("Total is" + totalSum);

            } else if (!StringUtils.isEmpty(provinceCode.trim()) && period != null) {
                if (period.equalsIgnoreCase("Week")) {
                    requestList = requestService.getRequestsPaidInfoByProvinceWeek(provinceCode);
                    Requests[] itemsArray = new Requests[requestList.size()];
                    itemsArray = requestList.toArray(itemsArray);
                    for (int i = 0; i < itemsArray.length; i++) {
                        totalSum = totalSum + Double.parseDouble(itemsArray[i].getTotalAmount());
                    }
                    System.out.println("Total Week is" + totalSum);
                } else if (period.equalsIgnoreCase("Month")) {
                    requestList = requestService.getRequestsPaidInfoByProvinceMonth(provinceCode);
                    Requests[] itemsArray = new Requests[requestList.size()];
                    itemsArray = requestList.toArray(itemsArray);
                    for (int i = 0; i < itemsArray.length; i++) {
                        totalSum = totalSum + Double.parseDouble(itemsArray[i].getTotalAmount());
                    }
                    System.out.println("Total Month is" + totalSum);
                }
            } else if (!StringUtils.isEmpty(provinceCode.trim())) {
                requestList = requestService.getRequestsPaidInfoByProvince(provinceCode);
                Requests[] itemsArray = new Requests[requestList.size()];
                itemsArray = requestList.toArray(itemsArray);
                for (int i = 0; i < itemsArray.length; i++) {
                    totalSum = totalSum + Double.parseDouble(itemsArray[i].getTotalAmount());
                }
                System.out.println("Total value  is" + totalSum);

            }
            return (CollectionUtils.isEmpty(requestList)) ? ResponseEntity.status(HttpStatus.OK).body(totalSum)
                    : ResponseEntity.status(HttpStatus.OK).body(totalSum);
        } catch (Exception exception) {
            return generateFailureResponse(request, exception);
        }
    }//getRequestsPaidInfoByProvince


    @GetMapping("/getRequestByRequestCode")
    public ResponseEntity<?> getRequestByRequestCode(HttpServletRequest request,
                                                     @RequestParam String requestCode) {
        try {

            Requests requests = this.requestService.getRequestsByRequestCode(requestCode);
            List<RequestItems> requestItemsList = this.requestItemService.getRequestsByRequestCode(requestCode);
            if (!StringUtils.isEmpty(requestItemsList)) {
                requests.setRequestItems(requestItemsList);
            }
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
                if (requests != null) {
                    String fileName = testService.store(file);
                    files.add(file.getOriginalFilename());
                    requests.setInvoiceFilePath(Constants.uploadDirectoryPath + fileName);
                    message = "You successfully uploaded " + requests.getInvoiceFilePath() + "!";
                    Requests requests1 = requestService.saveRequest(requests);
                    if (requests1 != null) {
                        requests1.setPaymentStatus("PAID");
                    }
                    requestService.saveRequest(requests1);
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
            requests.setRequestCode("REQ" + Long.toString(requests.getRequestId()));
            //requests.setReferenceNumber("REQ" + Long.toString(requests.getRequestId()));
            String provinceShortName = this.provinceService.getProvinceShortName(requests.getProvinceCode());
            requests.setReferenceNumber("IN" + Long.toString(requests.getRequestId()) + provinceShortName);
            requests.setPaymentStatus("PENDING");
            String processId = "infoRequest";
            List<RequestItems> req = new ArrayList<>();
            if (requests.getRequestItems() != null) {
                for (RequestItems requestItems : requests.getRequestItems()) {
                    Long requestItemCode = this.requestItemService.getRequestItemId();
                    requestItems.setRequestItemId(requestItemCode);
                    requestItems.setRequestId(requests.getRequestId());
                    requestItems.setRequestItemCode("REQITEM" + Long.toString(requestItems.getRequestItemId()));
                    requestItems.setRequestCode(requests.getRequestCode());
                    req.add(requestItems);
                }
            }
            requests.setRequestItems(req);
            Requests requestToSave = this.requestService.saveRequest(requests);
            MailDTO mailDTO = new MailDTO();
            sendMailToCreateRequestUser(requests, mailDTO);

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


    @SuppressWarnings("unused")
    @PostMapping("/sendEmailWithInvoice")
    public ResponseEntity<?> sendEmailWithInvoice(HttpServletRequest request, HttpServletResponse response,
                                                  @RequestParam("requestCode") String requestCode,
                                                  @RequestBody @Valid InvoiceDTO invoiceDTO) throws Exception {
        try {
            Requests req = requestService.getRequestsByRequestCode(requestCode);
            if (req != null) {
                String fileName = req.getInvoiceFilePath();
                File fileLater = new File(req.getInvoiceFilePath());
                MailDTO mailDTO = new MailDTO();
                sendMailInvoiceUser(req, mailDTO, fileName, fileLater);
                ProcessAdditionalInfo processAdditionalInfo = getProcessAdditionalInfo(invoiceDTO);
                processUserState(request, processAdditionalInfo);
                return ResponseEntity.status(HttpStatus.OK).body("Generated Invoice Successfully");
            } else {
                return generateEmptyResponse(request, "No Request found with requestCode " + requestCode);
            }

        } catch (Exception exception) {
            return generateFailureResponse(request, exception);
        }
    }


    private void sendMailInvoiceUser(Requests requests, MailDTO mailDTO, String fileName, File fileLater) throws Exception {

        Map<String, Object> model = new HashMap<String, Object>();

        model.put("firstName", requests.getUserName());
        model.put("body1", "Invoice Generated Successfully");
        model.put("body2", "");
        model.put("body3", "");
        model.put("body4", "");

        mailDTO.setMailSubject("Welcome to CIS");
        model.put("FOOTER", "CIS ADMIN");
        mailDTO.setMailFrom("cheifsurveyorgeneral@gmail.com");
        mailDTO.setMailTo(requests.getUserName());
        mailDTO.setModel(model);
        sendEmail(mailDTO, fileName, fileLater);

    }


    private void sendMailWithFTPPAth(Requests requests, MailDTO mailDTO, String ftpFilePath) throws Exception {

        Map<String, Object> model = new HashMap<String, Object>();

        model.put("firstName", requests.getUserName());
        model.put("body1", "FTP paths send successfully");
        model.put("body2", ftpFilePath);
        model.put("body3", "");
        model.put("body4", "");

        mailDTO.setMailSubject("Welcome to CIS");
        model.put("FOOTER", "CIS ADMIN");
        mailDTO.setMailFrom("cheifsurveyorgeneral@gmail.com");
        mailDTO.setMailTo(requests.getUserName());
        mailDTO.setModel(model);
        sendEmail(mailDTO);

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


    @RequestMapping(value = "/downloadInvoice", method = RequestMethod.POST)
    public ResponseEntity<ByteArrayResource> downloadInvoice(HttpServletRequest request,
                                                             @RequestBody @Valid Requests requests) throws IOException {

        Requests ir = requestService.getRequestsByRequestCode(requests.getRequestCode());
        System.out.println("Internal User Roles one " + ir.getInvoiceFilePath());
        int index = ir.getInvoiceFilePath().lastIndexOf("/");
        String fileName = ir.getInvoiceFilePath().substring(index + 1);
        System.out.println("File Name is " + fileName);
        File file = new File(ir.getInvoiceFilePath());

        HttpHeaders header = new HttpHeaders();
        header.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + file.getName());
        header.add("Cache-Control", "no-cache, no-store, must-revalidate");
        header.add("Pragma", "no-cache");
        header.add("Expires", "0");

        Path path = Paths.get(file.getAbsolutePath());
        ByteArrayResource resource = new ByteArrayResource(Files.readAllBytes(path));

        return ResponseEntity.ok()
                .headers(header)
                .contentLength(file.length())
                .contentType(MediaType.parseMediaType("application/octet-stream"))
                .body(resource);
    }


    @PostMapping("/uploadDispatchDocument")
    public ResponseEntity<?> uploadDispatchDocument(HttpServletRequest request, @RequestParam MultipartFile[] multipleFiles,
                                                    @RequestParam("requestCode") String requestCode
    ) {
        String message = "";
        String json = null;
        Gson gson = new Gson();
        UserControllerResponse userControllerResponse = new UserControllerResponse();
        try {
            Requests requests = this.requestService.getRequestsByRequestCode(requestCode);
            if (requests != null && !isEmpty(requests)) {
                if (requests.getDispatchDocs() != null) {
                    List<String> filesExist = new ArrayList<>();
                    String pathFromDB = requests.getDispatchDocs();
                    FilePathsDTO filePath = gson.fromJson(pathFromDB, FilePathsDTO.class);
                    System.out.println("filePath is " + filePath.getFiles().toString());
                    for (String str1 : filePath.getFiles()) {
                        filesExist.add(str1);
                    }
                    for (MultipartFile f : multipleFiles) {
                        List<String> files = new ArrayList<String>();
                        String fileName = testService.store(f);
                        files.add(f.getOriginalFilename());
                        filesExist.add(Constants.uploadDirectoryPath + fileName);
                        userControllerResponse.setFiles(filesExist);
                        json = gson.toJson(userControllerResponse);
                        requests.setDispatchDocs(json);
                        this.requestService.saveRequest(requests);
                    }
                } else {
                    List<String> filesExist = new ArrayList<>();
                    for (MultipartFile f : multipleFiles) {
                        List<String> files = new ArrayList<String>();
                        String fileName = testService.store(f);
                        files.add(f.getOriginalFilename());
                        filesExist.add(Constants.uploadDirectoryPath + fileName);
                        userControllerResponse.setFiles(filesExist);
                        json = gson.toJson(userControllerResponse);
                        requests.setDispatchDocs(json);
                        this.requestService.saveRequest(requests);
                    }
                }

            }

        } catch (Exception exception) {
            return generateFailureResponse(request, exception);
        }
        return ResponseEntity.status(HttpStatus.OK).body(json);
    }//uploadDispatchDocument

    @PostMapping("/uploadExternalUserRequestDocument")
    public ResponseEntity<?> uploadExternalUserDispatchDocument(HttpServletRequest request, @RequestParam MultipartFile[] multipleFiles,
                                                                @RequestParam("requestCode") String requestCode
    ) {
        String message = "";
        String json = null;
        Gson gson = new Gson();
        UserControllerResponse userControllerResponse = new UserControllerResponse();
        try {
            Requests requests = this.requestService.getRequestsByRequestCode(requestCode);

            if (requests != null && !isEmpty(requests)) {
                if (requests.getExternalUserDispatchDocs() != null) {
                    System.out.println("Get External User Dispatch DOcs" + requests.getExternalUserDispatchDocs());
                    List<String> filesExist = new ArrayList<>();
                    String pathFromDB = requests.getExternalUserDispatchDocs();
                    FilePathsDTO filePath = gson.fromJson(pathFromDB, FilePathsDTO.class);
                    System.out.println("filePath is " + filePath.getFiles().toString());
                    for (String str1 : filePath.getFiles()) {
                        filesExist.add(str1);
                    }
                    for (MultipartFile f : multipleFiles) {
                        List<String> files = new ArrayList<String>();
                        String fileName = testService.store(f);
                        files.add(f.getOriginalFilename());
                        filesExist.add(Constants.uploadDirectoryPath + fileName);
                        userControllerResponse.setFiles(filesExist);
                        json = gson.toJson(userControllerResponse);
                        requests.setExternalUserDispatchDocs(json);
                        this.requestService.saveRequest(requests);
                    }
                } else {
                    List<String> filesExist = new ArrayList<>();
                    for (MultipartFile f : multipleFiles) {
                        List<String> files = new ArrayList<String>();
                        String fileName = testService.store(f);
                        files.add(f.getOriginalFilename());
                        filesExist.add(Constants.uploadDirectoryPath + fileName);
                        userControllerResponse.setFiles(filesExist);
                        json = gson.toJson(userControllerResponse);
                        requests.setExternalUserDispatchDocs(json);
                        this.requestService.saveRequest(requests);
                    }
                }

            }

        } catch (Exception exception) {
            return generateFailureResponse(request, exception);
        }
        return ResponseEntity.status(HttpStatus.OK).body(json);
    }//uploadExternalUserRequestDocument


    @PostMapping("/dispatchDocumentSendMail")
    public ResponseEntity<?> dispatchDocumentSendMail(HttpServletRequest request,
                                                      @RequestBody Requests requestsParam
    ) {
        String message = "";
        String json = null;
        Gson gson = new Gson();
        UserControllerResponse userControllerResponse = new UserControllerResponse();
        FTPClient ftpClient = new FTPClient();
        try {
            Requests requests = this.requestService.getRequestsByRequestCode(requestsParam.getRequestCode());
            if (requests.getDeliveryMethod().equalsIgnoreCase("Electronic(Email)") || requests.getDeliveryMethod().equalsIgnoreCase("Electronic(FTP)")) {
                if (requests != null && !isEmpty(requests)) {
                    String pathFromDB = requests.getDispatchDocs();
                    FilePathsDTO filePath = gson.fromJson(pathFromDB, FilePathsDTO.class);
                    System.out.println("filePath is " + filePath.getFiles().toString());
                    List<String> files = new ArrayList<String>();
                    for (String str1 : filePath.getFiles()) {
                        System.out.println(str1);
                        files.add(str1);
                        ftpZipFiles(files);
                    }

                    String zipFilename = "ftpFilesDownload.zip";

                   /* boolean loginExists = ftpLogin(ftpClient);
                    if (loginExists) {
                        ftpClient.changeWorkingDirectory("/uploadFiles/");
                        File firstLocalFile = new File(Constants.uploadDirectoryPathFTP + zipFilename);
                        String firstRemoteFile = zipFilename;
                        InputStream inputStream = new FileInputStream(firstLocalFile);
                        System.out.println("Start uploading first file");
                        boolean done = ftpClient.storeFile(firstRemoteFile, inputStream);
                        inputStream.close();
                        if (done) {
                            String path = ftpClient.printWorkingDirectory();
                            String ftpFilePath = "ftp://160.119.101.57" + path + "/" + zipFilename;
                            System.out.println(ftpFilePath);
                            System.out.println("The first file is uploaded successfully.");
                        }
                    }


                    ftpClient.logout();*/
                    //File file = new File(Constants.downloadDirectoryPath + "DispatchDocumentsDownloadFiles.zip");



                /*    boolean loginExists = ftpLogin(ftpClient);
                    System.out.println("loginExists: " + loginExists);
                    if (loginExists) {
                        List<String> filesExist = new ArrayList<>();
                        for (MultipartFile f : multipleFiles) {
                            List<String> files = new ArrayList<String>();
                            String fileName = testService.storeFtpFiles(f);
                            files.add(f.getOriginalFilename());
                            filesExist.add(Constants.uploadDirectoryPathFTP + fileName);
                        }

                            String timeStamp = new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss").format(new Date());
                            ftpZipFiles(filesExist,timeStamp,ftpClient);

                           *//* String zipFilename = timeStamp+"download.zip";
                            ftpClient.changeWorkingDirectory("/uploadFiles/");
                            File localZip = new File(Constants.downloadDirectoryPath + zipFilename);
                            InputStream inputStream = new FileInputStream(localZip);
                            ftpClient.storeFile(zipFilename, inputStream);
                            String path = ftpClient.printWorkingDirectory();
                            String ftpFilePath = "ftp://160.119.101.57" + path + "/" + zipFilename;
                            System.out.println(ftpFilePath);*//*
                            //MailDTO mailDTO = new MailDTO();
                           // sendMailWithFTPPAth(requests, mailDTO, ftpFilePath);

                        ftpClient.logout();
                    }*/
                    String path = "/ftpFileDownload/";
                    String ftpFilePath = "ftp://160.119.101.57" + path + zipFilename;

                    MailDTO mailDTO = new MailDTO();
                    sendMailWithFTPPAth(requests, mailDTO, ftpFilePath);

                }
            }

        } catch (Exception exception) {
            return generateFailureResponse(request, exception);
        }
        return ResponseEntity.status(HttpStatus.OK).body("Successfully function");
    }//uploadFTPDispatchDocumentSendMail

    private boolean ftpLogin(FTPClient ftpClient) throws IOException {
        String server = "160.119.101.57";
        int port = 21;
        String user = "Administrator";
        String pass = "dataworld@1";

        ftpClient.connect(server, port);
        showServerReply(ftpClient);
        int replyCode = ftpClient.getReplyCode();
        if (!FTPReply.isPositiveCompletion(replyCode)) {
            System.out.println("Operation failed. Server reply code: " + replyCode);
            return false;
        }
        boolean success = ftpClient.login(user, pass);
        showServerReply(ftpClient);
        if (!success) {
            System.out.println("Could not login to the server");
            return false;
        } else {
            System.out.println("LOGGED IN SERVER");
            return true;
        }

    }


    @RequestMapping(value = "/deleteDispatchDocument", method = RequestMethod.POST)
    public ResponseEntity<?> deleteDispatchDocument(HttpServletRequest request,
                                                    @RequestBody @Valid Requests requestsBody,
                                                    @RequestParam String documentPath) throws IOException {
        try {
            String json = null;
            Gson gson = new Gson();
            UserControllerResponse userControllerResponse = new UserControllerResponse();
            Requests requests = this.requestService.getRequestsByRequestCode(requestsBody.getRequestCode());
            if (isEmpty(requests)) {
                return generateEmptyResponse(request, "Requests are  not found");
            }
            if (!isEmpty(requests)) {
                String pathFromDB = requests.getDispatchDocs();

                FilePathsDTO filePath = gson.fromJson(pathFromDB, FilePathsDTO.class);
                List<String> fileDeleted = new ArrayList<>();
                for (String str1 : filePath.getFiles()) {
                    fileDeleted.add(str1);
//                    System.out.println("Str1 is "+str1);
//                    if (te.contains(documentPath)) {
//                        Path fileToDeletePath = Paths.get(str1);
//                        Files.delete(fileToDeletePath);
//                        System.out.println("String values are "+str1);
//                        te.remove(documentPath);
//                    }
//                    System.out.println("size is" +te.size());
                }
                System.out.println("size is " + fileDeleted.size());
                if (fileDeleted.contains(documentPath)) {
                    Path fileToDeletePath = Paths.get(documentPath);
                    Files.delete(fileToDeletePath);
                    fileDeleted.remove(documentPath);
                    if (fileDeleted.size() == 0) {
                        System.out.println("Size of file is " + fileDeleted);
                        requests.setDispatchDocs("");
                        this.requestService.saveRequest(requests);
                        return ResponseEntity.status(HttpStatus.OK).body("Request Document Deleted Successfully");
                    }

                }


                userControllerResponse.setFiles(fileDeleted);
                json = gson.toJson(userControllerResponse);
                requests.setDispatchDocs(json);
                this.requestService.saveRequest(requests);
                System.out.println("size is after " + fileDeleted.size());
                return ResponseEntity.status(HttpStatus.OK).body("Request Document Deleted Successfully");
            }
            return generateEmptyResponse(request, "Request Document are  not found");
        } catch (Exception exception) {
            return generateFailureResponse(request, exception);
        }
    }//deleteDispatchDocument


    @RequestMapping(value = "/deleteExternalUserRequestDocument", method = RequestMethod.POST)
    public ResponseEntity<?> deleteExternalUserDispatchDocument(HttpServletRequest request,
                                                                @RequestBody @Valid Requests requestsBody,
                                                                @RequestParam String documentPath) throws IOException {
        try {
            String json = null;
            Gson gson = new Gson();
            UserControllerResponse userControllerResponse = new UserControllerResponse();
            Requests requests = this.requestService.getRequestsByRequestCode(requestsBody.getRequestCode());
            if (isEmpty(requests)) {
                return generateEmptyResponse(request, "Requests are  not found");
            }
            if (!isEmpty(requests)) {
                String pathFromDB = requests.getExternalUserDispatchDocs();

                FilePathsDTO filePath = gson.fromJson(pathFromDB, FilePathsDTO.class);
                List<String> fileDeleted = new ArrayList<>();
                for (String str1 : filePath.getFiles()) {
                    fileDeleted.add(str1);
                }
                System.out.println("size is " + fileDeleted.size());
                if (fileDeleted.contains(documentPath)) {
                    Path fileToDeletePath = Paths.get(documentPath);
                    Files.delete(fileToDeletePath);
                    fileDeleted.remove(documentPath);

                    if (fileDeleted.size() == 0) {
                        System.out.println("Size of external document file is " + fileDeleted);
                        requests.setExternalUserDispatchDocs("");
                        this.requestService.saveRequest(requests);
                        return ResponseEntity.status(HttpStatus.OK).body("Request External Document Deleted Successfully");
                    }
                }

                userControllerResponse.setFiles(fileDeleted);
                json = gson.toJson(userControllerResponse);
                requests.setExternalUserDispatchDocs(json);
                this.requestService.saveRequest(requests);
                System.out.println("size is after " + fileDeleted.size());
                return ResponseEntity.status(HttpStatus.OK).body("Request External Document Deleted Successfully");
            }
            return generateEmptyResponse(request, "Request External Document are  not found");
        } catch (Exception exception) {
            return generateFailureResponse(request, exception);
        }
    }//deleteDispatchDocument


    @RequestMapping(value = "/getDispatchDocsList", method = RequestMethod.GET)
    public ResponseEntity<?> getDispatchDocsList(HttpServletRequest request,
                                                 @RequestParam String requestCode) throws IOException {
        try {
            String json = null;
            List<String> test = new ArrayList<>();
            Requests requests = this.requestService.getRequestsByRequestCode(requestCode);
            if (!isEmpty(requests)) {
                String pathFromDB = requests.getDispatchDocs();
                Gson gson = new Gson();
                FilePathsDTO filePath = gson.fromJson(pathFromDB, FilePathsDTO.class);
                System.out.println("filePath is " + filePath.getFiles().toString());
                List<String> files = new ArrayList<String>();
                for (String str1 : filePath.getFiles()) {
                    files.add(str1);
                    json = gson.toJson(files);

                }
                return ResponseEntity.status(HttpStatus.OK).body(json);
            } else {
                return ResponseEntity.status(HttpStatus.OK).body(test);
            }

        } catch (Exception exception) {
            return generateFailureResponse(request, exception);
        }
    }//getDispatchDocsList


    @RequestMapping(value = "/getExternalUserRequestDocsList", method = RequestMethod.GET)
    public ResponseEntity<?> getExternalUserDispatchDocsList(HttpServletRequest request,
                                                             @RequestParam String requestCode) throws IOException {
        try {
            String json = null;
            List<String> test = new ArrayList<>();
            Requests requests = this.requestService.getRequestsByRequestCode(requestCode);
            if (!isEmpty(requests)) {
                String pathFromDB = requests.getExternalUserDispatchDocs();
                Gson gson = new Gson();
                FilePathsDTO filePath = gson.fromJson(pathFromDB, FilePathsDTO.class);
                System.out.println("filePath is " + filePath.getFiles().toString());
                List<String> files = new ArrayList<String>();
                for (String str1 : filePath.getFiles()) {
                    files.add(str1);
                    json = gson.toJson(files);

                }
                return ResponseEntity.status(HttpStatus.OK).body(json);
            } else {
                return ResponseEntity.status(HttpStatus.OK).body(test);
            }

        } catch (Exception exception) {
            return generateFailureResponse(request, exception);
        }
    }//getDispatchDocsList


    @RequestMapping(value = "/externalUserDownloadRequestDocuments", method = RequestMethod.POST)
    public ResponseEntity<InputStreamResource> externalUserDownloadDispatchDocuments(HttpServletRequest request,
                                                                                     @RequestBody @Valid Requests requests) throws IOException {
        Requests req = this.requestService.getRequestsByRequestCode(requests.getRequestCode());
        System.out.println("req documents are one " + req.getExternalUserDispatchDocs());
        String pathFromDB = req.getExternalUserDispatchDocs();

        Gson gson = new Gson();
        FilePathsDTO filePath = gson.fromJson(pathFromDB, FilePathsDTO.class);
        System.out.println("filePath is " + filePath.getFiles().toString());
        List<String> files = new ArrayList<String>();
        for (String str1 : filePath.getFiles()) {
            System.out.println(str1);
            files.add(str1);
            externalUserZipFiles(files);
        }
        File file = new File(Constants.downloadDirectoryPath + "ExternalUserDispatchDocumentsDownloadFiles.zip");

        InputStreamResource resource = new InputStreamResource(new FileInputStream(file));
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment;filename= ExternalUserDispatchDocumentsDownloadFiles.zip")
                .contentType(MediaType.parseMediaType("application/octet-stream"))
                .contentLength(file.length()) //
                .body(resource);
    }//downloadDispatchDocuments


    @RequestMapping(value = "/downloadDispatchDocuments", method = RequestMethod.POST)
    public ResponseEntity<InputStreamResource> downloadDocumentation(HttpServletRequest request,
                                                                     @RequestBody @Valid Requests requests) throws IOException {
        Requests req = this.requestService.getRequestsByRequestCode(requests.getRequestCode());
        System.out.println("req documents are one " + req.getDispatchDocs());
        String pathFromDB = req.getDispatchDocs();

        Gson gson = new Gson();
        FilePathsDTO filePath = gson.fromJson(pathFromDB, FilePathsDTO.class);
        System.out.println("filePath is " + filePath.getFiles().toString());
        List<String> files = new ArrayList<String>();
        for (String str1 : filePath.getFiles()) {
            System.out.println(str1);
            files.add(str1);
            zipFiles(files);
        }
        File file = new File(Constants.downloadDirectoryPath + "DispatchDocumentsDownloadFiles.zip");

        InputStreamResource resource = new InputStreamResource(new FileInputStream(file));
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment;filename= DispatchDocumentsDownloadFiles.zip")
                .contentType(MediaType.parseMediaType("application/octet-stream"))
                .contentLength(file.length()) //
                .body(resource);
    }//downloadDispatchDocuments


    public void externalUserZipFiles(List<String> files) {
        FileOutputStream fos = null;
        ZipOutputStream zipOut = null;
        FileInputStream fis = null;

        try {
            fos = new FileOutputStream(Constants.downloadDirectoryPath + "ExternalUserDispatchDocumentsDownloadFiles.zip");
            zipOut = new ZipOutputStream(new BufferedOutputStream(fos));
            for (String filePath : files) {
                File input = new File(filePath);
                fis = new FileInputStream(input);
                ZipEntry ze = new ZipEntry(input.getName());
                zipOut.putNextEntry(ze);
                byte[] tmp = new byte[4 * 1024];
                int size = 0;
                while ((size = fis.read(tmp)) != -1) {
                    zipOut.write(tmp, 0, size);
                }
                zipOut.flush();
                fis.close();
            }
            zipOut.close();
            System.out.println("Done... Zipped the files...");
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } finally {
            try {
                if (fos != null) fos.close();
            } catch (Exception ex) {

            }
        }
    }//zipFiles

    public void zipFiles(List<String> files) {

        FileOutputStream fos = null;
        ZipOutputStream zipOut = null;
        FileInputStream fis = null;
        try {
            fos = new FileOutputStream(Constants.downloadDirectoryPath + "DispatchDocumentsDownloadFiles.zip");
            zipOut = new ZipOutputStream(new BufferedOutputStream(fos));
            for (String filePath : files) {
                File input = new File(filePath);
                fis = new FileInputStream(input);
                ZipEntry ze = new ZipEntry(input.getName());
                zipOut.putNextEntry(ze);
                byte[] tmp = new byte[4 * 1024];
                int size = 0;
                while ((size = fis.read(tmp)) != -1) {
                    zipOut.write(tmp, 0, size);
                }
                zipOut.flush();
                fis.close();
            }
            zipOut.close();
            System.out.println("Done... Zipped the files...");
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } finally {
            try {
                if (fos != null) fos.close();
            } catch (Exception ex) {

            }
        }
    }//zipFiles


    public void ftpZipFiles(List<String> files) {
        FileOutputStream fos = null;
        ZipOutputStream zipOut = null;
        FileInputStream fis = null;

        String zipFilename = "download.zip";
        try {
            fos = new FileOutputStream(Constants.uploadDirectoryPathFTP + zipFilename);
            zipOut = new ZipOutputStream(new BufferedOutputStream(fos));
            for (String filePath : files) {
                System.out.println("filePath is " + filePath);
                File input = new File(filePath);
                fis = new FileInputStream(input);
                ZipEntry ze = new ZipEntry(input.getName());
                zipOut.putNextEntry(ze);
                byte[] tmp = new byte[4 * 1024];
                int size = 0;
                while ((size = fis.read(tmp)) != -1) {
                    zipOut.write(tmp, 0, size);
                }
                zipOut.flush();
                fis.close();
            }
            zipOut.close();
            System.out.println("Done... Zipped the files...");


        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } finally {
            try {
                if (fos != null) fos.close();
            } catch (Exception ex) {

            }
        }
    }//zipFiles


    private void sendMailToCreateRequestUser(@RequestBody @Valid Requests requests, MailDTO mailDTO) throws Exception {

        Map<String, Object> model = new HashMap<String, Object>();
        model.put("firstName", requests.getUserName());
        model.put("body1", "Your request is created successfully");
        model.put("body2", "");
        model.put("body3", "");
        model.put("body4", "");
        mailDTO.setMailSubject("Create Request");
        model.put("FOOTER", "CIS ADMIN");
        mailDTO.setMailFrom("cheifsurveyorgeneral@gmail.com");
        mailDTO.setMailTo(requests.getEmail());
        mailDTO.setModel(model);
        sendEmail(mailDTO);


    }


    protected static void showServerReply(FTPClient ftpClient) {
        String[] replies = ftpClient.getReplyStrings();
        if (replies != null && replies.length > 0) {
            for (String aReply : replies) {
                System.out.println("SERVER: " + aReply);
            }
        }
    }

}

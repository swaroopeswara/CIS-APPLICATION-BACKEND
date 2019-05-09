package com.dw.ngms.cis.uam.controller;

import com.dw.ngms.cis.exception.ExceptionConstants;
import com.dw.ngms.cis.uam.dto.MailDTO;
import com.dw.ngms.cis.uam.entity.IssueLog;
import com.dw.ngms.cis.uam.entity.User;
import com.dw.ngms.cis.uam.jsonresponse.UserControllerResponse;
import com.dw.ngms.cis.uam.service.IssueLogService;
import com.google.gson.Gson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by swaroop on 2019/04/11.
 */

@RestController
@RequestMapping("/cisorigin.uam/api/v1")
@CrossOrigin(origins = "*")
public class IssueLogController extends MessageController {

    @Autowired
    private IssueLogService issueLogService;

    @RequestMapping(value = "/saveIssueLog", method = RequestMethod.POST)
    public ResponseEntity<?> saveIssueLog(HttpServletRequest request, @RequestBody @Valid IssueLog issueLog) {
        try {
            issueLog = issueLogService.saveIssueLog(issueLog);
            if (issueLog != null) {
                return ResponseEntity.status(HttpStatus.OK).body(issueLog);
            }

            return generateEmptyWithOKResponse(request, "Empty");
        } catch (Exception exception) {
            return generateFailureResponse(request, exception);
        }

    }


    @GetMapping("/getAllIssueLogs")
    public ResponseEntity<?> getAllIssueLogs(HttpServletRequest request) {
        try {
            List<IssueLog> issueLogList = null;
            issueLogList = issueLogService.findAll();
            return (CollectionUtils.isEmpty(issueLogList)) ? ResponseEntity.status(HttpStatus.OK).body(issueLogList)
                    : ResponseEntity.status(HttpStatus.OK).body(issueLogList);
        } catch (Exception exception) {
            return generateFailureResponse(request, exception);
        }
    }//getAllIssueLogs


    @GetMapping("/getIssueLogStatus")
    public ResponseEntity getIssueStatusById(HttpServletRequest request,@RequestParam Long issueID) {
        UserControllerResponse userControllerResponse = new UserControllerResponse();
        Gson gson = new Gson();
        String json = null;
        try {
            String status = null;
            status = issueLogService.findIssueStatus(issueID);
            if(StringUtils.isEmpty(status)){
                return generateEmptyWithOKResponse(request,"No Status found");
            }
            userControllerResponse.setMessage(status);
            json = gson.toJson(userControllerResponse);
            return ResponseEntity.status(HttpStatus.OK).body(json);
        }catch (Exception exception) {
            return generateFailureResponse(request, exception);
        }
    }


    @GetMapping("/getMyIssues")
    public ResponseEntity getMyIssues(HttpServletRequest request,@RequestParam String email) {
        try {
            String status = null;
            List<IssueLog> issueLog = issueLogService.findIssueWithEmail(email);
            if(StringUtils.isEmpty(issueLog)){
                return generateEmptyWithOKResponse(request,"No Issue Log found");
            }
            return ResponseEntity.status(HttpStatus.OK).body(issueLog);
        }catch (Exception exception) {
            return generateFailureResponse(request, exception);
        }
    }


    @RequestMapping(value = "/issueLogUpdateStatus", method = RequestMethod.POST)
    public ResponseEntity updateStatus(HttpServletRequest request,@RequestParam Long issueID, @Valid @RequestBody IssueLog issueLogDetails) throws IOException {

        IssueLog issueLog = issueLogService.findById(issueID);
        if (!StringUtils.isEmpty(issueLog)) {
            issueLog.setIssueStatus(issueLogDetails.getIssueStatus());
            IssueLog updateIssueLog = this.issueLogService.saveIssueLog(issueLog);
            MailDTO mailDTO = getMailDTO(updateIssueLog);
            sendMailToUser(updateIssueLog, mailDTO);
            return ResponseEntity.status(HttpStatus.OK).body(updateIssueLog);
        }else{
            return generateEmptyWithOKResponse(request, "No Issue log found");
        }

    }


    private MailDTO getMailDTO(@RequestBody @Valid IssueLog issueLog) {
        MailDTO mailDTO = new MailDTO();
        mailDTO.setHeader(ExceptionConstants.header);
        mailDTO.setFooter(ExceptionConstants.footer);

        mailDTO.setSubject("Issue Log Registered");
        return mailDTO;
    }


    private void sendMailToUser(@RequestBody @Valid IssueLog issueLog, MailDTO mailDTO) throws IOException {
        Map<String, Object> model = new HashMap<String, Object>();
        if (issueLog.getIssueStatus().equalsIgnoreCase("OPEN")) {

            model.put("body1", "Your issue is open.");
            model.put("body2", "");
            model.put("body3", "");
            model.put("body4", "");

        } else  if (issueLog.getIssueStatus().equalsIgnoreCase("CLOSE"))  {
            model.put("body1", "Your issue is closed.");
            model.put("body2", "");
            model.put("body3", "");
            model.put("body4", "");

        }
        mailDTO.setSubject("Welcome to CIS");
        model.put("firstName", issueLog.getFullName());
        mailDTO.setFooter("CIS ADMIN");
        mailDTO.setMailFrom("dataworldproject@gmail.com");
        mailDTO.setMailTo(issueLog.getEmail());
        mailDTO.setModel(model);
        try {
            sendEmail(mailDTO);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}


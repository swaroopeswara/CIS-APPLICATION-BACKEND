package com.dw.ngms.cis.uam.controller;

import com.dw.ngms.cis.uam.entity.IssueLog;
import com.dw.ngms.cis.uam.service.IssueLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.io.IOException;

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
}


package com.dw.ngms.cis.uam.controller;

import com.dw.ngms.cis.uam.entity.SecurityQuestion;
import com.dw.ngms.cis.uam.service.SecurityQuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * Created by swaroop on 2019/03/24.
 */
@RestController
@RequestMapping("/cisorigin.uam/api/v1")
public class SecurityQuestionController extends MessageController {

    @Autowired
    private SecurityQuestionService securityQuestionService;

    @GetMapping("/getSecurityQuestions")
    public ResponseEntity<?> getAllSecurityQuestions(HttpServletRequest request) {
        try {
            List<SecurityQuestion> questionList = this.securityQuestionService.getAllSecurityQuestions();
            return (CollectionUtils.isEmpty(questionList)) ? generateEmptyResponse(request, "Security Questions not found")
                    : ResponseEntity.status(HttpStatus.OK).body(questionList);
        } catch (Exception exception) {
            return generateFailureResponse(request, exception);
        }
    }//getAllSectors
}

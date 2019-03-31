package com.dw.ngms.cis.uam.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.dw.ngms.cis.uam.entity.PlsUser;
import com.dw.ngms.cis.uam.service.PlsUserService;

@RestController
@RequestMapping("/cisorigin.uam/api/v1")
public class PlsUserController extends MessageController {

	@Autowired
    private PlsUserService plsUserService;

    @GetMapping("/getAllPlsUsers")
    public ResponseEntity<?> getAllPlsUsers(HttpServletRequest request) {
        try {
        	List<PlsUser> plsUserList = plsUserService.getAllPlsUsers();
        	return (CollectionUtils.isEmpty(plsUserList)) ? generateEmptyResponse(request, "PlsUser(s) not found") 
            		: ResponseEntity.status(HttpStatus.OK).body(plsUserList);
        } catch (Exception exception) {
            return generateFailureResponse(request, exception);
        }
    }//getAllPlsUsers
}

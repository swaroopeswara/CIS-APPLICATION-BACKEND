package com.dw.ngms.cis.uam.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.dw.ngms.cis.uam.entity.User;
import com.dw.ngms.cis.uam.service.UserService;

@RestController
@RequestMapping("/user")
public class UserController extends MessageController {

	private static final String INTERNAL_USER_TYPE_NAME = "INTERNAL";
//	private static final String EXTERNAL_USER_TYPE_NAME = "EXTERNAL";
	
	@Autowired
    private UserService userService;
	
	@GetMapping("/getAllInternalUsers")
    public ResponseEntity<?> getAllInternalUsers(HttpServletRequest request, @RequestParam String provincecode) {
        try {
        	List<User> userList = (StringUtils.isEmpty(provincecode) || "all".equalsIgnoreCase(provincecode.trim())) ? 
        		userService.getAllUsersByUserTypeName(INTERNAL_USER_TYPE_NAME) : 
        			userService.getAllUsersByUserTypeNameAndProvinceCode(INTERNAL_USER_TYPE_NAME, provincecode);
        	return (CollectionUtils.isEmpty(userList)) ? generateEmptyResponse(request, "User(s) not found") 
            		: ResponseEntity.status(HttpStatus.OK).body(userList);
        } catch (Exception exception) {
            return generateFailureResponse(request, exception);
        }
    }//getAllInternalUsers
}

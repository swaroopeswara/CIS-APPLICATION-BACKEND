package com.dw.ngms.cis.uam.controller;

import static org.springframework.util.StringUtils.isEmpty;

import java.io.IOException;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.dw.ngms.cis.uam.dto.UserDTO;
import com.dw.ngms.cis.uam.entity.User;
import com.dw.ngms.cis.uam.enums.ApprovalStatus;
import com.dw.ngms.cis.uam.enums.Status;
import com.dw.ngms.cis.uam.jsonresponse.UserControllerResponse;
import com.dw.ngms.cis.uam.service.UserService;
import com.google.gson.Gson;

import io.swagger.annotations.ApiOperation;

@RestController
@RequestMapping("/cisorigin.uam/api/v1")
public class UserController extends MessageController {

	private static final String INTERNAL_USER_TYPE_NAME = "INTERNAL";
	private static final String EXTERNAL_USER_TYPE_NAME = "EXTERNAL";
	
	@Autowired
    private UserService userService;
	
	@GetMapping("/checkADUserExists")
	public ResponseEntity<?> isADUserExists(HttpServletRequest request, @RequestParam String username, @RequestParam String password) {
		try {
        	return ResponseEntity.status(HttpStatus.OK).body(userService.isADUserExists(username, password));
        } catch (Exception exception) {
            return generateFailureResponse(request, exception);
        }
	}//isADUserExists
	
	@PostMapping("/submitInternalUserForApproval")
	public ResponseEntity<?> submitInternalUserForApproval(HttpServletRequest request, @RequestParam String usercode, 
			@RequestParam String username, @RequestParam String isapproved) {
		try {
        	User user = userService.submitInternalUserForApproval(usercode, username, INTERNAL_USER_TYPE_NAME, ApprovalStatus.PND);
        	return (user == null) ? generateEmptyResponse(request, "User(s) not found") : 
        				ResponseEntity.status(HttpStatus.OK).body(user);
        } catch (Exception exception) {
            return generateFailureResponse(request, exception);
        }
	}//submitInternalUserForApproval
	
	@GetMapping("/getAllInternalUsers")
    public ResponseEntity<?> getAllInternalUsers(HttpServletRequest request, @RequestParam String provincecode) {
        try {
        	List<User> userList = (StringUtils.isEmpty(provincecode) || "all".equalsIgnoreCase(provincecode.trim())) ? 
        		userService.getAllUsersByUserTypeName(INTERNAL_USER_TYPE_NAME) : 
        			userService.getAllInternalUsersByProvinceCode(provincecode);
        	return (CollectionUtils.isEmpty(userList)) ? generateEmptyResponse(request, "User(s) not found") 
            		: ResponseEntity.status(HttpStatus.OK).body(userList);
        } catch (Exception exception) {
            return generateFailureResponse(request, exception);
        }
    }//getAllInternalUsers
	
	@GetMapping("/getAllExternalUsers")
    public ResponseEntity<?> getAllExternalUsers(HttpServletRequest request, @RequestParam String provincecode) {
        try {
        	List<User> userList = (StringUtils.isEmpty(provincecode) || "all".equalsIgnoreCase(provincecode.trim())) ? 
        		userService.getAllUsersByUserTypeName(EXTERNAL_USER_TYPE_NAME) : 
        			userService.getAllExternalUsersByProvinceCode(provincecode);
        	return (CollectionUtils.isEmpty(userList)) ? generateEmptyResponse(request, "User(s) not found") 
            		: ResponseEntity.status(HttpStatus.OK).body(userList);
        } catch (Exception exception) {
            return generateFailureResponse(request, exception);
        }
    }//getAllExternalUsers

	@PostMapping("/updateExternalUser")
	public ResponseEntity<?> updateExternalUser(HttpServletRequest request, @RequestParam User user){
		try{
			user = userService.saveInternalUser(user);
			return (user == null) ? generateEmptyResponse(request, "Failed to update user") :
				ResponseEntity.status(HttpStatus.OK).body("Successful");
		} catch (Exception exception) {
			return generateFailureResponse(request, exception);
		}
	}//updateExternalUser
	
	@GetMapping("/getUsersForPendingApproval")
    public ResponseEntity<?> getUsersForPendingApproval(HttpServletRequest request, @RequestParam String provincecode) {
        try {
        	List<User> userList = (StringUtils.isEmpty(provincecode) || "all".equalsIgnoreCase(provincecode.trim())) ? 
        		userService.getAllExternalApprovalPendingUsers(ApprovalStatus.PND.name(), ApprovalStatus.Y.name()) : 
        			userService.getAllExternalApprovalPendingUsersByProvinceCode(ApprovalStatus.PND.name(), ApprovalStatus.Y.name(), provincecode);
        	return (CollectionUtils.isEmpty(userList)) ? generateEmptyResponse(request, "User(s) not found") 
            		: ResponseEntity.status(HttpStatus.OK).body(userList);
        } catch (Exception exception) {
            return generateFailureResponse(request, exception);
        }
    }//getUsersForPendingApproval
	
	@GetMapping("/getAssistantsForPendingApproval")
    public ResponseEntity<?> getAssistantsForPendingApproval(HttpServletRequest request, @RequestParam String surveyorusercode) {
        try {
        	List<User> userList = null;
        	if(!StringUtils.isEmpty(surveyorusercode)) {
        		userList = userService.getAllAssistantsForPendingApprovalBySurveyorUserCode(ApprovalStatus.N.name(), ApprovalStatus.PND.name(), surveyorusercode);
        	}
        	return (CollectionUtils.isEmpty(userList)) ? generateEmptyResponse(request, "User(s) not found") 
            		: ResponseEntity.status(HttpStatus.OK).body(userList);
        } catch (Exception exception) {
            return generateFailureResponse(request, exception);
        }
    }//getAssistantsForPendingApproval
	
	@GetMapping("/checkUserExist")
	public ResponseEntity<?> checkUserExistsInDB(HttpServletRequest request, @RequestParam String email) {
		User userExists = null;
		String exists = null;
		String json = null;
		Gson gson = new Gson();
		UserControllerResponse userControllerResponse = new UserControllerResponse();
		try {
			userExists = this.userService.findByEmail(email);
			if (userExists == null) {
				userControllerResponse.setExists("false");
				json = gson.toJson(userControllerResponse);
				return ResponseEntity.status(HttpStatus.OK).body(json);
			}

			if (userExists.getUserName() != null) {
				userControllerResponse.setExists("true");
				json = gson.toJson(userControllerResponse);
			} else {
				userControllerResponse.setExists("false");
				json = gson.toJson(userControllerResponse);
			}

		} catch (Exception exception) {
			return generateFailureResponse(request, exception);
		}
		return ResponseEntity.status(HttpStatus.OK).body(json);
	}


	@GetMapping("/getUserInfoByEmail")
	public ResponseEntity<?> getUserInfoByMail(HttpServletRequest request, @RequestParam String email) {

		try {
			User userInfo = this.userService.findByEmail(email);

			return (isEmpty(userInfo)) ? generateEmptyResponse(request, "Users not found")
					: ResponseEntity.status(HttpStatus.OK).body(userInfo);
		} catch (Exception exception) {
			return generateFailureResponse(request, exception);
		}
	}

	@ApiOperation(value = "Add an external user")
	@PostMapping("/registerExternalUser")
	public User createExternalUser(@Valid @RequestBody User user) {
		System.out.println("UserName " + user.getUserName());
		System.out.println("commName " + user.getExternaluser().getCommunicationmodetypename());
		return userService.saveExternalUser(user);
	}


	@PostMapping("/registerInternalUser")
	public User createInternalUser(@RequestBody @Valid User internalUser) {
		return userService.saveInternalUser(internalUser);
	}


	@RequestMapping(value = "/approveRejectUser", method = RequestMethod.PUT)
	public ResponseEntity<?> approveRejectAssitant(HttpServletRequest request, @RequestBody @Valid UserDTO userDTO) throws IOException {
		try {
			User user = this.userService.findByUserByNameAndCode(userDTO);
			if (isEmpty(user)) {
				return generateEmptyResponse(request, "Users not found");
			}
			if (!isEmpty(user)) {
				user.setIsApproved(ApprovalStatus.valueOf(userDTO.getIsapproved()));
				user.setRejectionReason(userDTO.getRejectionreason());
				user.setIsApprejDate(new Date());
			}
			this.userService.updateUserApproval(user);
			//todo Send Email to User
			return ResponseEntity.status(HttpStatus.OK).body("User Approval Updated Sucessfully");
		} catch (Exception exception) {
			return generateFailureResponse(request, exception);
		}
	}

	@RequestMapping(value = "/deactivateUser", method = RequestMethod.PUT)
	public ResponseEntity<?> deactivateUser(HttpServletRequest request, @RequestBody @Valid UserDTO userDTO) throws IOException {
		try {
			User user = this.userService.findByUserByNameAndCode(userDTO);
			if (isEmpty(user)) {
				return generateEmptyResponse(request, "Users not found");
			}
			if (!isEmpty(user)) {
				user.setIsActive(Status.valueOf(userDTO.getIsactive())); //todo test later with srinivas garu
			}
			this.userService.updateUserApproval(user);
			//todo Send Email to User
			return ResponseEntity.status(HttpStatus.OK).body("User DeActivated Successfully");
		} catch (Exception exception) {
			return generateFailureResponse(request, exception);
		}
	}
	
}

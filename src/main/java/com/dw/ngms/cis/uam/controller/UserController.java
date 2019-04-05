package com.dw.ngms.cis.uam.controller;

import static org.springframework.util.StringUtils.isEmpty;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import com.dw.ngms.cis.uam.dto.*;
import com.dw.ngms.cis.uam.service.*;
import org.apache.commons.lang.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.dw.ngms.cis.exception.ExceptionConstants;
import com.dw.ngms.cis.uam.entity.ExternalRole;
import com.dw.ngms.cis.uam.entity.ExternalUser;
import com.dw.ngms.cis.uam.entity.ExternalUserAssistant;
import com.dw.ngms.cis.uam.entity.ExternalUserRoles;
import com.dw.ngms.cis.uam.entity.InternalRole;
import com.dw.ngms.cis.uam.entity.InternalUserRoles;
import com.dw.ngms.cis.uam.entity.User;
import com.dw.ngms.cis.uam.enums.ApprovalStatus;
import com.dw.ngms.cis.uam.enums.Status;
import com.dw.ngms.cis.uam.jsonresponse.UserControllerResponse;
import com.dw.ngms.cis.uam.ldap.UserCredentials;
import com.google.gson.Gson;

@RestController
@RequestMapping("/cisorigin.uam/api/v1")
@CrossOrigin(origins = "*")
public class UserController extends MessageController {

    private static final String INTERNAL_USER_TYPE_NAME = "INTERNAL";
    private static final String EXTERNAL_USER_TYPE_NAME = "EXTERNAL";

    @Autowired
    private UserService userService;

    @Autowired
    private ExternalRoleService externalRoleService;

    @Autowired
    private ExternalUserService externalUserService;

    @Autowired
    private InternalRoleService internalRoleService;

    @Autowired
    private ExternalUserAssistantService externalUserAssistantService;

    @Autowired
    private InternalUserRoleService internalUserRoleService;

    @PostMapping("/checkADUserExists")
    public ResponseEntity<?> isADUserExists(HttpServletRequest request, @RequestBody @Valid UserCredentials userCredentials) {
    	if(userCredentials == null || StringUtils.isEmpty(userCredentials.getUsername()) ||
    			StringUtils.isEmpty(userCredentials.getPassword())) {
    		return generateFailureResponse(request, new Exception("Invalid credentials passed"));
    	}
        try {
            return ResponseEntity.status(HttpStatus.OK).body(userService.isADUserExists(userCredentials.getUsername(), userCredentials.getPassword()));
        } catch (Exception exception) {
            return generateFailureResponse(request, exception);
        }
    }//isADUserExists


    @PostMapping("/submitInternalUserForApproval")
    public ResponseEntity<?> submitInternalUserForApproval(HttpServletRequest request, @RequestBody @Valid UserDTO userDto) {
    	if(userDto == null || userDto.getUsercode() == null || 
    			userDto.getUsername() == null || userDto.getIsapproved() == null) {
    		return generateFailureResponse(request, new Exception("Required params missing"));
    	}

        try {
            User user = userService.submitInternalUserForApproval(userDto.getUsercode(), userDto.getUsername(), 
            		INTERNAL_USER_TYPE_NAME, ApprovalStatus.valueOf(userDto.getIsapproved()));
            return (user == null) ? generateEmptyResponse(request, "User(s) not found") :
                    ResponseEntity.status(HttpStatus.OK).body(user);
        } catch (Exception exception) {
            return generateFailureResponse(request, exception);
        }
    }//submitInternalUserForApproval

    @GetMapping("/getAllInternalUsers")
    public ResponseEntity<?> getAllInternalUsers(HttpServletRequest request, @RequestParam String provincecode) {

        try {
            List<User> userList = new ArrayList<>();

            if(StringUtils.isEmpty(provincecode) || "all".equalsIgnoreCase(provincecode.trim())){
                userList =  userService.getAllUsersByUserTypeName(INTERNAL_USER_TYPE_NAME);

                for(User userInfo: userList) {
                    ArrayList<InternalUserRoles> interUserRolesList = new ArrayList<>();
                    ArrayList<InternalUserRoles> internalUserRoles = this.internalUserRoleService.getChildElementsInternal(userInfo.getUserCode());
                    if (!isEmpty(internalUserRoles) && internalUserRoles != null) {
                        for (InternalUserRoles in : internalUserRoles) {
                            System.out.println("Internal user roles" + in.getInternalRoleCode());
                            InternalUserRoles internalUserRoles1 = new InternalUserRoles();
                            internalUserRoles1.setInternalRoleCode(in.getInternalRoleCode());
                            internalUserRoles1.setRoleCode(in.getRoleCode());
                            internalUserRoles1.setRoleName(in.getRoleName());
                            internalUserRoles1.setProvinceCode(in.getProvinceCode());
                            internalUserRoles1.setProvinceName(in.getProvinceName());
                            internalUserRoles1.setSectionCode(in.getSectionCode());
                            internalUserRoles1.setSectionName(in.getSectionName());
                            internalUserRoles1.setUserRoleId(in.getUserRoleId());
                            internalUserRoles1.setIsActive(in.getIsActive());
                            internalUserRoles1.setUserName(in.getUserName());
                            internalUserRoles1.setCreateddate(in.getCreateddate());
                            interUserRolesList.add(internalUserRoles1);
                            userInfo.setInternalUserRoles(interUserRolesList);
                        }
                    }else{
                        userInfo.setInternalUserRoles(new ArrayList<InternalUserRoles>());
                    }
                }
            }else
            {
                userList =   userService.getAllInternalUsersByProvinceCode(provincecode);

                for(User userInfo: userList) {
                    ArrayList<InternalUserRoles> interUserRolesList = new ArrayList<>();
                    ArrayList<InternalUserRoles> internalUserRoles = this.internalUserRoleService.getChildElementsInternal(userInfo.getUserCode());
                    if (!isEmpty(internalUserRoles) && internalUserRoles != null) {
                        for (InternalUserRoles in : internalUserRoles) {
                            System.out.println("Internal user roles" + in.getInternalRoleCode());
                            InternalUserRoles internalUserRoles1 = new InternalUserRoles();
                            internalUserRoles1.setInternalRoleCode(in.getInternalRoleCode());
                            internalUserRoles1.setRoleCode(in.getRoleCode());
                            internalUserRoles1.setRoleName(in.getRoleName());
                            internalUserRoles1.setProvinceCode(in.getProvinceCode());
                            internalUserRoles1.setProvinceName(in.getProvinceName());
                            internalUserRoles1.setSectionCode(in.getSectionCode());
                            internalUserRoles1.setSectionName(in.getSectionName());
                            internalUserRoles1.setUserRoleId(in.getUserRoleId());
                            internalUserRoles1.setIsActive(in.getIsActive());
                            internalUserRoles1.setUserName(in.getUserName());
                            internalUserRoles1.setCreateddate(in.getCreateddate());
                            interUserRolesList.add(internalUserRoles1);
                            userInfo.setInternalUserRoles(interUserRolesList);
                        }
                    }else{
                        userInfo.setInternalUserRoles(new ArrayList<InternalUserRoles>());
                    }
                }
            }

            return (CollectionUtils.isEmpty(userList)) ? generateEmptyResponse(request, "User(s) not found")
                    : ResponseEntity.status(HttpStatus.OK).body(userList);
        } catch (Exception exception) {
            return generateFailureResponse(request, exception);
        }
       /* try {
            List<User> userList = (StringUtils.isEmpty(provincecode) || "all".equalsIgnoreCase(provincecode.trim())) ?
                    userService.getAllUsersByUserTypeName(INTERNAL_USER_TYPE_NAME) :
                    userService.getAllInternalUsersByProvinceCode(provincecode);
            return (CollectionUtils.isEmpty(userList)) ? generateEmptyResponse(request, "User(s) not found")
                    : ResponseEntity.status(HttpStatus.OK).body(userList);
        } catch (Exception exception) {
            return generateFailureResponse(request, exception);
        }*/
    }//getAllInternalUsers


    /*@GetMapping("/getUserRegisteredCounts")
    public ResponseEntity<?> getCountOfRegisteredUsers(HttpServletRequest request, @RequestParam String provincecode,  @RequestParam String type) {
        try {
            ArrayList<InternalUserRoles> internalUserRoles = null;
            ArrayList<InternalUserRoles> interUserRolesList = null;
            List<InternalUserRoles> roles = new ArrayList<>();
            if((StringUtils.isEmpty(provincecode) || "all".equalsIgnoreCase(provincecode.trim()))){
                if(type.equalsIgnoreCase("Internal")){
                    List<User> userList =   userService.getAllUsersByUserTypeName(INTERNAL_USER_TYPE_NAME);
                    for(User userInfo: userList) {
                        interUserRolesList = new ArrayList<>();
                        internalUserRoles = this.internalUserRoleService.getChildElementsInternal(userInfo.getUserCode());
                        System.out.println("User code" +userInfo.getUserCode());
                        if (!isEmpty(internalUserRoles) && internalUserRoles != null) {
                            for (InternalUserRoles in : internalUserRoles) {
                                System.out.println("Internal user roles" + in.getInternalRoleCode());
                                InternalUserRoles internalUserRoles1 = new InternalUserRoles();
                                internalUserRoles1.setProvinceCode(in.getProvinceCode());
                                internalUserRoles1.setProvinceName(in.getProvinceName());
                                internalUserRoles1.setCreateddate(in.getCreateddate());
                                interUserRolesList.add(internalUserRoles1);
                                userInfo.setInternalUserRoles(interUserRolesList);
                            }
                        }
                    }

                    for(User userListInfo : userList) {
                        if (userListInfo.getInternalUserRoles() != null) {
                            for (InternalUserRoles internal : userListInfo.getInternalUserRoles()) {
                                InternalUserRoles internalUserRoles1 = new InternalUserRoles();
                                roles = userListInfo.getInternalUserRoles();
                                internalUserRoles1.setProvinceCode(internal.getProvinceCode());
                                internalUserRoles1.setProvinceName(internal.getProvinceName());
                                internalUserRoles1.setCreateddate(internal.getCreateddate());
                                roles.add(internalUserRoles1);
                            }
                        }
                    }
                    if (!isEmpty(interUserRolesList)) {
                        return ResponseEntity.status(HttpStatus.OK).body(roles);
                    } else {
                        return ResponseEntity.status(HttpStatus.OK).body(roles);
                    }
                }
            }
            return ResponseEntity.status(HttpStatus.OK).body("No List");


        } catch (Exception exception) {
            return generateFailureResponse(request, exception);
        }
    }//getCountOfRegisteredUsers*//*


*/
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
    public ResponseEntity<?> updateExternalUser(HttpServletRequest request, @RequestBody UserUpdateDTO externalUserDTO) {
        try {
            System.out.println("User Code "+externalUserDTO.getUserCode());
            User externalUser = this.userService.updateUser(externalUserDTO);

            return (externalUser == null) ? generateEmptyResponse(request, "Failed to update external user") :
                    ResponseEntity.status(HttpStatus.OK).body("Update Successful");
        } catch (Exception exception) {
            return generateFailureResponse(request, exception);
        }
    }//updateExternalUser




    @PostMapping("/updateAccessRights")
    public ResponseEntity<?> updateAccessRights(HttpServletRequest request, @RequestBody @Valid UpdateAccessRightsDTO updateAccessRightsDTO) {
        try {
            UserControllerResponse userControllerResponse = new UserControllerResponse();
            Gson gson = new Gson();
            String json = null;
            System.out.println("User Type is " + updateAccessRightsDTO.getUsertype());
            if (updateAccessRightsDTO.getUsertype().equalsIgnoreCase("Internal")) {
                for (RolesDTO roles : updateAccessRightsDTO.getRoles()) {
                    if (!isEmpty(roles)) {
                        InternalRole internalRole = this.internalRoleService.updateAccessRight(roles.getProvincecode(), roles.getRolecode(), roles.getSectioncode());
                        if (!isEmpty(internalRole)) {
                            internalRole.setAccessRightJson(updateAccessRightsDTO.getAccessrightjson());
                            this.internalRoleService.updateInternalRole(internalRole);
                            userControllerResponse.setMessage("updated Rights access successfully");
                            json = gson.toJson(userControllerResponse);
                            return ResponseEntity.status(HttpStatus.OK).body(userControllerResponse);
                        } else {
                            return generateEmptyResponse(request, "No roles found");
                        }

                    } else {
                        return generateEmptyResponse(request, "No roles found");
                    }
                }
            } else {
                for (RolesDTO roles : updateAccessRightsDTO.getRoles()) {
                    if (!isEmpty(roles)) {
                        ExternalRole externalRole = this.externalRoleService.updateAccessRight(roles.getProvincecode(), roles.getRolecode());
                        if (!isEmpty(externalRole)) {
                            externalRole.setAccessRightJson(updateAccessRightsDTO.getAccessrightjson());
                            this.externalRoleService.updateExternalRole(externalRole);
                            userControllerResponse.setMessage("updated Rights access successfully");
                            json = gson.toJson(userControllerResponse);
                            return ResponseEntity.status(HttpStatus.OK).body(userControllerResponse);
                        } else {
                            return generateEmptyResponse(request, "No roles found");
                        }
                    }
                }
            }
            return generateEmptyResponse(request, "No roles found");
        } catch (Exception exception) {
            return generateFailureResponse(request, exception);
        }
    }
    //updateAccessRights

    //

    @GetMapping("/getUsersForPendingApproval")
    public ResponseEntity<?> getUsersForPendingApproval(HttpServletRequest request, @RequestParam String provincecode) {
        try {
            List<User> userList = (StringUtils.isEmpty(provincecode) || "all".equalsIgnoreCase(provincecode.trim())) ?
                    userService.getAllExternalApprovalPendingUsers(ApprovalStatus.PENDING.name(), ApprovalStatus.YES.name()) :
                    userService.getAllExternalApprovalPendingUsersByProvinceCode(ApprovalStatus.PENDING.name(), ApprovalStatus.YES.name(), provincecode);
            return (CollectionUtils.isEmpty(userList)) ? generateEmptyResponse(request, "User(s) not found")
                    : ResponseEntity.status(HttpStatus.OK).body(userList);
        } catch (Exception exception) {
            return generateFailureResponse(request, exception);
        }
    }//getUsersForPendingApproval


    @GetMapping("/getMyAssistants")
    public ResponseEntity<?> getSurveyorAssistants(HttpServletRequest request, @RequestParam String surveyorusercode) {
        try {
            List<User> userList = null;
            if (!StringUtils.isEmpty(surveyorusercode)) {
                userList = userService.getAllAssistantsBySurveyorUserCode(surveyorusercode);
            }
            return (CollectionUtils.isEmpty(userList)) ? generateEmptyResponse(request, "Assistant user(s) not found")
                    : ResponseEntity.status(HttpStatus.OK).body(userList);
        } catch (Exception exception) {
            return generateFailureResponse(request, exception);
        }
    }//getSurveyorAssistants

    @GetMapping("/getMySurveyors")
    public ResponseEntity<?> getAssistantsSurveyor(HttpServletRequest request, @RequestParam String assistantusercode) {
        try {
            List<User> userList = null;
            if (!StringUtils.isEmpty(assistantusercode)) {
                userList = userService.getAllSurveyorsByAssistantsUserCode(assistantusercode);
            }
            return (CollectionUtils.isEmpty(userList)) ? generateEmptyResponse(request, "Assistant user(s) not found")
                    : ResponseEntity.status(HttpStatus.OK).body(userList);
        } catch (Exception exception) {
            return generateFailureResponse(request, exception);
        }
    }//getAssistantsSurveyor


    @GetMapping("/getAssistantsForPendingApproval")
    public ResponseEntity<?> getAssistantsForPendingApproval(HttpServletRequest request, @RequestParam String surveyorusercode) {
        try {
            List<User> userList = null;
            if (!StringUtils.isEmpty(surveyorusercode)) {
                userList = userService.getAllAssistantsForPendingApprovalBySurveyorUserCode(ApprovalStatus.WAITING.name(), ApprovalStatus.PENDING.name(), surveyorusercode);
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
    }//checkUserExistsInDB


    @GetMapping("/getUserInfoByEmail")
    public ResponseEntity<?> getUserInfoByMail(HttpServletRequest request, @RequestParam String email) {

        System.out.println("email is "+email);
        try {

            User userInfo = this.userService.findByEmail(email);
            if(!isEmpty(userInfo) && userInfo!= null && userInfo.getUserTypeName().equalsIgnoreCase("EXTERNAL")) {
                ExternalUser externalUser = this.userService.getChildElements(userInfo.getUserCode());
                userInfo.setExternaluser(externalUser);
            }else if(!isEmpty(userInfo) && userInfo!= null && userInfo.getUserTypeName().equalsIgnoreCase("INTERNAL")) {
                System.out.println("User code is: " + userInfo.getUserCode());
                List<InternalUserRoles> interUserRolesList = new ArrayList<>();
                List<InternalUserRoles> internalUserRoles = this.internalUserRoleService.getChildElementsInternal(userInfo.getUserCode());
                if(!isEmpty(internalUserRoles) && internalUserRoles!= null ) {
                    for(InternalUserRoles in: internalUserRoles) {
                        System.out.println("Internal user roles" + in.getInternalRoleCode());
                        InternalUserRoles internalUserRoles1 = new InternalUserRoles();
                        internalUserRoles1.setInternalRoleCode(in.getInternalRoleCode());
                        internalUserRoles1.setRoleCode(in.getRoleCode());
                        internalUserRoles1.setRoleName(in.getRoleName());
                        internalUserRoles1.setProvinceCode(in.getProvinceCode());
                        internalUserRoles1.setProvinceName(in.getProvinceName());
                        internalUserRoles1.setSectionCode(in.getSectionCode());
                        internalUserRoles1.setSectionName(in.getSectionName());
                        internalUserRoles1.setUserRoleId(in.getUserRoleId());
                        internalUserRoles1.setIsActive(in.getIsActive());
                        internalUserRoles1.setUserName(in.getUserName());
                        internalUserRoles1.setCreateddate(in.getCreateddate());
                        interUserRolesList.add(internalUserRoles1);
                        userInfo.setInternalUserRoles(interUserRolesList);
                    }
                }
            }
            return (isEmpty(userInfo)) ? generateEmptyWithOKResponse(request, "Users not found")
                    : ResponseEntity.status(HttpStatus.OK).body(userInfo);
        } catch (Exception exception) {
            return generateFailureResponse(request, exception);
        }
    }//getUserInfoByMail


    @RequestMapping(value = "/deleteExternalUser", method = RequestMethod.POST)
    public ResponseEntity<?> deleteAssistant(HttpServletRequest request, @RequestBody @Valid UserDTO userDTO) throws IOException {
        try {
            User user = this.userService.findByUserCode(userDTO);
            if (isEmpty(user)){
                return generateEmptyResponse(request, "Users are  not found");
            }
            if (!isEmpty(user)) {
                ExternalUser externalUser = this.userService.getChildElements(userDTO.getUsercode());
                user.setExternaluser(externalUser);
                this.userService.deleteUserAndChild(user);
            }
            //todo Send Email to User
            return ResponseEntity.status(HttpStatus.OK).body("User Deleted Successfully");
        } catch (Exception exception) {
            return generateFailureResponse(request, exception);
        }
    }
    @PostMapping("/registerExternalUser")
    public ResponseEntity<?> createExternalUser(HttpServletRequest request, @Valid @RequestBody User user) {
        Gson gson = new Gson();
        String json = null;
        UserControllerResponse userControllerResponse = new UserControllerResponse();
        try {
            System.out.println("Email is " + user.getEmail());

           User userExists = this.userService.findByEmail(user.getEmail());
            if (userExists!= null && userExists.getEmail() != null) {
                userControllerResponse.setMessage("User Already Exist with this email ID");
                json = gson.toJson(userControllerResponse);
                return ResponseEntity.status(HttpStatus.OK).body(json);
            }

            Long userID = this.userService.getUserId();
            System.out.println("UserId is " + userID);
            mapUserDetails(user, userID);
            if (!user.getExternalUserRoles().isEmpty()) {
                ArrayList<ExternalUserRoles> externalRoleCode = new ArrayList<>();
                for (ExternalUserRoles externalUserRoles : user.getExternalUserRoles()) {
                    if (externalUserRoles.getUserRoleCode().equalsIgnoreCase("EX011")) {
                        saveExternalUserAssistant(user, externalRoleCode, externalUserRoles);
                    } else {
                        ExternalRole externalRole = this.externalRoleService.getByRoleCodeRoleProvince(externalUserRoles.getUserRoleCode(), externalUserRoles.getUserProvinceCode());
                        externalUserRolesMapping(user, externalUserRoles, externalRole);
                        externalRoleCode.add(externalUserRoles);
                    }
                }
            }
            User response = userService.saveExternalUser(user);
            MailDTO mailDTO = getMailDTO(user);
            sendMailInformation(user, mailDTO);
            return ResponseEntity.status(HttpStatus.OK).body(response);
        } catch (Exception exception) {
            return generateFailureResponse(request, exception);
        }
    }//createExternalUser


    @PostMapping("/registerInternalUser")
    public ResponseEntity<?> createInternalUser(HttpServletRequest request, @RequestBody @Valid User internalUser) {
        Gson gson = new Gson();
        UserControllerResponse userControllerResponse = new UserControllerResponse();
        String json = null;
        try {
            User userExists = this.userService.findByEmail(internalUser.getEmail());
            if (userExists != null && userExists.getEmail() != null) {
                userControllerResponse.setMessage("User Already Exist with this email ID");
                json = gson.toJson(userControllerResponse);
                return ResponseEntity.status(HttpStatus.OK).body(json);
            }
            Long userID = this.userService.getUserId();
            internalUser.setUserId(userID);
            internalUser.setUserCode("USR000" + Long.toString(internalUser.getUserId()));
            User response = userService.saveInternalUser(internalUser);
            return ResponseEntity.status(HttpStatus.OK).body(response);
        } catch (Exception exception) {
            return generateFailureResponse(request, exception);
        }
    }//saveInternalUser


    @RequestMapping(value = "/approveRejectUser", method = RequestMethod.POST)
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
    }//approveRejectAssitant


   @RequestMapping(value = "/updatePassword", method = RequestMethod.POST)
    public ResponseEntity<?> updatePassword(HttpServletRequest request, @RequestBody @Valid UpdatePasswordDTO updatePasswordDTO) throws IOException {
        try {
            UserControllerResponse userControllerResponse = new UserControllerResponse();
            Gson gson = new Gson();
            String  json = null;
            UserDTO userDTO = new UserDTO();
            userDTO.setUsercode(updatePasswordDTO.getUsercode());
            userDTO.setUsername(updatePasswordDTO.getUsername());
            User user = this.userService.findByUserByNameAndCode(userDTO);
            if (isEmpty(user)) {
                return generateEmptyResponse(request, "Users not found");
            }
            if (!isEmpty(user)) {
                if(updatePasswordDTO.getType().equalsIgnoreCase("change")) {
                    if (user.getPassword().equalsIgnoreCase(updatePasswordDTO.getOldpassword())) {
                        user.setPassword(updatePasswordDTO.getNewpassword());
                    }else{
                        userControllerResponse.setMessage("Old Password and new password do not match");
                          json = gson.toJson(userControllerResponse);
                        return ResponseEntity.status(HttpStatus.OK).body(json);
                    }
                }else if(updatePasswordDTO.getType().equalsIgnoreCase("reset")){
                    user.setPassword(updatePasswordDTO.getNewpassword());
                }
            }
            this.userService.updatePassword(user);
            //todo Send Email to User
            userControllerResponse.setMessage("User Password Updated Sucessfully");
            json = gson.toJson(userControllerResponse);
            return ResponseEntity.status(HttpStatus.OK).body(json);
        } catch (Exception exception) {
            return generateFailureResponse(request, exception);
        }
    }//updatePassword*/



    @RequestMapping(value = "/deactivateUser", method = RequestMethod.POST)
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
    }//deactivateUser


    private void mapUserDetails(@RequestBody @Valid User user, Long userID) {
        user.setUserId(userID);
        user.setUserCode("USR000" + Long.toString(user.getUserId()));
        user.getExternaluser().setUserId(user.getUserId());
        user.getExternaluser().setUsercode(user.getUserCode());
        user.getExternaluser().setCreatedDate(new Date());
        final int SHORT_ID_LENGTH = 8;
        user.setPassword(RandomStringUtils.randomAlphanumeric(SHORT_ID_LENGTH));
        user.setFirstLogin("Y");
    }


    private void sendMailInformation(@RequestBody @Valid User user, MailDTO mailDTO) {
        String mailResponse;
        mailDTO.setHeader(ExceptionConstants.header + " " + user.getFirstName() + ",");
        mailDTO.setFooter(ExceptionConstants.footer + " CIS ADMIN");
        mailDTO.setToAddress(user.getEmail());//admin user for later
        mailResponse = sendMail(mailDTO);
    }

    private void externalUserRolesMapping(@RequestBody @Valid User user, ExternalUserRoles externalUserRoles, ExternalRole externalRole) {
        externalUserRoles.setExternalRoleCode(externalRole.getExternalRoleCode());
        externalUserRoles.setUserCode(user.getUserCode());
        externalUserRoles.setUserId(user.getUserId());
        externalUserRoles.setUserName(user.getUserName());
    }

    private void saveExternalUserAssistant(@RequestBody @Valid User user, ArrayList<ExternalUserRoles> externalRoleCode, ExternalUserRoles externalUserRoles) {
        System.out.println("Inside the role code " + externalUserRoles.getUserRoleCode());
        ExternalUserAssistant externalUserAssistant = new ExternalUserAssistant();
        externalUserAssistantMapping(user, externalUserAssistant);
        System.out.println("Inside the role code else " + externalUserRoles.getUserRoleCode());
        System.out.println("Role Code " + externalUserRoles.getUserRoleCode());
        System.out.println("province code " + externalUserRoles.getUserProvinceCode());
        ExternalRole externalRole = this.externalRoleService.getByRoleCodeRoleProvince(externalUserRoles.getUserRoleCode(), externalUserRoles.getUserProvinceCode());
        System.out.println(externalRole.getExternalRoleCode());
        externalUserRolesMapping(user, externalUserRoles, externalRole);
        externalRoleCode.add(externalUserRoles);
        ExternalUserAssistant externalUserAssistant1 = this.externalUserAssistantService.saveExternalAssistant(externalUserAssistant);
        //// TODO: 2019/04/01 Send email to surveyor
    }

    private void externalUserAssistantMapping(@RequestBody @Valid User user, ExternalUserAssistant externalUserAssistant) {
        externalUserAssistant.setSurveyorusercode(user.getUserCode());
        externalUserAssistant.setSurveyorusername(user.getUserName());
        externalUserAssistant.setAssistantusercode(user.getUserCode());
        externalUserAssistant.setAssistantusername(user.getEmail());
        externalUserAssistant.setIsApproved("Pending");
        externalUserAssistant.setIsActive("Y");
        externalUserAssistant.setCreateddate(new Date());
        externalUserAssistant.setUserId(user.getUserId());
    }

    private MailDTO getMailDTO(@RequestBody @Valid User user) {
        MailDTO mailDTO = new MailDTO();
        mailDTO.setHeader(ExceptionConstants.header);
        mailDTO.setFooter(ExceptionConstants.footer);
        System.out.println("user.getIsApproved().getDisplayString() " + user.getIsApproved().getDisplayString());
        if (user.getIsApproved().getDisplayString().equalsIgnoreCase("YES")) {
            mailDTO.setBody(ExceptionConstants.body + "\\n" + ". Your password is " + user.getPassword() + " User have been approved");
        } else {
            mailDTO.setBody(ExceptionConstants.body + ".Your password is " + user.getPassword() + " User is waiting for approval");
        }
        mailDTO.setSubject(ExceptionConstants.subject);
        return mailDTO;
    }

}

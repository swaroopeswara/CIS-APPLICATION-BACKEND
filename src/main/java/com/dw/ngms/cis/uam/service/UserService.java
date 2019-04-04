package com.dw.ngms.cis.uam.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.dw.ngms.cis.uam.dto.UserDTO;
import com.dw.ngms.cis.uam.entity.ExternalUser;
import com.dw.ngms.cis.uam.entity.User;
import com.dw.ngms.cis.uam.enums.ApprovalStatus;
import com.dw.ngms.cis.uam.ldap.LdapClient;
import com.dw.ngms.cis.uam.ldap.UserProfile;
import com.dw.ngms.cis.uam.repository.UserRepository;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class UserService {

	@Autowired
	private UserRepository userRepository;
	@Autowired
	private LdapClient ldapClient;

	public List<User> getAllUsersByUserTypeName(String userTypeName){
		return userRepository.findByUserTypeName(userTypeName);
	}//getAllUsersByUserTypeName
	
	public User submitInternalUserForApproval(String usercode, String username, String userType, ApprovalStatus approvalStatus) {
		if(StringUtils.isEmpty(usercode) || StringUtils.isEmpty(username)) {
    		throw new RuntimeException("User code and user name required");
    	}
		//fetch internal user by code and name
    	User user = getUserByUserCodeUserNameAndTypeName(usercode, username, userType);
    	if(user == null) {
    		throw new RuntimeException("User(s) not found");
    	}
    	//update approval status
    	user.setIsApproved(approvalStatus);
    	//persist to db
    	user = userRepository.save(user);
    	
    	return user;
	}//submitInternalUserForApproval
	
	public User getUserByUserCodeUserNameAndTypeName(String userCode, String userName, String userTypeName){
		return userRepository.findByUserCodeUserNameAndTypeName(userCode, userName, userTypeName);
	}//getAllUsersByUserTypeName
	
	public List<User> getAllInternalUsersByProvinceCode(String provinceCode){
		return userRepository.findInternalUsersByProvinceCode(provinceCode);
	}//getAllUsersByUserTypeNameAndProvinceCode

	public List<User> getAllExternalUsersByProvinceCode(String provinceCode){
		return userRepository.findExternalUsersByProvinceCode(provinceCode);
	}//getAllUsersByUserTypeNameAndProvinceCode
	
	public List<User> getAllApprovalPendingUsers(String approvalStatus){
		return userRepository.findByApprovalStatus(approvalStatus);
	}//getAllApprovalPendingUsers
	
	public List<User> getAllExternalApprovalPendingUsers(String userApprovalStatus, String assistantApprovalStatus){
		return userRepository.findExternalUsersByApprovalStatus(userApprovalStatus, assistantApprovalStatus);
	}//getAllExternalApprovalPendingUsers
	
	public List<User> getAllExternalApprovalPendingUsersByProvinceCode(String userApprovalStatus, String assistantApprovalStatus, String provinceCode){
		return userRepository.findExternalUsersByApprovalStatusAndProvinceCode(userApprovalStatus, assistantApprovalStatus, provinceCode);
	}//getAllExternalApprovalPendingUsersByProvinceCode
	
	public List<User> getAllAssistantsForPendingApprovalBySurveyorUserCode(String userApprovalStatus, String assistantApprovalStatus,
			String surveyorusercode) {
		return userRepository.findAssistantsForPendingApprovalStatusAndProvinceCode(userApprovalStatus, assistantApprovalStatus, surveyorusercode);
	}//getAllAssistantsForPendingApprovalBySurveyorUserCode
	
	public List<User> getAllAssistantsBySurveyorUserCode(String surveyorusercode) {
		return userRepository.findAssistantsSurveyorUserCode(surveyorusercode);
	}//getAllAssistantsBySurveyorUserCode
	
	public List<User> getAllSurveyorsByAssistantsUserCode(String assistantusercode){
		return userRepository.findSurveyorsByAssistantsUserCode(assistantusercode);
	}//getAllSurveyorByAssistantsUserCode
	
	public User findByEmail(String email) {
		return this.userRepository.findByEmail(email);
	} //FindUserByEmail

	public Long getUserId() {
		return this.userRepository.getUserId();
	} //FindUserByEmail

	public ExternalUser getChildElements(String userCode) {
		return this.userRepository.getChildElements(userCode);
	} //FindUserByEmail




	public User findByUserByNameAndCode(UserDTO userDTO) {
		return this.userRepository.findByUserByNameAndCode(userDTO.getUsercode(), userDTO.getUsername());
	}//FindUserByNameAndCode


	public User findByUserCode(UserDTO userDTO) {
		return this.userRepository.findByUserCode(userDTO.getUsercode());
	}

	public void deleteUserAndChild(User user) {
		 this.userRepository.delete(user);
	}




	public User updateUserApproval(User user) {
		return this.userRepository.save(user);
	}//updateUserApproval

	public User updatePassword(User user) {
		return this.userRepository.save(user);
	}//updateUserApproval

	public User saveExternalUser(User user) {
		return this.userRepository.save(user);
	}//saveExternalUser

	public User updateExternalUser(User uiUser) {
		if(uiUser == null || uiUser.getUserCode() == null || uiUser.getUserName() == null) return null;
		
		User user = userRepository.findByUserByNameAndCode(uiUser.getUserCode(), uiUser.getUserName());
		if(user == null) return null;

		return this.userRepository.save(getPopulatedUserWithModifiedDetails(user, uiUser));
	}//updateExternalUser
	
	private User getPopulatedUserWithModifiedDetails(User user, User uiUser) {
		if(uiUser.getEmail() != null) user.setEmail(uiUser.getEmail());
		if(uiUser.getExternaluser() != null) user.setExternaluser(uiUser.getExternaluser());
		if(uiUser.getEmail() != null) user.setEmail(uiUser.getEmail());
		if(uiUser.getFirstName() != null) user.setFirstName(uiUser.getFirstName());
		if(uiUser.getIsActive() != null) user.setIsActive(uiUser.getIsActive());
		if(uiUser.getIsApprejDate() != null) user.setIsApprejDate(uiUser.getIsApprejDate());
		if(uiUser.getIsApprejuserCode() != null) user.setIsApprejuserCode(uiUser.getIsApprejuserCode());
		if(uiUser.getIsApprejuserName() != null) user.setIsApprejuserName(uiUser.getIsApprejuserName());
		if(uiUser.getIsApproved() != null) user.setIsApproved(uiUser.getIsApproved());
		if(uiUser.getMobileNo() != null) user.setMobileNo(uiUser.getMobileNo());
//		if(uiUser.getPassword() != null) user.setPassword(uiUser.getPassword());//TODO confirm
		if(uiUser.getRejectionReason() != null) user.setRejectionReason(uiUser.getRejectionReason());
		if(uiUser.getSurname() != null) user.setSurname(uiUser.getSurname());
		if(uiUser.getTelephoneNo() != null) user.setTelephoneNo(uiUser.getTelephoneNo());
		if(uiUser.getTitle() != null) user.setTitle(uiUser.getTitle());
		if(uiUser.getUserTypeCode() != null) user.setUserTypeCode(uiUser.getUserTypeCode());
		if(uiUser.getUserTypeName() != null) user.setUserTypeName(uiUser.getUserTypeName());
		return user;
	}//getPopulatedUserWithModifiedDetails

	public User saveInternalUser(User user) {
		if(user == null) return null;
		return this.userRepository.save(user);
	}//saveInternalUser

	public UserProfile isADUserExists(String username, String password) {
		UserProfile userProfile = new UserProfile();
		try {
			if (ldapClient.authenticate(username.trim(), password.trim())) {
				List<UserProfile> userProfileList = ldapClient.searchUser(username.trim());
				userProfile = (userProfileList != null && userProfileList.size() == 1) ? userProfileList.get(0)
						: userProfile;
			}
		} catch (Exception e) {
			log.error("User '{}' LDAP authentication failed", username);
		}
		return userProfile;
	}// isADUserExists

}

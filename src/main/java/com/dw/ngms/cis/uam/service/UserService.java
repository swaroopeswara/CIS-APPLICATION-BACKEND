package com.dw.ngms.cis.uam.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.dw.ngms.cis.uam.dto.UserDTO;
import com.dw.ngms.cis.uam.entity.User;
import com.dw.ngms.cis.uam.repository.UserRepository;

@Service
public class UserService {

	@Autowired
	private UserRepository userRepository;
	
	public List<User> getAllUsersByUserTypeName(String userTypeName){
		return userRepository.findByUserTypeName(userTypeName);
	}//getAllUsersByUserTypeName
	
	public User submitInternalUserForApproval(String usercode, String username, String userType, String approvalStatus) {
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
	
	public List<User> getAllApprovalPendingUsersByProvinceCode(String approvalStatus, String provinceCode){
		return userRepository.findExternalUsersByApprovalStatusAndProvinceCode(approvalStatus, provinceCode);
	}//getAllApprovalPendingUsersByProvinceCode
	
	public List<User> getAllAssistantsForPendingApprovalBySurveyorUserCode(String approvalPendingStatus,
			String surveyorusercode) {
		return userRepository.findAssistantsForPendingApprovalStatusAndProvinceCode(approvalPendingStatus, surveyorusercode);
	}//getAllAssistantsForPendingApprovalBySurveyorUserCode
	
	public User findByEmail(String email) {
		return this.userRepository.findByEmail(email);
	} //FindUserByEmail

	public User findByUserByNameAndCode(UserDTO userDTO) {
		return this.userRepository.findByUserByNameAndCode(userDTO.getUsercode(), userDTO.getUsername());
	}//FindUserByNameAndCode

	public User updateUserApproval(User user) {
		return this.userRepository.save(user);
	}//updateUserApproval

	public User saveExternalUser(User user) {
		return this.userRepository.save(user);
	}//saveExternalUser

	public User saveInternalUser(User user) {
		return this.userRepository.save(user);
	}//saveInternalUser

	public Boolean isADUserExists(String username, String password) {
		// TODO Need to build LDAP UTILIES client and confirm user existence
		return Boolean.FALSE;
	}//isADUserExists

}

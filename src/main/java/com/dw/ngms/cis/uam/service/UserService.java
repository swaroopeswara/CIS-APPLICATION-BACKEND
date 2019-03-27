package com.dw.ngms.cis.uam.service;

import java.util.List;

import com.dw.ngms.cis.uam.dto.UserDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dw.ngms.cis.uam.entity.User;
import com.dw.ngms.cis.uam.repository.UserRepository;

@Service
public class UserService {

	@Autowired
	private UserRepository userRepository;
	
	public List<User> getAllUsersByUserTypeName(String userTypeName){
		return userRepository.findByUserTypeName(userTypeName);
	}//getAllUsersByUserTypeName
	
	public List<User> getAllUsersByUserTypeNameAndProvinceCode(String userTypeName, String provinceCode){
		return userRepository.findByUserTypeNameAndProvinceCode(userTypeName, provinceCode);
	}//getAllUsersByUserTypeNameAndProvinceCode

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


}

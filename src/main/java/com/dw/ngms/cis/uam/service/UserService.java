package com.dw.ngms.cis.uam.service;

import java.util.List;

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
}

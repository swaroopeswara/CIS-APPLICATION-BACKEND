package com.dw.ngms.cis.uam.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dw.ngms.cis.uam.entity.PlsUser;
import com.dw.ngms.cis.uam.repository.PlsUserRepository;

@Service
public class PlsUserService {

	@Autowired
	private PlsUserRepository plsUserRepository;

	public List<PlsUser> getAllPlsUsers() {
		return this.plsUserRepository.findAll();
	}//getAllPlsUsers
}

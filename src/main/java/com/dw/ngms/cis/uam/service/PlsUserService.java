package com.dw.ngms.cis.uam.service;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.dw.ngms.cis.uam.entity.PlsUser;
import com.dw.ngms.cis.uam.repository.PlsUserRepository;

@Service
public class PlsUserService {

	@Autowired
	private PlsUserRepository plsUserRepository;
	@Autowired
	private CodeGeneratorService codeGeneratorService;
	
	public List<PlsUser> getAllPlsUsers() {
		return this.plsUserRepository.findAll();
	}//getAllPlsUsers

	public PlsUser findByEmail(String email) {
		return this.plsUserRepository.findByEmail(email);
	} //FindUserByEmail
	
	public PlsUser findByCode(String plsCode) {
		return this.plsUserRepository.findByPlsCode(plsCode);
	}//getPlsUser

	public PlsUser addPlsUser(@Valid PlsUser plsuser) {
		if(plsuser == null) return null;
		if(StringUtils.isEmpty(plsuser.getEmail())) 
			throw new RuntimeException("PlsUser email required to register");
		PlsUser user = findByEmail(plsuser.getEmail());
		if(user != null)
			throw new RuntimeException("PlsUser is already registered with email: "+plsuser.getEmail());
	
		plsuser.setPlscode(getPlsCode());
        return this.plsUserRepository.save(plsuser);
    }//addSector
    
    private String getPlsCode() {
    	return codeGeneratorService.getPlsUserNextCode();
	}//getSectorCode
	
}

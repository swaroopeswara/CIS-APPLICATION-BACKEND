package com.dw.ngms.cis.uam.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dw.ngms.cis.uam.entity.InternalUserRoles;
import com.dw.ngms.cis.uam.repository.InternalUserRoleRepository;

/**
 * Created by swaroop on 2019/03/28.
 */

@Service
public class InternalUserRoleService {

    @Autowired
    private InternalUserRoleRepository internalUserRoleRepository;
    
    public InternalUserRoles getInternalUserRole(String email) {
    	return internalUserRoleRepository.findByEmail(email);
    }//getInternalUserRole
	
    public void deleteInternalUserRole(String usercode, String username, String internalrolecode) {
    	if(usercode == null || username == null || internalrolecode == null)
    		throw new RuntimeException("Usercode and username and internalrolecode required");
    	
    	InternalUserRoles internalUserRoles = internalUserRoleRepository.findByEmail(username);
    	
    	internalUserRoleRepository.delete(internalUserRoles);
    }//deleteInternalUserRole
    
    public InternalUserRoles getInternalUserRole(String usercode, String username, String internalrolecode) {
    	return internalUserRoleRepository.findByUserCodeUserNameAndInternalRoleCode(usercode, username, internalrolecode);
    }//getInternalUserRole

}

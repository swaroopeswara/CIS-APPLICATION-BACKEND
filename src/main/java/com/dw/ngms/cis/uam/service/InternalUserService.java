package com.dw.ngms.cis.uam.service;

import com.dw.ngms.cis.uam.entity.InternalRole;
import com.dw.ngms.cis.uam.entity.InternalUserRoles;
import com.dw.ngms.cis.uam.entity.User;
import com.dw.ngms.cis.uam.repository.InternalUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by swaroop on 2019/03/26.
 */

@Service
public class InternalUserService {

    @Autowired
    InternalUserRepository internalUserRepository;



    public InternalUserRoles saveInternalUserRole(InternalUserRoles internalUserRoles) {

        return this.internalUserRepository.save(internalUserRoles);
    }//saveExternalUser

    public InternalRole getInternalRoleCode(String provinceCode, String sectionCode, String roleCode) {
        return this.internalUserRepository.getInternalRoleCode(provinceCode,sectionCode,roleCode);
    }//get Internal Role Code

    public InternalUserRoles findByUserByNameAndCode(String userCode, String userName) {
        return this.internalUserRepository.findByUserByNameAndCode(userCode,userName);
    }//get Internal Role Code




}

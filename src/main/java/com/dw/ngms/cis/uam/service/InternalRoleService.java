package com.dw.ngms.cis.uam.service;

import com.dw.ngms.cis.uam.entity.InternalRole;
import com.dw.ngms.cis.uam.entity.SecurityQuestion;
import com.dw.ngms.cis.uam.repository.ExternalUserRepository;
import com.dw.ngms.cis.uam.repository.InternalRoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by swaroop on 2019/04/02.
 */

@Service
public class InternalRoleService {

    @Autowired
    InternalRoleRepository internalRoleRepository;


    public InternalRole updateAccessRight(String provinceCode, String roleCode,String sectionCode) {
        return this.internalRoleRepository.updateAccessRight(provinceCode,roleCode,sectionCode);
    }

    public InternalRole updateInternalRole(InternalRole internalRole) {
        return this.internalRoleRepository.save(internalRole);
    }

    public String getAccessRightJson(String internalRoleCode) {
        return this.internalRoleRepository.getAccessRightJson(internalRoleCode);
    }



}

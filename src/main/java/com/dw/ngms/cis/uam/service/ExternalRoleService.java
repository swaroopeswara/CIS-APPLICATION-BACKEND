package com.dw.ngms.cis.uam.service;

import com.dw.ngms.cis.uam.entity.ExternalRole;
import com.dw.ngms.cis.uam.entity.InternalRole;
import com.dw.ngms.cis.uam.repository.ExternalRoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by swaroop on 2019/03/30.
 */
@Service
public class ExternalRoleService {


    @Autowired
    ExternalRoleRepository externalRoleRepository;

    public ExternalRole getByRoleCodeRoleProvince(String rolesCode, String provinceCode) {
        return this.externalRoleRepository.getByRoleCodeRoleProvince(rolesCode, provinceCode);
    }

    public List<ExternalRole> findByRoleCode() {
        return this.externalRoleRepository.findByRoleCode();
    }



    public ExternalRole updateAccessRight(String provinceCode, String roleCode) {
        return this.externalRoleRepository.updateAccessRight(provinceCode, roleCode);
    }


    public ExternalRole updateExternalRole(ExternalRole externalRole) {
        return this.externalRoleRepository.save(externalRole);
    }

    public String getAccessRightJson(String internalRoleCode) {
        return this.externalRoleRepository.getAccessRightJson(internalRoleCode);
    }



}

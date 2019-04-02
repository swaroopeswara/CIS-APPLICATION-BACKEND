package com.dw.ngms.cis.uam.service;

import com.dw.ngms.cis.uam.dto.UserDTO;
import com.dw.ngms.cis.uam.entity.ExternalRole;
import com.dw.ngms.cis.uam.entity.ExternalUser;
import com.dw.ngms.cis.uam.repository.ExternalRoleRepository;
import com.dw.ngms.cis.uam.repository.ExternalUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by swaroop on 2019/03/30.
 */
@Service
public class ExternalRoleService {


    @Autowired
    ExternalRoleRepository externalRoleRepository;

    public ExternalRole getByRoleCodeRoleProvince(String rolesCode, String provinceCode) {
        return this.externalRoleRepository.getByRoleCodeRoleProvince(rolesCode,provinceCode);
    }



}

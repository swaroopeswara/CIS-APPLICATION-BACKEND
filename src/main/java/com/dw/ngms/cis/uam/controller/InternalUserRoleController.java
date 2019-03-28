package com.dw.ngms.cis.uam.controller;

import com.dw.ngms.cis.uam.dto.InternalUserRoleDTO;
import com.dw.ngms.cis.uam.entity.InternalRole;
import com.dw.ngms.cis.uam.entity.InternalUserRoles;
import com.dw.ngms.cis.uam.entity.User;
import com.dw.ngms.cis.uam.service.InternalUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.Date;

/**
 * Created by swaroop on 2019/03/28.
 */

@RestController
@RequestMapping("/cisorigin.uam/api/v1")
public class InternalUserRoleController {

    @Autowired
    private InternalUserService internalUserService;

    @PostMapping("/registerInternalUserRole")
    public InternalUserRoles createInternalUserRole(@RequestBody @Valid InternalUserRoleDTO internalUserRoleDTO) {
        System.out.println("Internal Role Code "+internalUserRoleDTO.getProvinceCode());
        InternalUserRoles internalUserRoles = new InternalUserRoles();
        internalUserRoles.setUserCode(internalUserRoleDTO.getUserCode());
        internalUserRoles.setUserName(internalUserRoleDTO.getUserName());
        internalUserRoles.setUserProvinceCode(internalUserRoleDTO.getProvinceCode());
        internalUserRoles.setUserProvinceName(internalUserRoleDTO.getProvinceName());
        internalUserRoles.setUserSectionCode(internalUserRoleDTO.getSectionCode());
        internalUserRoles.setUserSectionName(internalUserRoleDTO.getSectionName());
        internalUserRoles.setUserRoleCode(internalUserRoleDTO.getRoleCode());
        internalUserRoles.setUserRoleName(internalUserRoleDTO.getRoleName());
        internalUserRoles.setCreateddate(new Date());
        InternalRole internalRole = this.internalUserService.getInternalRoleCode(internalUserRoles.getUserProvinceCode(),internalUserRoles.getUserSectionCode(),internalUserRoles.getUserRoleCode());
        internalUserRoles.setInternalRoleCode(internalRole.getInternalRoleCode());
        System.out.println("Internal Role Code "+internalRole.getInternalRoleCode());
        System.out.println("User Province Name "+internalUserRoles.getUserProvinceName());
        return internalUserService.saveInternalUserRole(internalUserRoles);
    }
}

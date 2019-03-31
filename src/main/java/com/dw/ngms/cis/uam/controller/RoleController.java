package com.dw.ngms.cis.uam.controller;

import com.dw.ngms.cis.uam.entity.Roles;
import com.dw.ngms.cis.uam.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@RequestMapping("/cisorigin.uam/api/v1")
public class RoleController extends MessageController {

    @Autowired
    private RoleService roleService;

    @GetMapping("/getRoles")
    public ResponseEntity<?> getRolesBasedOnRoleType(HttpServletRequest request, @RequestParam String type) {
        try {
        	List<Roles> rolesList = this.roleService.findByRoleType(type);
            return (CollectionUtils.isEmpty(rolesList)) ? generateEmptyResponse(request, "Roles not found")
                    : ResponseEntity.status(HttpStatus.OK).body(rolesList);
        } catch (Exception exception) {
            return generateFailureResponse(request, exception);
        }
    }//getAllRoles
    
    @GetMapping("/getMenuOfUser")
    public ResponseEntity<?> getMenuOfUser(HttpServletRequest request, @RequestParam String roleCode) {
        try {
        	Roles role = this.roleService.getRoleByRoleCode(roleCode);
            return (role == null) ? generateEmptyResponse(request, "Menu for role not found")
                    : ResponseEntity.status(HttpStatus.OK).body(role);
        } catch (Exception exception) {
            return generateFailureResponse(request, exception);
        }
    }//getMenuOfUser
}

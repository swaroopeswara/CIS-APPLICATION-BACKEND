package com.dw.ngms.cis.uam.controller;

import com.dw.ngms.cis.controller.MessageController;
import com.dw.ngms.cis.uam.dto.UserDTO;
import com.dw.ngms.cis.uam.entity.*;
import com.dw.ngms.cis.uam.service.ExternalUserRolesService;
import com.dw.ngms.cis.uam.service.InternalUserRoleService;
import com.dw.ngms.cis.uam.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.io.IOException;
import java.util.List;

import static org.springframework.util.StringUtils.isEmpty;

@RestController
@RequestMapping("/cisorigin.uam/api/v1")
@CrossOrigin(origins = "*")
public class RoleController extends MessageController {

    @Autowired
    private RoleService roleService;

    @Autowired
    private InternalUserRoleService internalUserRoleService;

    @Autowired
    private ExternalUserRolesService externalUserRolesService;

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
        	String menu = this.roleService.getMenuByRoleCode(roleCode);
            return (menu == null) ? generateEmptyResponse(request, "Menu for role not found")
                    : ResponseEntity.status(HttpStatus.OK).body(menu);
        } catch (Exception exception) {
            return generateFailureResponse(request, exception);
        }
    }//getMenuOfUser



    @RequestMapping(value = "/deleteRole", method = RequestMethod.POST)
    public ResponseEntity<?> deleteRole(HttpServletRequest request, @RequestBody @Valid Roles role) throws IOException {
        try {
            Roles roleList = this.roleService.getRoleByRoleCode(role.getRolecode());
            if (isEmpty(roleList)) {
                return generateEmptyResponse(request, "Roles are  not found");
            }
            if (!isEmpty(roleList)) {

                if(roleList.getRoletype().equalsIgnoreCase("INTERNAL")){
                    List<InternalUserRoles> internalUserRoles = this.internalUserRoleService.getInternalUsersForDelete(role.getRolecode());
                    if(internalUserRoles.size() >0 && !internalUserRoles.isEmpty() && internalUserRoles.get(0).getRoleCode()!= null){
                        return generateEmptyWithOKResponse(request, "Can Not delete a User Role which is existing");
                    }else{
                        System.out.println("Delete this internal  user record");
                        this.roleService.deleteRole(roleList);
                        return generateEmptyWithOKResponse(request, "Role Deleted successfully");
                    }

                }else if(roleList.getRoletype().equalsIgnoreCase("EXTERNAL")){
                    List<ExternalUserRoles> externalUserRoles = this.externalUserRolesService.getExternalUsersForDelete(role.getRolecode());
                    if(externalUserRoles.size() >0 && !externalUserRoles.isEmpty() && externalUserRoles.get(0).getUserRoleCode()!= null){
                        return generateEmptyWithOKResponse(request, "Can Not delete a User Role which is existing");
                    }else{
                        System.out.println("Delete this external user record");
                        this.roleService.deleteRole(roleList);
                        return generateEmptyWithOKResponse(request, "Role Deleted successfully");
                    }
                }
            }
            return ResponseEntity.status(HttpStatus.OK).body("User Role Deleted Successfully");
        } catch (Exception exception) {
            return generateFailureResponse(request, exception);
        }
    }



}

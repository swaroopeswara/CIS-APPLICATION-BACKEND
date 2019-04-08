package com.dw.ngms.cis.uam.controller;

import com.dw.ngms.cis.uam.entity.InternalRole;
import com.dw.ngms.cis.uam.service.InternalRoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * Created by swaroop on 2019/04/08.
 */
@RestController
@RequestMapping("/cisorigin.uam/api/v1")
@CrossOrigin(origins = "*")
public class InternalRoleController extends MessageController {

    @Autowired
    private InternalRoleService internalRoleService;


  @GetMapping("/getSectionsByProvince")
    public ResponseEntity<?> getSectionsByProvince(HttpServletRequest request, @RequestParam String provinceCode
                                                   ) {
        try {
            List<InternalRole> internalUserList = this.internalRoleService.getSectionsByProvinceCode(provinceCode);
            return (CollectionUtils.isEmpty(internalUserList)) ? generateEmptyResponse(request, "Internal User not found for the province code")
                    : ResponseEntity.status(HttpStatus.OK).body(internalUserList);
        } catch (Exception exception) {
            return generateFailureResponse(request, exception);
        }
    }//getSectionsByProvince


    @GetMapping("/getNationalRoles")
    public ResponseEntity<?> getNationalRoles(HttpServletRequest request

    ) {
        try {
            List<InternalRole> internalUserList = this.internalRoleService.getNationalRoles();
            return (CollectionUtils.isEmpty(internalUserList)) ? generateEmptyResponse(request, "Internal User not found for the province code")
                    : ResponseEntity.status(HttpStatus.OK).body(internalUserList);
        } catch (Exception exception) {
            return generateFailureResponse(request, exception);
        }
    }//getNationalRoles


    @GetMapping("/getRolesBySections")
    public ResponseEntity<?> getRolesBySections(HttpServletRequest request,
                                                @RequestParam String sectionCode

    ) {
        try {
            List<InternalRole> internalUserList = this.internalRoleService.getRolesBySections(sectionCode);
            return (CollectionUtils.isEmpty(internalUserList)) ? generateEmptyResponse(request, "Internal User not found for the section code")
                    : ResponseEntity.status(HttpStatus.OK).body(internalUserList);
        } catch (Exception exception) {
            return generateFailureResponse(request, exception);
        }
    }//getRolesBySections






}

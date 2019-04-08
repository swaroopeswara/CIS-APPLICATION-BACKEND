package com.dw.ngms.cis.uam.controller;

import com.dw.ngms.cis.uam.entity.ExternalRole;
import com.dw.ngms.cis.uam.entity.InternalRole;
import com.dw.ngms.cis.uam.service.ExternalRoleService;
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
public class ExternalRoleController extends MessageController {

    @Autowired
    ExternalRoleService externalRoleService;



    @GetMapping("/getExternalRolesByRoleCode")
    public ResponseEntity<?> getExternalRolesByRoleCode(HttpServletRequest request
    ) {
        try {
            List<ExternalRole> externalRoleList = this.externalRoleService.findByRoleCode();
            return (CollectionUtils.isEmpty(externalRoleList)) ? ResponseEntity.status(HttpStatus.OK).body(externalRoleList)
                    : ResponseEntity.status(HttpStatus.OK).body(externalRoleList);
        } catch (Exception exception) {
            return generateFailureResponse(request, exception);
        }
    }//getSectionsByProvince



}

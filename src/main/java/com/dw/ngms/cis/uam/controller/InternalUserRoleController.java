package com.dw.ngms.cis.uam.controller;

import com.dw.ngms.cis.uam.dto.InternalUserRoleDTO;
import com.dw.ngms.cis.uam.dto.UserDTO;
import com.dw.ngms.cis.uam.entity.InternalRole;
import com.dw.ngms.cis.uam.entity.InternalUserRoles;
import com.dw.ngms.cis.uam.entity.User;
import com.dw.ngms.cis.uam.service.InternalUserService;
import com.dw.ngms.cis.uam.storage.StorageService;
import com.dw.ngms.cis.uam.utilities.Constants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

/**
 * Created by swaroop on 2019/03/28.
 */

@RestController
@RequestMapping("/cisorigin.uam/api/v1")
public class InternalUserRoleController extends MessageController {

    @Autowired
    private InternalUserService internalUserService;

    @Autowired
    StorageService testService;

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





    @PostMapping("/uploadSignedUserAccess")
    public ResponseEntity<?> handleFileUpload(HttpServletRequest request,@RequestParam("file") MultipartFile file,
                                              @RequestParam("userCode") String userCode,
                                              @RequestParam("userName") String userName,
                                              @RequestParam("provinceCode") String provinceCode,
                                              @RequestParam("provinceName") String provinceName,
                                              @RequestParam("sectionCode") String sectionCode,
                                              @RequestParam("sectionName") String sectionName,
                                              @RequestParam("roleCode") String roleCode,
                                              @RequestParam("roleName") String roleName

    ) {
        String message = "";
        List<String> files = new ArrayList<String>();

        try {
            if(file.isEmpty()){
                return generateEmptyResponse(request, "File Not Found to upload, Please upload a file");
            }else{
                InternalUserRoles internalUserRoles = new InternalUserRoles();
                internalUserRoles.setUserCode(userCode);
                internalUserRoles.setUserName(userName);
                internalUserRoles.setUserProvinceCode(provinceCode);
                internalUserRoles.setUserProvinceName(provinceName);
                internalUserRoles.setUserSectionCode(sectionCode);
                internalUserRoles.setUserSectionName(sectionName);
                internalUserRoles.setUserRoleCode(roleCode);
                internalUserRoles.setUserRoleName(roleName);
                internalUserRoles.setCreateddate(new Date());
                InternalRole internalRole = this.internalUserService.getInternalRoleCode(internalUserRoles.getUserProvinceCode(),internalUserRoles.getUserSectionCode(),internalUserRoles.getUserRoleCode());
                System.out.println("Internal Role is "+internalRole.getInternalRoleCode());
                internalUserRoles.setInternalRoleCode(internalRole.getInternalRoleCode());

               String fileName =  testService.store(file);
                files.add(file.getOriginalFilename());
                System.out.println("original Name File is "+file.getOriginalFilename() );

                internalUserRoles.setSignedAccessDocPath(Constants.uploadDirectoryPath + fileName);
                message = "You successfully uploaded " + internalUserRoles.getSignedAccessDocPath() + "!";
                internalUserService.saveInternalUserRole(internalUserRoles);
                return ResponseEntity.status(HttpStatus.OK).body(message);
            }

        } catch (Exception exception) {
            message = "FAIL to upload " + file.getOriginalFilename() + "!";
            return generateFailureResponse(request, exception);
        }
    }



    @GetMapping("/downloadSignedUserAccess")
    public ResponseEntity<?> downloadFile(HttpServletRequest request, @RequestBody @Valid InternalUserRoleDTO internalUserRoles) throws IOException {
        // Load file from database
        if(internalUserRoles.getUserName()!= null && internalUserRoles.getUserCode()!= null){
            InternalUserRoles ir = this.internalUserService.findByUserByNameAndCode(internalUserRoles.getUserCode(),internalUserRoles.getUserName());
            System.out.println("Internal User Roles one "+ir.getSignedAccessDocPath());
            int index = ir.getSignedAccessDocPath().lastIndexOf("/");
            String fileName = ir.getSignedAccessDocPath().substring(index + 1);
            System.out.println("File Name is "+fileName);
            File file = new File(ir.getSignedAccessDocPath());
            InputStreamResource resource = new InputStreamResource(new FileInputStream(file));
            String exportedContent = resource.getInputStream().toString();
            HttpHeaders headers = new HttpHeaders();
            headers.setAccessControlExposeHeaders(Collections.singletonList("Content-Disposition"));
            headers.set("Content-Disposition", "attachment; filename=" + fileName);
            headers.set("Content-Type","application/pdf");
            return new ResponseEntity<String>(exportedContent, headers, HttpStatus.OK);

        }else{
            return generateEmptyResponse(request, "No Internal Roles  found");
        }

    }
}

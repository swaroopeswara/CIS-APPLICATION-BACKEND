package com.dw.ngms.cis.uam.controller;

import com.dw.ngms.cis.controller.MessageController;
import com.dw.ngms.cis.uam.configuration.ApplicationPropertiesConfiguration;
import com.dw.ngms.cis.uam.dto.FilePathsDTO;
import com.dw.ngms.cis.uam.dto.UserDTO;
import com.dw.ngms.cis.uam.entity.ExternalUser;
import com.dw.ngms.cis.uam.entity.SecurityQuestion;
import com.dw.ngms.cis.uam.entity.User;
import com.dw.ngms.cis.uam.jsonresponse.UserControllerResponse;
import com.dw.ngms.cis.uam.service.ExternalUserService;
import com.dw.ngms.cis.uam.service.UserService;
import com.dw.ngms.cis.uam.storage.StorageService;
import com.google.gson.Gson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import static org.springframework.util.StringUtils.isEmpty;

/**
 * Created by swaroop on 2019/03/28.
 */
@RestController
@RequestMapping("/cisorigin.uam/api/v1")
@CrossOrigin(origins = "*")
public class ExternalController extends MessageController {

    @Autowired
    private ExternalUserService externalUserService;

    @Autowired
    private UserService userService;

    @Autowired
    private StorageService testService;

    @Autowired
    private ApplicationPropertiesConfiguration applicationPropertiesConfiguration;

    @RequestMapping(value = "/updateSecurityQuestions", method = RequestMethod.POST)
    public ResponseEntity<?> updateSecurityQuestions(HttpServletRequest request, @RequestBody @Valid UserDTO userDTO) throws IOException {

        try {
            ExternalUser externalUser = this.externalUserService.findByUserCode(userDTO);
            if (isEmpty(externalUser)) {
                return generateEmptyResponse(request, "Users not found");
            }
            if (!isEmpty(externalUser)) {
                externalUser.setSecurityquestiontypecode1(userDTO.getQuestion().get(0).getCode());
                externalUser.setSecurityquestion1(userDTO.getQuestion().get(0).getQue());
                externalUser.setSecurityanswer1(userDTO.getQuestion().get(0).getAns());
                ;
                externalUser.setSecurityquestiontypecode2(userDTO.getQuestion().get(1).getCode());
                externalUser.setSecurityquestion2(userDTO.getQuestion().get(1).getQue());
                externalUser.setSecurityanswer2(userDTO.getQuestion().get(1).getAns());
                externalUser.setSecurityquestiontypecode3(userDTO.getQuestion().get(2).getCode());
                externalUser.setSecurityquestion3(userDTO.getQuestion().get(2).getQue());
                externalUser.setSecurityanswer3(userDTO.getQuestion().get(2).getAns());
                System.out.println(" externalUser sec ans 3: " + externalUser.getSecurityanswer3());
                this.externalUserService.updateSecurityQuestions(externalUser);
            }
            return ResponseEntity.status(HttpStatus.OK).body("Security Questions updated successfully");
        } catch (Exception exception) {
            return generateFailureResponse(request, exception);
        }
    }//updateSecurityQuestions


    @PostMapping("/uploadDocumentationForInternalUsers")
    public ResponseEntity<?> uploadDocumentationForInternalUsers(HttpServletRequest request,
                                                                 @RequestParam MultipartFile[] multipleFiles,
                                                                 @RequestParam("userCode") String userCode
    ) {
        String json = null;
        Gson gson = new Gson();
        UserControllerResponse userControllerResponse = new UserControllerResponse();
        try {
            ExternalUser externalUser = this.externalUserService.findExternalByUserCode(userCode);
            if (isEmpty(externalUser)) {
                return generateEmptyResponse(request, "Users not found");
            }
            if (!isEmpty(externalUser)) {
                List<String> filesExist = new ArrayList<>();
                for (MultipartFile f : multipleFiles) {
                    List<String> files = new ArrayList<String>();
                    String fileName = testService.store(f);
                    files.add(f.getOriginalFilename());
                    filesExist.add(applicationPropertiesConfiguration.getUploadDirectoryPath() + fileName);
                    userControllerResponse.setFiles(filesExist);
                    json = gson.toJson(userControllerResponse);
                    externalUser.setDocumentUploadMultiple(json);
                    this.externalUserService.updateSecurityQuestions(externalUser);
                }
            }

        } catch (Exception exception) {
            String message = "FAIL to upload the files";
            return generateFailureResponse(request, exception);
        }
        return ResponseEntity.status(HttpStatus.OK).body(json);
    }//uploadDocumentationForInternalUsers


    @RequestMapping(value = "/downloadDocumentation", method = RequestMethod.POST)
    public ResponseEntity<InputStreamResource> downloadDocumentation(HttpServletRequest request,
                                                                     @RequestBody @Valid ExternalUser externalUser) throws IOException {
        ExternalUser ir = this.externalUserService.findExternalByUserCode(externalUser.getUsercode());
        System.out.println("Internal User Roles one " + ir.getDocumentUploadMultiple());
        String pathFromDB = ir.getDocumentUploadMultiple();

        Gson gson = new Gson();
        FilePathsDTO filePath = gson.fromJson(pathFromDB, FilePathsDTO.class);
        System.out.println("filePath is " + filePath.getFiles().toString());
        List<String> files = new ArrayList<String>();
        for (String str1 : filePath.getFiles()) {
            System.out.println(str1);
            files.add(str1);
            zipFiles(files);
        }
        File file = new File(applicationPropertiesConfiguration.getDownloadDirectoryPath() + "DownloadFiles.zip");

        InputStreamResource resource = new InputStreamResource(new FileInputStream(file));
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment;filename= MultipleFiles.zip")
                .contentType(MediaType.parseMediaType("application/octet-stream"))
                .contentLength(file.length()) //
                .body(resource);
    }


    public void zipFiles(List<String> files) {

        FileOutputStream fos = null;
        ZipOutputStream zipOut = null;
        FileInputStream fis = null;
        try {
            fos = new FileOutputStream(applicationPropertiesConfiguration.getDownloadDirectoryPath() + "DownloadFiles.zip");
            zipOut = new ZipOutputStream(new BufferedOutputStream(fos));
            for (String filePath : files) {
                File input = new File(filePath);
                fis = new FileInputStream(input);
                ZipEntry ze = new ZipEntry(input.getName());
                zipOut.putNextEntry(ze);
                byte[] tmp = new byte[4 * 1024];
                int size = 0;
                while ((size = fis.read(tmp)) != -1) {
                    zipOut.write(tmp, 0, size);
                }
                zipOut.flush();
                fis.close();
            }
            zipOut.close();
            System.out.println("Done... Zipped the files...");
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } finally {
            try {
                if (fos != null) fos.close();
            } catch (Exception ex) {

            }
        }
    }


    @RequestMapping(value = "/checkUserSecurityQuestions", method = RequestMethod.POST)
    public ResponseEntity<?> checkUserSecurityQuestions(HttpServletRequest request, @RequestBody @Valid UserDTO userDTO) {
        List<SecurityQuestion> securityQuestions = new ArrayList<>();
        Gson gson = new Gson();
        String json = null;
        UserControllerResponse userControllerResponse = new UserControllerResponse();
        userControllerResponse.setMessage("false");
        json = gson.toJson(userControllerResponse);
        try {
            User user = this.userService.findByEmail(userDTO.getEmail());
            if (isEmpty(user)) {
                return generateEmptyResponse(request, "Users not found");
            }
            ExternalUser externalUser = this.userService.getChildElements(user.getUserCode());
            if (!isEmpty(externalUser)) {
                System.out.println("externalUser.getSecurityquestion3() " + externalUser.getSecurityquestion3());
                System.out.println("userDTO.getQuestion().get(2).getAns() " + userDTO.getQuestion().get(2).getAns());
                if (externalUser.getSecurityquestiontypecode1().equalsIgnoreCase(userDTO.getQuestion().get(0).getCode())
                        && externalUser.getSecurityquestion1().equalsIgnoreCase(userDTO.getQuestion().get(0).getQue())
                        && externalUser.getSecurityanswer1().equalsIgnoreCase(userDTO.getQuestion().get(0).getAns())
                        && externalUser.getSecurityquestiontypecode2().equalsIgnoreCase(userDTO.getQuestion().get(1).getCode())
                        && externalUser.getSecurityquestion2().equalsIgnoreCase(userDTO.getQuestion().get(1).getQue())
                        && externalUser.getSecurityanswer2().equalsIgnoreCase(userDTO.getQuestion().get(1).getAns())
                        && externalUser.getSecurityquestiontypecode3().equalsIgnoreCase(userDTO.getQuestion().get(2).getCode())
                        && externalUser.getSecurityquestion3().equalsIgnoreCase(userDTO.getQuestion().get(2).getQue())
                        && externalUser.getSecurityanswer3().equalsIgnoreCase(userDTO.getQuestion().get(2).getAns())

                        ) {
                    userControllerResponse.setMessage("true");
                    json = gson.toJson(userControllerResponse);
                    return ResponseEntity.status(HttpStatus.OK).body(json);
                }
            }

            return ResponseEntity.status(HttpStatus.OK).body(json);
        } catch (Exception exception) {
            return generateFailureResponse(request, exception);
        }
    }//checkUserSecurityQuestions


    @GetMapping("/getPpNumber")
    public ResponseEntity<?> getPpNumber(HttpServletRequest request,
                                         @RequestParam String ppNumber) {
        Gson gson = new Gson();
        String json = null;
        UserControllerResponse userControllerResponse = new UserControllerResponse();
        try {
            String ppNo = externalUserService.getPpNumber(ppNumber);
            if (!isEmpty(ppNo)) {
                String[] parts = ppNo.split(",");
                userControllerResponse.setExists("true");
                userControllerResponse.setPractiseName(parts[1]);
                json = gson.toJson(userControllerResponse);

                return ResponseEntity.status(HttpStatus.OK).body(json);
            } else {
                userControllerResponse.setExists("false");
                json = gson.toJson(userControllerResponse);
                return ResponseEntity.status(HttpStatus.OK).body(json);
            }

        } catch (Exception exception) {
            return generateFailureResponse(request, exception);
        }
    }//getAllPlsUsers


}

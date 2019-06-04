package com.dw.ngms.cis.uam.controller;

import com.dw.ngms.cis.controller.MessageController;
import com.dw.ngms.cis.uam.configuration.ApplicationPropertiesConfiguration;
import com.dw.ngms.cis.uam.dto.FilePathsDTO;
import com.dw.ngms.cis.uam.dto.MailDTO;
import com.dw.ngms.cis.uam.entity.Notifications;
import com.dw.ngms.cis.uam.jsonresponse.UserControllerResponse;
import com.dw.ngms.cis.uam.service.NotificationService;
import com.dw.ngms.cis.uam.storage.StorageService;
import com.google.gson.Gson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.mail.internet.InternetAddress;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import static org.springframework.util.StringUtils.isEmpty;

/**
 * Created by swaroop on 2019/04/12.
 */

@RestController
@RequestMapping("/cisorigin.uam/api/v1")
@CrossOrigin(origins = "*")
public class NotificationController extends MessageController {

    @Autowired
    private StorageService testService;

    @Autowired
    private NotificationService notificationService;

    @Autowired
    private ApplicationPropertiesConfiguration applicationPropertiesConfiguration;


    @PostMapping("/saveNotification")
    public ResponseEntity<?> saveNotification(HttpServletRequest request, @RequestBody @Valid Notifications notification) {
        try {
            //Long notificationId = this.notificationService.getNotificationId();
            //System.out.println("notificationId is "+notificationId);
            //costCategories.setCategoryCode("COST" + Long.toString(categoryId));
            Notifications notificationSave = this.notificationService.saveNotification(notification);
            MailDTO mailDTO = new MailDTO();
            sendMailToNotification(mailDTO, notification);
            sendMailToNotification1(mailDTO,notification);
            sendMailToNotification2(mailDTO,notification);

            return ResponseEntity.status(HttpStatus.OK).body(notificationSave);
        } catch (Exception exception) {
            return generateFailureResponse(request, exception);
        }
    }//createCategory






    @GetMapping("/getNotifications")
    public ResponseEntity<?> getNotifications(HttpServletRequest request) {
        try {
            List<Notifications> notificationsList = notificationService.getAllNotifications();
            return (CollectionUtils.isEmpty(notificationsList)) ? generateEmptyResponse(request, "Notification(s) not found")
                    : ResponseEntity.status(HttpStatus.OK).body(notificationsList);
        } catch (Exception exception) {
            return generateFailureResponse(request, exception);
        }
    }//getNotifications


    private void sendMailToNotification(MailDTO mailDTO, Notifications notifications) throws Exception {
        Map<String, Object> model = new HashMap<String, Object>();
        model.put("firstName", "User");
        model.put("body1", notifications.getBody());
        model.put("body2", "");
        model.put("body3", "");
        model.put("body4", "");
        mailDTO.setMailSubject(notifications.getSubject());
        mailDTO.setMailTo("swaroopragava23@gmail.com");
        model.put("FOOTER", "CIS ADMIN");
        mailDTO.setMailFrom("cheifsurveyorgeneral@gmail.com");
        mailDTO.setModel(model);
        InternetAddress cc = new InternetAddress();
        sendEmail(mailDTO, cc);
    }


    private void sendMailToNotification1(MailDTO mailDTO, Notifications notifications) throws Exception {
        Map<String, Object> model = new HashMap<String, Object>();
        model.put("firstName", "User");
        model.put("body1", notifications.getBody());
        model.put("body2", "");
        model.put("body3", "");
        model.put("body4", "");
        mailDTO.setMailSubject(notifications.getSubject());
        model.put("FOOTER", "CIS ADMIN");
        mailDTO.setMailFrom("cheifsurveyorgeneral@gmail.com");
        mailDTO.setMailTo("dataworldproject@gmail.com");
        mailDTO.setModel(model);
        InternetAddress cc = new InternetAddress();
        sendEmail1(mailDTO,cc);
    }



    private void sendMailToNotification2(MailDTO mailDTO, Notifications notifications) throws Exception {
        Map<String, Object> model = new HashMap<String, Object>();
        model.put("firstName", "User");
        model.put("body1", notifications.getBody());
        model.put("body2", "");
        model.put("body3", "");
        model.put("body4", "");
        mailDTO.setMailSubject(notifications.getSubject());
        model.put("FOOTER", "CIS ADMIN");
        mailDTO.setMailFrom("cheifsurveyorgeneral@gmail.com");
        mailDTO.setMailTo("dataworldproject@gmail.com");
        mailDTO.setModel(model);
        InternetAddress cc = new InternetAddress();
        sendEmail2(mailDTO,cc);
    }


    @PostMapping("/uploadNotificationDocument")
    public ResponseEntity<?> uploadNotificationDocument(HttpServletRequest request, @RequestParam MultipartFile[] multipleFiles,
                                                        @RequestParam("notificationId") Long notificationId
    ) {
        String message = "";
        String json = null;
        Gson gson = new Gson();
        UserControllerResponse userControllerResponse = new UserControllerResponse();
        try {
            Notifications notifications = this.notificationService.getNotificationByID(notificationId);
            if (notifications != null && !isEmpty(notifications)) {
                if (notifications.getNotificationDocs() != null) {
                    List<String> filesExist = new ArrayList<>();
                    String pathFromDB = notifications.getNotificationDocs();
                    FilePathsDTO filePath = gson.fromJson(pathFromDB, FilePathsDTO.class);
                    System.out.println("filePath for notification docs is " + filePath.getFiles().toString());
                    for (String str1 : filePath.getFiles()) {
                        filesExist.add(str1);
                    }
                    for (MultipartFile f : multipleFiles) {
                        List<String> files = new ArrayList<String>();
                        String fileName = testService.store(f);
                        files.add(f.getOriginalFilename());
                        filesExist.add(applicationPropertiesConfiguration.getUploadDirectoryPath() + fileName);
                        userControllerResponse.setFiles(filesExist);
                        json = gson.toJson(userControllerResponse);
                        notifications.setNotificationDocs(json);
                        this.notificationService.saveNotification(notifications);
                    }
                } else {
                    List<String> filesExist = new ArrayList<>();
                    for (MultipartFile f : multipleFiles) {
                        List<String> files = new ArrayList<String>();
                        String fileName = testService.store(f);
                        files.add(f.getOriginalFilename());
                        filesExist.add(applicationPropertiesConfiguration.getUploadDirectoryPath() + fileName);
                        userControllerResponse.setFiles(filesExist);
                        json = gson.toJson(userControllerResponse);
                        notifications.setNotificationDocs(json);
                        this.notificationService.saveNotification(notifications);
                    }
                }

            }

        } catch (Exception exception) {
            return generateFailureResponse(request, exception);
        }
        return ResponseEntity.status(HttpStatus.OK).body(json);
    }//uploadNotificationDocument


    @RequestMapping(value = "/deleteNotificationDocument", method = RequestMethod.POST)
    public ResponseEntity<?> deleteNotificationDocument(HttpServletRequest request,
                                                        @RequestBody @Valid Notifications notificationBody,
                                                        @RequestParam String documentPath) throws IOException {
        try {
            String json = null;
            Gson gson = new Gson();
            UserControllerResponse userControllerResponse = new UserControllerResponse();
            Notifications notifications = this.notificationService.getNotificationByID(notificationBody.getNotificationId());
            if (isEmpty(notifications)) {
                return generateEmptyResponse(request, "Requests are  not found");
            }
            if (!isEmpty(notifications)) {
                String pathFromDB = notifications.getNotificationDocs();

                FilePathsDTO filePath = gson.fromJson(pathFromDB, FilePathsDTO.class);
                List<String> fileDeleted = new ArrayList<>();
                for (String str1 : filePath.getFiles()) {
                    fileDeleted.add(str1);
                }
                System.out.println("size is " + fileDeleted.size());
                if (fileDeleted.contains(documentPath)) {
                    Path fileToDeletePath = Paths.get(documentPath);
                    Files.delete(fileToDeletePath);
                    fileDeleted.remove(documentPath);
                    if (fileDeleted.size() == 0) {
                        System.out.println("Size of file is " + fileDeleted);
                        notifications.setNotificationDocs("");
                        this.notificationService.saveNotification(notifications);
                        return ResponseEntity.status(HttpStatus.OK).body("Notification Document Deleted Successfully");
                    }

                }
                userControllerResponse.setFiles(fileDeleted);
                json = gson.toJson(userControllerResponse);
                notifications.setNotificationDocs(json);
                this.notificationService.saveNotification(notifications);
                System.out.println("size is after " + fileDeleted.size());
                return ResponseEntity.status(HttpStatus.OK).body("Notification Document Deleted Successfully");
            }
            return generateEmptyResponse(request, "Notification Document are  not found");
        } catch (Exception exception) {
            return generateFailureResponse(request, exception);
        }
    }//deleteNotificationDocument


    @RequestMapping(value = "/getNotificationDocsList", method = RequestMethod.GET)
    public ResponseEntity<?> getNotificationDocsList(HttpServletRequest request,
                                                     @RequestParam Long notificationId) throws IOException {
        try {
            String json = null;
            List<String> test = new ArrayList<>();
            Notifications notifications = this.notificationService.getNotificationByID(notificationId);
            if (!isEmpty(notifications)) {
                String pathFromDB = notifications.getNotificationDocs();
                Gson gson = new Gson();
                FilePathsDTO filePath = gson.fromJson(pathFromDB, FilePathsDTO.class);
                System.out.println("filePath is " + filePath.getFiles().toString());
                List<String> files = new ArrayList<String>();
                for (String str1 : filePath.getFiles()) {
                    files.add(str1);
                    json = gson.toJson(files);

                }
                return ResponseEntity.status(HttpStatus.OK).body(json);
            } else {
                return ResponseEntity.status(HttpStatus.OK).body(test);
            }

        } catch (Exception exception) {
            return generateFailureResponse(request, exception);
        }
    }//getDispatchDocsList


    @RequestMapping(value = "/downloadNotificationDocuments", method = RequestMethod.POST)
    public ResponseEntity<InputStreamResource> downloadNotificationDocuments(HttpServletRequest request,
                                                                             @RequestBody @Valid Notifications notificationBody) throws IOException {
        Notifications notifications = this.notificationService.getNotificationByID(notificationBody.getNotificationId());
        System.out.println("notifications documents are one " + notifications.getNotificationDocs());
        String pathFromDB = notifications.getNotificationDocs();

        Gson gson = new Gson();
        FilePathsDTO filePath = gson.fromJson(pathFromDB, FilePathsDTO.class);
        System.out.println("filePath is " + filePath.getFiles().toString());
        List<String> files = new ArrayList<String>();
        for (String str1 : filePath.getFiles()) {
            System.out.println(str1);
            files.add(str1);
            zipFiles(files);
        }
        File file = new File(applicationPropertiesConfiguration.getDownloadDirectoryPath() + "NotificationDocumentsDownloadFiles.zip");

        InputStreamResource resource = new InputStreamResource(new FileInputStream(file));
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment;filename= NotificationDocumentsDownloadFiles.zip")
                .contentType(MediaType.parseMediaType("application/octet-stream"))
                .contentLength(file.length()) //
                .body(resource);
    }//downloadNotificationDocuments


    public void zipFiles(List<String> files) {

        FileOutputStream fos = null;
        ZipOutputStream zipOut = null;
        FileInputStream fis = null;
        try {
            fos = new FileOutputStream(applicationPropertiesConfiguration.getDownloadDirectoryPath() + "NotificationDocumentsDownloadFiles.zip");
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
    }//zipFiles


}

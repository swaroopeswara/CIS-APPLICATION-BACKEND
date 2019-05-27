package com.dw.ngms.cis.uam.controller;

import com.dw.ngms.cis.controller.MessageController;
import com.dw.ngms.cis.uam.dto.MailDTO;
import com.dw.ngms.cis.uam.entity.Notifications;
import com.dw.ngms.cis.uam.entity.User;
import com.dw.ngms.cis.uam.service.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import javax.mail.internet.InternetAddress;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by swaroop on 2019/04/12.
 */

@RestController
@RequestMapping("/cisorigin.uam/api/v1")
@CrossOrigin(origins = "*")
public class NotificationController extends MessageController {

    @Autowired
    private NotificationService notificationService;


    @PostMapping("/saveNotification")
    public ResponseEntity<?> saveNotification(HttpServletRequest request, @RequestBody @Valid Notifications notification) {
        try {
            //Long notificationId = this.notificationService.getNotificationId();
            //System.out.println("notificationId is "+notificationId);
            //costCategories.setCategoryCode("COST" + Long.toString(categoryId));
            Notifications notificationSave = this.notificationService.saveNotification(notification);
            MailDTO mailDTO = new MailDTO();
                sendMailToNotification(mailDTO,notification);
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
        model.put("FOOTER", "CIS ADMIN");
        mailDTO.setMailFrom("cheifsurveyorgeneral@gmail.com");
        mailDTO.setMailTo("sibusiso.dlamini@drdlr.gov.za");
        mailDTO.setModel(model);
        InternetAddress cc = new InternetAddress();
        sendEmail(mailDTO,cc);
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
        mailDTO.setMailTo("sibusiso.dlamini@drdlr.gov.za");
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
        mailDTO.setMailTo("sibusiso.dlamini@drdlr.gov.za");
        mailDTO.setModel(model);
        InternetAddress cc = new InternetAddress();
        sendEmail2(mailDTO,cc);
    }

}

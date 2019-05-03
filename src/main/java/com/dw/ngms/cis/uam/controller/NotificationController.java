package com.dw.ngms.cis.uam.controller;

import com.dw.ngms.cis.uam.entity.Notifications;
import com.dw.ngms.cis.uam.service.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.List;

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


}

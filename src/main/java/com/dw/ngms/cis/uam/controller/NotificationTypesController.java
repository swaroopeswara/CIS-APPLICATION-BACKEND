package com.dw.ngms.cis.uam.controller;

import com.dw.ngms.cis.controller.MessageController;
import com.dw.ngms.cis.uam.entity.NotificationTypes;
import com.dw.ngms.cis.uam.entity.Notifications;
import com.dw.ngms.cis.uam.service.NotificationService;
import com.dw.ngms.cis.uam.service.NotificationTypesService;
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
public class NotificationTypesController extends MessageController {

    @Autowired
    private NotificationTypesService notificationTypesService;


    @GetMapping("/getNotificationUserTypes")
    public ResponseEntity<?> getNotificationUserTypes(HttpServletRequest request) {
        try {
            List<NotificationTypes> notificationTypesList = notificationTypesService.getNotificationUserTypes();
            return (CollectionUtils.isEmpty(notificationTypesList)) ? generateEmptyResponse(request, "NotificationType(s) not found")
                    : ResponseEntity.status(HttpStatus.OK).body(notificationTypesList);
        } catch (Exception exception) {
            return generateFailureResponse(request, exception);
        }
    }//getNotificationUserTypes


}

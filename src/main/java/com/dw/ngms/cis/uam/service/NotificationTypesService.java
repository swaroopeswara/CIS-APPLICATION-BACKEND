package com.dw.ngms.cis.uam.service;

import com.dw.ngms.cis.uam.entity.NotificationTypes;
import com.dw.ngms.cis.uam.entity.Notifications;
import com.dw.ngms.cis.uam.repository.NotificationRepository;
import com.dw.ngms.cis.uam.repository.NotificationTypesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;


/**
 * Created by swaroop on 2019/04/12.
 */
@Service
public class NotificationTypesService {

    @Autowired
    private NotificationTypesRepository notificationTypesRepository;




    public List<NotificationTypes> getNotificationUserTypes() {
        return this.notificationTypesRepository.findAll();
    }//getNotificationUserTypes


}

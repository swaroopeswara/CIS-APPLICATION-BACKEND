package com.dw.ngms.cis.uam.service;

import com.dw.ngms.cis.uam.entity.NotificationSubTypes;
import com.dw.ngms.cis.uam.entity.NotificationTypes;
import com.dw.ngms.cis.uam.repository.NotificationSubTypesRepository;
import com.dw.ngms.cis.uam.repository.NotificationTypesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;


/**
 * Created by swaroop on 2019/04/12.
 */
@Service
public class NotificationSubTypesService {

    @Autowired
    private NotificationSubTypesRepository notificationSubTypesRepository;




    public List<NotificationSubTypes> getNotificationSubTypes() {
        return this.notificationSubTypesRepository.findAll();
    }//getNotificationSubTypes


}

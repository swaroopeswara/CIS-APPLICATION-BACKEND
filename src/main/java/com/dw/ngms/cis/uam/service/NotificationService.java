package com.dw.ngms.cis.uam.service;

import com.dw.ngms.cis.im.entity.Requests;
import com.dw.ngms.cis.uam.entity.Notifications;
import com.dw.ngms.cis.uam.repository.NotificationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;


/**
 * Created by swaroop on 2019/04/12.
 */
@Service
public class NotificationService {

    @Autowired
    private NotificationRepository notificationRepository;


    public Notifications saveNotification(Notifications notification) {
        return this.notificationRepository.save(notification);
    } //saveNotification


    public List<Notifications> getAllNotifications() {
        return this.notificationRepository.findAll(sortByIdDSC());
    }//getAllNotifications



    public Notifications getNotificationByID(Long notificationId) {
        return this.notificationRepository.getNotificationByID(notificationId);
    } //saveRequest


    private Sort sortByIdDSC() {
        return new Sort(Sort.Direction.DESC, "createdDate");
    }

}

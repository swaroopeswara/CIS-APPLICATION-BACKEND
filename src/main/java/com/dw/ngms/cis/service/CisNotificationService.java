package com.dw.ngms.cis.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dw.ngms.cis.entity.CisNotification;
import com.dw.ngms.cis.repository.CisNotificationRepository;


@Service
public class CisNotificationService {

    @Autowired
    private CisNotificationRepository notificationRepository;

    public void addNotification(CisNotification notification) {
    	if(notification != null)
    		notificationRepository.save(notification);
    }
    
    public void deleteNotification(CisNotification notification) {
    	if(notification != null)
    		notificationRepository.delete(notification);
    }
}

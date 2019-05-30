package com.dw.ngms.cis.uam.repository;

import com.dw.ngms.cis.im.entity.Requests;
import com.dw.ngms.cis.uam.entity.Notifications;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Created by swaroop on 2019/04/12.
 */
@Repository
public interface NotificationRepository extends JpaRepository<Notifications, Long> {



   /* @Query(value = "SELECT COSTCATEGORY_SEQ.nextval FROM dual", nativeQuery =
            true)
    Long getNotificationId();*/


    @Query("SELECT u FROM Notifications u WHERE u.notificationId = :notificationId")
    Notifications getNotificationByID(@Param("notificationId") long notificationId);



}

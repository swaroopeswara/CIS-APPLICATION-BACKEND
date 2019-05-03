package com.dw.ngms.cis.uam.repository;

import com.dw.ngms.cis.uam.entity.NotificationTypes;
import com.dw.ngms.cis.uam.entity.Notifications;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Created by swaroop on 2019/04/12.
 */
@Repository
public interface NotificationTypesRepository extends JpaRepository<NotificationTypes, Long> {



   /* @Query(value = "SELECT COSTCATEGORY_SEQ.nextval FROM dual", nativeQuery =
            true)
    Long getNotificationId();*/


}

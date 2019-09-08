package com.dw.ngms.cis.uam.repository;

import com.dw.ngms.cis.uam.entity.NotificationSubTypes;
import com.dw.ngms.cis.uam.entity.NotificationTypes;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Created by swaroop on 2019/04/12.
 */
@Repository
public interface NotificationSubTypesRepository extends JpaRepository<NotificationSubTypes, Long> {



   /* @Query(value = "SELECT COSTCATEGORY_SEQ.nextval FROM dual", nativeQuery =
            true)
    Long getNotificationId();*/


}

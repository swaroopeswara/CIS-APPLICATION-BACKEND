package com.dw.ngms.cis.im.repository;

import com.dw.ngms.cis.im.entity.DeliveryMethods;
import com.dw.ngms.cis.im.entity.Requests;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

/**
 * Created by swaroop on 2019/04/19.
 */
@Repository
public interface DeliveryMethodRepository extends JpaRepository<DeliveryMethods, UUID>  {


    @Query(value = "SELECT DELIVERYMETHOD_SEQ.nextval FROM dual", nativeQuery =
            true)
    Long getDeleviryMethodId();


    @Query("SELECT u FROM DeliveryMethods u WHERE u.deliveryMethodCode =:deliveryMethodCode")
    DeliveryMethods getDeliveryMethodByCode(@Param("deliveryMethodCode") String deliveryMethodCode);
}

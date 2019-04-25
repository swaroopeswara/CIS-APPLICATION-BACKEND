package com.dw.ngms.cis.im.repository;

import com.dw.ngms.cis.im.entity.RequestItems;
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
public interface RequestItemRepository extends JpaRepository<RequestItems, UUID>  {

    @Query(value = "SELECT REQUESTITEMS_SEQ.nextval FROM dual", nativeQuery =
            true)
    Long getRequestItemId();


    @Query("SELECT u FROM RequestItems u WHERE u.requestCode = :requestCode and u.requestItemCode =:requestItemCode ")
    RequestItems getRequestsByRequestCodeAndItemCode(@Param("requestCode") String requestCode,
                                                     @Param("requestItemCode") String requestItemCode);


    @Query("SELECT u FROM RequestItems u WHERE u.requestCode = :requestCode ")
    List<RequestItems>  getRequestsByRequestCode(@Param("requestCode") String requestCode);





}

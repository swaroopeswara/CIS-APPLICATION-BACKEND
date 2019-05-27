package com.dw.ngms.cis.im.repository;

import com.dw.ngms.cis.im.entity.RequestTypes;
import com.dw.ngms.cis.im.entity.Requests;
import com.dw.ngms.cis.uam.entity.ExternalUser;
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
public interface RequestRepository extends JpaRepository<Requests, UUID>  {


    @Query("SELECT u FROM Requests u WHERE u.userCode = :userCode and u.provinceCode =:provinceCode")
    List<Requests> getRequestByUserCodeProvinceCode(@Param("userCode") String userCode, @Param("provinceCode") String provinceCode);

    @Query("SELECT u FROM Requests u WHERE u.userCode = :userCode")
    List<Requests> getRequestByUserCode(@Param("userCode") String userCode);


    @Query("SELECT u FROM Requests u WHERE u.provinceCode =:provinceCode")
    List<Requests> getRequestsPaidInfoByProvince(@Param("provinceCode") String provinceCode);


    @Query("SELECT u FROM Requests u where createddate between sysdate-7 and sysdate and u.provinceCode =:provinceCode")
    List<Requests> getRequestsPaidInfoByProvinceWeek(@Param("provinceCode") String provinceCode);

    @Query("SELECT u FROM Requests u where createddate between sysdate-31 and sysdate and u.provinceCode =:provinceCode")
    List<Requests> getRequestsPaidInfoByProvinceMonth(@Param("provinceCode") String provinceCod);

    @Query("SELECT u FROM Requests u where createddate between sysdate-7 and sysdate")
    List<Requests> getAllRequestsPaidInfoByProvinceWeek();

    @Query("SELECT u FROM Requests u where createddate between sysdate-31 and sysdate")
    List<Requests> getAllRequestsPaidInfoByProvinceMonth();





    @Query(value = "SELECT REQUESTS_SEQ.nextval FROM dual", nativeQuery =
            true)
    Long getRequestId();


    @Query("SELECT u FROM Requests u WHERE u.requestCode = :requestCode")
    Requests getRequestsByRequestCode(@Param("requestCode") String requestCode);




}

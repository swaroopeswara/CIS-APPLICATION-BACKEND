package com.dw.ngms.cis.im.repository;

import com.dw.ngms.cis.im.entity.FormatTypes;
import com.dw.ngms.cis.im.entity.Requests;
import com.dw.ngms.cis.im.entity.UamDesignations;
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
public interface UamDesignationRepository extends JpaRepository<UamDesignations, UUID>  {


    @Query(value = "SELECT UAMDESIGNATIONS_SEQ.nextval FROM dual", nativeQuery =
            true)
    Long getUamDesignation();


    @Query("SELECT u FROM UamDesignations u WHERE u.designationCode = :designationCode")
     UamDesignations findByUamDesignationCode(@Param("designationCode") String designationCode);



}

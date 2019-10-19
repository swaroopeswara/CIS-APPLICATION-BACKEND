package com.dw.ngms.cis.im.repository;

import com.dw.ngms.cis.im.entity.DeliveryMethods;
import com.dw.ngms.cis.im.entity.FormatTypes;
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
public interface FormatTypeRepository extends JpaRepository<FormatTypes, UUID>  {


    @Query(value = "SELECT FORMATTYPES_SEQ.nextval FROM dual", nativeQuery =
            true)
    Long getFormatType();


    @Query("SELECT u FROM FormatTypes u WHERE u.formatTypeCode =:formatTypeCode")
    FormatTypes getFormatByCode(@Param("formatTypeCode") String formatTypeCode);

    @Query("SELECT u FROM FormatTypes u WHERE ISDELETED = 'N'")
    List<FormatTypes> getFormatTypeActive();


}

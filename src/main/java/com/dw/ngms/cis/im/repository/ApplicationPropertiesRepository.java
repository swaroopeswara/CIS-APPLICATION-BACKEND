package com.dw.ngms.cis.im.repository;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.dw.ngms.cis.im.entity.ApplicationProperties;

/**
 * Created by swaroop on 2019/04/16.
 */
@Repository
public interface ApplicationPropertiesRepository extends JpaRepository<ApplicationProperties,UUID> {


	@Query("SELECT u FROM ApplicationProperties u WHERE u.keyName = :keyName")
    ApplicationProperties getProperty(@Param("keyName") String keyName);
	
    @Query("SELECT u FROM ApplicationProperties u WHERE u.keyName = :keyName")
    List<ApplicationProperties> getPropertyValueByName(@Param("keyName") String keyName);

}

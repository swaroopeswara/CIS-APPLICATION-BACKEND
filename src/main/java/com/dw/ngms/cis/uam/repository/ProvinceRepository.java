package com.dw.ngms.cis.uam.repository;

import com.dw.ngms.cis.uam.entity.PlsUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.dw.ngms.cis.uam.entity.Province;

@Repository
public interface ProvinceRepository extends JpaRepository<Province, Long> {

    @Query("SELECT shortName FROM Province u WHERE u.code = ?1")
    String getProvinceShortName(String code);

}

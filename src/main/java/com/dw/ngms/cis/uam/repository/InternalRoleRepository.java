package com.dw.ngms.cis.uam.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.dw.ngms.cis.uam.entity.InternalRole;

/**
 * Created by swaroop on 2019/04/03.
 */

@Repository
public interface InternalRoleRepository extends JpaRepository<InternalRole, Long> {

    @Query("SELECT u FROM InternalRole u WHERE u.roleCode = :roleCode")
    List<InternalRole> updateAccessRight(
             @Param("roleCode") String roleCode);

    @Query("SELECT u FROM InternalRole u WHERE u.roleCode = :roleCode")
    List<InternalRole> findByRoleCode(@Param("roleCode") String roleCode);
    
    @Query("SELECT u FROM InternalRole  u WHERE u.roleName = ?1 ")
    List<InternalRole> findByRoleName(String roleName);
    
    @Query("SELECT u FROM InternalRole  u WHERE u.provinceCode = ?1 and u.roleName = ?2 ")
    List<InternalRole> findByProvinceCodeAndRoleName(String provinceCode, String roleName);
    
    @Query("SELECT u FROM InternalRole  u WHERE u.provinceCode = ?1 and u.sectionCode = ?2 and u.roleName = ?3 ")
    List<InternalRole> findByProvinceCodeAndSectionCodeAndRoleName(String provinceCode, String sectionCode, String roleName);
    
    @Query("SELECT u FROM InternalRole u WHERE u.roleCode = :roleCode")
    List<InternalRole>  updateDashBoardAccessRight(
           @Param("roleCode") String roleCode);

    @Query(value = "DELETE  FROM INTERNALROLES  u WHERE u.INTERNALROLECODE = ?1 ", nativeQuery = true)
    void deleteByInternalRoleCode(
            @Param("internalRoleCode") String internalRoleCode);

    @Query("SELECT u FROM InternalRole u WHERE u.internalRoleCode = :internalRoleCode")
    InternalRole findByInternalRoleCode(
            @Param("internalRoleCode") String internalRoleCode);

    @Query("SELECT accessRightJson FROM InternalRole u WHERE u.internalRoleCode = :internalRoleCode")
    String getAccessRightJson ( @Param("internalRoleCode") String internalRoleCode);

    @Query("SELECT DISTINCT (dashBoardRightJson) FROM InternalRole u WHERE u.roleCode = :roleCode")
    String getdashBoardRightJson ( @Param("roleCode") String roleCode);

    @Query("SELECT u FROM InternalRole  u WHERE u.provinceCode = :provinceCode")
    List<InternalRole> getSectionsByProvinceCode (@Param("provinceCode") String provinceCode);

    @Query("SELECT u FROM InternalRole  u WHERE  u.sectionCode = :sectionCode and u.provinceCode = :provinceCode")
    List<InternalRole> getRolesBySectionsAndProvince (@Param("sectionCode") String sectionCode,@Param("provinceCode") String provinceCode);

    @Query("SELECT u FROM InternalRole  u WHERE  u.sectionCode IS NULL and u.provinceCode = :provinceCode")
    List<InternalRole> getRolesBySectionsAndProvinceBySectionCodeNull (@Param("provinceCode") String provinceCode);

    @Query("SELECT u FROM InternalRole  u WHERE  u.sectionCode = :sectionCode and u.provinceCode IS NULL")
    List<InternalRole> getRolesBySectionsAndProvinceByProvinceCodeNull (@Param("sectionCode") String sectionCode);

    @Query(value = "SELECT U.*  FROM INTERNALROLES  u WHERE u.sectionCode is null and  u.provinceCode is null ",
            nativeQuery = true)
    List<InternalRole> getNationalRoles ();

}

package com.dw.ngms.cis.uam.repository;

import com.dw.ngms.cis.uam.entity.InternalUserRoles;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.dw.ngms.cis.uam.entity.ExternalUserRoles;

import java.util.List;

@Repository
public interface ExternalUserRolesRepository extends JpaRepository<ExternalUserRoles, Long> {



    @Query("select u from ExternalUserRoles u where u.userRoleCode = :userRoleCode")
    List<ExternalUserRoles> getExternalUsersForDelete(@Param("userRoleCode") String userRoleCode);



}

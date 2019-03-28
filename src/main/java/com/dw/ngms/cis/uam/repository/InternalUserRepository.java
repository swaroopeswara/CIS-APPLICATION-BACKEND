package com.dw.ngms.cis.uam.repository;

import com.dw.ngms.cis.uam.entity.InternalRole;
import com.dw.ngms.cis.uam.entity.InternalUserRoles;
import com.dw.ngms.cis.uam.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface InternalUserRepository extends JpaRepository<InternalUserRoles, Long> {




    @Query("SELECT u FROM InternalRole u WHERE u.provinceCode = :provinceCode and u.sectionCode = :sectionCode and u.roleCode = :roleCode")
    InternalRole getInternalRoleCode(@Param("provinceCode") String provinceCode, @Param("sectionCode") String sectionCode, @Param("roleCode") String roleCode);
}

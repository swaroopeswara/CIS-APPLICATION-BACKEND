package com.dw.ngms.cis.uam.repository;

import com.dw.ngms.cis.uam.entity.InternalRole;
import com.dw.ngms.cis.uam.entity.InternalUserRoles;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Created by swaroop on 2019/04/03.
 */

@Repository
public interface InternalRoleRepository extends JpaRepository<InternalRole, Long> {



    @Query("SELECT u FROM InternalRole u WHERE u.roleCode = :roleCode and u.provinceCode = :provinceCode and u.sectionCode = :sectionCode")
    InternalRole updateAccessRight(
            @Param("provinceCode") String provinceCode, @Param("roleCode") String roleCode, @Param("sectionCode") String sectionCode);
}

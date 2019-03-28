package com.dw.ngms.cis.uam.repository;

import com.dw.ngms.cis.uam.entity.InternalUserRoles;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Created by swaroop on 2019/03/28.
 */
public interface InternalUserRoleRepository  extends JpaRepository<InternalUserRoles, Long> {
}

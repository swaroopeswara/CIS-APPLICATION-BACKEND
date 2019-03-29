package com.dw.ngms.cis.uam.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.dw.ngms.cis.uam.entity.InternalUserRoles;

/**
 * Created by swaroop on 2019/03/28.
 */
public interface InternalUserRoleRepository  extends JpaRepository<InternalUserRoles, Long> {
	
	@Query("select iur from InternalUserRoles iur where iur.userName = ?1")
	InternalUserRoles findByEmail(String username);
	
	@Query("select iur from InternalUserRoles iur where iur.userCode = ?1 and iur.userName = ?2 and iur.internalRoleCode = ?3")
	InternalUserRoles findByUserCodeUserNameAndInternalRoleCode(String usercode, String username, String internalrolecode);

}

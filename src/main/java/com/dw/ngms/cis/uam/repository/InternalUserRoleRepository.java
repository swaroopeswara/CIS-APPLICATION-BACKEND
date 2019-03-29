package com.dw.ngms.cis.uam.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.dw.ngms.cis.uam.entity.InternalUserRoles;

/**
 * Created by swaroop on 2019/03/28.
 */
public interface InternalUserRoleRepository  extends JpaRepository<InternalUserRoles, Long> {
	
	@Query("SELECT IUR FROM INTERNALUSERROLES IUR WHERE IUR.userName = :username")
	InternalUserRoles findByEmail(@Param("username") String username);
	
	@Query("SELECT IUR FROM INTERNALUSERROLES IUR WHERE IUR.userCode = :usercode and IUR.userName = :username and IUR.userTypeName = :internalrolecode")
	InternalUserRoles findByUserCodeUserNameAndInternalRoleCode(@Param("usercode") String usercode,@Param("username") String username, @Param("internalrolecode") String internalrolecode);

}

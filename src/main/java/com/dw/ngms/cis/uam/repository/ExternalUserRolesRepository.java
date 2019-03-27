package com.dw.ngms.cis.uam.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.dw.ngms.cis.uam.entity.ExternalUserRoles;

@Repository
public interface ExternalUserRolesRepository extends JpaRepository<ExternalUserRoles, Long> {

}

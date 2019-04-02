package com.dw.ngms.cis.uam.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.dw.ngms.cis.uam.entity.PlsUser;

@Repository
public interface PlsUserRepository extends JpaRepository<PlsUser, String> {

	@Query("SELECT u FROM PlsUser u WHERE u.plscode = ?1")
	PlsUser findByPlsCode(String plsCode);
	
	@Query("SELECT u FROM PlsUser u WHERE u.email = ?1")
	PlsUser findByEmail(String email);

}

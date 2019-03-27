package com.dw.ngms.cis.uam.repository;

import com.dw.ngms.cis.uam.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

//@Repository
public interface InternalUserRepository extends JpaRepository<User, Long> {

}

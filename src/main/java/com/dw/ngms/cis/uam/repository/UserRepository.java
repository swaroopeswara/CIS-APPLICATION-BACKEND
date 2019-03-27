package com.dw.ngms.cis.uam.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.dw.ngms.cis.uam.entity.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

   @Query("SELECT u FROM User u WHERE u.userName = :userName")
   User findByLoginName(@Param("userName") String userName);

   @Query("SELECT u FROM User u WHERE u.email = :email")
   User findByEmail(@Param("email") String email);

   @Query("SELECT u FROM User u WHERE u.userTypeName = :userTypeName")
   List<User> findByUserTypeName(@Param("userTypeName") String userTypeName);
   
   @Query("SELECT u FROM User u WHERE u.userTypeName = :userTypeName and u.userTypeCode = :provincecode")
   List<User> findByUserTypeNameAndProvinceCode(@Param("userTypeName") String userTypeName, @Param("provincecode") String provincecode);


   @Query("SELECT u FROM User u WHERE u.usercode = :usercode and u.username = :username")
   User findByUserByNameAndCode(@Param("usercode") String usercode,@Param("username") String username);



}

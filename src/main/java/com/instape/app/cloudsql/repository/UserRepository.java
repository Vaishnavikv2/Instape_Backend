package com.instape.app.cloudsql.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.instape.app.cloudsql.model.Users;

public interface UserRepository extends JpaRepository<Users, Long> {

	@Query("SELECT u FROM Users u where u.isDeleted<>true and  u.email = :email")
	Users getByEmail(@Param("email") String email);

	@Query("SELECT u FROM Users u where u.isDeleted<>true and u.id=:id")
	Users getUsersById(@Param("id") long id);

	@Query("SELECT u FROM Users u where u.isDeleted<>true order by u.fullName asc")
	Page<Users> getAllUsers(PageRequest pageRequest);

	@Query("SELECT CASE WHEN COUNT(u) > 0 THEN TRUE ELSE FALSE END FROM Users u WHERE u.email = :email AND u.otp = :otp and u.isDeleted<>true")
	boolean existsByEmailAndOtp(@Param("email") String email, @Param("otp") String otp);

	@Query("SELECT CASE WHEN COUNT(u) > 0 THEN TRUE ELSE FALSE END FROM Users u WHERE u.email = :email "
			+ "AND u.isDeleted<>true")
	boolean existsByEmail(@Param("email") String email);

	@Query("SELECT u.fullName FROM Users u where u.isDeleted<>true and u.id=:id")
	String getUsersNameById(@Param("id") long id);

	@Query("SELECT u FROM Users u JOIN u.userRoles r where u.isDeleted<>true and (u.fullName ilike :searchText OR u.email ilike :searchText OR u.designation ilike :searchText OR u.mobile ilike :searchText OR u.status ilike :searchText OR r.roles.roleName ilike :searchText) order by u.fullName asc")
	Page<Users> getAllUsers(String searchText, PageRequest pageRequest);

}

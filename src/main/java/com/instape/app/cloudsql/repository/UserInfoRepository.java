package com.instape.app.cloudsql.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.instape.app.cloudsql.model.UserInfo;

/**
 * 
 * 
 * @Author - Nagaraj
 * @CreationDate - 10-Jan-2024
 * @ModifyDate - 10-Jan-2024
 * @Desc -
 */
public interface UserInfoRepository extends JpaRepository<UserInfo, Long> {

	@Query(value = "SELECT u from UserInfo u where u.id =:userId and u.isDeleted<>true")
	public UserInfo getUserInfoByUserId(@Param("userId") Long userId);

	@Query(value = "SELECT u from UserInfo u where (u.userName ilike :userName OR u.customerMaster.customerName ilike :customerName) AND u.status = :status order by u.createdDate desc")
	public Page<UserInfo> getUserDetailsUsingFilters(String userName, String customerName, PageRequest pageRequest,
			String status);

	@Query(value = "SELECT u from UserInfo u where u.status = :status order by u.createdDate desc")
	public Page<UserInfo> getAllUserDetailsByStatus(String status, PageRequest pageRequest);

	@Query("SELECT u FROM UserInfo u where u.isDeleted<>true and u.id=:id and u.customerMaster.id=:custId")
	UserInfo getUsersById(@Param("custId") long custId, @Param("id") long id);

	@Query("SELECT u FROM UserInfo u where u.isDeleted<>true and u.customerMaster.id=:custId order by u.userName asc")
	List<UserInfo> getAllUsers(@Param("custId") long custId);

	@Query("SELECT CASE WHEN COUNT(u) > 0 THEN TRUE ELSE FALSE END FROM UserInfo u WHERE u.email = :email "
			+ "AND u.isDeleted<>true")
	boolean existsByEmail(@Param("email") String email);
}

package com.instape.app.cloudsql.repository;

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
public interface UserInformationRepository extends JpaRepository<UserInfo, Long> {

	@Query(value = "SELECT u from UserInfo u where u.id =:userId")
	public UserInfo findUserInfoByUserId(@Param("userId") Long userId);

	@Query(value = "SELECT u from UserInfo u where (u.userName ilike :userName OR u.customerMaster.customerName ilike :customerName) AND u.status = :status order by u.createdDate desc")
	public Page<UserInfo> getUserDetailsUsingFilters(String userName, String customerName, PageRequest pageRequest,
			String status);

	@Query(value = "SELECT u from UserInfo u where u.status = :status order by u.createdDate desc")
	public Page<UserInfo> getAllUserDetailsByStatus(String status, PageRequest pageRequest);
}

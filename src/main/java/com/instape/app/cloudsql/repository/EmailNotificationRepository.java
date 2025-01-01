package com.instape.app.cloudsql.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.instape.app.cloudsql.model.Notification;

import io.lettuce.core.dynamic.annotation.Param;

public interface EmailNotificationRepository extends JpaRepository<Notification, Long> {
	@Query("SELECT CASE WHEN COUNT(n) > 0 THEN TRUE ELSE FALSE END FROM Notification n WHERE n.type = :type AND n.isDeleted <>true")
	boolean existsByType(@Param("type") String type);

	@Query("SELECT CASE WHEN COUNT(n) > 0 THEN TRUE ELSE FALSE END FROM Notification n WHERE n.type = :type "
			+ " AND n.isDeleted <>true AND n.id <> :id")
	boolean existsByTypeForUpdate(@Param("type") String type, @Param("id") Long id);

	@Query("SELECT n FROM Notification n WHERE n.id = :id AND n.isDeleted<> true")
	Notification getNotificationById(@Param("id") Long id);

	@Query("SELECT n FROM Notification n where n.isDeleted <>true   order by n.type asc")
	Page<Notification> getAllNotification(final Pageable pageable);

	@Query("SELECT n FROM Notification n where (n.type like %:searchtext% or n.subject like %:searchtext% ) and n.isDeleted <>true   order by n.type asc")
	Page<Notification> getAllNotificationWithSearch(@Param("searchtext") String searchtext, final Pageable pageable);

}
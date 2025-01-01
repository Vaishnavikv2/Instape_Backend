package com.instape.app.cloudsql.repository;

import java.sql.Timestamp;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import com.instape.app.cloudsql.model.InsGlobalErrorLog;

/**
 * 
 * 
 * @Author - Nagaraj
 * @CreationDate - 28-Jun-2024
 * @ModifyDate - 28-Jun-2024
 * @Desc -
 */
public interface INSGlobalErrorLogsRepository extends JpaRepository<InsGlobalErrorLog, Long>,JpaSpecificationExecutor<InsGlobalErrorLog> {

	@Query(value = "SELECT e from InsGlobalErrorLog e where e.category NOT ilike :dlq AND (e.category ilike :category OR e.identifier ilike :identifier OR e.appErrorCode ilike :errorCode OR e.spanId ilike :spanId OR e.contractCode = :contractCode OR e.employeeId = :employeeId) order by e.errorTs desc")
	public Page<InsGlobalErrorLog> getGlobalErrorLogsExceptDLQ(String category, String identifier, String errorCode,
			String spanId, String contractCode, String employeeId, String dlq, PageRequest pageRequest);

	@Query(value = "SELECT e from InsGlobalErrorLog e where e.category NOT ilike :dlq AND (e.category ilike :category OR e.identifier ilike :identifier OR e.appErrorCode ilike :errorCode OR e.spanId ilike :spanId OR e.contractCode = :contractCode OR e.employeeId = :employeeId) AND (e.errorTs >=:startDate AND e.errorTs <=:endDate) order by e.errorTs desc")
	public Page<InsGlobalErrorLog> getGlobalErrorLogsExceptDLQ(String category, String identifier, String errorCode,
			String spanId, String contractCode, String employeeId, Timestamp startDate, Timestamp endDate, String dlq,
			PageRequest pageRequest);

	@Query(value = "SELECT e from InsGlobalErrorLog e where e.category NOT ilike :dlq order by e.errorTs desc")
	public Page<InsGlobalErrorLog> getGlobalErrorLogsExceptDLQ(String dlq, PageRequest pageRequest);

	@Query(value = "SELECT e from InsGlobalErrorLog e where e.category NOT ilike :dlq AND (e.errorTs >=:startDate AND e.errorTs <=:endDate) order by e.errorTs desc")
	public Page<InsGlobalErrorLog> getGlobalErrorLogsExceptDLQ(Timestamp startDate, Timestamp endDate, String dlq,
			PageRequest pageRequest);

	@Query(value = "SELECT e from InsGlobalErrorLog e where e.category NOT ilike :dlq AND e.status ilike :status AND (e.category ilike :category OR e.identifier ilike :identifier OR e.appErrorCode ilike :errorCode OR e.spanId ilike :spanId OR e.contractCode = :contractCode OR e.employeeId = :employeeId) order by e.errorTs desc")
	public Page<InsGlobalErrorLog> getGlobalErrorLogsExceptDLQ(String status, String category, String identifier,
			String errorCode, String spanId, String contractCode, String employeeId, String dlq,
			PageRequest pageRequest);

	@Query(value = "SELECT e from InsGlobalErrorLog e where e.category NOT ilike :dlq AND e.status ilike :status AND (e.category ilike :category OR e.identifier ilike :identifier OR e.appErrorCode ilike :errorCode OR e.spanId ilike :spanId OR e.contractCode = :contractCode OR e.employeeId = :employeeId) AND (e.errorTs >=:startDate AND e.errorTs <=:endDate) order by e.errorTs desc")
	public Page<InsGlobalErrorLog> getGlobalErrorLogsExceptDLQ(String status, String category, String identifier,
			String errorCode, String spanId, String contractCode, String employeeId, Timestamp startDate,
			Timestamp endDate, String dlq, PageRequest pageRequest);

	@Query(value = "SELECT e from InsGlobalErrorLog e where e.category NOT ilike :dlq AND e.status ilike :status order by e.errorTs desc")
	public Page<InsGlobalErrorLog> getGlobalErrorLogsExceptDLQ(String status, String dlq, PageRequest pageRequest);

	@Query(value = "SELECT e from InsGlobalErrorLog e where e.category NOT ilike :dlq AND (e.errorTs >=:startDate AND e.errorTs <=:endDate) AND e.status ilike :status order by e.errorTs desc")
	public Page<InsGlobalErrorLog> getGlobalErrorLogsExceptDLQ(Timestamp startDate, Timestamp endDate, String status,
			String dlq, PageRequest pageRequest);
}

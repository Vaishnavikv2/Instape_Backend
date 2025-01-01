package com.instape.app.cloudsql.repository;

import java.sql.Timestamp;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import com.instape.app.cloudsql.model.EmployeeInfo;
import com.instape.app.utils.PortalConstant;

/**
 * 
 * 
 * @Author - Nagaraj
 * @CreationDate - 23-Nov-2023
 * @ModifyDate - 23-Nov-2023
 * @Desc -
 */
public interface EmployeeInfoRepository extends JpaRepository<EmployeeInfo, Long> {

	@Query(value = "SELECT e from EmployeeInfo e where  e.status IN (:status) AND e.code = :employeeCode  AND e.customerMaster.code =:employerCode")
	List<EmployeeInfo> getEmployeeInfoByEmployeeCodeAndCustomerCode(@Param("employerCode") String employerCode,
			@Param("employeeCode") String employeeCode, @Param(PortalConstant.STATUS) List<String> status)
			throws Exception;

	@Query(value = "SELECT e from EmployeeInfo e where (e.employeeName ilike:employeeName OR e.empId ilike:employeeName) AND e.customerMaster.id =:customerId")
	List<EmployeeInfo> getEmployeeInfoByCustomerIdAndEmployeeName(@Param("customerId") Long customerId,
			@Param("employeeName") String employeeName) throws Exception;

	@Query(value = "SELECT e from EmployeeInfo e where e.contract.code =:contractCode AND e.status NOT ilike :Inactive AND (e.empId = :employeeId OR e.employeeName ilike:employeeName OR e.mobSHA3 = :employeeMobile OR e.designation ilike:designation OR e.officeCity ilike:city OR e.officeState ilike:state OR e.gender ilike:gender OR e.status ilike:status OR e.journeyStatus ilike:journeyStatus) order by e.createdDate desc")
	Page<EmployeeInfo> getEmployeeDetailsUsingFilters(@Param("contractCode") String contractCode,
			@Param("employeeId") String employeeId, @Param("employeeName") String employeeName,
			@Param("employeeMobile") String employeeMobile, @Param("designation") String designation,
			@Param("city") String city, @Param("state") String state, @Param("gender") String gender,
			@Param(PortalConstant.STATUS) String status, String journeyStatus, PageRequest pageRequest,
			@Param("Inactive") String active);

	@Query(value = "SELECT e from EmployeeInfo e where e.contract.code =:contractCode AND e.status NOT ilike :status order by e.createdDate desc")
	Page<EmployeeInfo> getEmployeeDetailsByCustomerId(@Param("contractCode") String contractCode,
			@Param(PortalConstant.STATUS) String inactive, PageRequest pageRequest);

	@Query(value = "SELECT COUNT(e) from EmployeeInfo e where e.customerMaster.id = :customerId AND e.status ilike :status")
	Long getEmployeeCountByCustomerIdAndStatus(@Param("customerId") Long custId,
			@Param(PortalConstant.STATUS) String active);

	@Query(value = "SELECT e from EmployeeInfo e where e.customerMaster.id = :customerId AND e.createdDate >= :fromDate AND e.status ilike :status order by e.createdDate desc")
	List<EmployeeInfo> getEmployeesByCustomerIdAndDateRange(@Param("customerId") Long custId,
			@Param("fromDate") Timestamp fromDate, @Param(PortalConstant.STATUS) String active);

	@Query(value = "SELECT e from EmployeeInfo e where e.code = :employeeCode  AND e.customerMaster.code =:employerCode")
	EmployeeInfo getEmployeeInfoByEmployeeCodeAndCustomerCode(@Param("employerCode") String employerCode,
			@Param("employeeCode") String employeeCode) throws Exception;

	@Query(value = "SELECT COUNT(e) from EmployeeInfo e where e.customerMaster.id = :customerId")
	Long getTotalEmployeesCountByCustomerCode(@Param("customerId") Long custId);

	@Query(value = "SELECT COUNT(e) from EmployeeInfo e where e.customerMaster.id = :customerId AND e.fcmToken IS NOT NULL")
	Long getRegisteredEmployeesCountByCustomerCode(@Param("customerId") Long custId);

	@Query(value = "SELECT e from EmployeeInfo e where e.contract.code = :contractCode  AND e.mobSHA3 =:employeeMobile AND e.status NOT ilike :status")
	EmployeeInfo getEmployeeInfoByMobileNumberAndContractCode(@Param("contractCode") String contractCode,
			@Param("employeeMobile") String employeeMobile, @Param("status") String status) throws Exception;

	EmployeeInfo findByCode(String empCode);

	@Query(value = "SELECT e from EmployeeInfo e where e.empId =:empId AND e.customerMaster.id =:custId AND e.status NOT ilike :status")
	EmployeeInfo getEmployeeByEmployeeIdAndStatus(@Param("empId") String empId, @Param("custId") Long custId,
			@Param(PortalConstant.STATUS) String status);

	@Query(value = "SELECT e from EmployeeInfo e where e.code = :employeeCode  AND e.customerMaster.code =:employerCode AND e.status NOT ilike :Inactive")
	EmployeeInfo getEmployeeInfoByEmployeeCodeAndCustomerCodeAndStatus(@Param("employerCode") String employerCode,
			@Param("employeeCode") String employeeCode, @Param("Inactive") String inactive) throws Exception;

	@Query(value = "SELECT e from EmployeeInfo e where e.code = :employeeCode  AND e.customerMaster.code =:employerCode AND (e.status ilike :active OR e.status ilike :nameConsent)")
	EmployeeInfo getEmployeeInfoByEmployeeCodeAndCustomerCodeAndStatus(@Param("employerCode") String employerCode,
			@Param("employeeCode") String employeeCode, @Param("active") String active,
			@Param("nameConsent") String nameConsent) throws Exception;

	@Query(value = "SELECT e from EmployeeInfo e where e.mobSHA3 = :employeeMobile AND e.status NOT ilike :status")
	EmployeeInfo getEmployeeByEmployeeMobileAndStatus(@Param("employeeMobile") String employeeMobile,
			@Param(PortalConstant.STATUS) String status);

	@Query(value = "SELECT e from EmployeeInfo e where e.empId = :empId")
	EmployeeInfo findByEmpId(String empId);

	@Query(value = "SELECT e from EmployeeInfo e where e.customerMaster.id = :custId AND e.empId =:empId AND e.status NOT ilike :status")
	EmployeeInfo getEmployeeDetailsByCustomerId(Long custId, String empId, String status);

	@Query(value = "SELECT e.* from employee_info e where e.cust_id =:customerId AND e.fcm_token IS NOT NULL AND ((select count(*) from lender_record lr where lr.emp_id = e.code and lr.status = 'valid') > 0) AND e.status NOT ilike :status order by e.created_date desc", nativeQuery = true)
	Page<EmployeeInfo> getEmployeeDetailsByCustomerIdAndFCMNotNullAndOnboarded(Long customerId, String status,
			PageRequest pageRequest);

	@Query(value = "SELECT e.* from employee_info e where e.cust_id =:customerId AND e.fcm_token IS NOT NULL AND ((select count(*) from lender_record lr where lr.emp_id = e.code and lr.status = 'valid') = 0) AND e.status NOT ilike :status order by e.created_date desc", nativeQuery = true)
	Page<EmployeeInfo> getEmployeeDetailsByCustomerIdAndFCMNotNullAndNotOnboarded(Long customerId, String status,
			PageRequest pageRequest);

	@Query(value = "SELECT e.* from employee_info e where e.cust_id =:customerId AND e.fcm_token IS NULL AND ((select count(*) from lender_record lr where lr.emp_id = e.code and lr.status = 'valid') > 0) AND e.status NOT ilike :status order by e.created_date desc", nativeQuery = true)
	Page<EmployeeInfo> getEmployeeDetailsByCustomerIdAndFCMNullAndOnboarded(Long customerId, String status,
			PageRequest pageRequest);

	@Query(value = "SELECT e.* from employee_info e where e.cust_id =:customerId AND e.fcm_token IS NULL AND ((select count(*) from lender_record lr where lr.emp_id = e.code and lr.status = 'valid') = 0) AND e.status NOT ilike :status order by e.created_date desc", nativeQuery = true)
	Page<EmployeeInfo> getEmployeeDetailsByCustomerIdAndFCMNullAndNotOnboarded(Long customerId, String status,
			PageRequest pageRequest);

	@Query(value = "SELECT e from EmployeeInfo e where e.contract.code =:contractCode AND e.fcmToken IS NULL AND e.status NOT ilike :status order by e.createdDate desc")
	Page<EmployeeInfo> getEmployeeDetailsByCustomerIdAndFCMNull(String contractCode, String status,
			PageRequest pageRequest);

	@Query(value = "SELECT e.* from employee_info e where e.cust_id =:customerId AND ((select count(*) from lender_record lr where lr.emp_id = e.code and lr.status = 'valid') > 0) and e.status NOT ilike :status order by e.created_date desc", nativeQuery = true)
	Page<EmployeeInfo> getEmployeeDetailsByCustomerIdAndOnboarded(Long customerId, String status,
			PageRequest pageRequest);

	@Query(value = "SELECT e.* from employee_info e where e.cust_id =:customerId AND ((select count(*) from lender_record lr where lr.emp_id = e.code and lr.status = 'valid') = 0) and e.status NOT ilike :status order by e.created_date desc", nativeQuery = true)
	Page<EmployeeInfo> getEmployeeDetailsByCustomerIdAndNotOnboarded(Long customerId, String status,
			PageRequest pageRequest);

	@Query(value = "SELECT e from EmployeeInfo e where e.contract.code =:contractCode AND e.fcmToken IS NOT NULL AND e.status NOT ilike :status order by e.createdDate desc")
	Page<EmployeeInfo> getEmployeeDetailsByCustomerIdAndFCMNotNull(String contractCode, String status,
			PageRequest pageRequest);

	@Query(value = "SELECT e.* from employee_info e where e.contract_code =:contractCode AND e.fcm_token IS NOT NULL AND ((select count(*) from lender_record lr where lr.emp_id = e.code and lr.status = 'valid') > 0) AND e.status NOT ilike :status order by e.created_date desc", nativeQuery = true)
	Page<EmployeeInfo> getEmployeeDetailsByCustomerIdAndFCMNotNullAndOnboarded(String contractCode, String status,
			PageRequest pageRequest);

	@Query(value = "SELECT e.* from employee_info e where e.contract_code =:contractCode AND e.fcm_token IS NOT NULL AND ((select count(*) from lender_record lr where lr.emp_id = e.code and lr.status = 'valid') = 0) AND e.status NOT ilike :status order by e.created_date desc", nativeQuery = true)
	Page<EmployeeInfo> getEmployeeDetailsByCustomerIdAndFCMNotNullAndNotOnboarded(String contractCode, String status,
			PageRequest pageRequest);

	@Query(value = "SELECT e.* from employee_info e where e.contract_code =:contractCode AND e.fcm_token IS NULL AND ((select count(*) from lender_record lr where lr.emp_id = e.code and lr.status = 'valid') > 0) AND e.status NOT ilike :status order by e.created_date desc", nativeQuery = true)
	Page<EmployeeInfo> getEmployeeDetailsByCustomerIdAndFCMNullAndOnboarded(String contractCode, String status,
			PageRequest pageRequest);

	@Query(value = "SELECT e.* from employee_info e where e.contract_code =:contractCode AND e.fcm_token IS NULL AND ((select count(*) from lender_record lr where lr.emp_id = e.code and lr.status = 'valid') = 0) AND e.status NOT ilike :status order by e.created_date desc", nativeQuery = true)
	Page<EmployeeInfo> getEmployeeDetailsByCustomerIdAndFCMNullAndNotOnboarded(String contractCode, String status,
			PageRequest pageRequest);

	@Query(value = "SELECT e.* from employee_info e where e.contract_code =:contractCode AND ((select count(*) from lender_record lr where lr.emp_id = e.code and lr.status = 'valid') > 0) and e.status NOT ilike :status order by e.created_date desc", nativeQuery = true)
	Page<EmployeeInfo> getEmployeeDetailsByCustomerIdAndOnboarded(String contractCode, String status,
			PageRequest pageRequest);

	@Query(value = "SELECT e.* from employee_info e where e.contract_code =:contractCode AND ((select count(*) from lender_record lr where lr.emp_id = e.code and lr.status = 'valid') = 0) and e.status NOT ilike :status order by e.created_date desc", nativeQuery = true)
	Page<EmployeeInfo> getEmployeeDetailsByCustomerIdAndNotOnboarded(String contractCode, String status,
			PageRequest pageRequest);
}

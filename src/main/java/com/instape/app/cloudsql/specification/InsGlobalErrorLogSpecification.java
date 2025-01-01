package com.instape.app.cloudsql.specification;

import java.sql.Timestamp;

import org.springframework.data.jpa.domain.Specification;

import com.instape.app.cloudsql.model.InsGlobalErrorLog;

public class InsGlobalErrorLogSpecification {

	public static Specification<InsGlobalErrorLog> hasStatus(String status) {
		 return (root, query, builder) -> 
         status != null ? builder.equal(builder.lower(root.get("status")), status.toLowerCase()) : builder.conjunction();
	}
	
	public static Specification<InsGlobalErrorLog> hasDateRange(Timestamp startDate, Timestamp endDate) {
		return (root, query, builder) -> {
			if (startDate != null && endDate != null) {
				return builder.between(root.get("errorTs"), startDate, endDate);
			} else if (startDate != null) {
				return builder.greaterThanOrEqualTo(root.get("errorTs"), startDate);
			} else if (endDate != null) {
				return builder.lessThanOrEqualTo(root.get("errorTs"), endDate);
			} else {
				return builder.conjunction();
			}
		};
	}

	public static Specification<InsGlobalErrorLog> hasContractCode(String contractCode) {
		return (root, query, builder) -> 
        contractCode != null ? builder.like(builder.lower(root.get("contractCode")), "%" + contractCode.toLowerCase() + "%") : builder.conjunction();
	}

	public static Specification<InsGlobalErrorLog> hasIdentifier(String identifier) {
		   return (root, query, builder) -> 
           identifier != null ? builder.like(builder.lower(root.get("identifier")), "%" + identifier.toLowerCase() + "%") : builder.conjunction();
	}

	public static Specification<InsGlobalErrorLog> hasTraceId(String traceId) {
	     return (root, query, builder) -> 
         traceId != null ? builder.like(builder.lower(root.get("traceId")), "%" + traceId.toLowerCase() + "%") : builder.conjunction();
	}

	public static Specification<InsGlobalErrorLog> hasCategory(String category) {
		return (root, query, builder) -> category != null ? builder.like(builder.lower(root.get("category")), "%" + category.toLowerCase() + "%")
				: builder.conjunction();
	}
	
	public static Specification<InsGlobalErrorLog> hasEqualCategory(String category) {
		return (root, query, builder) -> category != null ? builder.equal(builder.lower(root.get("category")), category.toLowerCase())
				: builder.conjunction();
	}

	public static Specification<InsGlobalErrorLog> excludeDLQ() {
		return (root, query, builder) -> builder.notEqual(root.get("category"), "DLQ");
	}

	public static Specification<InsGlobalErrorLog> hasSpanId(String spanId) {
		return (root, query, builder) -> spanId != null ? builder.like(builder.lower(root.get("spanId")), "%" + spanId.toLowerCase() + "%")
				: builder.conjunction();
	}

	public static Specification<InsGlobalErrorLog> hasAppErrorCode(String appErrorCode) {
		return (root, query, builder) -> appErrorCode != null ? builder.like(builder.lower(root.get("appErrorCode")), "%" + appErrorCode.toLowerCase() + "%")
				: builder.conjunction();
	}

	public static Specification<InsGlobalErrorLog> hasEmployeeId(String employeeCode) {
		return (root, query, builder) -> employeeCode != null ? builder.equal(builder.lower(root.get("employeeId")), employeeCode.toLowerCase())
				: builder.conjunction();
	}
}

package com.instape.app.cloudsql.specification;

import java.sql.Timestamp;

import org.springframework.data.jpa.domain.Specification;

import com.instape.app.cloudsql.model.WorkflowRun;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.JoinType;
import jakarta.persistence.criteria.Root;

public class WorkflowRunSpecification {

	public static Specification<WorkflowRun> hasStatus(String status) {
		return (root, query, builder) -> status != null
				? builder.equal(builder.lower(root.get("status")), status.toLowerCase())
				: builder.conjunction();
	}

	public static Specification<WorkflowRun> hasDateRange(Timestamp startDate, Timestamp endDate) {
		return (root, query, builder) -> {
			if (startDate != null && endDate != null) {
				return builder.between(root.get("createdDate"), startDate, endDate);
			} else if (startDate != null) {
				return builder.greaterThanOrEqualTo(root.get("createdDate"), startDate);
			} else if (endDate != null) {
				return builder.lessThanOrEqualTo(root.get("createdDate"), endDate);
			} else {
				return builder.conjunction();
			}
		};
	}

	public static Specification<WorkflowRun> hasEmployeeCode(String employeeCode) {
		return (Root<WorkflowRun> root, CriteriaQuery<?> query, CriteriaBuilder builder) -> {
			if (employeeCode != null) {
				return builder.equal(root.join("employeeInfo", JoinType.INNER).get("code"), employeeCode);
			}
			return null;
		};
	}

	// Specification for filtering by contract code
	public static Specification<WorkflowRun> hasContractCode(String contractCode) {
		return (Root<WorkflowRun> root, CriteriaQuery<?> query, CriteriaBuilder builder) -> {
			if (contractCode != null) {
				return builder.equal(root.join("contract", JoinType.INNER).get("code"), contractCode);
			}
			return null;
		};
	}

	public static Specification<WorkflowRun> hasWorkflowName(String workflowName) {
		return (Root<WorkflowRun> root, CriteriaQuery<?> query, CriteriaBuilder builder) -> {
			if (workflowName != null) {
				return builder.equal(
						root.join("workflowStep", JoinType.INNER).join("workflow", JoinType.INNER).get("name"),
						workflowName);
			}
			return null;
		};
	}

	public static Specification<WorkflowRun> hasWorkflowType(String workflowType) {
		return (root, query, builder) -> workflowType != null
				? builder.like(builder.lower(root.get("workflowType")), "%" + workflowType.toLowerCase() + "%")
				: builder.conjunction();
	}

	public static Specification<WorkflowRun> hasFunctionName(String functionName) {
		return (root, query, builder) -> functionName != null
				? builder.like(builder.lower(root.get("functionName")), "%" + functionName.toLowerCase() + "%")
				: builder.conjunction();
	}
	
	public static Specification<WorkflowRun> hasLoanApprovalStatus(String loanApprovalStatus) {
        return (Root<WorkflowRun> root, CriteriaQuery<?> query, CriteriaBuilder builder) -> {
            if (loanApprovalStatus != null) {
                // Use the PostgreSQL JSON function to access the "status" field inside the "data" object
                return builder.equal(
                        builder.function(
                                "jsonb_extract_path_text",
                                String.class,
                                root.get("responsePayload"), // Column name (responsePayload)
                                builder.literal("data"),      // First key (data)
                                builder.literal("status")     // Second key (status)
                        ),
                        loanApprovalStatus
                );
            }
            return null;
        };
    }

}

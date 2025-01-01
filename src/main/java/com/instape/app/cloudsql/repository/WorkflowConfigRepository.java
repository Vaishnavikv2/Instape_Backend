package com.instape.app.cloudsql.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.instape.app.cloudsql.model.WorkflowConfig;

/**
 * 
 * 
 * @Author - Nagaraj
 * @CreationDate - 25-Jul-2024
 * @ModifyDate - 25-Jul-2024
 * @Desc -
 */
public interface WorkflowConfigRepository extends JpaRepository<WorkflowConfig, Long> {

}

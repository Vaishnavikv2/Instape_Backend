package com.instape.app.cloudsql.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.instape.app.cloudsql.model.PartnerProductMapping;

/**
 * 
 * 
 * @Author - Nagaraj
 * @CreationDate - 03-Sep-2024
 * @ModifyDate - 03-Sep-2024
 * @Desc -
 */
public interface LenderProductMappingRepository extends JpaRepository<PartnerProductMapping, Long> {

}

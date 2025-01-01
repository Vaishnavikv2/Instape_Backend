package com.instape.app.cloudsql.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.instape.app.cloudsql.model.BankMaster;

/**
 * 
 * 
 * @Author - Nagaraj
 * @CreationDate - 20-Jun-2024
 * @ModifyDate - 20-Jun-2024
 * @Desc -
 */
public interface BankMasterRepository extends JpaRepository<BankMaster, Long> {

	@Query(value = "SELECT b FROM BankMaster b WHERE b.status NOT ilike :status")
	public List<BankMaster> getAllActiveBankMaster(String status);

	public BankMaster findByCode(String code);
}

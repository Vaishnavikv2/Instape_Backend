package com.instape.app.cloudsql.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.instape.app.cloudsql.model.PaymentConfirmationFile;

/**
 * 
 * 
 * @Author - Nagaraj
 * @CreationDate - 09-Oct-2024
 * @ModifyDate - 09-Oct-2024
 * @Desc -
 */
public interface PaymentConfirmationFileRepository extends JpaRepository<PaymentConfirmationFile, Long> {

}

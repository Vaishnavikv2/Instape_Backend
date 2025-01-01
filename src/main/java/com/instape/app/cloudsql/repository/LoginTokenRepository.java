package com.instape.app.cloudsql.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.instape.app.cloudsql.model.LoginToken;

public interface LoginTokenRepository extends JpaRepository<LoginToken, Long> {
	@Query("select lt from LoginToken lt where  lt.tokenString=:tokenString and lt.service='SP'")
	LoginToken getLoginTokenByTokenString(@Param("tokenString") String tokenString);
}

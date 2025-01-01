package com.instape.app.cloudsql.configuration;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import jakarta.persistence.EntityManagerFactory;


@Configuration
@EnableTransactionManagement
@EnableJpaRepositories(entityManagerFactoryRef = "entityManagerFactory", transactionManagerRef = "entityTransactionManager", basePackages = {
		"com.instape.app.cloudsql" })
public class CloudSqlConfiguration {
	@Primary
	@Bean(name = "entityManagerFactory")
	public LocalContainerEntityManagerFactoryBean entityManagerFactory(EntityManagerFactoryBuilder builder,
			@Qualifier("intapeDataSource") DataSource dataSource) {
		return builder.dataSource(dataSource).packages("com.instape.app.cloudsql.model").persistenceUnit("instape").build();
	}

	@Primary
	@Bean(name = "entityTransactionManager")
	public PlatformTransactionManager entityTransactionManager(
			@Qualifier("entityManagerFactory") EntityManagerFactory entityManagerFactory) {
		return new JpaTransactionManager(entityManagerFactory);

	}

	@Primary
	@Bean(name = "intapeDataSource")
	@ConfigurationProperties(prefix = "spring.datasource")
	public DataSource intapeDataSource() {
		return DataSourceBuilder.create().build();
	}
}

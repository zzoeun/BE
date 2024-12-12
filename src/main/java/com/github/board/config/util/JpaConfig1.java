package com.github.board.config.util;

import com.github.board.config.properties.DataSourceProperties;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.JpaVendorAdapter;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;

@Configuration
@RequiredArgsConstructor
@EnableConfigurationProperties(DataSourceProperties.class)
@EnableJpaRepositories(
        basePackages = {"com.github.board.repository.auth.user","com.github.board.repository.auth.role"},
        entityManagerFactoryRef = "entityManagerFactoryBean1",
        transactionManagerRef = "tmJpa1"
)
public class JpaConfig1 {
        static JpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();
        private final DataSourceProperties dataSourceProperties;

    @Bean
    public DataSource dataSource1(){
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setUsername(dataSourceProperties.getUsername());
        dataSource.setPassword(dataSourceProperties.getPassword());
        dataSource.setDriverClassName(dataSourceProperties.getDriverClassName());
        dataSource.setUrl(dataSourceProperties.getUrl());
        return dataSource;
    }
    @Bean
    public LocalContainerEntityManagerFactoryBean entityManagerFactoryBean1(@Qualifier("dataSource1")DataSource dataSource) {
        LocalContainerEntityManagerFactoryBean em = new LocalContainerEntityManagerFactoryBean();
        em.setDataSource(dataSource);
        em.setPackagesToScan("com.github.board.repository.auth.user","com.github.board.repository.auth.role");

        em.setJpaVendorAdapter(vendorAdapter);

        Map<String, Object> properties = new HashMap<>();
        properties.put("hibernate.format_sql","true");
        properties.put("hibernate.user_sql_comment","true");

        em.setJpaPropertyMap(properties);

        return em;
    }

    @Bean(name = "tmJpa1")
    public PlatformTransactionManager transactionManager1(@Qualifier("dataSource1")DataSource dataSource) {
        JpaTransactionManager transactionManager = new JpaTransactionManager();
        transactionManager.setEntityManagerFactory(entityManagerFactoryBean1(dataSource).getObject());
        return transactionManager;
    }
}
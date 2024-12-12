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
        basePackages = {"com.github.board.repository.post"},
        entityManagerFactoryRef = "entityManagerFactoryBeanPost",
        transactionManagerRef = "tmJpaPost"
)
public class JpaConfigPost {
    static JpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();
    private final DataSourceProperties dataSourceProperties;

    @Bean
    public DataSource dataSourcePost(){
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setUsername(dataSourceProperties.getUsername());
        dataSource.setPassword(dataSourceProperties.getPassword());
        dataSource.setDriverClassName(dataSourceProperties.getDriverClassName());
        dataSource.setUrl(dataSourceProperties.getUrl());
        return dataSource;
    }
    @Bean
    public LocalContainerEntityManagerFactoryBean entityManagerFactoryBeanPost(@Qualifier("dataSourcePost")DataSource dataSource) {
        LocalContainerEntityManagerFactoryBean em = new LocalContainerEntityManagerFactoryBean();
        em.setDataSource(dataSource);
        em.setPackagesToScan("com.github.movieproject.repository.post","com.github.movieproject.repository.auth");

        em.setJpaVendorAdapter(vendorAdapter);

        Map<String, Object> properties = new HashMap<>();
        properties.put("hibernate.format_sql","true");
        properties.put("hibernate.user_sql_comment","true");

        em.setJpaPropertyMap(properties);

        return em;
    }

    @Bean(name = "tmJpaPost")
    public PlatformTransactionManager transactionManagerPost(@Qualifier("dataSourcePost")DataSource dataSource) {
        JpaTransactionManager transactionManager = new JpaTransactionManager();
        transactionManager.setEntityManagerFactory(entityManagerFactoryBeanPost(dataSource).getObject());
        return transactionManager;
    }
}
package com.buddhi.multidatasource.api;

import com.zaxxer.hikari.HikariDataSource;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;

import javax.sql.DataSource;


@Configuration
@EnableJpaRepositories(
        entityManagerFactoryRef = "apiEntityManagerFactory",
        basePackages = {"com.buddhi.multidatasource.api.repository"}
)
public class ApiDataSourceConfig {
    @Bean(name = "apiDataSource")
    @Primary
    @ConfigurationProperties(prefix = "datasource.apidb")
    public HikariDataSource dataSource() {
        return DataSourceBuilder.create().type(HikariDataSource.class).build();
    }

    @Primary
    @Bean(name = "apiEntityManagerFactory")
    public LocalContainerEntityManagerFactoryBean apiEntityManagerFactory(
            EntityManagerFactoryBuilder builder,
            @Qualifier("apiDataSource") DataSource dataSource
    ) {
        return builder
                .dataSource(dataSource)
                .packages("com.buddhi.multidatasource.api.model")
                .persistenceUnit("apidb")
                .build();
    }

    /*@Bean
    @Primary
    @ConfigurationProperties(prefix="datasource.apidb")
    public DataSourceProperties apiDataSourceProperties() {
        return new DataSourceProperties();
    }

    @Primary
    @Bean
    public DataSource apiDataSource() {
        DataSourceProperties apiDataSourceProperties = apiDataSourceProperties();
        return DataSourceBuilder.create()
                .driverClassName(apiDataSourceProperties.getDriverClassName())
                .url(apiDataSourceProperties.getUrl())
                .username(apiDataSourceProperties.getUsername())
                .password(apiDataSourceProperties.getPassword())
                .build();
    }

    @Primary
    @Bean
    public LocalContainerEntityManagerFactoryBean apiEntityManagerFactory()
    {
        LocalContainerEntityManagerFactoryBean factory = new LocalContainerEntityManagerFactoryBean();
        factory.setDataSource(apiDataSource());
        factory.setPackagesToScan(new String[]{"com.buddhi.multidatasource.api.model"});
        factory.setJpaVendorAdapter(new HibernateJpaVendorAdapter());

        *//*Properties jpaProperties = new Properties();
        jpaProperties.put("hibernate.hbm2ddl.auto", env.getProperty("spring.jpa.hibernate.ddl-auto"));
        jpaProperties.put("hibernate.show-sql", env.getProperty("spring.jpa.show-sql"));
        factory.setJpaProperties(jpaProperties);*//*

        return factory;
    }*/
}
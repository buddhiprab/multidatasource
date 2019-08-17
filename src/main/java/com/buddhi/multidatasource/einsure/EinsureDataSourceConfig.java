package com.buddhi.multidatasource.einsure;

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
        entityManagerFactoryRef = "einsureEntityManagerFactory",
        basePackages = {"com.buddhi.multidatasource.einsure.repository"}
)
public class EinsureDataSourceConfig {
    /*@Bean(name = "einsureDataSource")
    @ConfigurationProperties(prefix = "datasource.einsure")
    public DataSource einsureDataSource() {
        return DataSourceBuilder.create().build();
    }

    @Bean(name = "einsureEntityManagerFactory")
    public LocalContainerEntityManagerFactoryBean einsureEntityManagerFactory(
            EntityManagerFactoryBuilder builder,
            @Qualifier("einsureDataSource") DataSource dataSource
    ) {
        return builder
                .dataSource(dataSource)
                .packages("com.buddhi.multidatasource.einsure.model")
                .persistenceUnit("einsuredb")
                .build();
    }*/

    @ConfigurationProperties(prefix="datasource.einsure")
    public DataSourceProperties einsureDataSourceProperties() {
        return new DataSourceProperties();
    }

    @Bean
    public DataSource einsureDataSource() {
        DataSourceProperties einsureDataSourceProperties = einsureDataSourceProperties();
        return DataSourceBuilder.create()
                .driverClassName(einsureDataSourceProperties.getDriverClassName())
                .url(einsureDataSourceProperties.getUrl())
                .username(einsureDataSourceProperties.getUsername())
                .password(einsureDataSourceProperties.getPassword())
                .build();
    }

    @Bean
    public LocalContainerEntityManagerFactoryBean einsureEntityManagerFactory()
    {
        LocalContainerEntityManagerFactoryBean factory = new LocalContainerEntityManagerFactoryBean();
        factory.setDataSource(einsureDataSource());
        factory.setPackagesToScan(new String[]{"com.buddhi.multidatasource.einsure.model"});
        factory.setJpaVendorAdapter(new HibernateJpaVendorAdapter());

        /*Properties jpaProperties = new Properties();
        jpaProperties.put("hibernate.hbm2ddl.auto", env.getProperty("spring.jpa.hibernate.ddl-auto"));
        jpaProperties.put("hibernate.show-sql", env.getProperty("spring.jpa.show-sql"));
        factory.setJpaProperties(jpaProperties);*/

        return factory;
    }
}
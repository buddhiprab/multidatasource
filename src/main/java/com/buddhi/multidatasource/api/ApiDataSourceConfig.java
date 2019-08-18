package com.buddhi.multidatasource.api;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.jndi.JndiObjectFactoryBean;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;

import javax.naming.NamingException;
import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;
import java.util.Properties;


@Configuration
@EnableJpaRepositories(
        entityManagerFactoryRef = "apiEntityManagerFactory",
        basePackages = {"com.buddhi.multidatasource.api.repository"}
)
public class ApiDataSourceConfig {

    /*@Bean(name = "apiDataSource")
    @Primary
    @ConfigurationProperties(prefix = "datasource.apidb")
    public HikariDataSource dataSource() {
        return DataSourceBuilder.create().type(HikariDataSource.class).build();
    }*/

    /*@Bean
    @Primary
    public DataSource jndiDataSource() {
        JndiDataSourceLookup dataSourceLookup = new JndiDataSourceLookup();
        DataSource dataSource = dataSourceLookup.getDataSource("java:comp/env/jdbc/apidb");
        return dataSource;
    }*/

    @Bean(name = "apiJndiDataSource", destroyMethod = "")
    @Primary
    public DataSource jndiDataSource() throws IllegalArgumentException, NamingException {
        JndiObjectFactoryBean bean = new JndiObjectFactoryBean();
        bean.setJndiName("java:comp/env/jdbc/apidb");
        bean.setProxyInterface(DataSource.class);
        bean.setExpectedType(DataSource.class);
        bean.setLookupOnStartup(false);
        bean.afterPropertiesSet();
        return (DataSource)bean.getObject();
    }
    /*@Primary
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
    }*/

    @Bean(name = "apiEntityManagerFactory")
    @Primary
    public EntityManagerFactory entityManagerFactory() throws NamingException {

        HibernateJpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();
        vendorAdapter.setGenerateDdl(true);

        LocalContainerEntityManagerFactoryBean factory = new LocalContainerEntityManagerFactoryBean();
        factory.setJpaVendorAdapter(vendorAdapter);
        factory.setPackagesToScan("com.buddhi.multidatasource.api.model");
        factory.setJpaProperties(additionalProperties());
        factory.setDataSource(jndiDataSource());
        factory.afterPropertiesSet();

        return factory.getObject();
    }

    Properties additionalProperties() {
        Properties properties = new Properties();
        properties.setProperty("hibernate.dialect", "org.hibernate.dialect.MySQLDialect");
        return properties;
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
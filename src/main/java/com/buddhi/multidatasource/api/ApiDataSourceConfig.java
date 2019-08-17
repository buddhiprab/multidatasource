package com.buddhi.multidatasource.api;

import com.zaxxer.hikari.HikariDataSource;
import org.apache.catalina.Context;
import org.apache.tomcat.util.descriptor.web.ContextResource;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory;
import org.springframework.boot.web.embedded.tomcat.TomcatWebServer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.springframework.context.annotation.Primary;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.jdbc.datasource.lookup.JndiDataSourceLookup;
import org.springframework.jndi.JndiObjectFactoryBean;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;

import javax.naming.NamingException;
import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;
import java.io.IOException;


@Configuration
@EnableJpaRepositories(
        entityManagerFactoryRef = "apiEntityManagerFactory",
        basePackages = {"com.buddhi.multidatasource.api.repository"}
)
public class ApiDataSourceConfig {
    @Bean(name = "tomcatFactory")
    public TomcatServletWebServerFactory tomcatFactory() {
        return new TomcatServletWebServerFactory() {
            @Override
            protected TomcatWebServer getTomcatWebServer(org.apache.catalina.startup.Tomcat tomcat) {
                tomcat.enableNaming();
                return super.getTomcatWebServer(tomcat);
            }
            @Override
            protected void postProcessContext(Context context) {
                ContextResource resource = new ContextResource();
                resource.setName("jdbc/apidb");
                resource.setType(HikariDataSource.class.getName());
                //resource.setProperty("factory", "org.apache.tomcat.jdbc.pool.DataSourceFactory");
                resource.setProperty("driverClassName", "com.mysql.jdbc.Driver");
                resource.setProperty("url", "jdbc:mysql://localhost:3306/apidb?useSSL=false");
                resource.setProperty("password", "root");
                resource.setProperty("username", "Mouse123");
                context.getNamingResources().addResource(resource);
            }
        };
    }
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

    @Primary
    @Bean(name = "apiEntityManagerFactory")
    public EntityManagerFactory entityManagerFactory() throws NamingException {

        HibernateJpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();
        vendorAdapter.setGenerateDdl(true);

        LocalContainerEntityManagerFactoryBean factory = new LocalContainerEntityManagerFactoryBean();
        factory.setJpaVendorAdapter(vendorAdapter);
        factory.setPackagesToScan("com.buddhi.multidatasource.api.model");
        factory.setDataSource(jndiDataSource());
        factory.afterPropertiesSet();

        return factory.getObject();
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
    @Bean(name = "apiJndiDataSource", destroyMethod = "")
    @DependsOn(value = {"tomcatFactory"})
    @Primary
    public DataSource jndiDataSource() throws IllegalArgumentException, NamingException{
        JndiObjectFactoryBean bean = new JndiObjectFactoryBean();
        bean.setJndiName("java:comp/env/jdbc/apidb");
        bean.setProxyInterface(DataSource.class);
        bean.setLookupOnStartup(false);
//        bean.afterPropertiesSet();
        return (DataSource)bean.getObject();
    }
}
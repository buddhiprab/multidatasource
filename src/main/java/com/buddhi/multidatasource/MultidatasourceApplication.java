package com.buddhi.multidatasource;

import com.zaxxer.hikari.HikariDataSource;
import org.apache.catalina.Context;
import org.apache.tomcat.util.descriptor.web.ContextResource;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory;
import org.springframework.boot.web.embedded.tomcat.TomcatWebServer;
import org.springframework.context.annotation.Bean;

import javax.sql.DataSource;

@SpringBootApplication
public class MultidatasourceApplication {

	public static void main(String[] args) {
		SpringApplication.run(MultidatasourceApplication.class, args);
	}

	/*@Bean("apiDataSoure")
	public DataSource getDataSource() {
		DataSourceBuilder dataSourceBuilder = DataSourceBuilder.create();
		dataSourceBuilder.driverClassName("com.mysql.jdbc.Driver");
		dataSourceBuilder.url("jdbc:mysql://localhost:3306/apidb?useSSL=false");
		dataSourceBuilder.username("root");
		dataSourceBuilder.password("Mouse123");
		return dataSourceBuilder.build();
	}*/

	@Bean
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
				resource.setType(DataSource.class.getName());
				//resource.setProperty("factory", "org.apache.tomcat.jdbc.pool.DataSourceFactory");
//				resource.setProperty("factory", "com.zaxxer.hikari.HikariDataSource");
				resource.setProperty("factory", "org.apache.tomcat.dbcp.dbcp2.BasicDataSourceFactory");
				resource.setProperty("driverClassName", "com.mysql.jdbc.Driver");
				resource.setProperty("url", "jdbc:mysql://localhost:3306/apidb?useSSL=false&allowPublicKeyRetrieval=true");
				resource.setProperty("username", "root");
				resource.setProperty("password", "Mouse123");
				context.getNamingResources().addResource(resource);
			}
		};
	}
}

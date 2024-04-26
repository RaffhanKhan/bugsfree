package com.bugsfree.registration;

import com.bugsfree.registration.model.Role;
import com.bugsfree.registration.model.User;
import com.bugsfree.registration.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import javax.sql.DataSource;
@EnableJpaRepositories("com.bugsfree.registration")
@SpringBootApplication
@PropertySource("classpath:registration.properties")
public class RegistrationApplication {

	@Autowired
	Environment environment;


	public static void main(String[] args) {
		SpringApplication.run(RegistrationApplication.class, args);
	}

	@Bean(destroyMethod = "")
	public DataSource dataSource() {
		DriverManagerDataSource dataSource = new DriverManagerDataSource();
		dataSource.setDriverClassName(environment.getProperty("bugsfree.datasource.driver-class-name"));
		dataSource.setUsername(environment.getProperty("bugsfree.datasource.username"));
		dataSource.setPassword(environment.getProperty("bugsfree.datasource.password"));
		dataSource.setUrl(environment.getProperty("bugsfree.datasource.url"));
		return dataSource;
	}
}

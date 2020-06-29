package com.excilys.cdb.spring;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportResource;
import org.springframework.jdbc.core.JdbcTemplate;

import com.excilys.cdb.model.validator.CompanyValidator;
import com.excilys.cdb.model.validator.ComputerValidator;
import com.excilys.cdb.model.validator.PageValidator;
import com.excilys.cdb.persistence.implementation.CompanyDAOImpl;
import com.excilys.cdb.persistence.implementation.ComputerDAOImpl;
import com.excilys.cdb.services.implementation.ServiceCompanyImpl;
import com.excilys.cdb.services.implementation.ServiceComputerImpl;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

@Configuration
@ComponentScan(basePackages = "com.excilys.cdb")
@ComponentScan(basePackageClasses = { CompanyDAOImpl.class,
        ComputerDAOImpl.class, ServiceCompanyImpl.class,
        ServiceComputerImpl.class, ComputerValidator.class,
        CompanyValidator.class, PageValidator.class })
@ImportResource("classpath:/applicationcontext.xml")
public class SpringConfig {

    public static void init() {
    };

    static {
        System.out.println("hello");
    }

    @SuppressWarnings("unused")
    private static final Logger LOGGER =
            LoggerFactory.getLogger(SpringConfig.class);

    private static final AnnotationConfigApplicationContext APP_CONTEXT =
            new AnnotationConfigApplicationContext(SpringConfig.class);

    @Bean
    public JdbcTemplate getJdbcTemplate() {
        return new JdbcTemplate(
                new HikariDataSource(new HikariConfig("/hikari.properties")));
    }
}

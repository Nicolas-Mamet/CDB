package com.excilys.cdb.spring;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportResource;

import com.excilys.cdb.model.validator.CompanyValidator;
import com.excilys.cdb.model.validator.ComputerValidator;
import com.excilys.cdb.model.validator.ICompanyValidator;
import com.excilys.cdb.model.validator.IComputerValidator;
import com.excilys.cdb.model.validator.IPageValidator;
import com.excilys.cdb.model.validator.PageValidator;
import com.excilys.cdb.persistence.implementation.CompanyDAOImpl;
import com.excilys.cdb.persistence.implementation.ComputerDAOImpl;
import com.excilys.cdb.persistence.implementation.HikariCP;
import com.excilys.cdb.persistence.interfaces.DataSource;
import com.excilys.cdb.services.implementation.ServiceCompanyImpl;
import com.excilys.cdb.services.implementation.ServiceComputerImpl;
import com.excilys.cdb.services.interfaces.ServiceCompany;
import com.excilys.cdb.services.interfaces.ServiceComputer;

@Configuration
@ComponentScan(basePackages = "com.excilys.cdb")
@ComponentScan(basePackageClasses = { HikariCP.class, CompanyDAOImpl.class,
        ComputerDAOImpl.class, ServiceCompanyImpl.class,
        ServiceComputerImpl.class, ComputerValidator.class,
        CompanyValidator.class, PageValidator.class })
@ImportResource("classpath:/applicationcontext.xml")
public class SpringConfig {

    public static void init() {
    };

    private static final Logger LOGGER =
            LoggerFactory.getLogger(SpringConfig.class);

    private static final AnnotationConfigApplicationContext APP_CONTEXT =
            new AnnotationConfigApplicationContext(SpringConfig.class);

    public static ServiceComputer getServiceComputer() {
        return APP_CONTEXT.getBean(ServiceComputer.class);
    }

    public static ServiceCompany getServiceCompany() {
        return APP_CONTEXT.getBean(ServiceCompany.class);
    }

    public static IComputerValidator getComputerValidator() {
        return APP_CONTEXT.getBean(IComputerValidator.class);
    }

    public static ICompanyValidator getCompanyValidator() {
        return APP_CONTEXT.getBean(ICompanyValidator.class);
    }

    public static IPageValidator getPageValidator() {
        return APP_CONTEXT.getBean(IPageValidator.class);
    }

    @Bean
    public DataSource getDataSource() {

        return new HikariCP("/hikari.properties");
    }
}

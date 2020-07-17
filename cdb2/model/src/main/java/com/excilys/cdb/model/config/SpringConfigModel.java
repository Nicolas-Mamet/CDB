package com.excilys.cdb.model.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import com.excilys.cdb.model.Company;
import com.excilys.cdb.model.Computer;
import com.excilys.cdb.model.service.ServiceCompanyImpl;
import com.excilys.cdb.model.service.ServiceComputerImpl;

@Configuration
@ComponentScan(
    basePackageClasses = { Company.class, Computer.class,
            ServiceCompanyImpl.class, ServiceComputerImpl.class })
public class SpringConfigModel {
}
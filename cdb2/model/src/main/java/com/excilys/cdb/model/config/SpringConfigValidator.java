package com.excilys.cdb.model.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import com.excilys.cdb.model.validator.CompanyValidator;
import com.excilys.cdb.model.validator.ComputerValidator;
import com.excilys.cdb.model.validator.PageValidator;

@Configuration
@ComponentScan(
    basePackageClasses = { CompanyValidator.class, ComputerValidator.class,
            PageValidator.class })
public class SpringConfigValidator {
}

package com.excilys.cdb.model.validator;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan(
    basePackageClasses = { CompanyValidator.class, ComputerValidator.class,
            PageValidator.class })
public class SpringConfigValidator {
}

package com.excilys.cdb.adapter.DAOAdapter;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan(
    basePackageClasses = { CompanyDAOAdapter.class, ComputerDAOAdapter.class,
            EComputerBuilderDirector.class })
public class SpringConfigDAOAdapter {

}

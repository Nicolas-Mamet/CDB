package com.excilys.cdb.adapter.ServiceAdapter;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan(
    basePackageClasses = { CompanyServiceAdapter.class,
            ComputerServiceAdapter.class })
public class SpringConfigService {

}

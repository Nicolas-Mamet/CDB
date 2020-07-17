package com.excilys.cdb.spring;

import javax.servlet.Filter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.support.AbstractAnnotationConfigDispatcherServletInitializer;

import com.excilys.cdb.adapter.DAOAdapter.SpringConfigDAOAdapter;
import com.excilys.cdb.adapter.ServiceAdapter.SpringConfigService;
import com.excilys.cdb.crossProject.LoggerSetup;
import com.excilys.cdb.model.config.SpringConfigModel;
import com.excilys.cdb.model.config.SpringConfigValidator;
import com.excilys.cdb.mvc.SpringConfigMVC;
import com.excilys.cdb.mvc.controller.AddressFilter;
import com.excilys.cdb.persistence.implementation.SpringConfigDAO;
import com.excilys.cdb.security.config.SpringConfigSecurity;

public class MyWebInitializer
        extends AbstractAnnotationConfigDispatcherServletInitializer {

    @SuppressWarnings("unused")
    private static final Logger LOGGER;

    static {
        LoggerSetup.setDefaultLevelToTrace();
        LOGGER = LoggerFactory.getLogger(MyWebInitializer.class);
//        LOGGER.trace("MyWebInitializer static initialization");
    }

    @Override
    protected Class<?>[] getRootConfigClasses() {
        return new Class[] { SpringConfigValidator.class,
                SpringConfigModel.class,
                SpringConfigDAO.class,
                SpringConfigDAOAdapter.class, SpringConfigService.class,
                SpringConfigSecurity.class };
    }

    @Override
    protected Class<?>[] getServletConfigClasses() {
        return new Class<?>[] { SpringConfigMVC.class };
    }

    @Override
    protected String[] getServletMappings() {
        return new String[] { "/" };
    }

    @Override
    protected Filter[] getServletFilters() {
        return new Filter[] { new AddressFilter() };
    }
}

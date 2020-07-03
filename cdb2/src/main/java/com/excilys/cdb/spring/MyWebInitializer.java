package com.excilys.cdb.spring;

import javax.servlet.Filter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.support.AbstractAnnotationConfigDispatcherServletInitializer;

import com.excilys.cdb.logger.LoggerSetup;
import com.excilys.cdb.servlet.AddressFilter;

public class MyWebInitializer
        extends AbstractAnnotationConfigDispatcherServletInitializer {
//    @Override
//    public void onStartup(ServletContext servletContext)
//            throws ServletException {
//        AnnotationConfigWebApplicationContext webContext =
//                new AnnotationConfigWebApplicationContext();
//        webContext.register(SpringConfig.class);
//        webContext.setServletContext(servletContext);
//        webContext.refresh();
//        ServletRegistration.Dynamic registration = servletContext
//                .addServlet("dispatcher", new DispatcherServlet(webContext));
//        registration.setLoadOnStartup(1);
//        registration.addMapping("/");
//    }
    @SuppressWarnings("unused")
    private static final Logger LOGGER;

    static {
        LoggerSetup.setDefaultLevelToTrace();
        LOGGER = LoggerFactory.getLogger(MyWebInitializer.class);
    }

    @Override
    protected Class<?>[] getRootConfigClasses() {
        return null;
    }

    @Override
    protected Class<?>[] getServletConfigClasses() {
        return new Class<?>[] { SpringConfig.class };
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

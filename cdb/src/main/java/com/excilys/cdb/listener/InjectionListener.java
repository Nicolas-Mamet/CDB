package com.excilys.cdb.listener;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.excilys.cdb.cli.AbstractServiceUser;
import com.excilys.cdb.persistence.implementation.CompanyDAOImpl;
import com.excilys.cdb.persistence.implementation.ComputerDAOImpl;
import com.excilys.cdb.persistence.implementation.DAOFactory;
import com.excilys.cdb.persistence.implementation.HikariCP;
import com.excilys.cdb.persistence.interfaces.CompanyDAO;
import com.excilys.cdb.persistence.interfaces.ComputerDAO;
import com.excilys.cdb.persistence.interfaces.SQLDataSource;
import com.excilys.cdb.services.implementation.AbstractDAOUser;
import com.excilys.cdb.services.implementation.ServiceCompanyImpl;
import com.excilys.cdb.services.implementation.ServiceComputerImpl;
import com.excilys.cdb.services.implementation.ServiceFactory;

/**
 * Application Lifecycle Listener implementation class InjectionListener
 *
 */
@WebListener
public class InjectionListener implements ServletContextListener {

    private final Logger logger =
            LoggerFactory.getLogger(InjectionListener.class);

    @Override
    public void contextInitialized(ServletContextEvent servletContextEvent) {
        ServletContext ctx = servletContextEvent.getServletContext();
        SQLDataSource dataSource = new HikariCP();
        CompanyDAO companyDAO = new CompanyDAOImpl();
        companyDAO.setDataSource(dataSource);
        ComputerDAO computerDAO = new ComputerDAOImpl();
        computerDAO.setDataSource(dataSource);
        ServiceFactory serviceFactory = new ServiceFactory();
        ServiceCompanyImpl serviceCompany = new ServiceCompanyImpl();
        serviceFactory.setServiceCompany(serviceCompany);
        ServiceComputerImpl serviceComputer = new ServiceComputerImpl();
        serviceFactory.setServiceComputer(serviceComputer);
        AbstractServiceUser.setServiceFactory(serviceFactory);
        ctx.setAttribute("serviceFactory", serviceFactory);
        DAOFactory daoFactory = new DAOFactory();
        daoFactory.setCompanyDAO(companyDAO);
        daoFactory.setComputerDAO(computerDAO);
        AbstractDAOUser.setDAOFactory(daoFactory);
        System.setProperty("org.slf4j.simpleLogger.defaultLogLevel", "trace");
        logger.info("Injection OK");
    }

    @Override
    public void contextDestroyed(ServletContextEvent servletContextEvent) {
        ;
    }
}

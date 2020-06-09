package listener;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

import com.excilys.cdb.cli.AbstractServiceUser;
import com.excilys.cdb.persistence.implementation.CompanyDAOImpl;
import com.excilys.cdb.persistence.implementation.ComputerDAOImpl;
import com.excilys.cdb.persistence.implementation.DAOFactory;
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
    @Override
    public void contextInitialized(ServletContextEvent servletContextEvent) {
        ServletContext ctx = servletContextEvent.getServletContext();
        System.out.println("injectionListener");
        ServiceFactory serviceFactory = new ServiceFactory();
        ServiceCompanyImpl serviceCompany = new ServiceCompanyImpl();
        serviceFactory.setServiceCompany(serviceCompany);
        ServiceComputerImpl serviceComputer = new ServiceComputerImpl();
        serviceFactory.setServiceComputer(serviceComputer);
        AbstractServiceUser.setServiceFactory(serviceFactory);
        ctx.setAttribute("serviceFactory", serviceFactory);
        DAOFactory daoFactory = new DAOFactory();
        daoFactory.setCompanyDAO(new CompanyDAOImpl());
        daoFactory.setComputerDAO(new ComputerDAOImpl());
        AbstractDAOUser.setDAOFactory(daoFactory);
    }
}

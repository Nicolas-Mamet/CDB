package util;

import javax.servlet.ServletContext;

import com.excilys.cdb.exceptions.WrongInitializationException;
import com.excilys.cdb.services.implementation.ServiceFactory;
import com.excilys.cdb.services.interfaces.ServiceCompany;
import com.excilys.cdb.services.interfaces.ServiceComputer;

public class ServletUtil {
    public static ServiceComputer getComputerService(ServletContext context)
            throws WrongInitializationException {
        return getServiceFactory(context).getServiceComputer();
    }

    public static ServiceCompany getCompanyService(ServletContext context)
            throws WrongInitializationException {
        return getServiceFactory(context).getServiceCompany();
    }

    private static ServiceFactory getServiceFactory(ServletContext context)
            throws WrongInitializationException {
        Object serviceObject = context.getAttribute("serviceFactory");
        if (!(serviceObject instanceof ServiceFactory)) {
            throw new WrongInitializationException(
                    "Expected ServiceFactory and found "
                            + serviceObject.getClass().getSimpleName() + " )");
        }
        return (ServiceFactory) serviceObject;
    }
}

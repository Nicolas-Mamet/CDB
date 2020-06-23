package com.excilys.cdb.servlet;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.excilys.cdb.cli.AbstractServiceUser;
import com.excilys.cdb.dto.CompanyDTO;
import com.excilys.cdb.dto.ComputerDTO;
import com.excilys.cdb.dto.PageDTO;
import com.excilys.cdb.exceptions.DBException;
import com.excilys.cdb.exceptions.NotLongException;
import com.excilys.cdb.exceptions.NullComputerException;
import com.excilys.cdb.exceptions.ProblemListException;
import com.excilys.cdb.services.interfaces.ServiceCompany;
import com.excilys.cdb.services.interfaces.ServiceComputer;

public class ServletCommonFunction extends AbstractServiceUser {

    private static final Logger LOGGER =
            LoggerFactory.getLogger(ServletCommonFunction.class);

    public static ServiceComputer getComputerService() {
        return getServiceFactory().getServiceComputer();
    }

    public static ServiceCompany getCompanyService() {
        return getServiceFactory().getServiceCompany();
    }

    public static void forward(HttpServletRequest request,
            HttpServletResponse response, Address url)
            throws ServletException, IOException {
        RequestDispatcher rd = request.getRequestDispatcher(url.getAddress());
        rd.forward(request, response);
    }

    private static void dealWithProblemList(HttpServletRequest request,
            HttpServletResponse response, ProblemListException e)
            throws ServletException, IOException {
        request.setAttribute("problemlist", e.getList());
        System.out.println("problemlist");
        RequestDispatcher rd =
                request.getRequestDispatcher(Address.PROBLEM.getAddress());
        rd.forward(request, response);
    }

    public static void dealWithInvalidPage(HttpServletRequest request,
            HttpServletResponse response, PageDTO pageDTO)
            throws ServletException, IOException {
        request.setAttribute("pagedto", pageDTO);
        System.out.println("invalidpage");
        RequestDispatcher rd =
                request.getRequestDispatcher(Address.INVALIDPAGE.getAddress());
        rd.forward(request, response);
    }

    private static void dealWithDB(HttpServletRequest request,
            HttpServletResponse response, DBException e)
            throws ServletException, IOException {
        LOGGER.debug(e.getMessage(), e);
        request.setAttribute("exception", e);
        System.out.println("dbissue");
        e.printStackTrace();
        System.out.println(e.getMessage());
        RequestDispatcher rd =
                request.getRequestDispatcher(Address.DB.getAddress());
        rd.forward(request, response);
    }

    public static void dealWithAbsurdity(HttpServletResponse response,
            HttpServletRequest request) throws ServletException, IOException {
        RequestDispatcher rd =
                request.getRequestDispatcher(Address.ABSURD.getAddress());
        rd.forward(request, response);
    }

    public static List<CompanyDTO> getCompanies() throws DBException {
        return getCompanyService().getCompanies();
    }

    public static Optional<ComputerDTO> getComputer(String id)
            throws NotLongException, DBException {
        return getComputerService().getComputer(id);
    }

    /**
     *
     * @param request
     * @param name
     * @return empty if the parameter does not exist or contains ""
     */
    public static Optional<String> getParameter(HttpServletRequest request,
            String name) {
        String parameter = request.getParameter(name);
        if (parameter == null || parameter.contentEquals("")) {
            return Optional.empty();
        } else {
            return Optional.of(parameter);
        }
    }

    public static Optional<CompanyDTO> buildCompany(String id) {
        if (id == null) {
            return Optional.empty();
        } else {
            return Optional.of(CompanyDTO.builder().withId(id).build());
        }
    }

//    public static ServiceFactory getServiceFactory(ServletContext context)
//            throws ServletException {
//        Object serviceObject = context.getAttribute("serviceFactory");
//        if (!(serviceObject instanceof ServiceFactory)) {
//            throw new ServletException(new WrongInitializationException(
//                    "Expected ServiceFactory and found "
//                            + serviceObject.getClass().getSimpleName() + " )"));
//        }
//        return (ServiceFactory) serviceObject;
//    }

    public static void dealWithException(Exception ex,
            HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            throw ex;
        } catch (DBException e) {
            ServletCommonFunction.dealWithDB(request, response, e);
        } catch (ProblemListException e) {
            ServletCommonFunction.dealWithProblemList(request, response, e);
        } catch (NullComputerException e) {
            throw new RuntimeException(
                    "check if the computer is null before calling addComputer");
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            dealWithAbsurdity(response, request);
        }
    }
}

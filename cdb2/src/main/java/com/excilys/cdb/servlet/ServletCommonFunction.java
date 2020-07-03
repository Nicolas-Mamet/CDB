package com.excilys.cdb.servlet;

import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.ModelAndView;

import com.excilys.cdb.dto.CompanyDTO;
import com.excilys.cdb.dto.ComputerDTO;
import com.excilys.cdb.exceptions.DBException;
import com.excilys.cdb.exceptions.NotLongException;
import com.excilys.cdb.exceptions.NullComputerException;
import com.excilys.cdb.exceptions.ProblemListException;
import com.excilys.cdb.services.interfaces.ServiceCompany;
import com.excilys.cdb.services.interfaces.ServiceComputer;

public final class ServletCommonFunction {

    private static final Logger LOGGER =
            LoggerFactory.getLogger(ServletCommonFunction.class);

    /**
     * effectively final
     */
    private static ServiceComputer serviceComputer;

    /**
     * effectively final
     */
    private static ServiceCompany serviceCompany;

    /**
     * Used to initialize the static attributes of ServletCommonFunction; should
     * only be instantiated once to make sure the services fields are
     * effectively final
     *
     */
    @Component
    private static class Initializer {
        private Initializer(ServiceComputer serviceComputer,
                ServiceCompany serviceCompany) {
            ServletCommonFunction.serviceComputer = serviceComputer;
            ServletCommonFunction.serviceCompany = serviceCompany;
        }
    }

    public static ServiceCompany getCompanyService() {
        return serviceCompany;
    }

    public static ServiceComputer getComputerService() {
        LOGGER.debug("computer service : " + serviceComputer.toString());
        return serviceComputer;
    }

    public static ModelAndView forward(Address url) {
        return new ModelAndView("/" + url.getAddress());
    }

    public static ModelAndView redirect(Address url) {
        return new ModelAndView("redirect:/" + url.getAddress());
    }

    public static ModelAndView forwardToController(Address url) {
        return new ModelAndView("forward:/" + url.getAddress());
    }

    private static ModelAndView dealWithProblemList(ProblemListException e) {
        LOGGER.debug("dealwithproblemlist : ", e.getMessage(), e);
        LOGGER.debug("list : " + e.getList().toString());
        ModelAndView modelAndView = forwardToController(Address.PROBLEM);
//                new ModelAndView("redirect:/" + Address.PROBLEM.getAddress());
        return modelAndView.addObject("e", e);
    }

//    public static void dealWithInvalidPage(HttpServletRequest request,
//            HttpServletResponse response, PageDTO pageDTO)
//            throws ServletException, IOException {
//        request.setAttribute("pagedto", pageDTO);
//        System.out.println("invalidpage");
//        RequestDispatcher rd =
//                request.getRequestDispatcher(Address.INVALIDPAGE.getAddress());
//        rd.forward(request, response);
//    }

    private static ModelAndView dealWithDB(DBException e) {
        LOGGER.debug("dealWithDB : ", e.getMessage(), e);
        ModelAndView modelAndView =
                new ModelAndView("redirect:/" + Address.DB.getAddress());
        return modelAndView.addObject("exception", e);
    }

    public static ModelAndView dealWithAbsurdity() {
        return new ModelAndView(":redirect/" + Address.ABSURD.getAddress());
    }

    public static List<CompanyDTO> getCompanies() throws DBException {
        return serviceCompany.getCompanies();
    }

    public static Optional<ComputerDTO> getComputer(String id)
            throws NotLongException, DBException {
        return serviceComputer.getComputer(id);
    }

    /**
     *
     * @param request
     * @param name
     * @return empty if the parameter does not exist or contains ""
     */
    public static Optional<String> getParameter(WebRequest request,
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

    public static ModelAndView dealWithException(Exception ex) {
        try {
            throw ex;
        } catch (DBException e) {
            return dealWithDB(e);
        } catch (ProblemListException e) {
            return dealWithProblemList(e);
        } catch (NullComputerException e) {
            return dealWithNullComputerException(e);
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            return dealWithAbsurdity();
        }
    }

    private static ModelAndView dealWithNullComputerException(
            NullComputerException e) {
        LOGGER.error("NullcomputerException treated as absurd", e.getMessage(),
                e);
        return new ModelAndView("redirect:/" + Address.ABSURD.getAddress());
    }
}

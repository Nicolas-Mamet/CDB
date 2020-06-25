package com.excilys.cdb.servlet;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.excilys.cdb.dto.ComputerDTO;
import com.excilys.cdb.dto.PageDTO;
import com.excilys.cdb.exceptions.AbsurdOptionalException;
import com.excilys.cdb.exceptions.DBException;
import com.excilys.cdb.exceptions.InvalidPageException;
import com.excilys.cdb.exceptions.NotLongException;
import com.excilys.cdb.exceptions.ProblemListException;
import com.excilys.cdb.mapper.Mapper;
import com.excilys.cdb.services.interfaces.ServiceComputer;
import com.excilys.cdb.servlet.Order.OrderBy;
import com.excilys.cdb.util.PageManager;

/**
 * Servlet implementation class PrepareListing
 */
@WebServlet("/dashboard")
public class PrepareDashboard extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private static final String DEFAULT_OFFSET = "0";
    private static final String DEFAULT_LIMIT = "10";
    @SuppressWarnings("unused")
    private static final Logger LOGGER =
            LoggerFactory.getLogger(PrepareDashboard.class);

    /**
     * @see HttpServlet#HttpServlet()
     */
    public PrepareDashboard() {
        super();
        // TODO Auto-generated constructor stub
    }

    /**
     * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
     *      response)
     */
    @Override
    protected void doGet(HttpServletRequest request,
            HttpServletResponse response) throws ServletException, IOException {
        ServiceComputer serviceComputer =
                ServletCommonFunction.getComputerService();
        String offset = getOffset(request);
        String limit = getLimit(request);
        Optional<String> search =
                ServletCommonFunction.getParameter(request, "search");
        Optional<Order> order = getOrder(request);
        PageDTO pageDTO =
                PageDTO.builder().withOffset(offset).withLimit(limit).build();
        try {
            prepareAndForward(request, response, serviceComputer, offset, limit,
                    pageDTO, search, order);
        } catch (Exception e) {
            ServletCommonFunction.dealWithException(e, request, response);
        }
    }

    /**
     * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
     *      response)
     */
    @Override
    protected void doPost(HttpServletRequest request,
            HttpServletResponse response) throws ServletException, IOException {
        // TODO Auto-generated method stub
        doGet(request, response);
    }

    private String getOffset(HttpServletRequest request) {
        if (request.getParameterMap().containsKey("offset")) {
            return request.getParameter("offset");
        } else {
            return DEFAULT_OFFSET;
        }
    }

    private String getLimit(HttpServletRequest request) {
        if (request.getParameterMap().containsKey("limit")) {
            return request.getParameter("limit");
        } else {
            return DEFAULT_LIMIT;
        }
    }

    private long getNbComputers(ServiceComputer serviceComputer)
            throws DBException {
        return serviceComputer.countComputers();
    }

    private Optional<PageManager> buildPageManager(String offset, String limit,
            long nbComputers) {
        try {
            return Optional.of(PageManager.builder().withNbItem(nbComputers)
                    .withLimit(Mapper.mapLong(limit))
                    .withOffset(Mapper.mapLong(offset)).build());
        } catch (NotLongException e) {
            return Optional.empty();
        }
    }

    private void prepareAndForward(HttpServletRequest request,
            HttpServletResponse response, ServiceComputer serviceComputer,
            String offset, String limit, PageDTO pageDTO,
            Optional<String> search, Optional<Order> order)
            throws ServletException, IOException {
        try {
            prepare(request, serviceComputer, offset, limit, pageDTO, search,
                    order);
            ServletCommonFunction.forward(request, response, Address.DASHBOARD);
        } catch (Exception e) {
            ServletCommonFunction.dealWithException(e, request, response);
        }
    }

    private void prepare(HttpServletRequest request,
            ServiceComputer serviceComputer, String offset, String limit,
            PageDTO pageDTO, Optional<String> search, Optional<Order> order)
            throws InvalidPageException, ProblemListException, DBException {
        List<ComputerDTO> listComputer;
        long nbComputers;
        if (search.isPresent()) {
            if (order.isPresent()) {
                listComputer = serviceComputer.getComputers(pageDTO,
                        search.get(), order.get());
                request.setAttribute("search", search.get());
            } else {
                listComputer =
                        serviceComputer.getComputers(pageDTO, search.get());
                request.setAttribute("search", search.get());
            }
            nbComputers = getNbComputers(serviceComputer, search.get());
        } else {
            if (order.isPresent()) {
                listComputer =
                        serviceComputer.getComputers(pageDTO, order.get());
            } else {
                listComputer = serviceComputer.getComputers(pageDTO);
            }
            nbComputers = getNbComputers(serviceComputer);
        }
        request.setAttribute("list", listComputer);
        setOrder(request, order);
        PageManager pageManager = buildPageManager(offset, limit, nbComputers)
                .orElseThrow(AbsurdOptionalException::new);
        request.setAttribute("pagemanager", pageManager);
    }

    private void setOrder(HttpServletRequest request, Optional<Order> order) {
        if (order.isEmpty()) {
            request.setAttribute("orderby", "");
            request.setAttribute("order", "");
        } else {
            if (order.get().isAsc()) {
                request.setAttribute("order", "asc");
            } else {
                request.setAttribute("order", "desc");
            }
            switch (order.get().getOrderBy()) {
            case COMPANY:
                request.setAttribute("orderby", "company");
                break;
            case COMPUTER:
                request.setAttribute("orderby", "computer");
                break;
            case DISCONTINUED:
                request.setAttribute("orderby", "discontinued");
                break;
            case INTRODUCED:
                request.setAttribute("orderby", "introduced");
                break;
            default:
                throw new RuntimeException(
                        "You forgot to complete the switch in PrepareDashboard");
            }
        }
    }

    private long getNbComputers(ServiceComputer serviceComputer, String search)
            throws DBException {
        return serviceComputer.countComputers(search);
    }

    private Optional<Order> getOrder(HttpServletRequest request) {
        Optional<String> orderby =
                ServletCommonFunction.getParameter(request, "orderby");
        Optional<String> order =
                ServletCommonFunction.getParameter(request, "order");
        if (orderby.isEmpty() || order.isEmpty()) {
            LOGGER.debug("No order provided to dashboard");
            return Optional.empty();
        }
        OrderBy orderBy;
        switch (orderby.get()) {
        case "computer":
            orderBy = OrderBy.COMPUTER;
            break;
        case "introduced":
            orderBy = OrderBy.INTRODUCED;
            break;
        case "discontinued":
            orderBy = OrderBy.DISCONTINUED;
            break;
        case "company":
            orderBy = OrderBy.COMPANY;
            break;
        default:
            LOGGER.debug("Orderby was found but contains an incorrect value");
            return Optional.empty();
        }
        boolean asc;
        switch (order.get()) {
        case "asc":
            asc = true;
            break;
        case "desc":
            asc = false;
            break;
        default:
            LOGGER.debug("order was found but contains an incorrect value");
            return Optional.empty();
        }
        return Optional
                .of(Order.builder().withOrderBy(orderBy).withAsc(asc).build());
    }
}

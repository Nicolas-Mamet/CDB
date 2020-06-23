package com.excilys.cdb.servlet;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.excilys.cdb.dto.ComputerDTO;
import com.excilys.cdb.dto.PageDTO;
import com.excilys.cdb.exceptions.AbsurdOptionalException;
import com.excilys.cdb.exceptions.DBException;
import com.excilys.cdb.exceptions.InvalidPageException;
import com.excilys.cdb.exceptions.NotLongException;
import com.excilys.cdb.exceptions.ProblemListException;
import com.excilys.cdb.mapper.Mapper;
import com.excilys.cdb.services.interfaces.ServiceComputer;
import com.excilys.cdb.util.PageManager;

/**
 * Servlet implementation class PrepareListing
 */
@WebServlet("/dashboard")
public class PrepareDashboard extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private static final String DEFAULT_OFFSET = "0";
    private static final String DEFAULT_LIMIT = "10";

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
        PageDTO pageDTO =
                PageDTO.builder().withOffset(offset).withLimit(limit).build();
        try {
            prepareAndForward(request, response, serviceComputer, offset, limit,
                    pageDTO, search);
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
            Optional<String> search) throws ServletException, IOException {
        try {
            prepare(request, serviceComputer, offset, limit, pageDTO, search);
            ServletCommonFunction.forward(request, response, Address.DASHBOARD);
        } catch (Exception e) {
            ServletCommonFunction.dealWithException(e, request, response);
        }
    }

    private void prepare(HttpServletRequest request,
            ServiceComputer serviceComputer, String offset, String limit,
            PageDTO pageDTO, Optional<String> search)
            throws InvalidPageException, ProblemListException, DBException {
        List<ComputerDTO> listComputer;
        long nbComputers;
        if (search.isPresent()) {
            listComputer = serviceComputer.getComputers(pageDTO, search.get());
            request.setAttribute("search", search.get());
            nbComputers = getNbComputers(serviceComputer, search.get());
        } else {
            listComputer = serviceComputer.getComputers(pageDTO);
            nbComputers = getNbComputers(serviceComputer);
        }
        request.setAttribute("list", listComputer);
        PageManager pageManager = buildPageManager(offset, limit, nbComputers)
                .orElseThrow(AbsurdOptionalException::new);
        request.setAttribute("pagemanager", pageManager);
    }

    private long getNbComputers(ServiceComputer serviceComputer, String search)
            throws DBException {
        return serviceComputer.countComputers(search);
    }

}

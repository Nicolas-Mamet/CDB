package com.excilys.cdb.servlet;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.excilys.cdb.dto.ComputerDTO;
import com.excilys.cdb.dto.PageDTO;
import com.excilys.cdb.exceptions.AbsurdOptionalException;
import com.excilys.cdb.exceptions.InvalidPageException;
import com.excilys.cdb.exceptions.NotLongException;
import com.excilys.cdb.exceptions.ProblemListException;
import com.excilys.cdb.mapper.Mapper;
import com.excilys.cdb.services.interfaces.ServiceComputer;
import com.excilys.cdb.util.PageManager;
import com.excilys.cdb.util.ServletUtil;

/**
 * Servlet implementation class PrepareListing
 */
@WebServlet("/computerlist")
public class PrepareListing extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private static final String DEFAULT_OFFSET = "0";
    private static final String DEFAULT_LIMIT = "10";

    /**
     * @see HttpServlet#HttpServlet()
     */
    public PrepareListing() {
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
                ServletUtil.getComputerService(getServletContext());
        String offset = getOffset(request);
        String limit = getLimit(request);
        PageDTO pageDTO =
                PageDTO.builder().withOffset(offset).withLimit(limit).build();
        long nbComputers = getNbComputers(serviceComputer);
        prepareAndForward(request, response, serviceComputer, offset, limit,
                pageDTO, nbComputers);
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
            throws ServletException {
        try {
            return serviceComputer.countComputers();
        } catch (SQLException e) {
            throw new ServletException(e);
        }
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
            String offset, String limit, PageDTO pageDTO, long nbComputers)
            throws ServletException, IOException {
        try {
            prepare(request, serviceComputer, offset, limit, pageDTO,
                    nbComputers);
            forward(request, response);
        } catch (SQLException e) {
            throw new ServletException(e);
        } catch (InvalidPageException e) {
            forwardInvalidPage(request, response, pageDTO);
        } catch (ProblemListException e) {
            forwardProblemList(request, response, e);
        }
    }

    private void forwardProblemList(HttpServletRequest request,
            HttpServletResponse response, ProblemListException e)
            throws ServletException, IOException {
        request.setAttribute("problemlist", e.getList());
        System.out.println("problemlist");
        RequestDispatcher rd = request.getRequestDispatcher("/problemlist");
        rd.forward(request, response);
    }

    private void forwardInvalidPage(HttpServletRequest request,
            HttpServletResponse response, PageDTO pageDTO)
            throws ServletException, IOException {
        request.setAttribute("pagedto", pageDTO);
        System.out.println("invalidpage");
        RequestDispatcher rd = request.getRequestDispatcher("/invalidpage");
        rd.forward(request, response);
    }

    private void forward(HttpServletRequest request,
            HttpServletResponse response) throws ServletException, IOException {
        RequestDispatcher rd =
                request.getRequestDispatcher("WEB-INF/dashboard.jsp");
        rd.forward(request, response);
    }

    private void prepare(HttpServletRequest request,
            ServiceComputer serviceComputer, String offset, String limit,
            PageDTO pageDTO, long nbComputers)
            throws SQLException, InvalidPageException, ProblemListException {
        List<ComputerDTO> listComputer = serviceComputer.getComputers(pageDTO);
        request.setAttribute("list", listComputer);
        PageManager pageManager = buildPageManager(offset, limit, nbComputers)
                .orElseThrow(AbsurdOptionalException::new);
        request.setAttribute("pagemanager", pageManager);
    }

}

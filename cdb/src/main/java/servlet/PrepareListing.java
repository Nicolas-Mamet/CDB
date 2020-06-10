package servlet;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.excilys.cdb.dto.ComputerDTO;
import com.excilys.cdb.dto.PageDTO;
import com.excilys.cdb.exceptions.InvalidPageException;
import com.excilys.cdb.exceptions.ProblemListException;
import com.excilys.cdb.exceptions.WrongInitializationException;
import com.excilys.cdb.services.interfaces.ServiceComputer;

import util.ServletUtil;

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
        ServiceComputer serviceComputer;
        try {
            serviceComputer =
                    ServletUtil.getComputerService(getServletContext());
        } catch (WrongInitializationException e) {
            throw new ServletException(e);
        }
        String offset;
        if (request.getParameterMap().containsKey("offset")) {
            offset = request.getParameter("offset");
        } else {
            offset = DEFAULT_OFFSET;
        }
        String limit;
        if (request.getParameterMap().containsKey("limit")) {
            limit = request.getParameter("limit");
        } else {
            limit = DEFAULT_LIMIT;
        }
        PageDTO pageDTO =
                PageDTO.builder().withOffset(offset).withLimit(limit).build();
        try {
            List<ComputerDTO> listComputer =
                    serviceComputer.getComputers(pageDTO);
            request.setAttribute("list", listComputer);
            request.setAttribute("limit", limit);
            request.setAttribute("offset", offset);
            RequestDispatcher rd =
                    request.getRequestDispatcher("WEB-INF/dashboard.jsp");
            rd.forward(request, response);
        } catch (SQLException e) {
            throw new ServletException(e);
        } catch (InvalidPageException e) {
            request.setAttribute("pagedto", pageDTO);
            System.out.println("invalidpage");
            RequestDispatcher rd = request.getRequestDispatcher("/invalidpage");
            rd.forward(request, response);
        } catch (ProblemListException e) {
            request.setAttribute("problemlist", e.getList());
            System.out.println("problemlist");
            RequestDispatcher rd = request.getRequestDispatcher("/problemlist");
            rd.forward(request, response);
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

}

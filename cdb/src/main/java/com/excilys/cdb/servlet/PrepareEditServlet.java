package com.excilys.cdb.servlet;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.excilys.cdb.dto.CompanyDTO;
import com.excilys.cdb.dto.ComputerDTO;
import com.excilys.cdb.exceptions.DBException;
import com.excilys.cdb.exceptions.NotLongException;

/**
 * Servlet implementation class PrepareEditServlet
 */
@WebServlet("/editcomputer")
public class PrepareEditServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    /**
     * @see HttpServlet#HttpServlet()
     */
    public PrepareEditServlet() {
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
        List<CompanyDTO> companies;
        try {
            companies = ServletCommonFunction.getCompanies();
        } catch (DBException e1) {
            ServletCommonFunction.dealWithException(e1, request, response);
            return;
        }
        request.setAttribute("list", companies);
        Optional<String> idString = getId(request, response);
        if (idString.isEmpty()) {
            ServletCommonFunction.dealWithAbsurdity(response, request);
            return;
        } else {
            try {
                ComputerDTO computer =
                        ServletCommonFunction.getComputer(idString.get()).get();
                request.setAttribute("computer", computer);
                ServletCommonFunction.forward(request, response, Address.VIEWEDIT);
            } catch (NotLongException e) {
                ServletCommonFunction.dealWithAbsurdity(response, request);
            } catch (DBException e) {
                ServletCommonFunction.dealWithException(e, request, response);
            }

        }
    }

    /**
     * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
     *      response)
     */
    @Override
    protected void doPost(HttpServletRequest request,
            HttpServletResponse response) throws ServletException, IOException {
        doGet(request, response);
    }

    private Optional<String> getId(HttpServletRequest request,
            HttpServletResponse response) {

        if (request.getParameterMap().containsKey("id")) {
            return Optional.ofNullable(request.getParameter("id"));

        } else {
            return Optional.empty();
        }

    }
}

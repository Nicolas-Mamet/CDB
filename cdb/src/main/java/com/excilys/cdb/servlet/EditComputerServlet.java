package com.excilys.cdb.servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.excilys.cdb.dto.CompanyDTO;
import com.excilys.cdb.dto.ComputerDTO;

/**
 * Servlet implementation class EditComputerServlet
 */
@WebServlet("/editcomputer2")
public class EditComputerServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    /**
     * @see HttpServlet#HttpServlet()
     */
    public EditComputerServlet() {
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
        ComputerDTO computer = buildComputer(request, response);
        if (editComputer(computer, request, response)) {
            response.sendRedirect(Address.BEFOREDASHBOARD.getAddress());
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

    private boolean editComputer(ComputerDTO computer,
            HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            ServletCommonFunction.getComputerService().updateComputer(computer);
            return true;
        } catch (Exception e) {
            ServletCommonFunction.dealWithException(e, request, response);
        }
        return false;
    }

    public static ComputerDTO buildComputer(HttpServletRequest request,
            HttpServletResponse response) throws ServletException {
        String id =
                ServletCommonFunction.getParameter(request, "id").orElse(null);
        String introduced = ServletCommonFunction
                .getParameter(request, "introduced").orElse(null);
        String discontinued = ServletCommonFunction
                .getParameter(request, "discontinued").orElse(null);
        String name = ServletCommonFunction.getParameter(request, "name")
                .orElse(null);
        CompanyDTO company = ServletCommonFunction
                .buildCompany(request.getParameter("company")).orElse(null);
        return ComputerDTO.builder().withId(id).withName(name)
                .withIntroduced(introduced).withDiscontinued(discontinued)
                .withCompany(company).build();
    }
}

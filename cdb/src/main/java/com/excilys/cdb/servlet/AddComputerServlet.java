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
 * Servlet implementation class addComputerServlet
 */
@WebServlet("/addcomputer2")
public class AddComputerServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    /**
     * @see HttpServlet#HttpServlet()
     */
    public AddComputerServlet() {
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
        if (addComputer(computer, request, response)) {
            response.sendRedirect(Address.BEFOREADD.getAddress());
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

    private boolean addComputer(ComputerDTO computer,
            HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            ServletCommonFunction.getComputerService().createComputer(computer);
            return true;
        } catch (Exception e) {
            ServletCommonFunction.dealWithException(e, request, response);
        }
        return false;
    }

    public static ComputerDTO buildComputer(HttpServletRequest request,
            HttpServletResponse response) throws ServletException {
        String introduced = ServletCommonFunction
                .getParameter(request, "introduced").orElse(null);
        String discontinued = ServletCommonFunction
                .getParameter(request, "discontinued").orElse(null);
        String name = ServletCommonFunction.getParameter(request, "name")
                .orElse(null);
        CompanyDTO company = ServletCommonFunction
                .buildCompany(request.getParameter("company")).orElse(null);
        return ComputerDTO.builder().withName(name).withIntroduced(introduced)
                .withDiscontinued(discontinued).withCompany(company).build();
    }
}

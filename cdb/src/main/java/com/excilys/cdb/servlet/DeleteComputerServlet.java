package com.excilys.cdb.servlet;

import java.io.IOException;
import java.util.Arrays;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Servlet implementation class DeleteComputerServlet
 */
@WebServlet("/deletecomputers")
public class DeleteComputerServlet extends HttpServlet {

    @SuppressWarnings("unused")
    private final Logger logger =
            LoggerFactory.getLogger(DeleteComputerServlet.class);

    private static final long serialVersionUID = 1L;

    /**
     * @see HttpServlet#HttpServlet()
     */
    public DeleteComputerServlet() {
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
        String[] ids = request.getParameterValues("cb");
        if (ids != null) {
            try {
                ServletCommonFunction.getComputerService()
                        .deleteComputers(Arrays.asList(ids));
            } catch (Exception e) {
                ServletCommonFunction.dealWithException(e, request, response);
                return;
            }
        }
        response.sendRedirect(Address.BEFOREDASHBOARD.getAddress());
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

}

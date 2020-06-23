package com.excilys.cdb.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.excilys.cdb.exceptions.Problem;

/**
 * Servlet implementation class ProblemListServlet
 */
@WebServlet("/problemlist")
public class ProblemListServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    /**
     * @see HttpServlet#HttpServlet()
     */
    public ProblemListServlet() {
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
        // TODO Auto-generated method stub
        Object list = request.getAttribute("problemlist");
        if (list == null || !(list instanceof List)) {
            response.getWriter().println("1t'5 N0t 34sY t0 b3 4 H4CK3r");
            return;
        } else {
            response.setContentType("text/html");
            List<?> problemList = (List<?>) request.getAttribute("problemlist");
            displayProblems(problemList, response);
            response.getWriter()
                    .println("<a href=/cdb/prepareadd>Back to add</a>");
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

    private void displayProblems(List<?> problemList,
            HttpServletResponse response) throws IOException {
        for (Object item : problemList) {
            if (!(item instanceof Problem)) {
                response.getWriter().println("N1C3 TrY k1dD0");
            } else {
                displayProblem((Problem) item, response);
            }
        }
    }

    private void displayProblem(Problem problem, HttpServletResponse response)
            throws IOException {
        PrintWriter out = response.getWriter();
        switch (problem.getNature()) {
        case NOCOMPANY:
            out.println("No such company");
            break;
        case NOCOMPUTER:
            out.println("No such computer");
            break;
        case NONAME:
            out.println("Name is mandatory");
            break;
        case NOTADATE:
            out.println(
                    problem.getOrigin().orElse("") + " is not a valid date");
            break;
        case NOTALONG:
            out.println(
                    problem.getOrigin().orElse("") + " is not a valid number");
            break;
        case WRONGORDER:
            out.println("Introduced and discontinued dates are in"
                    + " the wrong order");
            break;
        case BEFORE1970:
            out.println(problem.getOrigin().orElse("")
                    + " is not valid. Dates must be after 1970");
            break;
        default:
            throw new RuntimeException("Non exhaustive switch");
        }
        out.println("<br/>");
    }
}

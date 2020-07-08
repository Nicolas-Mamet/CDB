package com.excilys.cdb.mvc.controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

import com.excilys.cdb.crossProject.exceptions.Problem;
import com.excilys.cdb.crossProject.exceptions.ProblemListException;

@Controller
public class ProblemListController {
    private static final Logger LOGGER =
            LoggerFactory.getLogger(ControllerCommonFunction.class);

    @RequestMapping("/problemlist")
    public void treatRequest(
            @RequestAttribute(name = "e") ProblemListException e,
            HttpServletResponse response) {
        try {
            List<Problem> list = e.getList();
            PrintWriter writer = response.getWriter();
            if (list == null || !(list instanceof List)) {
                writer.println("1t'5 N0t 34sY t0 b3 4 H4CK3r");
            } else {
                response.setContentType("text/html");
                displayProblems(list, response);
                writer.println(
                        "<a href=/cdb/" + Address.BEFOREDASHBOARD.getAddress()
                                + ">Back to dashboard</a>");
            }
        } catch (IOException ex) {
            LOGGER.error(
                    "ProblemListServlet : could not get writer from the response",
                    ex.getMessage(), ex);
        }
    }

    private void displayProblems(
            List<?> problemList,
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
                    + " is not valid. Dates must be between 1970 and 2037");
            break;
        default:
            throw new RuntimeException("Non exhaustive switch");
        case WRONGLIMIT:
            out.println(problem.getOrigin().orElse("")
                    + " is not valid. Limit must be strictly greater than 0");
            break;
        case WRONGOFFSET:
            out.println(problem.getOrigin().orElse("")
                    + " is not valid. Offset must be strictly greater than -1");
            break;
        }
        out.println("<br/>");
    }
}

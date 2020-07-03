package com.excilys.cdb.servlet;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.excilys.cdb.dto.CompanyDTO;
import com.excilys.cdb.dto.ComputerDTO;
import com.excilys.cdb.exceptions.DBException;
import com.excilys.cdb.exceptions.NotLongException;

@Controller

public class PrepareEditServlet {

    private static final Logger LOGGER =
            LoggerFactory.getLogger(PrepareEditServlet.class);

    @RequestMapping("/editcomputer")
    public ModelAndView treatRequest(String id)
            throws ServletException, IOException {
        List<CompanyDTO> companies;
        try {
            companies = ServletCommonFunction.getCompanies();
        } catch (DBException e1) {
            return ServletCommonFunction.dealWithException(e1);
        }
        ModelAndView modelAndView =
                ServletCommonFunction.forward(Address.VIEWEDIT);
        modelAndView.addObject("list", companies);
        if (id == null || id.contentEquals("")) {
            return ServletCommonFunction.dealWithAbsurdity();
        } else {
            try {
                ComputerDTO computer =
                        ServletCommonFunction.getComputer(id).get();
                LOGGER.debug("edit computer : initial state of computer "
                        + computer.toString());
                modelAndView.addObject("computer", computer);
                return modelAndView;
            } catch (NotLongException e) {
                return ServletCommonFunction.dealWithAbsurdity();
            } catch (DBException e) {
                return ServletCommonFunction.dealWithException(e);
            }
        }
    }
}

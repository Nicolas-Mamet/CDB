package com.excilys.cdb.mvc.controller;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.excilys.cdb.crossProject.exceptions.DBException;
import com.excilys.cdb.crossProject.exceptions.NotLongException;
import com.excilys.cdb.mvc.dto.CompanyDTO;
import com.excilys.cdb.mvc.dto.ComputerDTO;

@Controller

public class PrepareEditController {

    private static final Logger LOGGER =
            LoggerFactory.getLogger(PrepareEditController.class);

    @RequestMapping("/editcomputer")
    public ModelAndView treatRequest(String id)
            throws ServletException, IOException {
        List<CompanyDTO> companies;
        try {
            companies = ControllerCommonFunction.getCompanies();
        } catch (DBException e1) {
            return ControllerCommonFunction.dealWithException(e1);
        }
        ModelAndView modelAndView =
                ControllerCommonFunction.forward(Address.VIEWEDIT);
        modelAndView.addObject("list", companies);
        if (id == null || id.contentEquals("")) {
            return ControllerCommonFunction.dealWithAbsurdity();
        } else {
            try {
                ComputerDTO computer =
                        ControllerCommonFunction.getComputer(id).get();
                LOGGER.debug("edit computer : initial state of computer "
                        + computer.toString());
                modelAndView.addObject("computer", computer);
                return modelAndView;
            } catch (NotLongException e) {
                return ControllerCommonFunction.dealWithAbsurdity();
            } catch (DBException e) {
                return ControllerCommonFunction.dealWithException(e);
            }
        }
    }
}

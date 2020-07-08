package com.excilys.cdb.mvc.controller;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.excilys.cdb.crossProject.exceptions.DBException;
import com.excilys.cdb.mvc.dto.CompanyDTO;

/**
 * Servlet implementation class prepareAdd
 */
@Controller
public class PrepareAddController {

    @RequestMapping("/addcomputer")
    public ModelAndView treatRequest() {
        List<CompanyDTO> companyList;
        try {
            companyList = ControllerCommonFunction.getCompanies();
            ModelAndView modelAndView =
                    ControllerCommonFunction.forward(Address.VIEWADD);
            return modelAndView.addObject("list", companyList);
        } catch (DBException e) {
            return ControllerCommonFunction.dealWithException(e);
        }
    }
}

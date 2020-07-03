package com.excilys.cdb.servlet;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.excilys.cdb.dto.CompanyDTO;
import com.excilys.cdb.exceptions.DBException;

/**
 * Servlet implementation class prepareAdd
 */
@Controller
public class PrepareAddServlet {
    private static final long serialVersionUID = 1L;

    @RequestMapping("/addcomputer")
    public ModelAndView treatRequest() {
        List<CompanyDTO> companyList;
        try {
            companyList = ServletCommonFunction.getCompanies();
            ModelAndView modelAndView =
                    ServletCommonFunction.forward(Address.VIEWADD);
            return modelAndView.addObject("list", companyList);
        } catch (DBException e) {
            return ServletCommonFunction.dealWithException(e);
        }
    }
}

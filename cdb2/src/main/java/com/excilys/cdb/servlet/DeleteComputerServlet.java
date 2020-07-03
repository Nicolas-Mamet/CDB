package com.excilys.cdb.servlet;

import java.util.Arrays;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class DeleteComputerServlet {

    @SuppressWarnings("unused")
    private final Logger logger =
            LoggerFactory.getLogger(DeleteComputerServlet.class);

    @RequestMapping("/deletecomputers")
    public ModelAndView treatRequest(String[] cb) {
        if (cb != null) {
            try {
                ServletCommonFunction.getComputerService()
                        .deleteComputers(Arrays.asList(cb));
            } catch (Exception e) {
                return ServletCommonFunction.dealWithException(e);
            }
        }
        return ServletCommonFunction.redirect(Address.BEFOREDASHBOARD);
    }
}

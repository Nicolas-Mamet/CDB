package com.excilys.cdb.mvc.controller;

import java.util.Arrays;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class DeleteComputerController {

    @SuppressWarnings("unused")
    private final Logger logger =
            LoggerFactory.getLogger(DeleteComputerController.class);

    @RequestMapping("/deletecomputers")
    public ModelAndView treatRequest(String[] cb) {
        if (cb != null) {
            try {
                ControllerCommonFunction.getComputerService()
                        .deleteComputers(Arrays.asList(cb));
            } catch (Exception e) {
                return ControllerCommonFunction.dealWithException(e);
            }
        }
        return ControllerCommonFunction.redirect(Address.BEFOREDASHBOARD);
    }
}

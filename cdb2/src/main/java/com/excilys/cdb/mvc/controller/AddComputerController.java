package com.excilys.cdb.mvc.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.excilys.cdb.mvc.dto.ComputerDTO;

@Controller
public class AddComputerController {

//    @RequestParam(required = false) String introduced,
//    @RequestParam(required = false) String discontinued,
//    @RequestParam String name,
//    @RequestParam(required = false) String companyId

    @SuppressWarnings("unused")
    private static final Logger LOGGER =
            LoggerFactory.getLogger(PrepareDashboardController.class);

    @RequestMapping("/addcomputer2")
    public ModelAndView treatRequest(ComputerDTO computer) {
//        ComputerDTO computer =
//                buildComputer(introduced, discontinued, name, companyId);
        LOGGER.trace("AddComputerServlet; computer = " + computer);
        return addComputer(computer);
    }

    private ModelAndView addComputer(ComputerDTO computer) {
        try {
            ControllerCommonFunction.getComputerService().createComputer(computer);
            return ControllerCommonFunction.redirect(Address.BEFOREADD);
        } catch (Exception e) {
            return ControllerCommonFunction.dealWithException(e);
        }
    }

//    private static ComputerDTO buildComputer(String introduced,
//            String discontinued, String name, String companyId) {
//
//        CompanyDTO company =
//                ServletCommonFunction.buildCompany(companyId).orElse(null);
//        return ComputerDTO.builder().withName(name).withIntroduced(introduced)
//                .withDiscontinued(discontinued).withCompany(company).build();
//    }
}

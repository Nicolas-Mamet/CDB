package com.excilys.cdb.servlet;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.excilys.cdb.dto.ComputerDTO;

@Controller
public class AddComputerServlet {
    private static final long serialVersionUID = 1L;

//    @RequestParam(required = false) String introduced,
//    @RequestParam(required = false) String discontinued,
//    @RequestParam String name,
//    @RequestParam(required = false) String companyId

    @SuppressWarnings("unused")
    private static final Logger LOGGER =
            LoggerFactory.getLogger(PrepareDashboard.class);

    @RequestMapping("/addcomputer2")
    public ModelAndView treatRequest(ComputerDTO computer) {
//        ComputerDTO computer =
//                buildComputer(introduced, discontinued, name, companyId);
        LOGGER.trace("AddComputerServlet; computer = " + computer);
        return addComputer(computer);
    }

    private ModelAndView addComputer(ComputerDTO computer) {
        try {
            ServletCommonFunction.getComputerService().createComputer(computer);
            return ServletCommonFunction.redirect(Address.BEFOREADD);
        } catch (Exception e) {
            return ServletCommonFunction.dealWithException(e);
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

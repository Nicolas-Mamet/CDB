package com.excilys.cdb.mvc.controller;

import java.io.IOException;

import javax.servlet.ServletException;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.excilys.cdb.mvc.dto.ComputerDTO;

@Controller
public class EditComputerController {

    @RequestMapping("/editcomputer2")
    protected ModelAndView treatRequest(ComputerDTO computer)
            throws ServletException, IOException {
        return editComputer(computer);
    }

    private ModelAndView editComputer(ComputerDTO computer)
            throws ServletException, IOException {
        try {
            ControllerCommonFunction.getComputerService().updateComputer(computer);
            return ControllerCommonFunction.redirect(Address.BEFOREDASHBOARD);
        } catch (Exception e) {
            return ControllerCommonFunction.dealWithException(e);
        }
    }

//    public static ComputerDTO buildComputer(HttpServletRequest request,
//            HttpServletResponse response) throws ServletException {
//        String id =
//                ServletCommonFunction.getParameter(request, "id").orElse(null);
//        String introduced = ServletCommonFunction
//                .getParameter(request, "introduced").orElse(null);
//        String discontinued = ServletCommonFunction
//                .getParameter(request, "discontinued").orElse(null);
//        String name = ServletCommonFunction.getParameter(request, "name")
//                .orElse(null);
//        CompanyDTO company = ServletCommonFunction
//                .buildCompany(request.getParameter("company")).orElse(null);
//        return ComputerDTO.builder().withId(id).withName(name)
//                .withIntroduced(introduced).withDiscontinued(discontinued)
//                .withCompany(company).build();
//    }
}

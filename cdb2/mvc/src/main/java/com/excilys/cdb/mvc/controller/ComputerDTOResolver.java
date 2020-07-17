package com.excilys.cdb.mvc.controller;

import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import com.excilys.cdb.mvc.dto.CompanyDTO;
import com.excilys.cdb.mvc.dto.ComputerDTO;

public class ComputerDTOResolver implements HandlerMethodArgumentResolver {

    private ComputerDTO buildComputerDTO(NativeWebRequest request) {
        String id =
                ControllerCommonFunction.getParameter(request, "id")
                        .orElse(null);
        String introduced = ControllerCommonFunction
                .getParameter(request, "introduced").orElse(null);
        String discontinued = ControllerCommonFunction
                .getParameter(request, "discontinued").orElse(null);
        String name = ControllerCommonFunction.getParameter(request, "name")
                .orElse(null);
        CompanyDTO company = ControllerCommonFunction
                .buildCompany(request.getParameter("company")).orElse(null);
        return ComputerDTO.builder().withId(id).withName(name)
                .withIntroduced(introduced).withDiscontinued(discontinued)
                .withCompany(company).build();
    }

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.getParameterType().equals(ComputerDTO.class);
    }

    @Override
    public ComputerDTO resolveArgument(
            MethodParameter parameter,
            ModelAndViewContainer mavContainer,
            NativeWebRequest webRequest,
            WebDataBinderFactory binderFactory) throws Exception {
        return buildComputerDTO(webRequest);
    }

}

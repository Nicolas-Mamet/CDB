package com.excilys.cdb.servlet;

import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import com.excilys.cdb.dto.CompanyDTO;
import com.excilys.cdb.dto.ComputerDTO;

public class ComputerDTOResolver implements HandlerMethodArgumentResolver {

    private Object buildComputerDTO(NativeWebRequest request) {
        String id =
                ServletCommonFunction.getParameter(request, "id").orElse(null);
        String introduced = ServletCommonFunction
                .getParameter(request, "introduced").orElse(null);
        String discontinued = ServletCommonFunction
                .getParameter(request, "discontinued").orElse(null);
        String name = ServletCommonFunction.getParameter(request, "name")
                .orElse(null);
        CompanyDTO company = ServletCommonFunction
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
    public Object resolveArgument(MethodParameter parameter,
            ModelAndViewContainer mavContainer, NativeWebRequest webRequest,
            WebDataBinderFactory binderFactory) throws Exception {
        return buildComputerDTO(webRequest);
    }

}

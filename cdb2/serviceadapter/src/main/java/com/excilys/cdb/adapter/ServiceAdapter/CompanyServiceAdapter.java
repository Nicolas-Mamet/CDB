package com.excilys.cdb.adapter.ServiceAdapter;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.excilys.cdb.crossProject.Mapper;
import com.excilys.cdb.crossProject.exceptions.DBException;
import com.excilys.cdb.crossProject.exceptions.InvalidCompanyException;
import com.excilys.cdb.crossProject.exceptions.NotLongException;
import com.excilys.cdb.crossProject.exceptions.ProblemListException;
import com.excilys.cdb.model.Page;
import com.excilys.cdb.mvc.dto.CompanyDTO;
import com.excilys.cdb.mvc.dto.PageDTO;

@Component
public class CompanyServiceAdapter
        implements com.excilys.cdb.mvc.service.CompanyService {

    @Autowired
    com.excilys.cdb.model.service.CompanyService companyService;

    @Override
    public List<CompanyDTO> getCompanies() throws DBException {
        return companyService.getCompanies().stream().filter(c -> c != null)
                .map(c -> MapperDTO.companyToDTO(c).get())
                .collect(Collectors.toList());
    }

    @Override
    public List<CompanyDTO> getCompanies(PageDTO pageDTO)
            throws ProblemListException, DBException {
        Page page = MapperDTO.dtoToPage(pageDTO).orElse(null);
        return companyService.getCompanies(page).stream().filter(c -> c != null)
                .map(c -> MapperDTO.companyToDTO(c).get())
                .collect(Collectors.toList());
    }

    @Override
    public boolean deleteCompany(String idString)
            throws NotLongException, InvalidCompanyException, DBException {
        long id = Mapper.mapLong(idString);
        return companyService.deleteCompany(id);
    }
}

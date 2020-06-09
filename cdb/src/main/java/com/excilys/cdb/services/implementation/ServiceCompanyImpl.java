package com.excilys.cdb.services.implementation;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import com.excilys.cdb.dto.CompanyDTO;
import com.excilys.cdb.dto.PageDTO;
import com.excilys.cdb.exceptions.InvalidPageException;
import com.excilys.cdb.exceptions.NotLongException;
import com.excilys.cdb.exceptions.ProblemListException;
import com.excilys.cdb.mapper.Mapper;
import com.excilys.cdb.mapper.MapperDTO;
import com.excilys.cdb.model.Company;
import com.excilys.cdb.model.Page;
import com.excilys.cdb.services.interfaces.ServiceCompany;

public final class ServiceCompanyImpl extends AbstractDAOUser
        implements ServiceCompany {

    private boolean isValid(Company company) {
        return company != null && company.getId() > 0;
    }

    @Override
    public List<CompanyDTO> getCompanies(PageDTO pageDTO)
            throws SQLException, InvalidPageException, ProblemListException {
        Page page = MapperDTO.DTOToPage(pageDTO).orElse(null);
        if (page == null || page.getOffset() < 0 || page.getLimit() < 1) {
            throw new InvalidPageException();
        }
        List<Company> companies =
                getDAOFactory().getCompanyDAO().getPageOfCompanies(page);

        System.out.println(companies.size());
        return getDAOFactory().getCompanyDAO().getPageOfCompanies(page).stream()
                .filter(c -> c != null)
                .map(c -> MapperDTO.CompanyToDTO(c).get())
                .collect(Collectors.toList());
    }

    @Override
    public Optional<String> getCompanyName(String iDDTO)
            throws SQLException, NotLongException {
        Long iD = Mapper.mapLong(iDDTO);
        return getDAOFactory().getCompanyDAO().getCompanyName(iD);
    }
}

package com.excilys.cdb.services.implementation;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.excilys.cdb.dto.CompanyDTO;
import com.excilys.cdb.dto.PageDTO;
import com.excilys.cdb.exceptions.DBException;
import com.excilys.cdb.exceptions.InvalidCompanyException;
import com.excilys.cdb.exceptions.NotLongException;
import com.excilys.cdb.exceptions.ProblemListException;
import com.excilys.cdb.mapper.Mapper;
import com.excilys.cdb.mapper.MapperDTO;
import com.excilys.cdb.model.Page;
import com.excilys.cdb.persistence.interfaces.CompanyDAO;
import com.excilys.cdb.services.interfaces.ServiceCompany;

@Service
public final class ServiceCompanyImpl implements ServiceCompany {

    @Autowired
    CompanyDAO companyDAO;

    private ServiceCompanyImpl() {
    }

    @Override
    public List<CompanyDTO> getCompanies(PageDTO pageDTO)
            throws ProblemListException, DBException {
        Page page = MapperDTO.dtoToPage(pageDTO).orElse(null);
        return companyDAO.getPageOfCompanies(page).stream()
                .filter(c -> c != null)
                .map(c -> MapperDTO.companyToDTO(c).get())
                .collect(Collectors.toList());
    }

    @Override
    public List<CompanyDTO> getCompanies() throws DBException {
        return companyDAO.getCompanies().stream().filter(c -> c != null)
                .map(c -> MapperDTO.companyToDTO(c).get())
                .collect(Collectors.toList());
    }

    @Override
    public boolean deleteCompany(String idString)
            throws NotLongException, InvalidCompanyException, DBException {
        long id = Mapper.mapLong(idString);
        return companyDAO.deleteCompany(id);
    }
}

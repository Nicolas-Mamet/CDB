package com.excilys.cdb.model.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.excilys.cdb.crossProject.exceptions.DBException;
import com.excilys.cdb.model.Company;
import com.excilys.cdb.model.Page;
import com.excilys.cdb.model.persistence.CompanyDAO;

@Service
public final class ServiceCompanyImpl implements CompanyService {

    @Autowired
    CompanyDAO companyDAO;

    /**
     * default access for testing purpose only
     */
    ServiceCompanyImpl() {
    }

    @Override
    public List<Company> getCompanies(Page page) throws DBException {
        return companyDAO.getPageOfCompanies(page);
    }

    @Override
    public List<Company> getCompanies() throws DBException {
        return companyDAO.getCompanies();
    }

    @Override
    public boolean deleteCompany(long id) throws DBException {
        return companyDAO.deleteCompany(id);
    }
}

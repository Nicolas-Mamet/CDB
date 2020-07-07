package com.excilys.cdb.model.service;

import java.util.List;

import com.excilys.cdb.crossProject.exceptions.DBException;
import com.excilys.cdb.model.Company;
import com.excilys.cdb.model.Page;

public interface CompanyService {
    List<Company> getCompanies() throws DBException;

    List<Company> getCompanies(Page page) throws DBException;

    boolean deleteCompany(long id) throws DBException;
}

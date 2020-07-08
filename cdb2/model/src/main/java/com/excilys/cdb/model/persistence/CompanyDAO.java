package com.excilys.cdb.model.persistence;

import java.util.List;

import com.excilys.cdb.crossProject.exceptions.DBException;
import com.excilys.cdb.model.Company;
import com.excilys.cdb.model.Page;

public interface CompanyDAO {

    List<Company> getCompanies() throws DBException;

    List<Company> getPageOfCompanies(Page page) throws DBException;

//    Optional<Company> getCompany(long iD) throws DBException;

    boolean deleteCompany(long id) throws DBException;
}

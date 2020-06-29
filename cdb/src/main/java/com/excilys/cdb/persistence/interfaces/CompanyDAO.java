package com.excilys.cdb.persistence.interfaces;

import java.util.List;

import com.excilys.cdb.exceptions.DBException;
import com.excilys.cdb.model.Company;
import com.excilys.cdb.model.Page;

public interface CompanyDAO {

    List<Company> getCompanies() throws DBException;

    List<Company> getPageOfCompanies(Page page) throws DBException;

//    Optional<Company> getCompany(long iD) throws DBException;

    boolean deleteCompany(long id) throws DBException;
}

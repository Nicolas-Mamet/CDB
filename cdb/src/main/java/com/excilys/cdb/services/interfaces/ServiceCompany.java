package com.excilys.cdb.services.interfaces;

import java.util.List;

import com.excilys.cdb.dto.CompanyDTO;
import com.excilys.cdb.dto.PageDTO;
import com.excilys.cdb.exceptions.DBException;
import com.excilys.cdb.exceptions.InvalidCompanyException;
import com.excilys.cdb.exceptions.NotLongException;
import com.excilys.cdb.exceptions.ProblemListException;

public interface ServiceCompany {
    List<CompanyDTO> getCompanies() throws DBException;

    List<CompanyDTO> getCompanies(PageDTO page)
            throws ProblemListException, DBException;

    boolean deleteCompany(String idString)
            throws NotLongException, InvalidCompanyException, DBException;
}

package com.excilys.cdb.mvc.service;

import java.util.List;

import com.excilys.cdb.crossProject.exceptions.DBException;
import com.excilys.cdb.crossProject.exceptions.InvalidCompanyException;
import com.excilys.cdb.crossProject.exceptions.NotLongException;
import com.excilys.cdb.crossProject.exceptions.ProblemListException;
import com.excilys.cdb.mvc.dto.CompanyDTO;
import com.excilys.cdb.mvc.dto.PageDTO;

public interface CompanyService {
    List<CompanyDTO> getCompanies() throws DBException;

    List<CompanyDTO> getCompanies(PageDTO page)
            throws ProblemListException, DBException;

    boolean deleteCompany(String idString)
            throws NotLongException, InvalidCompanyException, DBException;
}

package com.excilys.cdb.services.interfaces;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

import com.excilys.cdb.dto.CompanyDTO;
import com.excilys.cdb.dto.PageDTO;
import com.excilys.cdb.exceptions.InvalidPageException;
import com.excilys.cdb.exceptions.NotLongException;
import com.excilys.cdb.exceptions.ProblemListException;

public interface ServiceCompany {
    List<CompanyDTO> getCompanies(PageDTO page)
            throws SQLException, InvalidPageException, ProblemListException;

    Optional<String> getCompanyName(String iD)
            throws SQLException, NotLongException;
}

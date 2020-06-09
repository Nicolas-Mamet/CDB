package com.excilys.cdb.persistence.interfaces;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

import com.excilys.cdb.model.Company;
import com.excilys.cdb.model.Page;

public interface CompanyDAO {

    List<Company> getCompanies() throws SQLException;

    List<Company> getPageOfCompanies(Page page) throws SQLException;

    Optional<String> getCompanyName(Long iD) throws SQLException;
}

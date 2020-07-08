package com.excilys.cdb.persistence.interfaces;

import java.util.List;
import java.util.Optional;

import com.excilys.cdb.persistence.Page;
import com.excilys.cdb.persistence.entity.ECompany;

public interface CompanyDAO {
    List<ECompany> getCompanies();

    List<ECompany> getPageOfCompanies(Page page);

    Optional<ECompany> getCompany(long id);

    boolean deleteCompany(long id);
}

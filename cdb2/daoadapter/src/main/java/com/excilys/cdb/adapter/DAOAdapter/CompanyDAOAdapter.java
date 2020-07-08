package com.excilys.cdb.adapter.DAOAdapter;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.excilys.cdb.crossProject.exceptions.DBException;
import com.excilys.cdb.model.Company;
import com.excilys.cdb.model.Page;

@Component
public class CompanyDAOAdapter
        implements com.excilys.cdb.model.persistence.CompanyDAO {

    @Autowired
    com.excilys.cdb.persistence.interfaces.CompanyDAO companyDAO;

    @Autowired
    CompanyMapper companyMapper;

    @Autowired
    PageBuilderDirector director;

    @Override
    public List<Company> getCompanies() throws DBException {
        try {
            return companyDAO.getCompanies().stream().map(companyMapper::map)
                    .collect(Collectors.toList());
        } catch (Exception e) {
            throw new DBException(e);
        }
    }

    @Override
    public List<Company> getPageOfCompanies(Page page) throws DBException {
        try {
            return companyDAO.getPageOfCompanies(director.buildPage(page)).stream()
                    .map(companyMapper::map)
                    .collect(Collectors.toList());
        } catch (Exception e) {
            throw new DBException(e);
        }
    }

    @Override
    public boolean deleteCompany(long id) throws DBException {
        try {
            return companyDAO.deleteCompany(id);
        } catch (Exception e) {
            throw new DBException(e);
        }
    }

}

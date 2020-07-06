package com.excilys.cdb.persistence.implementation;

import java.util.List;
import java.util.stream.Collectors;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.excilys.cdb.exceptions.DBException;
import com.excilys.cdb.model.Company;
import com.excilys.cdb.model.Page;
import com.excilys.cdb.persistence.implementation.mapper.CompanyMapper;
import com.excilys.cdb.persistence.interfaces.CompanyDAO;
import com.excilys.cdb.persistence.interfaces.ComputerDAO;
import com.excilys.cdb.persistence.model.ECompany;
import com.excilys.cdb.persistence.model.QECompany;
import com.querydsl.jpa.impl.JPADeleteClause;
import com.querydsl.jpa.impl.JPAQuery;

@Repository
public class CompanyDAOJPA implements CompanyDAO {

    @PersistenceContext
    private EntityManager entityManager;

    @Autowired
    private CompanyMapper companyMapper;

    @Autowired
    private ComputerDAO computerDAO;

    @Override
    public List<Company> getCompanies() throws DBException {
        try {
            return companyListQuery().fetch().stream().map(companyMapper::map)
                    .collect(Collectors.toList());
        } catch (Exception e) {
            throw new DBException(e);
        }
    }

    @Override
    public List<Company> getPageOfCompanies(Page page) throws DBException {
        try {
            return pageListQuery(page).fetch().stream().map(companyMapper::map)
                    .collect(Collectors.toList());
        } catch (Exception e) {
            throw new DBException(e);
        }
    }

    @Override
    @Transactional
    public boolean deleteCompany(long id) throws DBException {
        computerDAO.deleteComputersFromCompany(id);
        try {
            return 1 == deleteCompanyQuery(id).execute();
        } catch (Exception e) {
            throw new DBException(e);
        }
    }

    private JPAQuery<ECompany> companyListQuery() {
        QECompany company = QECompany.eCompany;
        return new JPAQuery<ECompany>(entityManager).from(company)
                .orderBy(company.name.asc());
    }

    private JPAQuery<ECompany> pageListQuery(Page page) {
        return companyListQuery().limit(page.getLimit())
                .offset(page.getOffset());
    }

    private JPADeleteClause deleteCompanyQuery(long id) {
        QECompany company = QECompany.eCompany;
        return new JPADeleteClause(entityManager, company)
                .where(company.id.eq(id));
    }
}

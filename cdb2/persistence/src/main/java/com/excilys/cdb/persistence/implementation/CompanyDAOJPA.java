package com.excilys.cdb.persistence.implementation;

import java.util.List;
import java.util.Optional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.excilys.cdb.persistence.Page;
import com.excilys.cdb.persistence.entity.ECompany;
import com.excilys.cdb.persistence.entity.QECompany;
import com.excilys.cdb.persistence.interfaces.CompanyDAO;
import com.excilys.cdb.persistence.interfaces.ComputerDAO;
import com.querydsl.jpa.impl.JPADeleteClause;
import com.querydsl.jpa.impl.JPAQuery;

@Repository
public class CompanyDAOJPA implements CompanyDAO {

    @PersistenceContext
    private EntityManager entityManager;

    @Autowired
    private ComputerDAO computerDAO;

    @Override
    public List<ECompany> getCompanies() {
        return companyListQuery().fetch();
    }

    @Override
    public List<ECompany> getPageOfCompanies(Page page) {
        return pageListQuery(page).fetch();
    }

    @Override
    @Transactional
    public boolean deleteCompany(long id) {
        computerDAO.deleteComputersFromCompany(id);
        return 1 == deleteCompanyQuery(id).execute();
    }

    @Override
    public Optional<ECompany> getCompany(long id) {
        return Optional.ofNullable(entityManager.find(ECompany.class,
                id));
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

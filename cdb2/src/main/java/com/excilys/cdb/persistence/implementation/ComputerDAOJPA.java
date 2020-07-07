package com.excilys.cdb.persistence.implementation;

import java.util.List;
import java.util.Optional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import com.excilys.cdb.crossProject.Order;
import com.excilys.cdb.crossProject.exceptions.AbsurdException;
import com.excilys.cdb.persistence.Page;
import com.excilys.cdb.persistence.entity.EComputer;
import com.excilys.cdb.persistence.entity.QECompany;
import com.excilys.cdb.persistence.entity.QEComputer;
import com.excilys.cdb.persistence.interfaces.ComputerDAO;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.ComparableExpressionBase;
import com.querydsl.jpa.impl.JPADeleteClause;
import com.querydsl.jpa.impl.JPAQuery;

@Repository
public class ComputerDAOJPA implements ComputerDAO {

    @SuppressWarnings("unused")
    private Logger logger = LoggerFactory.getLogger(ComputerDAOJPA.class);

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public long countComputers() {
        return countComputersQuery().fetchCount();
    }

    @Override
    public long countComputers(String search) {
        return countComputersQuery(search).fetchCount();
    }

    @Override
    public List<EComputer> searchComputers(Page page, String search) {
        return listComputersQuery(page, search).fetch();
    }

    @Override
    public List<EComputer> getPageOfComputers(Page page) {
        return listComputersQuery(page).fetch();
    }

    @Override
    public Optional<EComputer> getComputer(long iD) {
        List<EComputer> computers = getComputerQuery(iD).fetch();
        if (computers.size() == 1) {
            return Optional
                    .of(computers.get(0));
        }
        if (computers.size() == 0) {
            return Optional.empty();
        }
        throw new AbsurdException("Found multiple primary key");
    }

    @Override
    @Transactional
    public void createComputer(EComputer computer) {
        entityManager.persist(computer);
    }

    @Override
    @Transactional
    public boolean updateComputer(EComputer computer) {
        entityManager.merge(computer);
        return true;

    }

    @Override
    @Transactional
    public boolean deleteComputer(long iD) {
        return 1 == deleteComputerClause(iD).execute();
    }

    @Override
    @Transactional
    public void deleteComputers(List<Long> ids) {
        deleteComputersClause(ids).execute();
    }

    @Override
    @Transactional
    public void deleteComputersFromCompany(long id) {
        deleteComputersCompanyClause(id).execute();
    }

    @Override
    public List<EComputer>
            searchComputers(Page page, String search, Order order) {
        return listComputersQuery(page, search, order)
                .fetch();
    }

    @Override
    public List<EComputer> searchComputers(Page page, Order order) {
        return listComputersQuery(page, order).fetch();
    }

    private JPAQuery<EComputer> countComputersQuery() {
        QEComputer computer = QEComputer.eComputer;
        return new JPAQuery<EComputer>(entityManager).from(computer);
    }

    private JPAQuery<EComputer> countComputersQuery(String search) {
        QEComputer computer = QEComputer.eComputer;
        return countComputersQuery().where(computer.name.contains(search));
    }

    private JPAQuery<EComputer> listComputersQuery() {
        QEComputer computer = QEComputer.eComputer;
        QECompany company = QECompany.eCompany;
        return new JPAQuery<EComputer>(entityManager).from(computer)
                .leftJoin(computer.company, company);
    }

    private JPAQuery<EComputer> listComputersQuery(Page page, String search) {
        return decorate(listComputersQuery(page), search);
    }

    private JPAQuery<EComputer> listComputersQuery(Page page) {
        return decorate(listComputersQuery(), page);
    }

    private JPAQuery<EComputer> listComputersQuery(
            Page page,
            String search,
            Order order) {
        return decorate(listComputersQuery(page, search), order);
    }

    private JPAQuery<EComputer> listComputersQuery(Page page, Order order) {
        return decorate(listComputersQuery(page), order);
    }

    private JPAQuery<EComputer> getComputerQuery(long id) {
        QEComputer computer = QEComputer.eComputer;
        return listComputersQuery().where(computer.id.eq(id));
    }

    private JPADeleteClause deleteComputerClause(long id) {
        QEComputer computer = QEComputer.eComputer;
        return new JPADeleteClause(entityManager, computer)
                .where(computer.id.eq(id));
    }

    private JPADeleteClause deleteComputersClause(List<Long> ids) {
        QEComputer computer = QEComputer.eComputer;
        return new JPADeleteClause(entityManager, computer)
                .where(computer.id.in(ids));
    }

    private JPADeleteClause deleteComputersCompanyClause(long id) {
        QEComputer computer = QEComputer.eComputer;
        return new JPADeleteClause(entityManager, computer)
                .where(computer.company.id.eq(id));
    }

    private JPAQuery<EComputer> decorate(JPAQuery<EComputer> query, Page page) {
        return query.limit(page.getLimit()).offset(page.getOffset());
    }

    private JPAQuery<EComputer> decorate(
            JPAQuery<EComputer> query,
            String search) {
        QEComputer computer = QEComputer.eComputer;
        return query.where(computer.name.contains(search));
    }

    private JPAQuery<EComputer> decorate(
            JPAQuery<EComputer> query,
            Order order) {
        return query.orderBy(getOrder(order));
    }

    private OrderSpecifier<?> getOrder(Order order) {
        QEComputer computer = QEComputer.eComputer;
        ComparableExpressionBase<?> orderBy;
        switch (order.getOrderBy()) {
        case COMPANY:
            orderBy = computer.company.name;
            break;
        case COMPUTER:
            orderBy = computer.name;
            break;
        case DISCONTINUED:
            orderBy = computer.discontinued;
            break;
        case INTRODUCED:
            orderBy = computer.introduced;
            break;
        default:
            throw new RuntimeException(
                    "You forgot to complete the switch in ComputerDAOJPA");
        }
        if (order.isAsc()) {
            return orderBy.asc();
        } else {
            return orderBy.desc();
        }
    }
}

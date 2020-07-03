package com.excilys.cdb.persistence.implementation;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.excilys.cdb.exceptions.AbsurdException;
import com.excilys.cdb.exceptions.CorruptComputerException;
import com.excilys.cdb.exceptions.CorruptComputersException;
import com.excilys.cdb.exceptions.DBException;
import com.excilys.cdb.exceptions.Problem;
import com.excilys.cdb.exceptions.ProblemListException;
import com.excilys.cdb.model.Company;
import com.excilys.cdb.model.Computer;
import com.excilys.cdb.model.Computer.ComputerBuilder;
import com.excilys.cdb.model.Page;
import com.excilys.cdb.persistence.implementation.mapper.ComputerMapper;
import com.excilys.cdb.persistence.interfaces.ComputerDAO;
import com.excilys.cdb.persistence.model.ECompany;
import com.excilys.cdb.persistence.model.EComputer;
import com.excilys.cdb.persistence.model.QECompany;
import com.excilys.cdb.persistence.model.QEComputer;
import com.excilys.cdb.servlet.Order;
import com.querydsl.jpa.impl.JPADeleteClause;
import com.querydsl.jpa.impl.JPAQuery;

@Repository
public class ComputerDAOJPA implements ComputerDAO {

    @SuppressWarnings("unused")
    private Logger logger = LoggerFactory.getLogger(CompanyDAOImpl.class);

    @PersistenceContext
    private EntityManager entityManager;

    @Autowired
    ComputerMapper computerMapper;

    @Override
    public long countComputers() throws DBException, CorruptComputersException {
        try {
            return countComputersQuery().fetchCount();
        } catch (Exception e) {
            throw new DBException(e);
        }
    }

    @Override
    public long countComputers(String search) throws DBException {
        try {
            return countComputersQuery(search).fetchCount();
        } catch (Exception e) {
            throw new DBException(e);
        }
    }

    @Override
    public List<Computer> searchComputers(Page page, String search)
            throws DBException {
        try {
            return buildComputers(listComputersQuery(search, page).fetch()
                    .stream().map(computerMapper::map)
                    .collect(Collectors.toList()));
        } catch (Exception e) {
            throw new DBException(e);
        }
    }

    @Override
    public List<Computer> getPageOfComputers(Page page) throws DBException {
        try {
            return buildComputers(listComputersQuery(page).fetch().stream()
                    .map(computerMapper::map).collect(Collectors.toList()));
        } catch (Exception e) {
            throw new DBException(e);
        }
    }

    @Override
    public Optional<Computer> getComputer(long iD) throws DBException {
        List<EComputer> computers = getComputerQuery(iD).fetch();
        if (computers.size() == 1) {
            try {
                return Optional
                        .of(computerMapper.map(computers.get(0)).build());
            } catch (ProblemListException e) {
                throw new CorruptComputerException(e.getList());
            }
        }
        if (computers.size() == 0) {
            return Optional.empty();
        }
        throw new AbsurdException("Found multiple primary key");
    }

    @Override
    public void createComputer(Computer computer) throws DBException {
        Optional<ECompany> company = getCompany(computer);
        try {
            entityManager.persist(
                    EComputer.createEComputer(computer, company.orElse(null)));
        } catch (Exception e) {
            throw new DBException(e);
        }
    }

    @Override
    public boolean updateComputer(Computer computer) throws DBException {
        Optional<ECompany> company = getCompany(computer);
        try {
            entityManager.merge(
                    EComputer.createEComputer(computer, company.orElse(null)));
        } catch (Exception e) {
            throw new DBException(e);
        }
        return true;
    }

    @Override
    public boolean deleteComputer(long iD) throws DBException {
        return 1 == deleteComputerClause(iD).execute();
    }

    @Override
    public void deleteComputers(List<Long> ids) throws DBException {
        deleteComputersClause(ids).execute();
    }

    @Override
    public void deleteComputersFromCompany(long id) throws DBException {
        deleteComputersCompanyClause(id).execute();
    }

    @Override
    public Collection<Computer> searchComputers(Page page, String search,
            Order order) throws DBException {

    }

    @Override
    public Collection<Computer> searchComputers(Page page, Order order)
            throws DBException {

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
        return new JPAQuery<EComputer>().from(computer)
                .leftJoin(computer.company, company);
    }

    private JPAQuery<EComputer> listComputersQuery(String search, Page page) {
        return decorate(listComputersQuery(page), search);
    }

    private JPAQuery<EComputer> listComputersQuery(Page page) {
        return decorate(listComputersQuery(), page);
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

//    private JPAUpdateClause updateComputerClause(Computer computer) {
//        QEComputer eComputer = QEComputer.eComputer;
//        return new JPAUpdateClause(entityManager, eComputer)
//                .where(eComputer.id.eq(computer.getId())).set(paths, values)
//    }

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

    private JPAQuery<EComputer> decorate(JPAQuery<EComputer> query,
            String search) {
        QEComputer computer = QEComputer.eComputer;
        return query.where(computer.name.contains(search));
    }

    private List<Computer> buildComputers(List<ComputerBuilder> builders) {
        List<Computer> computers = new ArrayList<>();
        List<List<Problem>> corruptComputers = new ArrayList<>();
        for (ComputerBuilder bob : builders) {
            try {
                computers.add(bob.build());
            } catch (ProblemListException e) {
                corruptComputers.add(e.getList());
            }
        }
        dealWithCorruptComputers(corruptComputers);
        return computers;
    }

    private void dealWithCorruptComputers(
            List<List<Problem>> corruptComputers) {
        if (corruptComputers.size() > 0) {
            logger.error("Could not instantiate computers from database : "
                    + corruptComputers.toString());
            throw new CorruptComputersException(corruptComputers);
        }
    }

    private Optional<ECompany> getCompany(Computer computer) {
        Company company = computer.getCompany();
        if (company != null) {
            return Optional.of(entityManager.getReference(ECompany.class,
                    company.getId()));
        }
        return Optional.empty();
    }

}

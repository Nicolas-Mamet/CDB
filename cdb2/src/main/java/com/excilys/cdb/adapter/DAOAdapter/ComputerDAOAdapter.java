package com.excilys.cdb.adapter.DAOAdapter;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.excilys.cdb.crossProject.Order;
import com.excilys.cdb.crossProject.exceptions.CorruptComputerException;
import com.excilys.cdb.crossProject.exceptions.CorruptComputersException;
import com.excilys.cdb.crossProject.exceptions.DBException;
import com.excilys.cdb.crossProject.exceptions.Problem;
import com.excilys.cdb.crossProject.exceptions.ProblemListException;
import com.excilys.cdb.model.Company;
import com.excilys.cdb.model.Computer;
import com.excilys.cdb.model.Computer.ComputerBuilder;
import com.excilys.cdb.model.Page;
import com.excilys.cdb.persistence.entity.ECompany;
import com.excilys.cdb.persistence.entity.EComputer;

@Component
public class ComputerDAOAdapter
        implements com.excilys.cdb.model.persistence.ComputerDAO {

    @SuppressWarnings("unused")
    private Logger logger = LoggerFactory.getLogger(ComputerDAOAdapter.class);

    @Autowired
    com.excilys.cdb.persistence.interfaces.ComputerDAO computerDAO;

    @Autowired
    com.excilys.cdb.persistence.interfaces.CompanyDAO companyDAO;

    @Autowired
    ComputerMapper computerMapper;

    @Autowired
    EComputerBuilderDirector computerDirector;

    @Autowired
    PageBuilderDirector pageDirector;

    @Override
    public long countComputers() throws DBException {
        try {
            return computerDAO.countComputers();
        } catch (Exception e) {
            throw new DBException(e);
        }
    }

    @Override
    public long countComputers(String search) throws DBException {
        try {
            return computerDAO.countComputers();
        } catch (Exception e) {
            throw new DBException(e);
        }
    }

    @Override
    public List<Computer> searchComputers(Page page, String search)
            throws DBException {
        try {
            return buildComputers(computerDAO
                    .searchComputers(pageDirector.buildPage(page), search)
                    .stream().map(computerMapper::map)
                    .collect(Collectors.toList()));
        } catch (Exception e) {
            throw new DBException(e);
        }
    }

    @Override
    public List<Computer> getPageOfComputers(Page page) throws DBException {
        try {
            return buildComputers(computerDAO
                    .getPageOfComputers(pageDirector.buildPage(page)).stream()
                    .map(computerMapper::map).collect(Collectors.toList()));
        } catch (Exception e) {
            throw new DBException(e);
        }
    }

    @Override
    public Optional<Computer> getComputer(long iD) throws DBException {
        Optional<EComputer> oEComputer;
        try {
            oEComputer = computerDAO.getComputer(iD);
        } catch (Exception e) {
            throw new DBException(e);
        }

        return oEComputer.flatMap(c -> {
            try {
                return Optional.of(computerMapper.map(c).build());
            } catch (ProblemListException e) {
                throw new CorruptComputerException(e.getList());
            }
        });
    }

    @Override
    public void createComputer(Computer computer) throws DBException {
        try {
            Optional<ECompany> company = getCompany(computer);
            computerDAO.createComputer(
                    computerDirector.buildComputer(computer, company.orElse(null)));
        } catch (Exception e) {
            throw new DBException(e);
        }
    }

    @Override
    public boolean updateComputer(Computer computer) throws DBException {
        try {
            Optional<ECompany> company = getCompany(computer);
            return computerDAO.updateComputer(
                    computerDirector.buildComputer(computer, company.orElse(null)));
        } catch (Exception e) {
            throw new DBException(e);
        }
    }

    @Override
    public boolean deleteComputer(long iD) throws DBException {
        try {
            return computerDAO.deleteComputer(iD);
        } catch (Exception e) {
            throw new DBException(e);
        }
    }

    @Override
    public void deleteComputers(List<Long> ids) throws DBException {
        try {
            computerDAO.deleteComputers(ids);
        } catch (Exception e) {
            throw new DBException(e);
        }
    }

    @Override
    public void deleteComputersFromCompany(long id) throws DBException {
        try {
            computerDAO.deleteComputersFromCompany(id);
        } catch (Exception e) {
            throw new DBException(e);
        }
    }

    @Override
    public List<Computer> searchComputers(Page page, String search, Order order)
            throws DBException {
        List<ComputerBuilder> builders;
        try {
            builders = computerDAO
                    .searchComputers(pageDirector.buildPage(page), search, order)
                    .stream()
                    .map(computerMapper::map).collect(Collectors.toList());
        } catch (Exception e) {
            throw new DBException(e);
        }
        return buildComputers(builders);
    }

    @Override
    public List<Computer> searchComputers(Page page, Order order)
            throws DBException {
        List<ComputerBuilder> builders;
        try {
            builders = computerDAO
                    .searchComputers(pageDirector.buildPage(page), order).stream()
                    .map(computerMapper::map).collect(Collectors.toList());
        } catch (Exception e) {
            throw new DBException(e);
        }
        return buildComputers(builders);
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
            return companyDAO.getCompany(company.getId());
        }
        return Optional.empty();
    }

}

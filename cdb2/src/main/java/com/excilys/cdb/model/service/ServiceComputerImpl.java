package com.excilys.cdb.model.service;

import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.excilys.cdb.crossProject.Order;
import com.excilys.cdb.crossProject.exceptions.DBException;
import com.excilys.cdb.model.Computer;
import com.excilys.cdb.model.Page;
import com.excilys.cdb.model.persistence.ComputerDAO;

@Service
public class ServiceComputerImpl implements ComputerService {

    @Autowired
    ComputerDAO computerDAO;

    @SuppressWarnings("unused")
    private final Logger logger =
            LoggerFactory.getLogger(ComputerService.class);

    @Override
    public List<Computer> getComputers(Page page) throws DBException {
        logger.debug("getComputers without search nor order");
        return computerDAO.getPageOfComputers(page);
    }

    @Override
    public List<Computer> getComputers(Page page, String search)
            throws DBException {
        logger.debug("getComputers with search but no order");
        return computerDAO.searchComputers(page, search);
    }

    @Override
    public List<Computer> getComputers(Page page, String search, Order order)
            throws DBException {
        logger.debug("getComputers with search and order");
        return computerDAO.searchComputers(page, search, order);
    }

    @Override
    public List<Computer> getComputers(Page page, Order order)
            throws DBException {
        logger.debug("getComputers with order but no search");
        return computerDAO.searchComputers(page, order);

    }

    @Override
    public Optional<Computer> getComputer(long id) throws DBException {
        return computerDAO.getComputer(id);
    }

    @Override
    public boolean deleteComputer(long id) throws DBException {
        return computerDAO.deleteComputer(id);
    }

    @Override
    public void createComputer(Computer computer) throws DBException {
        computerDAO.createComputer(computer);
    }

    @Override
    public boolean updateComputer(Computer computer) throws DBException {
        return computerDAO.updateComputer(computer);
    }

    @Override
    public long countComputers() throws DBException {
        return computerDAO.countComputers();
    }

    @Override
    public long countComputers(String search) throws DBException {
        return computerDAO.countComputers(search);
    }

    @Override
    public void deleteComputers(List<Long> ids) throws DBException {
        computerDAO.deleteComputers(ids);
    }
}

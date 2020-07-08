package com.excilys.cdb.model.service;

import java.util.List;
import java.util.Optional;

import com.excilys.cdb.crossProject.Order;
import com.excilys.cdb.crossProject.exceptions.DBException;
import com.excilys.cdb.model.Computer;
import com.excilys.cdb.model.Page;

public interface ComputerService {
    List<Computer> getComputers(Page page) throws DBException;

    List<Computer> getComputers(Page page, String search) throws DBException;

    Optional<Computer> getComputer(long id) throws DBException;

    boolean deleteComputer(long id) throws DBException;

    void deleteComputers(List<Long> ids) throws DBException;

    void createComputer(Computer computer) throws DBException;

    boolean updateComputer(Computer computer) throws DBException;

    long countComputers() throws DBException;

    long countComputers(String search) throws DBException;

    List<Computer> getComputers(Page page, String search, Order order)
            throws DBException;

    List<Computer> getComputers(Page page, Order order) throws DBException;
}

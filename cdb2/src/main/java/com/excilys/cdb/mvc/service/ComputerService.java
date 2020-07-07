package com.excilys.cdb.mvc.service;

import java.util.List;
import java.util.Optional;

import com.excilys.cdb.crossProject.Order;
import com.excilys.cdb.crossProject.exceptions.DBException;
import com.excilys.cdb.crossProject.exceptions.NotLongException;
import com.excilys.cdb.crossProject.exceptions.NullComputerException;
import com.excilys.cdb.crossProject.exceptions.ProblemListException;
import com.excilys.cdb.mvc.dto.ComputerDTO;
import com.excilys.cdb.mvc.dto.PageDTO;

public interface ComputerService {

    List<ComputerDTO> getComputers(PageDTO page)
            throws ProblemListException, DBException;

    List<ComputerDTO> getComputers(PageDTO pageDTO, String search)
            throws ProblemListException, DBException;

    Optional<ComputerDTO> getComputer(String id)
            throws NotLongException, DBException;

    boolean deleteComputer(String id) throws NotLongException, DBException;

    void deleteComputers(List<String> ids)
            throws DBException, ProblemListException;

    void createComputer(ComputerDTO computer)
            throws ProblemListException, NullComputerException, DBException;

    boolean updateComputer(ComputerDTO computer)
            throws ProblemListException, NullComputerException, DBException;

    long countComputers() throws DBException;

    long countComputers(String search) throws DBException;

    List<ComputerDTO> getComputers(PageDTO pageDTO, String string, Order order)
            throws ProblemListException, DBException;

    List<ComputerDTO> getComputers(PageDTO pageDTO, Order order)
            throws DBException, ProblemListException;
}

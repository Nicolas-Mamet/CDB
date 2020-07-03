package com.excilys.cdb.services.interfaces;

import java.util.List;
import java.util.Optional;

import com.excilys.cdb.dto.ComputerDTO;
import com.excilys.cdb.dto.PageDTO;
import com.excilys.cdb.exceptions.DBException;
import com.excilys.cdb.exceptions.NotLongException;
import com.excilys.cdb.exceptions.NullComputerException;
import com.excilys.cdb.exceptions.ProblemListException;
import com.excilys.cdb.servlet.Order;

public interface ServiceComputer {

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

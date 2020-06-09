package com.excilys.cdb.services.interfaces;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

import com.excilys.cdb.dto.ComputerDTO;
import com.excilys.cdb.dto.PageDTO;
import com.excilys.cdb.exceptions.InvalidPageException;
import com.excilys.cdb.exceptions.NotLongException;
import com.excilys.cdb.exceptions.NullComputerException;
import com.excilys.cdb.exceptions.ProblemListException;

public interface ServiceComputer {

    List<ComputerDTO> getComputers(PageDTO page)
            throws SQLException, InvalidPageException, ProblemListException;

    Optional<ComputerDTO> getComputer(String id)
            throws SQLException, NotLongException;

    boolean deleteComputer(String id) throws SQLException, NotLongException;

    void createComputer(ComputerDTO computer)
            throws SQLException, ProblemListException, NullComputerException;

    boolean updateComputer(ComputerDTO computer)
            throws SQLException, ProblemListException, NullComputerException;
}

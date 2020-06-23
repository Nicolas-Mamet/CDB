package com.excilys.cdb.services.interfaces;

import java.util.List;
import java.util.Optional;

import com.excilys.cdb.dto.ComputerDTO;
import com.excilys.cdb.dto.PageDTO;
import com.excilys.cdb.exceptions.DBException;
import com.excilys.cdb.exceptions.InvalidPageException;
import com.excilys.cdb.exceptions.NotLongException;
import com.excilys.cdb.exceptions.NullComputerException;
import com.excilys.cdb.exceptions.ProblemListException;

public interface ServiceComputer {

    List<ComputerDTO> getComputers(PageDTO page)
            throws InvalidPageException, ProblemListException, DBException;

    List<ComputerDTO> getComputers(PageDTO pageDTO, String search)
            throws ProblemListException, InvalidPageException, DBException;

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
}

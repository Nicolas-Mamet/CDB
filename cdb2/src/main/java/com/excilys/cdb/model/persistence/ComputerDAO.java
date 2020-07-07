package com.excilys.cdb.model.persistence;

import java.util.List;
import java.util.Optional;

import com.excilys.cdb.crossProject.Order;
import com.excilys.cdb.crossProject.exceptions.CorruptComputersException;
import com.excilys.cdb.crossProject.exceptions.DBException;
import com.excilys.cdb.model.Computer;
import com.excilys.cdb.model.Page;

public interface ComputerDAO {

    long countComputers() throws DBException, CorruptComputersException;

    long countComputers(String search) throws DBException;

    List<Computer> searchComputers(Page page, String search) throws DBException;

    List<Computer> getPageOfComputers(Page page) throws DBException;

    Optional<Computer> getComputer(long iD) throws DBException;

    /**
     * Add a computer in the database based on the parameter.
     *
     * @param c The computer created will be identical to c except for the id
     * @throws DBException if something goes wrong with the database
     */
    void createComputer(Computer computer) throws DBException;

    /**
     * Updates the computer with the specified ID to make it identical to the
     * parameter.
     *
     * @param c the computer having the same id as c will be updated to
     *          correspond to the content of c
     * @throws DBException if something goes wrong with the database
     */
    boolean updateComputer(Computer computer) throws DBException;

    /**
     * Delete the computer with the specified ID.
     *
     * @param id
     * @throws DBException if something goes wrong with the database
     */
    boolean deleteComputer(long iD) throws DBException;

    /**
     * Delete all computer whose ids are passed in the parameter
     *
     * @param ids list of computer ids to delete
     * @return list of unknown ids
     * @throws DBException
     */
    void deleteComputers(List<Long> ids) throws DBException;

    /**
     * delete all computers from given company
     *
     * @param id company id
     * @throws DBException
     */
    void deleteComputersFromCompany(long id) throws DBException;

    List<Computer> searchComputers(Page page, String search, Order order)
            throws DBException;

    List<Computer> searchComputers(Page page, Order order) throws DBException;
}

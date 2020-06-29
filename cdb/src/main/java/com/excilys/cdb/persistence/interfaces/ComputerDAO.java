package com.excilys.cdb.persistence.interfaces;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

import com.excilys.cdb.exceptions.CorruptComputersException;
import com.excilys.cdb.exceptions.DBException;
import com.excilys.cdb.model.Computer;
import com.excilys.cdb.model.Page;
import com.excilys.cdb.servlet.Order;

public interface ComputerDAO {

    List<Computer> getComputers()
            throws SQLException, CorruptComputersException;

    List<Computer> getComputers(String search) throws SQLException;

    List<Computer> searchComputers(Page page, String search)
            throws SQLException;

    List<Computer> getPageOfComputers(Page page) throws SQLException;

    Optional<Computer> getComputer(long iD) throws SQLException;

    /**
     * Add a computer in the database based on the parameter.
     *
     * @param c The computer created will be identical to c except for the id
     * @throws SQLException if something goes wrong with the database
     */
    void createComputer(Computer c) throws SQLException;

    /**
     * Updates the computer with the specified ID to make it identical to the
     * parameter.
     *
     * @param c the computer having the same id as c will be updated to
     *          correspond to the content of c
     * @throws SQLException if something goes wrong with the database
     */
    boolean updateComputer(Computer c) throws SQLException;

    /**
     * Delete the computer with the specified ID.
     *
     * @param id
     * @throws SQLException if something goes wrong with the database
     */
    boolean deleteComputer(long iD) throws SQLException;

    /**
     * Delete all computer whose ids are passed in the parameter
     *
     * @param ids list of computer ids to delete
     * @return list of unknown ids
     * @throws SQLException
     */
    void deleteComputers(List<Long> ids) throws SQLException;

    /**
     * delete all computers from given company
     *
     * @param id company id
     * @throws SQLException
     */
    void deleteComputersFromCompany(long id, Connection connection)
            throws SQLException;

    Collection<Computer> searchComputers(Page page, String search, Order order)
            throws DBException;

    Collection<Computer> searchComputers(Page page, Order order)
            throws DBException;
}

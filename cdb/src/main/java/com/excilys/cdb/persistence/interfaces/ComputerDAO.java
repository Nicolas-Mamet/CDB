package com.excilys.cdb.persistence.interfaces;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

import com.excilys.cdb.model.Computer;
import com.excilys.cdb.model.Page;

public interface ComputerDAO {

    List<Computer> getComputers() throws SQLException;

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
}

package com.excilys.cdb.persistence.interfaces;

import java.util.List;
import java.util.Optional;

import com.excilys.cdb.crossProject.Order;
import com.excilys.cdb.crossProject.exceptions.DBException;
import com.excilys.cdb.persistence.Page;
import com.excilys.cdb.persistence.entity.EComputer;

public interface ComputerDAO {
    long countComputers();

    long countComputers(String search);

    List<EComputer> searchComputers(Page page, String search);

    List<EComputer> getPageOfComputers(Page page);

    Optional<EComputer> getComputer(long iD);

    /**
     * Add a computer in the database based on the parameter.
     *
     * @param c The computer created will be identical to c except for the id
     */
    void createComputer(EComputer computer);

    /**
     * Updates the computer with the specified ID to make it identical to the
     * parameter.
     *
     * @param c the computer having the same id as c will be updated to
     *          correspond to the content of c
     */
    boolean updateComputer(EComputer computer);

    /**
     * Delete the computer with the specified ID.
     *
     * @param id
     */
    boolean deleteComputer(long iD);

    /**
     * Delete all computer whose ids are passed in the parameter
     *
     * @param ids list of computer ids to delete
     * @return list of unknown ids
     */
    void deleteComputers(List<Long> ids);

    /**
     * delete all computers from given company
     *
     * @param id company id
     */
    void deleteComputersFromCompany(long id);

    List<EComputer> searchComputers(Page page, String search, Order order)
            throws DBException;

    List<EComputer> searchComputers(Page page, Order order);
}

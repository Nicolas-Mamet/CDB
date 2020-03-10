package persistence.interfaces;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

import model.Computer;

public interface ComputerDAO {

	public List<Computer> getComputers() throws SQLException;

	public Optional<Computer> getComputer(long l) throws SQLException;
	
	/**
	 * Add a computer in the database based on the parameter
	 * @param c the id of this parameter must be invalid
	 * @throws SQLException if something goes wrong with the database
	 * @throws IllegalArgumentException if the id of c is not 0
	 */
	public void createComputer(Computer c) throws SQLException;
	
	/**
	 * Updates the computer with the specified ID to make it identical to the
	 * parameter
	 * @param c ID must be valid
	 * @throws SQLException if something goes wrong with the database
	 * @throws IllegalArgumentException if the id of c is not in the database
	 */
	public void updateComputer(Computer c) throws SQLException;
	
	/**
	 * Delete the computer with the specified ID
	 * @param id
	 * @throws SQLException if something goes wrong with the database
	 * @throws IllegalArgumentException if there is no computer with the
	 * specified id
	 */
	public void deleteComputer(long id) throws SQLException;
}

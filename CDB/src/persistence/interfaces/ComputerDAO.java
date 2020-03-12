package persistence.interfaces;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

import model.Computer;

public interface ComputerDAO {

	public List<Computer> getComputers() throws SQLException;

	public Optional<Computer> getComputer(long id) throws SQLException;
	
	/**
	 * Add a computer in the database based on the parameter
	 * @param c The computer created will be identical to c except for the id
	 * @throws SQLException if something goes wrong with the database
	 */
	public void createComputer(Computer c) throws SQLException;
	
	/**
	 * Updates the computer with the specified ID to make it identical to the
	 * parameter
	 * @param c the computer having the same id as c will be updated to
	 * correspond to the content of c
	 * @throws SQLException if something goes wrong with the database
	 */
	public void updateComputer(Computer c) throws SQLException;
	
	/**
	 * Delete the computer with the specified ID
	 * @param id
	 * @throws SQLException if something goes wrong with the database
	 */
	public void deleteComputer(long id) throws SQLException;
}

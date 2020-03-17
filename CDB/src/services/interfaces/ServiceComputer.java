package services.interfaces;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

import exceptions.ProblemListException;
import exceptions.NotLongException;
import model.Computer;

public interface ServiceComputer {
	
	public List<Computer> getComputers() throws SQLException;
	public Optional<Computer> getComputer(String id)
			throws SQLException, NotLongException;
	public boolean deleteComputer(String id)
			throws SQLException, NotLongException;
	public void createComputer(
			String name,
			String introduced,
			String discontinued,
			String companyID,	
			String companyName)
			throws SQLException, ProblemListException;
	public boolean updateComputer(
			String iD,
			String name,
			String introduced,
			String discontinued,
			String companyID,
			String companyName)
			throws SQLException, ProblemListException;
}

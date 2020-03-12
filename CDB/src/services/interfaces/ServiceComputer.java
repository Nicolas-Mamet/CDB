package services.interfaces;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

import exceptions.NoNameComputerException;
import exceptions.WrongDatesException;
import model.Company;
import model.Computer;

public interface ServiceComputer {
	
	public List<Computer> getComputers() throws SQLException;
	public Optional<Computer> getComputer(long id) throws SQLException;
	public void createComputer(Computer c)
			throws SQLException, WrongDatesException, NoNameComputerException;
	public void updateComputer(Computer c) 
			throws SQLException, WrongDatesException, NoNameComputerException;
	public void deleteComputer(long id) throws SQLException; 
}

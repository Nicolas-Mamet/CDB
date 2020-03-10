package persistence.implementation;

import persistence.interfaces.*;

public class DAOFactory {
	public ComputerDAO getComputerDAO() {
		return ComputerDAOImpl.getInstance();
	}
	
	public CompanyDAO getCompanyDAO() {
		return CompanyDAOImpl.getInstance();
	}
}

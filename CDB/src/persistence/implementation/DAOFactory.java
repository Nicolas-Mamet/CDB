package persistence.implementation;

import persistence.interfaces.*;

public class DAOFactory {
	
	private CompanyDAO companyDAO;
	private ComputerDAO computerDAO;
	
	public CompanyDAO getCompanyDAO() {
		return companyDAO;
	}
	public void setCompanyDAO(CompanyDAO companyDAO) {
		this.companyDAO = companyDAO;
	}
	public ComputerDAO getComputerDAO() {
		return computerDAO;
	}
	public void setComputerDAO(ComputerDAO computerDAO) {
		this.computerDAO = computerDAO;
	}
}

package services.implementation;

import java.sql.SQLException;
import java.util.List;

import model.Company;
import services.interfaces.ServiceCompany;

final class ServiceCompanyImpl extends AbstractDAOUser 
		implements ServiceCompany{
	
	private static final ServiceCompanyImpl INSTANCE = new ServiceCompanyImpl();
	
	protected static final ServiceCompanyImpl getInstance() { return INSTANCE; }
	
	@Override
	public List<Company> getCompanies() throws SQLException {
		return getDAOFactory().getCompanyDAO().getCompanies();
	}

	@Override
	public boolean checkCompany(Company company) throws SQLException {
		return company == null 
				|| getDAOFactory().getCompanyDAO().checkCompany(company);
	}

}

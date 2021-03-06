package persistence.interfaces;

import java.sql.SQLException;
import java.util.List;

import model.Company;

public interface CompanyDAO {

	public List<Company> getCompanies() throws SQLException;
	public boolean checkCompany(Company c) throws SQLException;
}

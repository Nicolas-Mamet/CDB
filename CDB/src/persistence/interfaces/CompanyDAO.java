package persistence.interfaces;

import java.sql.SQLException;
import java.util.List;

import model.Company;

public interface CompanyDAO {

	List<Company> getCompanies() throws SQLException;

}

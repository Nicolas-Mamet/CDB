package services.interfaces;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;
import exceptions.NotLongException;
import model.Company;

public interface ServiceCompany {
	public List<Company> getCompanies() throws SQLException;
	public Optional<Company> getCompany(String id, String name)
			throws SQLException, NotLongException;
}

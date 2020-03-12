package persistence.implementation;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import model.Company;
import persistence.interfaces.CompanyDAO;

final class CompanyDAOImpl implements CompanyDAO{
	
	private static final CompanyDAOImpl INSTANCE = new CompanyDAOImpl();
	
	private static final String COMPANY_LIST_QUERY =
			"SELECT id, name FROM company";
	
	public List<Company> getCompanies() throws SQLException{
		List<Company> companies = new ArrayList<>();
		
		try (
				Connection connection = ConnectionDB.getConnection();
				Statement statement = connection.createStatement();
				ResultSet resultSet = 
						statement.executeQuery(COMPANY_LIST_QUERY);
			) {
				while(resultSet.next()) {
					companies.add(
							Company.createCompany(
									resultSet.getInt("id"),
									resultSet.getString("name")
								)
						);
				}
		}
		return companies;
	}
	
	private CompanyDAOImpl() {}

	public static CompanyDAOImpl getInstance() {
		return INSTANCE;
	}
	
	private static final String GET_COMPANY_QUERY =
			"SELECT id,name FROM company"
			+ " WHERE id = ?";
	
	/**
	 * Check if the company exists in the database; if name != null the function
	 * checks if there is a company in the database with the corresponding name
	 * and id; if name == null the function checks if a company with the
	 * provided id exists
	 * @param company
	 * @return false if no company was found; true if a corresponding company
	 * was found
	 * @throws SQLException if something went wrong with the database
	 */
	public boolean checkCompany(Company company) throws SQLException {
		try(
			Connection connection = ConnectionDB.getConnection();
			PreparedStatement statement = 
					connection.prepareStatement(GET_COMPANY_QUERY);
		) {
			statement.setLong(1,company.getId());
			try(ResultSet resultSet = statement.executeQuery();){
				if(!resultSet.next()) {
					return false;
				} else {
					if(company.getName() != null) {
						return company.getName() == resultSet.getString("name"); 
					} else {
						return true;
					}
				}
			}		
		}
	}
}

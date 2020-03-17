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

public final class CompanyDAOImpl implements CompanyDAO{
	
	private static final String COMPANY_LIST_QUERY =
			"SELECT id, name FROM company";
	
	private static final String GET_COMPANY_QUERY =
			"SELECT id,name FROM company"
			+ " WHERE id = ?";
	
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
							Company.builder()
							.withID(resultSet.getInt("id"))
							.withName(resultSet.getString("name"))
							.build()
								);
				}
		}
		return companies;
	}
	
	public boolean checkCompany(Company company) throws SQLException {
		try(
			Connection connection = ConnectionDB.getConnection();
			PreparedStatement statement = 
					connection.prepareStatement(GET_COMPANY_QUERY);
		) {
			statement.setLong(1, company.getId());
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

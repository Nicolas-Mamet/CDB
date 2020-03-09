package persistence;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import model.Company;

public class CompanyDAO {
	private static final String COMPANY_LIST_QUERY =
			"SELECT id, name FROM company";
	
	public static List<Company> getCompanies() throws SQLException{
		List<Company> companies = new ArrayList<>();
		
		try (
				Connection connection = ConnectionDB.getConnection();
				Statement statement = connection.createStatement();
				ResultSet resultSet = statement.executeQuery(COMPANY_LIST_QUERY);
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
	
	private CompanyDAO() {}
}

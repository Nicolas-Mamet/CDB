package persistence;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import model.Company;
import model.Computer;
import model.Computer.ComputerBuilder;

public class ComputerDAO {
	
	private ComputerDAO() {}
	
	private static final String COMPUTER_LIST_QUERY =
			"SELECT computer.id as idcomputer,"
					+ " computer.name as namecomputer,"
					+ " introduced, discontinued, company_id,"
					+ " company.name as namecompany"
					+ " FROM computer LEFT OUTER JOIN company"
					+ " ON company_id = company.id";
	
	private static LocalDateTime toLocalDateTime(Timestamp t) {
		 if(t == null) {
			 return null;
		 } else {
			 return t.toLocalDateTime();
		 }
	}

	private static Computer getComputerFromResultSet(ResultSet rs)
			throws SQLException 
	{
		Company company = Company.createCompany(rs.getInt("company_id"),
				rs.getString("namecompany"));
		
		Computer computer = new ComputerBuilder(rs.getInt("idcomputer"))
				.withCompany(company)
				.withDiscontinued(
						toLocalDateTime(rs.getTimestamp("discontinued")))
				.withIntroduced(
						toLocalDateTime(rs.getTimestamp("introduced")))
				.withName(rs.getString("namecomputer"))
				.build();
	
		return computer;
	}
	
	public static List<Computer> getComputers() throws SQLException{
		List<Computer> computers = new ArrayList<>();
		
		try (
			Connection connection = ConnectionDB.getConnection();
			Statement statement = connection.createStatement();
			ResultSet resultSet = statement.executeQuery(COMPUTER_LIST_QUERY);
		) {
			while(resultSet.next()) {
				computers.add(getComputerFromResultSet(resultSet));
			}
		}
		return computers;
	}
	
	private static final String COMPUTER_QUERY =
			COMPUTER_LIST_QUERY
			+ " WHERE computer.id = ?";
	
	public static Optional<Computer> getComputer(long id) throws SQLException{
		Optional<Computer> computer = Optional.empty();
		try (
			Connection connection = ConnectionDB.getConnection();
			PreparedStatement preparedStatement = 
					connection.prepareStatement(COMPUTER_QUERY);
		) {
			preparedStatement.setLong(1, id);
			try(ResultSet resultSet = preparedStatement.executeQuery()){
				if(resultSet.next()) {
					computer = Optional.of(
							getComputerFromResultSet(resultSet));
				}
			}
			
		}
		
		return computer;
	}
	
	
	
	public static void createComputer(Computer computer) throws SQLException {
		
	}
	
}

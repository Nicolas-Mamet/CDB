package persistence.implementation;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.sql.Types;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import model.Company;
import model.Computer;
import model.Computer.ComputerBuilder;
import persistence.interfaces.ComputerDAO;

public final class ComputerDAOImpl implements ComputerDAO{
	
	private ComputerDAOImpl() {}
	
	private static final ComputerDAOImpl INSTANCE = new ComputerDAOImpl();
	
	protected static ComputerDAOImpl getInstance() { return INSTANCE; }
	
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
	
	private static Timestamp valueOf(LocalDateTime ldt) {
		if(ldt == null) {
			 return null;
		 } else {
			 return Timestamp.valueOf(ldt);
		 }
	}

	private static Computer getComputerFromResultSet(ResultSet rs)
			throws SQLException 
	{
		Company company = Company.createCompany(rs.getInt("company_id"),
				rs.getString("namecompany"));
		
		Computer computer = new ComputerBuilder(rs.getString("nameComputer"))
				.withCompany(company)
				.withDiscontinued(
						toLocalDateTime(rs.getTimestamp("discontinued")))
				.withIntroduced(
						toLocalDateTime(rs.getTimestamp("introduced")))
				.withId(rs.getInt("idcomputer"))
				.build();
	
		return computer;
	}
	
	public List<Computer> getComputers() throws SQLException{
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
	
	private static final String GET_COMPUTER_QUERY =
			COMPUTER_LIST_QUERY
			+ " WHERE computer.id = ?";
	
	public Optional<Computer> getComputer(long id) throws SQLException{
		Optional<Computer> computer = Optional.empty();
		try (
			Connection connection = ConnectionDB.getConnection();
			PreparedStatement preparedStatement = 
					connection.prepareStatement(GET_COMPUTER_QUERY);
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
	
	private static final String CREATE_COMPUTER_QUERY =
			"INSERT INTO computer "
			+ "(name, introduced, discontinued, company_id)"
			+ " VALUES (?,?,?,?)";
	
	public void createComputer(Computer computer) throws SQLException {
		
		if(computer.hasValidID()) {
			throw new IllegalArgumentException("To be created computers should"
					+ " have an ID of 0");
		}
		
		Company company = computer.getCompany();
		if(company != null
				&& !CompanyDAOImpl.getInstance().checkCompany(company)) {
			throw new IllegalArgumentException(
					"The associated company does not exist");
		}
		
		try(
			Connection connection = ConnectionDB.getConnection();
			PreparedStatement preparedStatement = 
					connection.prepareStatement(CREATE_COMPUTER_QUERY);
		){
			prepareStatement(preparedStatement, computer);
			preparedStatement.executeUpdate();
		}
	}

	private static final String UPDATE_COMPUTER_QUERY =
			"UPDATE computer"
			+ " SET name = ?,"
			+ " introduced = ?,"
			+ " discontinued = ?,"
			+ " company_id = ?"
			+ " WHERE id = ?";
			

	@Override
	public void updateComputer(Computer computer) throws SQLException {
		try(
			Connection connection = ConnectionDB.getConnection();
			PreparedStatement preparedStatement = 
					connection.prepareStatement(UPDATE_COMPUTER_QUERY);
		){
			prepareStatement(preparedStatement, computer);
			preparedStatement.setLong(5, computer.getId());
			if(preparedStatement.executeUpdate() == 0) {
				throw new IllegalArgumentException("Computer does not exist");
			};
		}
	}


	private void prepareStatement(
			PreparedStatement preparedStatement, Computer computer) 
					throws SQLException {
		preparedStatement.setString(1,computer.getName());
		preparedStatement.setTimestamp(
				2,valueOf(computer.getIntroduced()));
		preparedStatement.setTimestamp(
				3,valueOf(computer.getDiscontinued()));
		Company company = computer.getCompany();
		if(company != null) {
			preparedStatement.setLong(4, company.getId());
		} else {
			preparedStatement.setNull(4, Types.BIGINT);
		}
	}

	private static final String DELETE_COMPUTER_QUERY =
			"DELETE FROM computer WHERE id = ?";
	
	@Override
	public void deleteComputer(long id) throws SQLException {
		try(
			Connection connection = ConnectionDB.getConnection();
			PreparedStatement preparedStatement = 
					connection.prepareStatement(DELETE_COMPUTER_QUERY);
		){
			preparedStatement.setLong(1, id);
			if(preparedStatement.executeUpdate() == 0) {
				throw new IllegalArgumentException("Computer does not exist");
			}
		}
	}
}

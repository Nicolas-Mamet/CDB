package com.excilys.cdb.persistence.implementation;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.excilys.cdb.mapper.Mapper;
import com.excilys.cdb.model.Company;
import com.excilys.cdb.model.Computer;
import com.excilys.cdb.model.Computer.ComputerBuilder;
import com.excilys.cdb.model.Page;
import com.excilys.cdb.persistence.interfaces.ComputerDAO;

public final class ComputerDAOImpl implements ComputerDAO {

    public ComputerDAOImpl() {
    }

    private static final String COMPUTER_LIST_QUERY =
            "SELECT computer.id as idcomputer,"
                    + " computer.name as namecomputer,"
                    + " introduced, discontinued, company_id,"
                    + " company.name as namecompany"
                    + " FROM computer LEFT OUTER JOIN company"
                    + " ON company_id = company.id" + " ORDER BY computer.id";

    private static final String PAGE_LIST_QUERY =
            "SELECT computer.id as idcomputer,"
                    + " computer.name as namecomputer,"
                    + " introduced, discontinued, company_id,"
                    + " company.name as namecompany"
                    + " FROM computer LEFT OUTER JOIN company"
                    + " ON company_id = company.id" + " ORDER BY computer.id"
                    + " LIMIT ? OFFSET ?";

    private static final String GET_COMPUTER_QUERY =
            "SELECT computer.id as idcomputer,"
                    + " computer.name as namecomputer,"
                    + " introduced, discontinued, company_id,"
                    + " company.name as namecompany"
                    + " FROM computer LEFT OUTER JOIN company"
                    + " ON company_id = company.id" + " WHERE computer.id = ?";

    private static final String CREATE_COMPUTER_QUERY = "INSERT INTO computer "
            + "(name, introduced, discontinued, company_id)"
            + " VALUES (?,?,?,?)";

    private static final String UPDATE_COMPUTER_QUERY = "UPDATE computer"
            + " SET name = ?," + " introduced = ?," + " discontinued = ?,"
            + " company_id = ?" + " WHERE id = ?";

    private static final String DELETE_COMPUTER_QUERY =
            "DELETE FROM computer WHERE id = ?";

    private static Computer getComputerFromResultSet(ResultSet rs)
            throws SQLException {
        ComputerBuilder bob =
                Computer.builder().withName(rs.getString("nameComputer"))
                        .withID(rs.getLong("idcomputer"));
        Mapper.toLocalDateTime(rs.getTimestamp("discontinued"))
                .ifPresent(date -> bob.withDiscontinued(date));
        Mapper.toLocalDateTime(rs.getTimestamp("introduced"))
                .ifPresent(date -> bob.withIntroduced(date));
        getCompanyFromResultSet(rs)
                .ifPresent(company -> bob.withCompany(company));
        return bob.build();
    }

    private static Optional<Company> getCompanyFromResultSet(ResultSet rs)
            throws SQLException {
        long iD = rs.getLong("company_id");
        if (iD == 0) {
            return Optional.empty();
        } else {
            return Optional.of(Company.builder().withID(iD)
                    .withName(rs.getString("namecompany")).build());
        }
    }

    @Override
    public List<Computer> getComputers() throws SQLException {
        List<Computer> computers = new ArrayList<>();

        try (Connection connection = ConnectionDB.getConnection();
                Statement statement = connection.createStatement();
                ResultSet resultSet =
                        statement.executeQuery(COMPUTER_LIST_QUERY);) {
            while (resultSet.next()) {
                computers.add(getComputerFromResultSet(resultSet));
            }
        }
        return computers;
    }

    @Override
    public List<Computer> getPageOfComputers(Page page) throws SQLException {
        List<Computer> computers = new ArrayList<>();

        try (Connection connection = ConnectionDB.getConnection();
                PreparedStatement preparedStatement =
                        connection.prepareStatement(PAGE_LIST_QUERY);) {
            preparedStatement.setLong(1, page.getLimit());
            preparedStatement.setLong(2, page.getOffset());
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    computers.add(getComputerFromResultSet(resultSet));
                }
            }
        }
        return computers;
    }

    @Override
    public Optional<Computer> getComputer(long id) throws SQLException {
        Optional<Computer> computer = Optional.empty();
        try (Connection connection = ConnectionDB.getConnection();
                PreparedStatement preparedStatement =
                        connection.prepareStatement(GET_COMPUTER_QUERY);) {
            preparedStatement.setLong(1, id);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    computer = Optional.of(getComputerFromResultSet(resultSet));
                }
            }
        }
        return computer;
    }

    @Override
    public void createComputer(Computer computer) throws SQLException {

        try (Connection connection = ConnectionDB.getConnection();
                PreparedStatement preparedStatement =
                        connection.prepareStatement(CREATE_COMPUTER_QUERY);) {
            prepareStatement(preparedStatement, computer);
            preparedStatement.executeUpdate();
        }
    }

    @Override
    public boolean updateComputer(Computer computer) throws SQLException {
        try (Connection connection = ConnectionDB.getConnection();
                PreparedStatement preparedStatement =
                        connection.prepareStatement(UPDATE_COMPUTER_QUERY);) {
            prepareStatement(preparedStatement, computer);
            preparedStatement.setLong(5, computer.getID());
            return preparedStatement.executeUpdate() == 1;
        }
    }

    private void setDate(PreparedStatement preparedStatement,
            Optional<Timestamp> date, int index) throws SQLException {
        if (date.isPresent()) {
            preparedStatement.setTimestamp(index, date.get());
        } else {
            preparedStatement.setNull(index, Types.TIMESTAMP);
        }
    }

    private void setCompany(PreparedStatement preparedStatement,
            Company company, int index) throws SQLException {
        if (company != null) {
            preparedStatement.setLong(4, company.getId());
        } else {
            preparedStatement.setNull(4, Types.BIGINT);
        }
    }

    private void prepareStatement(PreparedStatement preparedStatement,
            Computer computer) throws SQLException {
        preparedStatement.setString(1, computer.getName());
        setDate(preparedStatement, Mapper.valueOf(computer.getIntroduced()), 2);
        setDate(preparedStatement, Mapper.valueOf(computer.getDiscontinued()),
                3);
        setCompany(preparedStatement, computer.getCompany(), 4);
    }

    @Override
    public boolean deleteComputer(long id) throws SQLException {
        try (Connection connection = ConnectionDB.getConnection();
                PreparedStatement preparedStatement =
                        connection.prepareStatement(DELETE_COMPUTER_QUERY);) {
            preparedStatement.setLong(1, id);
            return preparedStatement.executeUpdate() == 1;
        }
    }
}

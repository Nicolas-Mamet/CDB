package com.excilys.cdb.persistence.implementation;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.sql.Types;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.excilys.cdb.exceptions.AbsurdException;
import com.excilys.cdb.exceptions.CorruptComputerException;
import com.excilys.cdb.exceptions.CorruptComputersException;
import com.excilys.cdb.exceptions.DBException;
import com.excilys.cdb.exceptions.Problem;
import com.excilys.cdb.exceptions.ProblemListException;
import com.excilys.cdb.mapper.Mapper;
import com.excilys.cdb.model.Company;
import com.excilys.cdb.model.Computer;
import com.excilys.cdb.model.Computer.ComputerBuilder;
import com.excilys.cdb.model.Page;
import com.excilys.cdb.persistence.interfaces.ComputerDAO;
import com.excilys.cdb.persistence.interfaces.DataSource;
import com.excilys.cdb.servlet.Order;

@Repository
public final class ComputerDAOImpl implements ComputerDAO {

    @SuppressWarnings("unused")
    private Logger logger = LoggerFactory.getLogger(CompanyDAOImpl.class);

    @Autowired
    private DataSource dataSource;

    private ComputerDAOImpl() {
    }

    private static final String COMPUTER_LIST_QUERY =
            "SELECT computer.id as idcomputer,"
                    + " computer.name as namecomputer,"
                    + " introduced, discontinued, company_id,"
                    + " company.name as namecompany"
                    + " FROM computer LEFT OUTER JOIN company"
                    + " ON company_id = company.id" + " ORDER BY computer.id";

    private static final String SEARCH_LIST_QUERY =
            "SELECT computer.id as idcomputer,"
                    + " computer.name as namecomputer,"
                    + " introduced, discontinued, company_id,"
                    + " company.name as namecompany"
                    + " FROM computer LEFT OUTER JOIN company"
                    + " ON company_id = company.id"
                    + " WHERE computer.name LIKE ?" + " ORDER BY computer.id";

    private static final String COMPUTER_SEARCH_QUERY =
            "SELECT computer.id as idcomputer,"
                    + " computer.name as namecomputer,"
                    + " introduced, discontinued, company_id,"
                    + " company.name as namecompany"
                    + " FROM computer LEFT OUTER JOIN company"
                    + " ON company_id = company.id"
                    + " WHERE computer.name LIKE ?" + " ORDER BY computer.id"
                    + " LIMIT ? OFFSET ?";

    private static final String COMPUTER_SEARCH_ORDER_QUERY =
            "SELECT computer.id as idcomputer,"
                    + " computer.name as namecomputer,"
                    + " introduced, discontinued, company_id,"
                    + " company.name as namecompany"
                    + " FROM computer LEFT OUTER JOIN company"
                    + " ON company_id = company.id"
                    + " WHERE computer.name LIKE ?" + " ORDER BY %s"
                    + " LIMIT ? OFFSET ?";

    private static final String COMPUTER_ORDER_QUERY =
            "SELECT computer.id as idcomputer,"
                    + " computer.name as namecomputer,"
                    + " introduced, discontinued, company_id,"
                    + " company.name as namecompany"
                    + " FROM computer LEFT OUTER JOIN company"
                    + " ON company_id = company.id" + " ORDER BY %s"
                    + " LIMIT ? OFFSET ?";

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

    private static final String DELETE_COMPUTERS_QUERY =
            "DELETE FROM computer WHERE id IN (";

    private static final String DELETE_ALL_QUERY =
            "DELETE FROM computer WHERE company_id = ?";

    private static Computer getComputerFromResultSet(ResultSet rs)
            throws SQLException, CorruptComputerException {
        ComputerBuilder bob =
                Computer.builder().withName(rs.getString("nameComputer"))
                        .withID(rs.getLong("idcomputer"));
        Mapper.toLocalDateTime(rs.getTimestamp("discontinued"))
                .ifPresent(date -> bob.withDiscontinued(date));
        Mapper.toLocalDateTime(rs.getTimestamp("introduced"))
                .ifPresent(date -> bob.withIntroduced(date));
        getCompanyFromResultSet(rs)
                .ifPresent(company -> bob.withCompany(company));
        try {
            return bob.build();
        } catch (ProblemListException e) {
            throw new CorruptComputerException(e.getList());
        }
    }

    @Override
    public List<Computer> getComputers() throws SQLException {
        List<Computer> computers = new ArrayList<>();
        List<List<Problem>> corruptComputers = new ArrayList<>();
        try (Connection connection = dataSource.getConnection();
                Statement statement = connection.createStatement();
                ResultSet resultSet =
                        statement.executeQuery(COMPUTER_LIST_QUERY);) {
            while (resultSet.next()) {
                try {
                    computers.add(getComputerFromResultSet(resultSet));
                } catch (CorruptComputerException e) {
                    corruptComputers.add(e.getProblemList());
                }
            }
            if (corruptComputers.size() > 0) {
                logger.error("Could not instantiate computers from database : "
                        + corruptComputers.toString());
                throw new CorruptComputersException(corruptComputers);
            }
        }
        return computers;
    }

    @Override
    public List<Computer> getComputers(String search) throws SQLException {
        List<Computer> computers = new ArrayList<>();
        List<List<Problem>> corruptComputers = new ArrayList<>();
        try (Connection connection = dataSource.getConnection();
                PreparedStatement preparedStatement =
                        connection.prepareStatement(SEARCH_LIST_QUERY);) {
            preparedStatement.setString(1, "%" + search + "%");
            try (ResultSet resultSet = preparedStatement.executeQuery();) {
                while (resultSet.next()) {
                    try {
                        computers.add(getComputerFromResultSet(resultSet));
                    } catch (CorruptComputerException e) {
                        corruptComputers.add(e.getProblemList());
                    }
                }
                if (corruptComputers.size() > 0) {
                    logger.error(
                            "Could not instantiate computers from database : "
                                    + corruptComputers.toString());
                    throw new CorruptComputersException(corruptComputers);
                }
                if (corruptComputers.size() > 0) {
                    logger.error(
                            "Could not instantiate computers from database : "
                                    + corruptComputers.toString());
                    throw new CorruptComputersException(corruptComputers);
                }
                return computers;
            }
        }
    }

    @Override
    public List<Computer> getPageOfComputers(Page page) throws SQLException {
        List<Computer> computers = new ArrayList<>();
        List<List<Problem>> corruptComputers = new ArrayList<>();
        try (Connection connection = dataSource.getConnection();
                PreparedStatement preparedStatement =
                        connection.prepareStatement(PAGE_LIST_QUERY);) {
            preparedStatement.setLong(1, page.getLimit());
            preparedStatement.setLong(2, page.getOffset());
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    try {
                        computers.add(getComputerFromResultSet(resultSet));
                    } catch (CorruptComputerException e) {
                        corruptComputers.add(e.getProblemList());
                    }
                }
            }
            if (corruptComputers.size() > 0) {
                logger.error("Could not instantiate computers from database : "
                        + corruptComputers.toString());
                throw new CorruptComputersException(corruptComputers);
            }
        }
        return computers;
    }

    @Override
    public List<Computer> searchComputers(Page page, String search)
            throws SQLException {
        List<Computer> computers = new ArrayList<>();
        List<List<Problem>> corruptComputers = new ArrayList<>();
        try (Connection connection = dataSource.getConnection();
                PreparedStatement preparedStatement =
                        connection.prepareStatement(COMPUTER_SEARCH_QUERY);) {
            preparedStatement.setString(1, "%" + search + "%");
            preparedStatement.setLong(2, page.getLimit());
            preparedStatement.setLong(3, page.getOffset());
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    try {
                        computers.add(getComputerFromResultSet(resultSet));
                    } catch (CorruptComputerException e) {
                        corruptComputers.add(e.getProblemList());
                    }
                }
            }
        }
        if (corruptComputers.size() > 0) {
            logger.error("Could not instantiate computers from database : "
                    + corruptComputers.toString());
            throw new CorruptComputersException(corruptComputers);
        }
        return computers;
    }

    @Override
    public Collection<Computer> searchComputers(Page page, String search,
            Order order) throws DBException {
        List<Computer> computers = new ArrayList<>();
        List<List<Problem>> corruptComputers = new ArrayList<>();
        try (Connection connection = dataSource.getConnection();
                PreparedStatement preparedStatement =
                        connection.prepareStatement(
                                String.format(COMPUTER_SEARCH_ORDER_QUERY,
                                        formatOrder(order)));) {
            preparedStatement.setString(1, "%" + search + "%");
            preparedStatement.setLong(2, page.getLimit());
            preparedStatement.setLong(3, page.getOffset());
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    try {
                        computers.add(getComputerFromResultSet(resultSet));
                    } catch (CorruptComputerException e) {
                        corruptComputers.add(e.getProblemList());
                    }
                }
            }
        } catch (SQLException e) {
            throw new DBException(e);
        }
        if (corruptComputers.size() > 0) {
            logger.error("Could not instantiate computers from database : "
                    + corruptComputers.toString());
            throw new CorruptComputersException(corruptComputers);
        }
        return computers;
    }

    @Override
    public Collection<Computer> searchComputers(Page page, Order order)
            throws DBException {
        List<Computer> computers = new ArrayList<>();
        List<List<Problem>> corruptComputers = new ArrayList<>();
        try (Connection connection = dataSource.getConnection();
                PreparedStatement preparedStatement =
                        connection.prepareStatement(String.format(
                                COMPUTER_ORDER_QUERY, formatOrder(order)));) {
            preparedStatement.setLong(1, page.getLimit());
            preparedStatement.setLong(2, page.getOffset());
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    try {
                        computers.add(getComputerFromResultSet(resultSet));
                    } catch (CorruptComputerException e) {
                        corruptComputers.add(e.getProblemList());
                    }
                }
            }
        } catch (SQLException e) {
            throw new DBException(e);
        }
        if (corruptComputers.size() > 0) {
            logger.error("Could not instantiate computers from database : "
                    + corruptComputers.toString());
            throw new CorruptComputersException(corruptComputers);
        }
        return computers;
    }

    @Override
    public Optional<Computer> getComputer(long id) throws SQLException {
        Optional<Computer> computer = Optional.empty();
        try (Connection connection = dataSource.getConnection();
                PreparedStatement preparedStatement =
                        connection.prepareStatement(GET_COMPUTER_QUERY);) {
            preparedStatement.setLong(1, id);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    try {
                        computer = Optional
                                .of(getComputerFromResultSet(resultSet));
                    } catch (CorruptComputerException e) {
                        logger.error(
                                "Could not instantiate computers from database : "
                                        + e.getProblemList().toString());
                        throw new CorruptComputersException(
                                Arrays.asList(e.getProblemList()));
                    }
                }
            }
        }
        return computer;
    }

    @Override
    public void createComputer(Computer computer) throws SQLException {

        try (Connection connection = dataSource.getConnection();
                PreparedStatement preparedStatement =
                        connection.prepareStatement(CREATE_COMPUTER_QUERY);) {
            prepareStatement(preparedStatement, computer);
            preparedStatement.executeUpdate();
        }
    }

    @Override
    public boolean updateComputer(Computer computer) throws SQLException {
        try (Connection connection = dataSource.getConnection();
                PreparedStatement preparedStatement =
                        connection.prepareStatement(UPDATE_COMPUTER_QUERY);) {
            prepareStatement(preparedStatement, computer);
            preparedStatement.setLong(5, computer.getId());
            return preparedStatement.executeUpdate() == 1;
        }
    }

    @Override
    public boolean deleteComputer(long id) throws SQLException {
        try (Connection connection = dataSource.getConnection();
                PreparedStatement preparedStatement =
                        connection.prepareStatement(DELETE_COMPUTER_QUERY);) {
            preparedStatement.setLong(1, id);
            return preparedStatement.executeUpdate() == 1;
        }
    }

    @Override
    public void deleteComputers(List<Long> ids) throws SQLException {
        String query = prepareDeleteComputersQuery(ids.size());
        try (Connection connection = dataSource.getConnection();
                PreparedStatement preparedStatement =
                        connection.prepareStatement(query);) {
            preparedStatement.setObject(1, ids);
            prepareDeleteStatement(ids, preparedStatement);
            preparedStatement.executeUpdate();
        }
    }

    @Override
    public void deleteComputersFromCompany(long id, Connection connection)
            throws SQLException {
        try (PreparedStatement preparedStatement =
                connection.prepareStatement(DELETE_ALL_QUERY)) {
            preparedStatement.setLong(1, id);
            preparedStatement.executeUpdate();
        }
    }

    private void prepareDeleteStatement(List<Long> ids,
            PreparedStatement preparedStatement) throws SQLException {
        try {
            Stream.iterate(1, n -> n + 1).limit(ids.size()).forEach(n -> {
                try {
                    preparedStatement.setLong(n, ids.get(n - 1));
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            });
        } catch (RuntimeException outer) {
            if (outer.getCause() instanceof SQLException) {
                throw (SQLException) outer.getCause();
            } else {
                throw outer;
            }
        }
    }

    private String prepareDeleteComputersQuery(int size) {
        StringBuilder bob = new StringBuilder(DELETE_COMPUTERS_QUERY);
        bob.append(Stream.iterate(0, i -> i + 1).limit(size).map(id -> "?")
                .collect(Collectors.joining(","))).append(")");
        logger.debug(bob.toString());
        return bob.toString();
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

    private static Optional<Company> getCompanyFromResultSet(ResultSet rs)
            throws SQLException {
        long iD = rs.getLong("company_id");
        if (iD == 0) {
            return Optional.empty();
        } else {
            try {
                return Optional.of(Company.builder().withID(iD)
                        .withName(rs.getString("namecompany")).build());
            } catch (ProblemListException e) {
                throw new AbsurdException("Could not instantiate a company;"
                        + " the database schema should" + " prevent this");
            }
        }
    }

    private String formatOrder(Order order) {
        String orderBy;
        switch (order.getOrderBy()) {
        case COMPANY:
            orderBy = " company.name ";
            break;
        case COMPUTER:
            orderBy = " computer.name ";
            break;
        case DISCONTINUED:
            orderBy = " discontinued ";
            break;
        case INTRODUCED:
            orderBy = " introduced ";
            break;
        default:
            throw new RuntimeException(
                    "You forgot to complete the switch in ComputerDAOImpl");
        }
        String orderString;
        if (order.isAsc()) {
            orderString = " asc ";
        } else {
            orderString = " desc ";
        }
        return orderBy + orderString;
    }
}

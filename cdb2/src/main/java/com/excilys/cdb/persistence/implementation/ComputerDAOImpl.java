package com.excilys.cdb.persistence.implementation;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.sql.Types;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;

import com.excilys.cdb.exceptions.CorruptComputersException;
import com.excilys.cdb.exceptions.DBException;
import com.excilys.cdb.exceptions.Problem;
import com.excilys.cdb.exceptions.ProblemListException;
import com.excilys.cdb.mapper.Mapper;
import com.excilys.cdb.model.Company;
import com.excilys.cdb.model.Computer;
import com.excilys.cdb.model.Computer.ComputerBuilder;
import com.excilys.cdb.model.Page;
import com.excilys.cdb.persistence.implementation.mapper.ComputerRowMapper;
import com.excilys.cdb.persistence.interfaces.ComputerDAO;
import com.excilys.cdb.servlet.Order;

//@Repository
public final class ComputerDAOImpl implements ComputerDAO {

    @SuppressWarnings("unused")
    private Logger logger = LoggerFactory.getLogger(CompanyDAOImpl.class);

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private ComputerRowMapper computerMapper;

    private ComputerDAOImpl() {
    }

    private static final String COMPUTER_LIST_QUERY =
            "SELECT COUNT(*) FROM computer";

    private static final String SEARCH_LIST_QUERY = "SELECT COUNT(*)"
            + " FROM computer LEFT OUTER JOIN company"
            + " ON company_id = company.id" + " WHERE computer.name LIKE ?";

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

    @Override
    public long countComputers() throws DBException {
        try {
            return jdbcTemplate.queryForObject(COMPUTER_LIST_QUERY, Long.class);
        } catch (DataAccessException e) {
            throw new DBException(e);
        }
    }

    @Override
    public long countComputers(String search) throws DBException {
        try {
            return jdbcTemplate.queryForObject(SEARCH_LIST_QUERY,
                    new Object[] { "%" + search + "%" }, Long.class);
        } catch (DataAccessException e) {
            throw new DBException(e);
        }
    }

    @Override
    public List<Computer> getPageOfComputers(Page page) throws DBException {
        List<ComputerBuilder> builders = jdbcTemplate.query(PAGE_LIST_QUERY,
                new Object[] { page.getLimit(), page.getOffset() },
                computerMapper);
        try {
            return buildComputers(builders);
        } catch (DataAccessException e) {
            throw new DBException(e);
        }
    }

    @Override
    public List<Computer> searchComputers(Page page, String search)
            throws DBException {
        List<ComputerBuilder> builders = jdbcTemplate
                .query(COMPUTER_SEARCH_QUERY, (PreparedStatement ps) -> {
                    ps.setString(1, "%" + search + "%");
                    ps.setLong(2, page.getLimit());
                    ps.setLong(3, page.getOffset());
                }, computerMapper);
        try {
            return buildComputers(builders);
        } catch (DataAccessException e) {
            throw new DBException(e);
        }
    }

    @Override
    public Collection<Computer> searchComputers(Page page, String search,
            Order order) throws DBException {
        List<ComputerBuilder> builders = jdbcTemplate.query(
                String.format(COMPUTER_SEARCH_ORDER_QUERY, formatOrder(order)),
                (PreparedStatement ps) -> {
                    ps.setString(1, "%" + search + "%");
                    ps.setLong(2, page.getLimit());
                    ps.setLong(3, page.getOffset());
                }, computerMapper);
        try {
            return buildComputers(builders);
        } catch (DataAccessException e) {
            throw new DBException(e);
        }
    }

    @Override
    public Collection<Computer> searchComputers(Page page, Order order)
            throws DBException {
        List<ComputerBuilder> builders = jdbcTemplate.query(
                String.format(COMPUTER_ORDER_QUERY, formatOrder(order)),
                (PreparedStatement ps) -> {
                    ps.setLong(1, page.getLimit());
                    ps.setLong(2, page.getOffset());
                }, computerMapper);
        try {
            return buildComputers(builders);
        } catch (DataAccessException e) {
            throw new DBException(e);
        }
    }

    @Override
    public Optional<Computer> getComputer(long id) throws DBException {
        try {
            List<ComputerBuilder> builders = jdbcTemplate.query(
                    GET_COMPUTER_QUERY, new Object[] { id }, computerMapper);
            List<Computer> computers = buildComputers(builders);
            if (computers.size() == 1) {
                return Optional.of(computers.get(0));
            } else {
                return Optional.empty();
            }
        } catch (DataAccessException e) {
            throw new DBException(e);
        }
    }

    @Override
    public void createComputer(Computer computer) throws DBException {
        try {
            jdbcTemplate.update(CREATE_COMPUTER_QUERY,
                    ps -> prepareStatement(ps, computer));
        } catch (DataAccessException e) {
            throw new DBException(e);
        }
    }

    @Override
    public boolean updateComputer(Computer computer) throws DBException {
        try {
            return 1 == jdbcTemplate.update(UPDATE_COMPUTER_QUERY, ps -> {
                prepareStatement(ps, computer);
                ps.setLong(5, computer.getId());
            });
        } catch (DataAccessException e) {
            throw new DBException(e);
        }
    }

    @Override
    public boolean deleteComputer(long id) throws DBException {
        try {
            return 1 == jdbcTemplate.update(DELETE_COMPUTER_QUERY,
                    new Object[] { id });
        } catch (DataAccessException e) {
            throw new DBException(e);
        }
    }

    @Override
    public void deleteComputers(List<Long> ids) throws DBException {
        String query = prepareDeleteComputersQuery(ids.size());
        try {
            jdbcTemplate.update(query, ps -> prepareDeleteStatement(ids, ps));
        } catch (DataAccessException e) {
            throw new DBException(e);
        }
    }

    @Override
    public void deleteComputersFromCompany(long id) throws DBException {
        try {
            jdbcTemplate.update(DELETE_ALL_QUERY, new Object[] { id });
        } catch (DataAccessException e) {
            throw new DBException(e);
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

    private List<Computer> buildComputers(List<ComputerBuilder> builders) {
        List<Computer> computers = new ArrayList<>();
        List<List<Problem>> corruptComputers = new ArrayList<>();
        for (ComputerBuilder bob : builders) {
            try {
                computers.add(bob.build());
            } catch (ProblemListException e) {
                corruptComputers.add(e.getList());
            }
        }
        dealWithCorruptComputers(corruptComputers);
        return computers;
    }

    private void dealWithCorruptComputers(
            List<List<Problem>> corruptComputers) {
        if (corruptComputers.size() > 0) {
            logger.error("Could not instantiate computers from database : "
                    + corruptComputers.toString());
            throw new CorruptComputersException(corruptComputers);
        }
    }

}

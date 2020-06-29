package com.excilys.cdb.persistence.implementation;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.excilys.cdb.exceptions.AbsurdException;
import com.excilys.cdb.exceptions.ProblemListException;
import com.excilys.cdb.model.Company;
import com.excilys.cdb.model.Page;
import com.excilys.cdb.persistence.interfaces.CompanyDAO;
import com.excilys.cdb.persistence.interfaces.ComputerDAO;
import com.excilys.cdb.persistence.interfaces.DataSource;

@Repository
public final class CompanyDAOImpl implements CompanyDAO {

    @Autowired
    private DataSource dataSource;

    @Autowired
    private ComputerDAO computerDAO;

    private static final String COMPANY_LIST_QUERY =
            "SELECT id, name FROM company ORDER BY name";

    private static final String PAGE_LIST_QUERY =
            "SELECT id, name FROM company" + " ORDER BY id LIMIT ? OFFSET ?";

    private static final String GET_COMPANY_QUERY =
            "SELECT id,name FROM company" + " WHERE id = ?";

    private static final String DELETE_COMPANY_QUERY =
            "DELETE FROM company where id = ?";

    private CompanyDAOImpl() {
    }

    @Override
    public List<Company> getCompanies() throws SQLException {
        List<Company> companies = new ArrayList<>();

        try (Connection connection = dataSource.getConnection();
                Statement statement = connection.createStatement();
                ResultSet resultSet =
                        statement.executeQuery(COMPANY_LIST_QUERY);) {
            while (resultSet.next()) {
                try {
                    companies.add(Company.builder()
                            .withID(resultSet.getInt("id"))
                            .withName(resultSet.getString("name")).build());
                } catch (ProblemListException e) {
                    throw new AbsurdException("Could not instantiate a company;"
                            + " the database schema should" + " prevent this");
                }
            }
        }
        return companies;
    }

    @Override
    public Optional<String> getCompanyName(long iD) throws SQLException {
        try (Connection connection = dataSource.getConnection();
                PreparedStatement statement =
                        connection.prepareStatement(GET_COMPANY_QUERY);) {
            statement.setLong(1, iD);
            try (ResultSet resultSet = statement.executeQuery();) {
                if (!resultSet.next()) {
                    return Optional.empty();
                } else {
                    String name = resultSet.getString("name");
                    if (name == null) {
                        name = "";
                    }
                    return Optional.of(name);
                }
            }
        }
    }

    @Override
    public List<Company> getPageOfCompanies(Page page) throws SQLException {
        List<Company> companies = new ArrayList<>();

        try (Connection connection = dataSource.getConnection();
                PreparedStatement preparedStatement =
                        connection.prepareStatement(PAGE_LIST_QUERY);) {
            preparedStatement.setLong(1, page.getLimit());
            preparedStatement.setLong(2, page.getOffset());
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    try {
                        companies.add(Company.builder()
                                .withID(resultSet.getInt("id"))
                                .withName(resultSet.getString("name")).build());
                    } catch (ProblemListException e) {
                        throw new AbsurdException(
                                "Could not instantiate a company;"
                                        + " the database schema should"
                                        + " prevent this");
                    }
                }
            }
        }
        // System.out.println(companies.size());
        return companies;
    }

    @Override
    public boolean deleteCompany(long id) throws SQLException {
        boolean ok;
        try (Connection connection = dataSource.getConnection()) {
            connection.setAutoCommit(false);
            computerDAO.deleteComputersFromCompany(id, connection);
            try (PreparedStatement statement =
                    connection.prepareStatement(DELETE_COMPANY_QUERY)) {
                statement.setLong(1, id);
                ok = 1 == statement.executeUpdate();
            }
            connection.commit();
        }
        return ok;
    }
}

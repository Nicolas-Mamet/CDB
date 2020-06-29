package com.excilys.cdb.persistence.implementation.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import com.excilys.cdb.exceptions.AbsurdException;
import com.excilys.cdb.exceptions.ProblemListException;
import com.excilys.cdb.model.Company;

@Component
public class CompanyMapper implements RowMapper<Company> {

    private CompanyMapper() {
    }

    @Override
    public Company mapRow(ResultSet resultSet, int rowNum) throws SQLException {
        try {
            return Company.builder().withID(resultSet.getInt("id"))
                    .withName(resultSet.getString("name")).build();
        } catch (ProblemListException e) {
            throw new AbsurdException("Could not instantiate a company;"
                    + " the database schema should" + " prevent this");
        }
    }

}

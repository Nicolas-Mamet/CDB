package com.excilys.cdb.persistence.implementation.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import com.excilys.cdb.exceptions.AbsurdException;
import com.excilys.cdb.exceptions.ProblemListException;
import com.excilys.cdb.mapper.Mapper;
import com.excilys.cdb.model.Company;
import com.excilys.cdb.model.Computer;
import com.excilys.cdb.model.Computer.ComputerBuilder;

@Component
public class ComputerRowMapper implements RowMapper<ComputerBuilder> {

    private ComputerRowMapper() {
    }

    @Override
    public ComputerBuilder mapRow(ResultSet rs, int rowNum)
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
        return bob;
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
}

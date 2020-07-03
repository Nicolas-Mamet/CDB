package com.excilys.cdb.persistence.implementation;

import java.sql.PreparedStatement;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.transaction.annotation.Transactional;

import com.excilys.cdb.exceptions.DBException;
import com.excilys.cdb.model.Company;
import com.excilys.cdb.model.Page;
import com.excilys.cdb.persistence.implementation.mapper.CompanyRowMapper;
import com.excilys.cdb.persistence.interfaces.CompanyDAO;
import com.excilys.cdb.persistence.interfaces.ComputerDAO;

//@Repository
public final class CompanyDAOImpl implements CompanyDAO {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private CompanyRowMapper companyMapper;

    @Autowired
    private ComputerDAO computerDAO;

    private static final String COMPANY_LIST_QUERY =
            "SELECT id, name FROM company ORDER BY name";

    private static final String PAGE_LIST_QUERY =
            "SELECT id, name FROM company" + " ORDER BY id LIMIT ? OFFSET ?";

    private static final String DELETE_COMPANY_QUERY =
            "DELETE FROM company where id = ?";

    private CompanyDAOImpl() {
    }

    @Override
    public List<Company> getCompanies() throws DBException {
        try {
            return jdbcTemplate.query(COMPANY_LIST_QUERY, companyMapper);
        } catch (DataAccessException e) {
            throw new DBException(e);
        }
    }

    @Override
    public List<Company> getPageOfCompanies(Page page) throws DBException {
        try {
            return jdbcTemplate.query(PAGE_LIST_QUERY,
                    (PreparedStatement ps) -> {
                        ps.setLong(1, page.getLimit());
                        ps.setLong(2, page.getOffset());
                    }, companyMapper);
        } catch (DataAccessException e) {
            throw new DBException(e);
        }
    }

    @Override
    @Transactional
    public boolean deleteCompany(long id) throws DBException {
        computerDAO.deleteComputersFromCompany(id);
        try {
            return 1 == jdbcTemplate.update(DELETE_COMPANY_QUERY,
                    new Object[] { id });
        } catch (DataAccessException e) {
            throw new DBException(e);
        }
    }
}

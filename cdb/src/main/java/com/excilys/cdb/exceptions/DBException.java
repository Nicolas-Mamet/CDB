package com.excilys.cdb.exceptions;

import java.sql.SQLException;

import org.springframework.dao.DataAccessException;

public class DBException extends Exception {
    public DBException(SQLException e) {
        super(e);
    }

    public DBException(DataAccessException e) {
        super(e);
    }
}
